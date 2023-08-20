import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WebArticleComponent } from '../list/web-article.component';
import { WebArticleDetailComponent } from '../detail/web-article-detail.component';
import { WebArticleUpdateComponent } from '../update/web-article-update.component';
import { WebArticleRoutingResolveService } from './web-article-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const webArticleRoute: Routes = [
  {
    path: '',
    component: WebArticleComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WebArticleDetailComponent,
    resolve: {
      webArticle: WebArticleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WebArticleUpdateComponent,
    resolve: {
      webArticle: WebArticleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WebArticleUpdateComponent,
    resolve: {
      webArticle: WebArticleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(webArticleRoute)],
  exports: [RouterModule],
})
export class WebArticleRoutingModule {}
