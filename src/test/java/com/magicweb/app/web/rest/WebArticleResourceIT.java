package com.magicweb.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.magicweb.app.IntegrationTest;
import com.magicweb.app.domain.Comment;
import com.magicweb.app.domain.Tag;
import com.magicweb.app.domain.WebArticle;
import com.magicweb.app.repository.WebArticleRepository;
import com.magicweb.app.service.WebArticleService;
import com.magicweb.app.service.criteria.WebArticleCriteria;
import com.magicweb.app.service.dto.WebArticleDTO;
import com.magicweb.app.service.mapper.WebArticleMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link WebArticleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WebArticleResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_BODY = "AAAAAAAAAA";
    private static final String UPDATED_BODY = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_AUTHOR = "AAAAAAAAAA";
    private static final String UPDATED_AUTHOR = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/web-articles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WebArticleRepository webArticleRepository;

    @Mock
    private WebArticleRepository webArticleRepositoryMock;

    @Autowired
    private WebArticleMapper webArticleMapper;

    @Mock
    private WebArticleService webArticleServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWebArticleMockMvc;

    private WebArticle webArticle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WebArticle createEntity(EntityManager em) {
        WebArticle webArticle = new WebArticle()
            .title(DEFAULT_TITLE)
            .body(DEFAULT_BODY)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .author(DEFAULT_AUTHOR)
            .date(DEFAULT_DATE);
        return webArticle;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WebArticle createUpdatedEntity(EntityManager em) {
        WebArticle webArticle = new WebArticle()
            .title(UPDATED_TITLE)
            .body(UPDATED_BODY)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .author(UPDATED_AUTHOR)
            .date(UPDATED_DATE);
        return webArticle;
    }

    @BeforeEach
    public void initTest() {
        webArticle = createEntity(em);
    }

    @Test
    @Transactional
    void createWebArticle() throws Exception {
        int databaseSizeBeforeCreate = webArticleRepository.findAll().size();
        // Create the WebArticle
        WebArticleDTO webArticleDTO = webArticleMapper.toDto(webArticle);
        restWebArticleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(webArticleDTO)))
            .andExpect(status().isCreated());

        // Validate the WebArticle in the database
        List<WebArticle> webArticleList = webArticleRepository.findAll();
        assertThat(webArticleList).hasSize(databaseSizeBeforeCreate + 1);
        WebArticle testWebArticle = webArticleList.get(webArticleList.size() - 1);
        assertThat(testWebArticle.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testWebArticle.getBody()).isEqualTo(DEFAULT_BODY);
        assertThat(testWebArticle.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testWebArticle.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testWebArticle.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
        assertThat(testWebArticle.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createWebArticleWithExistingId() throws Exception {
        // Create the WebArticle with an existing ID
        webArticle.setId(1L);
        WebArticleDTO webArticleDTO = webArticleMapper.toDto(webArticle);

        int databaseSizeBeforeCreate = webArticleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWebArticleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(webArticleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WebArticle in the database
        List<WebArticle> webArticleList = webArticleRepository.findAll();
        assertThat(webArticleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWebArticles() throws Exception {
        // Initialize the database
        webArticleRepository.saveAndFlush(webArticle);

        // Get all the webArticleList
        restWebArticleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(webArticle.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWebArticlesWithEagerRelationshipsIsEnabled() throws Exception {
        when(webArticleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWebArticleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(webArticleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWebArticlesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(webArticleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWebArticleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(webArticleRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getWebArticle() throws Exception {
        // Initialize the database
        webArticleRepository.saveAndFlush(webArticle);

        // Get the webArticle
        restWebArticleMockMvc
            .perform(get(ENTITY_API_URL_ID, webArticle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(webArticle.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.body").value(DEFAULT_BODY))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getWebArticlesByIdFiltering() throws Exception {
        // Initialize the database
        webArticleRepository.saveAndFlush(webArticle);

        Long id = webArticle.getId();

        defaultWebArticleShouldBeFound("id.equals=" + id);
        defaultWebArticleShouldNotBeFound("id.notEquals=" + id);

        defaultWebArticleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultWebArticleShouldNotBeFound("id.greaterThan=" + id);

        defaultWebArticleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultWebArticleShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWebArticlesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        webArticleRepository.saveAndFlush(webArticle);

        // Get all the webArticleList where title equals to DEFAULT_TITLE
        defaultWebArticleShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the webArticleList where title equals to UPDATED_TITLE
        defaultWebArticleShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllWebArticlesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        webArticleRepository.saveAndFlush(webArticle);

        // Get all the webArticleList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultWebArticleShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the webArticleList where title equals to UPDATED_TITLE
        defaultWebArticleShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllWebArticlesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        webArticleRepository.saveAndFlush(webArticle);

        // Get all the webArticleList where title is not null
        defaultWebArticleShouldBeFound("title.specified=true");

        // Get all the webArticleList where title is null
        defaultWebArticleShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllWebArticlesByTitleContainsSomething() throws Exception {
        // Initialize the database
        webArticleRepository.saveAndFlush(webArticle);

        // Get all the webArticleList where title contains DEFAULT_TITLE
        defaultWebArticleShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the webArticleList where title contains UPDATED_TITLE
        defaultWebArticleShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllWebArticlesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        webArticleRepository.saveAndFlush(webArticle);

        // Get all the webArticleList where title does not contain DEFAULT_TITLE
        defaultWebArticleShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the webArticleList where title does not contain UPDATED_TITLE
        defaultWebArticleShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllWebArticlesByBodyIsEqualToSomething() throws Exception {
        // Initialize the database
        webArticleRepository.saveAndFlush(webArticle);

        // Get all the webArticleList where body equals to DEFAULT_BODY
        defaultWebArticleShouldBeFound("body.equals=" + DEFAULT_BODY);

        // Get all the webArticleList where body equals to UPDATED_BODY
        defaultWebArticleShouldNotBeFound("body.equals=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    void getAllWebArticlesByBodyIsInShouldWork() throws Exception {
        // Initialize the database
        webArticleRepository.saveAndFlush(webArticle);

        // Get all the webArticleList where body in DEFAULT_BODY or UPDATED_BODY
        defaultWebArticleShouldBeFound("body.in=" + DEFAULT_BODY + "," + UPDATED_BODY);

        // Get all the webArticleList where body equals to UPDATED_BODY
        defaultWebArticleShouldNotBeFound("body.in=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    void getAllWebArticlesByBodyIsNullOrNotNull() throws Exception {
        // Initialize the database
        webArticleRepository.saveAndFlush(webArticle);

        // Get all the webArticleList where body is not null
        defaultWebArticleShouldBeFound("body.specified=true");

        // Get all the webArticleList where body is null
        defaultWebArticleShouldNotBeFound("body.specified=false");
    }

    @Test
    @Transactional
    void getAllWebArticlesByBodyContainsSomething() throws Exception {
        // Initialize the database
        webArticleRepository.saveAndFlush(webArticle);

        // Get all the webArticleList where body contains DEFAULT_BODY
        defaultWebArticleShouldBeFound("body.contains=" + DEFAULT_BODY);

        // Get all the webArticleList where body contains UPDATED_BODY
        defaultWebArticleShouldNotBeFound("body.contains=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    void getAllWebArticlesByBodyNotContainsSomething() throws Exception {
        // Initialize the database
        webArticleRepository.saveAndFlush(webArticle);

        // Get all the webArticleList where body does not contain DEFAULT_BODY
        defaultWebArticleShouldNotBeFound("body.doesNotContain=" + DEFAULT_BODY);

        // Get all the webArticleList where body does not contain UPDATED_BODY
        defaultWebArticleShouldBeFound("body.doesNotContain=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    void getAllWebArticlesByAuthorIsEqualToSomething() throws Exception {
        // Initialize the database
        webArticleRepository.saveAndFlush(webArticle);

        // Get all the webArticleList where author equals to DEFAULT_AUTHOR
        defaultWebArticleShouldBeFound("author.equals=" + DEFAULT_AUTHOR);

        // Get all the webArticleList where author equals to UPDATED_AUTHOR
        defaultWebArticleShouldNotBeFound("author.equals=" + UPDATED_AUTHOR);
    }

    @Test
    @Transactional
    void getAllWebArticlesByAuthorIsInShouldWork() throws Exception {
        // Initialize the database
        webArticleRepository.saveAndFlush(webArticle);

        // Get all the webArticleList where author in DEFAULT_AUTHOR or UPDATED_AUTHOR
        defaultWebArticleShouldBeFound("author.in=" + DEFAULT_AUTHOR + "," + UPDATED_AUTHOR);

        // Get all the webArticleList where author equals to UPDATED_AUTHOR
        defaultWebArticleShouldNotBeFound("author.in=" + UPDATED_AUTHOR);
    }

    @Test
    @Transactional
    void getAllWebArticlesByAuthorIsNullOrNotNull() throws Exception {
        // Initialize the database
        webArticleRepository.saveAndFlush(webArticle);

        // Get all the webArticleList where author is not null
        defaultWebArticleShouldBeFound("author.specified=true");

        // Get all the webArticleList where author is null
        defaultWebArticleShouldNotBeFound("author.specified=false");
    }

    @Test
    @Transactional
    void getAllWebArticlesByAuthorContainsSomething() throws Exception {
        // Initialize the database
        webArticleRepository.saveAndFlush(webArticle);

        // Get all the webArticleList where author contains DEFAULT_AUTHOR
        defaultWebArticleShouldBeFound("author.contains=" + DEFAULT_AUTHOR);

        // Get all the webArticleList where author contains UPDATED_AUTHOR
        defaultWebArticleShouldNotBeFound("author.contains=" + UPDATED_AUTHOR);
    }

    @Test
    @Transactional
    void getAllWebArticlesByAuthorNotContainsSomething() throws Exception {
        // Initialize the database
        webArticleRepository.saveAndFlush(webArticle);

        // Get all the webArticleList where author does not contain DEFAULT_AUTHOR
        defaultWebArticleShouldNotBeFound("author.doesNotContain=" + DEFAULT_AUTHOR);

        // Get all the webArticleList where author does not contain UPDATED_AUTHOR
        defaultWebArticleShouldBeFound("author.doesNotContain=" + UPDATED_AUTHOR);
    }

    @Test
    @Transactional
    void getAllWebArticlesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        webArticleRepository.saveAndFlush(webArticle);

        // Get all the webArticleList where date equals to DEFAULT_DATE
        defaultWebArticleShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the webArticleList where date equals to UPDATED_DATE
        defaultWebArticleShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllWebArticlesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        webArticleRepository.saveAndFlush(webArticle);

        // Get all the webArticleList where date in DEFAULT_DATE or UPDATED_DATE
        defaultWebArticleShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the webArticleList where date equals to UPDATED_DATE
        defaultWebArticleShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllWebArticlesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        webArticleRepository.saveAndFlush(webArticle);

        // Get all the webArticleList where date is not null
        defaultWebArticleShouldBeFound("date.specified=true");

        // Get all the webArticleList where date is null
        defaultWebArticleShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllWebArticlesByCommentsIsEqualToSomething() throws Exception {
        Comment comments;
        if (TestUtil.findAll(em, Comment.class).isEmpty()) {
            webArticleRepository.saveAndFlush(webArticle);
            comments = CommentResourceIT.createEntity(em);
        } else {
            comments = TestUtil.findAll(em, Comment.class).get(0);
        }
        em.persist(comments);
        em.flush();
        webArticle.addComments(comments);
        webArticleRepository.saveAndFlush(webArticle);
        Long commentsId = comments.getId();

        // Get all the webArticleList where comments equals to commentsId
        defaultWebArticleShouldBeFound("commentsId.equals=" + commentsId);

        // Get all the webArticleList where comments equals to (commentsId + 1)
        defaultWebArticleShouldNotBeFound("commentsId.equals=" + (commentsId + 1));
    }

    @Test
    @Transactional
    void getAllWebArticlesByTagsIsEqualToSomething() throws Exception {
        Tag tags;
        if (TestUtil.findAll(em, Tag.class).isEmpty()) {
            webArticleRepository.saveAndFlush(webArticle);
            tags = TagResourceIT.createEntity(em);
        } else {
            tags = TestUtil.findAll(em, Tag.class).get(0);
        }
        em.persist(tags);
        em.flush();
        webArticle.addTags(tags);
        webArticleRepository.saveAndFlush(webArticle);
        Long tagsId = tags.getId();

        // Get all the webArticleList where tags equals to tagsId
        defaultWebArticleShouldBeFound("tagsId.equals=" + tagsId);

        // Get all the webArticleList where tags equals to (tagsId + 1)
        defaultWebArticleShouldNotBeFound("tagsId.equals=" + (tagsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWebArticleShouldBeFound(String filter) throws Exception {
        restWebArticleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(webArticle.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));

        // Check, that the count call also returns 1
        restWebArticleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWebArticleShouldNotBeFound(String filter) throws Exception {
        restWebArticleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWebArticleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWebArticle() throws Exception {
        // Get the webArticle
        restWebArticleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWebArticle() throws Exception {
        // Initialize the database
        webArticleRepository.saveAndFlush(webArticle);

        int databaseSizeBeforeUpdate = webArticleRepository.findAll().size();

        // Update the webArticle
        WebArticle updatedWebArticle = webArticleRepository.findById(webArticle.getId()).get();
        // Disconnect from session so that the updates on updatedWebArticle are not directly saved in db
        em.detach(updatedWebArticle);
        updatedWebArticle
            .title(UPDATED_TITLE)
            .body(UPDATED_BODY)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .author(UPDATED_AUTHOR)
            .date(UPDATED_DATE);
        WebArticleDTO webArticleDTO = webArticleMapper.toDto(updatedWebArticle);

        restWebArticleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, webArticleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(webArticleDTO))
            )
            .andExpect(status().isOk());

        // Validate the WebArticle in the database
        List<WebArticle> webArticleList = webArticleRepository.findAll();
        assertThat(webArticleList).hasSize(databaseSizeBeforeUpdate);
        WebArticle testWebArticle = webArticleList.get(webArticleList.size() - 1);
        assertThat(testWebArticle.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testWebArticle.getBody()).isEqualTo(UPDATED_BODY);
        assertThat(testWebArticle.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testWebArticle.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testWebArticle.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testWebArticle.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingWebArticle() throws Exception {
        int databaseSizeBeforeUpdate = webArticleRepository.findAll().size();
        webArticle.setId(count.incrementAndGet());

        // Create the WebArticle
        WebArticleDTO webArticleDTO = webArticleMapper.toDto(webArticle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWebArticleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, webArticleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(webArticleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WebArticle in the database
        List<WebArticle> webArticleList = webArticleRepository.findAll();
        assertThat(webArticleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWebArticle() throws Exception {
        int databaseSizeBeforeUpdate = webArticleRepository.findAll().size();
        webArticle.setId(count.incrementAndGet());

        // Create the WebArticle
        WebArticleDTO webArticleDTO = webArticleMapper.toDto(webArticle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWebArticleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(webArticleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WebArticle in the database
        List<WebArticle> webArticleList = webArticleRepository.findAll();
        assertThat(webArticleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWebArticle() throws Exception {
        int databaseSizeBeforeUpdate = webArticleRepository.findAll().size();
        webArticle.setId(count.incrementAndGet());

        // Create the WebArticle
        WebArticleDTO webArticleDTO = webArticleMapper.toDto(webArticle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWebArticleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(webArticleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WebArticle in the database
        List<WebArticle> webArticleList = webArticleRepository.findAll();
        assertThat(webArticleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWebArticleWithPatch() throws Exception {
        // Initialize the database
        webArticleRepository.saveAndFlush(webArticle);

        int databaseSizeBeforeUpdate = webArticleRepository.findAll().size();

        // Update the webArticle using partial update
        WebArticle partialUpdatedWebArticle = new WebArticle();
        partialUpdatedWebArticle.setId(webArticle.getId());

        partialUpdatedWebArticle.title(UPDATED_TITLE).body(UPDATED_BODY).author(UPDATED_AUTHOR);

        restWebArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWebArticle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWebArticle))
            )
            .andExpect(status().isOk());

        // Validate the WebArticle in the database
        List<WebArticle> webArticleList = webArticleRepository.findAll();
        assertThat(webArticleList).hasSize(databaseSizeBeforeUpdate);
        WebArticle testWebArticle = webArticleList.get(webArticleList.size() - 1);
        assertThat(testWebArticle.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testWebArticle.getBody()).isEqualTo(UPDATED_BODY);
        assertThat(testWebArticle.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testWebArticle.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testWebArticle.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testWebArticle.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void fullUpdateWebArticleWithPatch() throws Exception {
        // Initialize the database
        webArticleRepository.saveAndFlush(webArticle);

        int databaseSizeBeforeUpdate = webArticleRepository.findAll().size();

        // Update the webArticle using partial update
        WebArticle partialUpdatedWebArticle = new WebArticle();
        partialUpdatedWebArticle.setId(webArticle.getId());

        partialUpdatedWebArticle
            .title(UPDATED_TITLE)
            .body(UPDATED_BODY)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .author(UPDATED_AUTHOR)
            .date(UPDATED_DATE);

        restWebArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWebArticle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWebArticle))
            )
            .andExpect(status().isOk());

        // Validate the WebArticle in the database
        List<WebArticle> webArticleList = webArticleRepository.findAll();
        assertThat(webArticleList).hasSize(databaseSizeBeforeUpdate);
        WebArticle testWebArticle = webArticleList.get(webArticleList.size() - 1);
        assertThat(testWebArticle.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testWebArticle.getBody()).isEqualTo(UPDATED_BODY);
        assertThat(testWebArticle.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testWebArticle.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testWebArticle.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testWebArticle.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingWebArticle() throws Exception {
        int databaseSizeBeforeUpdate = webArticleRepository.findAll().size();
        webArticle.setId(count.incrementAndGet());

        // Create the WebArticle
        WebArticleDTO webArticleDTO = webArticleMapper.toDto(webArticle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWebArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, webArticleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(webArticleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WebArticle in the database
        List<WebArticle> webArticleList = webArticleRepository.findAll();
        assertThat(webArticleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWebArticle() throws Exception {
        int databaseSizeBeforeUpdate = webArticleRepository.findAll().size();
        webArticle.setId(count.incrementAndGet());

        // Create the WebArticle
        WebArticleDTO webArticleDTO = webArticleMapper.toDto(webArticle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWebArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(webArticleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WebArticle in the database
        List<WebArticle> webArticleList = webArticleRepository.findAll();
        assertThat(webArticleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWebArticle() throws Exception {
        int databaseSizeBeforeUpdate = webArticleRepository.findAll().size();
        webArticle.setId(count.incrementAndGet());

        // Create the WebArticle
        WebArticleDTO webArticleDTO = webArticleMapper.toDto(webArticle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWebArticleMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(webArticleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WebArticle in the database
        List<WebArticle> webArticleList = webArticleRepository.findAll();
        assertThat(webArticleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWebArticle() throws Exception {
        // Initialize the database
        webArticleRepository.saveAndFlush(webArticle);

        int databaseSizeBeforeDelete = webArticleRepository.findAll().size();

        // Delete the webArticle
        restWebArticleMockMvc
            .perform(delete(ENTITY_API_URL_ID, webArticle.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WebArticle> webArticleList = webArticleRepository.findAll();
        assertThat(webArticleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
