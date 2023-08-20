import { IWebArticle } from 'app/entities/web-article/web-article.model';

export interface ITag {
  id: number;
  name?: string | null;
  articles?: Pick<IWebArticle, 'id'>[] | null;
}

export type NewTag = Omit<ITag, 'id'> & { id: null };
