import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWebArticle, NewWebArticle } from '../web-article.model';

export type PartialUpdateWebArticle = Partial<IWebArticle> & Pick<IWebArticle, 'id'>;

type RestOf<T extends IWebArticle | NewWebArticle> = Omit<T, 'date'> & {
  date?: string | null;
};

export type RestWebArticle = RestOf<IWebArticle>;

export type NewRestWebArticle = RestOf<NewWebArticle>;

export type PartialUpdateRestWebArticle = RestOf<PartialUpdateWebArticle>;

export type EntityResponseType = HttpResponse<IWebArticle>;
export type EntityArrayResponseType = HttpResponse<IWebArticle[]>;

@Injectable({ providedIn: 'root' })
export class WebArticleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/web-articles');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(webArticle: NewWebArticle): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(webArticle);
    return this.http
      .post<RestWebArticle>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(webArticle: IWebArticle): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(webArticle);
    return this.http
      .put<RestWebArticle>(`${this.resourceUrl}/${this.getWebArticleIdentifier(webArticle)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(webArticle: PartialUpdateWebArticle): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(webArticle);
    return this.http
      .patch<RestWebArticle>(`${this.resourceUrl}/${this.getWebArticleIdentifier(webArticle)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestWebArticle>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestWebArticle[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getWebArticleIdentifier(webArticle: Pick<IWebArticle, 'id'>): number {
    return webArticle.id;
  }

  compareWebArticle(o1: Pick<IWebArticle, 'id'> | null, o2: Pick<IWebArticle, 'id'> | null): boolean {
    return o1 && o2 ? this.getWebArticleIdentifier(o1) === this.getWebArticleIdentifier(o2) : o1 === o2;
  }

  addWebArticleToCollectionIfMissing<Type extends Pick<IWebArticle, 'id'>>(
    webArticleCollection: Type[],
    ...webArticlesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const webArticles: Type[] = webArticlesToCheck.filter(isPresent);
    if (webArticles.length > 0) {
      const webArticleCollectionIdentifiers = webArticleCollection.map(webArticleItem => this.getWebArticleIdentifier(webArticleItem)!);
      const webArticlesToAdd = webArticles.filter(webArticleItem => {
        const webArticleIdentifier = this.getWebArticleIdentifier(webArticleItem);
        if (webArticleCollectionIdentifiers.includes(webArticleIdentifier)) {
          return false;
        }
        webArticleCollectionIdentifiers.push(webArticleIdentifier);
        return true;
      });
      return [...webArticlesToAdd, ...webArticleCollection];
    }
    return webArticleCollection;
  }

  protected convertDateFromClient<T extends IWebArticle | NewWebArticle | PartialUpdateWebArticle>(webArticle: T): RestOf<T> {
    return {
      ...webArticle,
      date: webArticle.date?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restWebArticle: RestWebArticle): IWebArticle {
    return {
      ...restWebArticle,
      date: restWebArticle.date ? dayjs(restWebArticle.date) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestWebArticle>): HttpResponse<IWebArticle> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestWebArticle[]>): HttpResponse<IWebArticle[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
