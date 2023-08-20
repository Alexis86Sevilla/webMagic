package com.magicweb.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.magicweb.app.IntegrationTest;
import com.magicweb.app.domain.Card;
import com.magicweb.app.repository.CardRepository;
import com.magicweb.app.service.criteria.CardCriteria;
import com.magicweb.app.service.dto.CardDTO;
import com.magicweb.app.service.mapper.CardMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CardResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CardResourceIT {

    private static final String DEFAULT_ID_MAGIC = "AAAAAAAAAA";
    private static final String UPDATED_ID_MAGIC = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_ENGLISH = "AAAAAAAAAA";
    private static final String UPDATED_NAME_ENGLISH = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_SPANISH = "AAAAAAAAAA";
    private static final String UPDATED_NAME_SPANISH = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardMapper cardMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCardMockMvc;

    private Card card;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Card createEntity(EntityManager em) {
        Card card = new Card()
            .idMagic(DEFAULT_ID_MAGIC)
            .nameEnglish(DEFAULT_NAME_ENGLISH)
            .nameSpanish(DEFAULT_NAME_SPANISH)
            .imageUrl(DEFAULT_IMAGE_URL);
        return card;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Card createUpdatedEntity(EntityManager em) {
        Card card = new Card()
            .idMagic(UPDATED_ID_MAGIC)
            .nameEnglish(UPDATED_NAME_ENGLISH)
            .nameSpanish(UPDATED_NAME_SPANISH)
            .imageUrl(UPDATED_IMAGE_URL);
        return card;
    }

    @BeforeEach
    public void initTest() {
        card = createEntity(em);
    }

    @Test
    @Transactional
    void createCard() throws Exception {
        int databaseSizeBeforeCreate = cardRepository.findAll().size();
        // Create the Card
        CardDTO cardDTO = cardMapper.toDto(card);
        restCardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardDTO)))
            .andExpect(status().isCreated());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeCreate + 1);
        Card testCard = cardList.get(cardList.size() - 1);
        assertThat(testCard.getIdMagic()).isEqualTo(DEFAULT_ID_MAGIC);
        assertThat(testCard.getNameEnglish()).isEqualTo(DEFAULT_NAME_ENGLISH);
        assertThat(testCard.getNameSpanish()).isEqualTo(DEFAULT_NAME_SPANISH);
        assertThat(testCard.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
    }

    @Test
    @Transactional
    void createCardWithExistingId() throws Exception {
        // Create the Card with an existing ID
        card.setId(1L);
        CardDTO cardDTO = cardMapper.toDto(card);

        int databaseSizeBeforeCreate = cardRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCards() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get all the cardList
        restCardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(card.getId().intValue())))
            .andExpect(jsonPath("$.[*].idMagic").value(hasItem(DEFAULT_ID_MAGIC)))
            .andExpect(jsonPath("$.[*].nameEnglish").value(hasItem(DEFAULT_NAME_ENGLISH)))
            .andExpect(jsonPath("$.[*].nameSpanish").value(hasItem(DEFAULT_NAME_SPANISH)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)));
    }

    @Test
    @Transactional
    void getCard() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get the card
        restCardMockMvc
            .perform(get(ENTITY_API_URL_ID, card.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(card.getId().intValue()))
            .andExpect(jsonPath("$.idMagic").value(DEFAULT_ID_MAGIC))
            .andExpect(jsonPath("$.nameEnglish").value(DEFAULT_NAME_ENGLISH))
            .andExpect(jsonPath("$.nameSpanish").value(DEFAULT_NAME_SPANISH))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL));
    }

    @Test
    @Transactional
    void getCardsByIdFiltering() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        Long id = card.getId();

        defaultCardShouldBeFound("id.equals=" + id);
        defaultCardShouldNotBeFound("id.notEquals=" + id);

        defaultCardShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCardShouldNotBeFound("id.greaterThan=" + id);

        defaultCardShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCardShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCardsByIdMagicIsEqualToSomething() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get all the cardList where idMagic equals to DEFAULT_ID_MAGIC
        defaultCardShouldBeFound("idMagic.equals=" + DEFAULT_ID_MAGIC);

        // Get all the cardList where idMagic equals to UPDATED_ID_MAGIC
        defaultCardShouldNotBeFound("idMagic.equals=" + UPDATED_ID_MAGIC);
    }

    @Test
    @Transactional
    void getAllCardsByIdMagicIsInShouldWork() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get all the cardList where idMagic in DEFAULT_ID_MAGIC or UPDATED_ID_MAGIC
        defaultCardShouldBeFound("idMagic.in=" + DEFAULT_ID_MAGIC + "," + UPDATED_ID_MAGIC);

        // Get all the cardList where idMagic equals to UPDATED_ID_MAGIC
        defaultCardShouldNotBeFound("idMagic.in=" + UPDATED_ID_MAGIC);
    }

    @Test
    @Transactional
    void getAllCardsByIdMagicIsNullOrNotNull() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get all the cardList where idMagic is not null
        defaultCardShouldBeFound("idMagic.specified=true");

        // Get all the cardList where idMagic is null
        defaultCardShouldNotBeFound("idMagic.specified=false");
    }

    @Test
    @Transactional
    void getAllCardsByIdMagicContainsSomething() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get all the cardList where idMagic contains DEFAULT_ID_MAGIC
        defaultCardShouldBeFound("idMagic.contains=" + DEFAULT_ID_MAGIC);

        // Get all the cardList where idMagic contains UPDATED_ID_MAGIC
        defaultCardShouldNotBeFound("idMagic.contains=" + UPDATED_ID_MAGIC);
    }

    @Test
    @Transactional
    void getAllCardsByIdMagicNotContainsSomething() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get all the cardList where idMagic does not contain DEFAULT_ID_MAGIC
        defaultCardShouldNotBeFound("idMagic.doesNotContain=" + DEFAULT_ID_MAGIC);

        // Get all the cardList where idMagic does not contain UPDATED_ID_MAGIC
        defaultCardShouldBeFound("idMagic.doesNotContain=" + UPDATED_ID_MAGIC);
    }

    @Test
    @Transactional
    void getAllCardsByNameEnglishIsEqualToSomething() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get all the cardList where nameEnglish equals to DEFAULT_NAME_ENGLISH
        defaultCardShouldBeFound("nameEnglish.equals=" + DEFAULT_NAME_ENGLISH);

        // Get all the cardList where nameEnglish equals to UPDATED_NAME_ENGLISH
        defaultCardShouldNotBeFound("nameEnglish.equals=" + UPDATED_NAME_ENGLISH);
    }

    @Test
    @Transactional
    void getAllCardsByNameEnglishIsInShouldWork() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get all the cardList where nameEnglish in DEFAULT_NAME_ENGLISH or UPDATED_NAME_ENGLISH
        defaultCardShouldBeFound("nameEnglish.in=" + DEFAULT_NAME_ENGLISH + "," + UPDATED_NAME_ENGLISH);

        // Get all the cardList where nameEnglish equals to UPDATED_NAME_ENGLISH
        defaultCardShouldNotBeFound("nameEnglish.in=" + UPDATED_NAME_ENGLISH);
    }

    @Test
    @Transactional
    void getAllCardsByNameEnglishIsNullOrNotNull() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get all the cardList where nameEnglish is not null
        defaultCardShouldBeFound("nameEnglish.specified=true");

        // Get all the cardList where nameEnglish is null
        defaultCardShouldNotBeFound("nameEnglish.specified=false");
    }

    @Test
    @Transactional
    void getAllCardsByNameEnglishContainsSomething() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get all the cardList where nameEnglish contains DEFAULT_NAME_ENGLISH
        defaultCardShouldBeFound("nameEnglish.contains=" + DEFAULT_NAME_ENGLISH);

        // Get all the cardList where nameEnglish contains UPDATED_NAME_ENGLISH
        defaultCardShouldNotBeFound("nameEnglish.contains=" + UPDATED_NAME_ENGLISH);
    }

    @Test
    @Transactional
    void getAllCardsByNameEnglishNotContainsSomething() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get all the cardList where nameEnglish does not contain DEFAULT_NAME_ENGLISH
        defaultCardShouldNotBeFound("nameEnglish.doesNotContain=" + DEFAULT_NAME_ENGLISH);

        // Get all the cardList where nameEnglish does not contain UPDATED_NAME_ENGLISH
        defaultCardShouldBeFound("nameEnglish.doesNotContain=" + UPDATED_NAME_ENGLISH);
    }

    @Test
    @Transactional
    void getAllCardsByNameSpanishIsEqualToSomething() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get all the cardList where nameSpanish equals to DEFAULT_NAME_SPANISH
        defaultCardShouldBeFound("nameSpanish.equals=" + DEFAULT_NAME_SPANISH);

        // Get all the cardList where nameSpanish equals to UPDATED_NAME_SPANISH
        defaultCardShouldNotBeFound("nameSpanish.equals=" + UPDATED_NAME_SPANISH);
    }

    @Test
    @Transactional
    void getAllCardsByNameSpanishIsInShouldWork() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get all the cardList where nameSpanish in DEFAULT_NAME_SPANISH or UPDATED_NAME_SPANISH
        defaultCardShouldBeFound("nameSpanish.in=" + DEFAULT_NAME_SPANISH + "," + UPDATED_NAME_SPANISH);

        // Get all the cardList where nameSpanish equals to UPDATED_NAME_SPANISH
        defaultCardShouldNotBeFound("nameSpanish.in=" + UPDATED_NAME_SPANISH);
    }

    @Test
    @Transactional
    void getAllCardsByNameSpanishIsNullOrNotNull() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get all the cardList where nameSpanish is not null
        defaultCardShouldBeFound("nameSpanish.specified=true");

        // Get all the cardList where nameSpanish is null
        defaultCardShouldNotBeFound("nameSpanish.specified=false");
    }

    @Test
    @Transactional
    void getAllCardsByNameSpanishContainsSomething() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get all the cardList where nameSpanish contains DEFAULT_NAME_SPANISH
        defaultCardShouldBeFound("nameSpanish.contains=" + DEFAULT_NAME_SPANISH);

        // Get all the cardList where nameSpanish contains UPDATED_NAME_SPANISH
        defaultCardShouldNotBeFound("nameSpanish.contains=" + UPDATED_NAME_SPANISH);
    }

    @Test
    @Transactional
    void getAllCardsByNameSpanishNotContainsSomething() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get all the cardList where nameSpanish does not contain DEFAULT_NAME_SPANISH
        defaultCardShouldNotBeFound("nameSpanish.doesNotContain=" + DEFAULT_NAME_SPANISH);

        // Get all the cardList where nameSpanish does not contain UPDATED_NAME_SPANISH
        defaultCardShouldBeFound("nameSpanish.doesNotContain=" + UPDATED_NAME_SPANISH);
    }

    @Test
    @Transactional
    void getAllCardsByImageUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get all the cardList where imageUrl equals to DEFAULT_IMAGE_URL
        defaultCardShouldBeFound("imageUrl.equals=" + DEFAULT_IMAGE_URL);

        // Get all the cardList where imageUrl equals to UPDATED_IMAGE_URL
        defaultCardShouldNotBeFound("imageUrl.equals=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllCardsByImageUrlIsInShouldWork() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get all the cardList where imageUrl in DEFAULT_IMAGE_URL or UPDATED_IMAGE_URL
        defaultCardShouldBeFound("imageUrl.in=" + DEFAULT_IMAGE_URL + "," + UPDATED_IMAGE_URL);

        // Get all the cardList where imageUrl equals to UPDATED_IMAGE_URL
        defaultCardShouldNotBeFound("imageUrl.in=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllCardsByImageUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get all the cardList where imageUrl is not null
        defaultCardShouldBeFound("imageUrl.specified=true");

        // Get all the cardList where imageUrl is null
        defaultCardShouldNotBeFound("imageUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllCardsByImageUrlContainsSomething() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get all the cardList where imageUrl contains DEFAULT_IMAGE_URL
        defaultCardShouldBeFound("imageUrl.contains=" + DEFAULT_IMAGE_URL);

        // Get all the cardList where imageUrl contains UPDATED_IMAGE_URL
        defaultCardShouldNotBeFound("imageUrl.contains=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllCardsByImageUrlNotContainsSomething() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get all the cardList where imageUrl does not contain DEFAULT_IMAGE_URL
        defaultCardShouldNotBeFound("imageUrl.doesNotContain=" + DEFAULT_IMAGE_URL);

        // Get all the cardList where imageUrl does not contain UPDATED_IMAGE_URL
        defaultCardShouldBeFound("imageUrl.doesNotContain=" + UPDATED_IMAGE_URL);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCardShouldBeFound(String filter) throws Exception {
        restCardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(card.getId().intValue())))
            .andExpect(jsonPath("$.[*].idMagic").value(hasItem(DEFAULT_ID_MAGIC)))
            .andExpect(jsonPath("$.[*].nameEnglish").value(hasItem(DEFAULT_NAME_ENGLISH)))
            .andExpect(jsonPath("$.[*].nameSpanish").value(hasItem(DEFAULT_NAME_SPANISH)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)));

        // Check, that the count call also returns 1
        restCardMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCardShouldNotBeFound(String filter) throws Exception {
        restCardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCardMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCard() throws Exception {
        // Get the card
        restCardMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCard() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        int databaseSizeBeforeUpdate = cardRepository.findAll().size();

        // Update the card
        Card updatedCard = cardRepository.findById(card.getId()).get();
        // Disconnect from session so that the updates on updatedCard are not directly saved in db
        em.detach(updatedCard);
        updatedCard
            .idMagic(UPDATED_ID_MAGIC)
            .nameEnglish(UPDATED_NAME_ENGLISH)
            .nameSpanish(UPDATED_NAME_SPANISH)
            .imageUrl(UPDATED_IMAGE_URL);
        CardDTO cardDTO = cardMapper.toDto(updatedCard);

        restCardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardDTO))
            )
            .andExpect(status().isOk());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeUpdate);
        Card testCard = cardList.get(cardList.size() - 1);
        assertThat(testCard.getIdMagic()).isEqualTo(UPDATED_ID_MAGIC);
        assertThat(testCard.getNameEnglish()).isEqualTo(UPDATED_NAME_ENGLISH);
        assertThat(testCard.getNameSpanish()).isEqualTo(UPDATED_NAME_SPANISH);
        assertThat(testCard.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void putNonExistingCard() throws Exception {
        int databaseSizeBeforeUpdate = cardRepository.findAll().size();
        card.setId(count.incrementAndGet());

        // Create the Card
        CardDTO cardDTO = cardMapper.toDto(card);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCard() throws Exception {
        int databaseSizeBeforeUpdate = cardRepository.findAll().size();
        card.setId(count.incrementAndGet());

        // Create the Card
        CardDTO cardDTO = cardMapper.toDto(card);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCard() throws Exception {
        int databaseSizeBeforeUpdate = cardRepository.findAll().size();
        card.setId(count.incrementAndGet());

        // Create the Card
        CardDTO cardDTO = cardMapper.toDto(card);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCardWithPatch() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        int databaseSizeBeforeUpdate = cardRepository.findAll().size();

        // Update the card using partial update
        Card partialUpdatedCard = new Card();
        partialUpdatedCard.setId(card.getId());

        partialUpdatedCard.idMagic(UPDATED_ID_MAGIC).imageUrl(UPDATED_IMAGE_URL);

        restCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCard.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCard))
            )
            .andExpect(status().isOk());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeUpdate);
        Card testCard = cardList.get(cardList.size() - 1);
        assertThat(testCard.getIdMagic()).isEqualTo(UPDATED_ID_MAGIC);
        assertThat(testCard.getNameEnglish()).isEqualTo(DEFAULT_NAME_ENGLISH);
        assertThat(testCard.getNameSpanish()).isEqualTo(DEFAULT_NAME_SPANISH);
        assertThat(testCard.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void fullUpdateCardWithPatch() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        int databaseSizeBeforeUpdate = cardRepository.findAll().size();

        // Update the card using partial update
        Card partialUpdatedCard = new Card();
        partialUpdatedCard.setId(card.getId());

        partialUpdatedCard
            .idMagic(UPDATED_ID_MAGIC)
            .nameEnglish(UPDATED_NAME_ENGLISH)
            .nameSpanish(UPDATED_NAME_SPANISH)
            .imageUrl(UPDATED_IMAGE_URL);

        restCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCard.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCard))
            )
            .andExpect(status().isOk());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeUpdate);
        Card testCard = cardList.get(cardList.size() - 1);
        assertThat(testCard.getIdMagic()).isEqualTo(UPDATED_ID_MAGIC);
        assertThat(testCard.getNameEnglish()).isEqualTo(UPDATED_NAME_ENGLISH);
        assertThat(testCard.getNameSpanish()).isEqualTo(UPDATED_NAME_SPANISH);
        assertThat(testCard.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void patchNonExistingCard() throws Exception {
        int databaseSizeBeforeUpdate = cardRepository.findAll().size();
        card.setId(count.incrementAndGet());

        // Create the Card
        CardDTO cardDTO = cardMapper.toDto(card);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cardDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCard() throws Exception {
        int databaseSizeBeforeUpdate = cardRepository.findAll().size();
        card.setId(count.incrementAndGet());

        // Create the Card
        CardDTO cardDTO = cardMapper.toDto(card);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCard() throws Exception {
        int databaseSizeBeforeUpdate = cardRepository.findAll().size();
        card.setId(count.incrementAndGet());

        // Create the Card
        CardDTO cardDTO = cardMapper.toDto(card);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cardDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCard() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        int databaseSizeBeforeDelete = cardRepository.findAll().size();

        // Delete the card
        restCardMockMvc
            .perform(delete(ENTITY_API_URL_ID, card.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
