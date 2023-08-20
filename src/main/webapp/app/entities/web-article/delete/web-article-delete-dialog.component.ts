import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWebArticle } from '../web-article.model';
import { WebArticleService } from '../service/web-article.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './web-article-delete-dialog.component.html',
})
export class WebArticleDeleteDialogComponent {
  webArticle?: IWebArticle;

  constructor(protected webArticleService: WebArticleService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.webArticleService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
