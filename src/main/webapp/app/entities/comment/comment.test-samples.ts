import dayjs from 'dayjs/esm';

import { IComment, NewComment } from './comment.model';

export const sampleWithRequiredData: IComment = {
  id: 78899,
};

export const sampleWithPartialData: IComment = {
  id: 38860,
  date: dayjs('2023-08-20T12:30'),
};

export const sampleWithFullData: IComment = {
  id: 78272,
  body: 'Andalucía Global',
  date: dayjs('2023-08-20T12:00'),
};

export const sampleWithNewData: NewComment = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
