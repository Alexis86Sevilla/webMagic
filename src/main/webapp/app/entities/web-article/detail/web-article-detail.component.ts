import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWebArticle } from '../web-article.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-web-article-detail',
  templateUrl: './web-article-detail.component.html',
})
export class WebArticleDetailComponent implements OnInit {
  webArticle: IWebArticle | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ webArticle }) => {
      this.webArticle = webArticle;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
