import dayjs from 'dayjs/esm';
import { IWebArticle } from 'app/entities/web-article/web-article.model';

export interface IComment {
  id: number;
  body?: string | null;
  date?: dayjs.Dayjs | null;
  webArticle?: Pick<IWebArticle, 'id'> | null;
}

export type NewComment = Omit<IComment, 'id'> & { id: null };
