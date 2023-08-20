import { ICard, NewCard } from './card.model';

export const sampleWithRequiredData: ICard = {
  id: 57013,
};

export const sampleWithPartialData: ICard = {
  id: 78922,
  idMagic: 'Guapa',
  nameEnglish: 'Heredado',
  nameSpanish: 'Avon Money',
  imageUrl: 'Riel',
};

export const sampleWithFullData: ICard = {
  id: 50175,
  idMagic: 'Cataluña',
  nameEnglish: 'Singapur',
  nameSpanish: 'Deportes next-generation International',
  imageUrl: 'Realineado',
};

export const sampleWithNewData: NewCard = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
