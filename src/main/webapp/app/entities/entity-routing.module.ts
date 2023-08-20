import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'web-article',
        data: { pageTitle: 'magicwebApp.webArticle.home.title' },
        loadChildren: () => import('./web-article/web-article.module').then(m => m.WebArticleModule),
      },
      {
        path: 'card',
        data: { pageTitle: 'magicwebApp.card.home.title' },
        loadChildren: () => import('./card/card.module').then(m => m.CardModule),
      },
      {
        path: 'comment',
        data: { pageTitle: 'magicwebApp.comment.home.title' },
        loadChildren: () => import('./comment/comment.module').then(m => m.CommentModule),
      },
      {
        path: 'tag',
        data: { pageTitle: 'magicwebApp.tag.home.title' },
        loadChildren: () => import('./tag/tag.module').then(m => m.TagModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
