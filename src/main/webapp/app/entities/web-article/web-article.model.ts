import dayjs from 'dayjs/esm';
import { ITag } from 'app/entities/tag/tag.model';

export interface IWebArticle {
  id: number;
  title?: string | null;
  body?: string | null;
  image?: string | null;
  imageContentType?: string | null;
  author?: string | null;
  date?: dayjs.Dayjs | null;
  tags?: Pick<ITag, 'id'>[] | null;
}

export type NewWebArticle = Omit<IWebArticle, 'id'> & { id: null };
