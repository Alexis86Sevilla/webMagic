import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWebArticle } from '../web-article.model';
import { WebArticleService } from '../service/web-article.service';

@Injectable({ providedIn: 'root' })
export class WebArticleRoutingResolveService implements Resolve<IWebArticle | null> {
  constructor(protected service: WebArticleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWebArticle | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((webArticle: HttpResponse<IWebArticle>) => {
          if (webArticle.body) {
            return of(webArticle.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
