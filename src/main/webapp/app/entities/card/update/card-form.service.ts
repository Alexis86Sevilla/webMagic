import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICard, NewCard } from '../card.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICard for edit and NewCardFormGroupInput for create.
 */
type CardFormGroupInput = ICard | PartialWithRequiredKeyOf<NewCard>;

type CardFormDefaults = Pick<NewCard, 'id'>;

type CardFormGroupContent = {
  id: FormControl<ICard['id'] | NewCard['id']>;
  idMagic: FormControl<ICard['idMagic']>;
  nameEnglish: FormControl<ICard['nameEnglish']>;
  nameSpanish: FormControl<ICard['nameSpanish']>;
  imageUrl: FormControl<ICard['imageUrl']>;
};

export type CardFormGroup = FormGroup<CardFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CardFormService {
  createCardFormGroup(card: CardFormGroupInput = { id: null }): CardFormGroup {
    const cardRawValue = {
      ...this.getFormDefaults(),
      ...card,
    };
    return new FormGroup<CardFormGroupContent>({
      id: new FormControl(
        { value: cardRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      idMagic: new FormControl(cardRawValue.idMagic),
      nameEnglish: new FormControl(cardRawValue.nameEnglish),
      nameSpanish: new FormControl(cardRawValue.nameSpanish),
      imageUrl: new FormControl(cardRawValue.imageUrl),
    });
  }

  getCard(form: CardFormGroup): ICard | NewCard {
    return form.getRawValue() as ICard | NewCard;
  }

  resetForm(form: CardFormGroup, card: CardFormGroupInput): void {
    const cardRawValue = { ...this.getFormDefaults(), ...card };
    form.reset(
      {
        ...cardRawValue,
        id: { value: cardRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CardFormDefaults {
    return {
      id: null,
    };
  }
}
