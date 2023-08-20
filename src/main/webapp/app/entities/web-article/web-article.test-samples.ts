import dayjs from 'dayjs/esm';

import { IWebArticle, NewWebArticle } from './web-article.model';

export const sampleWithRequiredData: IWebArticle = {
  id: 95639,
};

export const sampleWithPartialData: IWebArticle = {
  id: 56539,
  title: 'optimizada Usabilidad card',
  body: 'Pequeño tangible Agente',
  author: 'Videojuegos payment firewall',
};

export const sampleWithFullData: IWebArticle = {
  id: 33474,
  title: 'primary',
  body: 'Proactivo',
  image: '../fake-data/blob/hipster.png',
  imageContentType: 'unknown',
  author: 'Morado',
  date: dayjs('2023-08-20T06:09'),
};

export const sampleWithNewData: NewWebArticle = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
