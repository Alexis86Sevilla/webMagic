export interface ICard {
  id: number;
  idMagic?: string | null;
  nameEnglish?: string | null;
  nameSpanish?: string | null;
  imageUrl?: string | null;
}

export type NewCard = Omit<ICard, 'id'> & { id: null };
