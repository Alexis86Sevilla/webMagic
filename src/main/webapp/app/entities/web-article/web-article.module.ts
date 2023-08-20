import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { WebArticleComponent } from './list/web-article.component';
import { WebArticleDetailComponent } from './detail/web-article-detail.component';
import { WebArticleUpdateComponent } from './update/web-article-update.component';
import { WebArticleDeleteDialogComponent } from './delete/web-article-delete-dialog.component';
import { WebArticleRoutingModule } from './route/web-article-routing.module';

@NgModule({
  imports: [SharedModule, WebArticleRoutingModule],
  declarations: [WebArticleComponent, WebArticleDetailComponent, WebArticleUpdateComponent, WebArticleDeleteDialogComponent],
})
export class WebArticleModule {}
