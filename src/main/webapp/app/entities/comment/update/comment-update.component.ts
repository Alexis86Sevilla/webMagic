import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CommentFormService, CommentFormGroup } from './comment-form.service';
import { IComment } from '../comment.model';
import { CommentService } from '../service/comment.service';
import { IWebArticle } from 'app/entities/web-article/web-article.model';
import { WebArticleService } from 'app/entities/web-article/service/web-article.service';

@Component({
  selector: 'jhi-comment-update',
  templateUrl: './comment-update.component.html',
})
export class CommentUpdateComponent implements OnInit {
  isSaving = false;
  comment: IComment | null = null;

  webArticlesSharedCollection: IWebArticle[] = [];

  editForm: CommentFormGroup = this.commentFormService.createCommentFormGroup();

  constructor(
    protected commentService: CommentService,
    protected commentFormService: CommentFormService,
    protected webArticleService: WebArticleService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareWebArticle = (o1: IWebArticle | null, o2: IWebArticle | null): boolean => this.webArticleService.compareWebArticle(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ comment }) => {
      this.comment = comment;
      if (comment) {
        this.updateForm(comment);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const comment = this.commentFormService.getComment(this.editForm);
    if (comment.id !== null) {
      this.subscribeToSaveResponse(this.commentService.update(comment));
    } else {
      this.subscribeToSaveResponse(this.commentService.create(comment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IComment>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(comment: IComment): void {
    this.comment = comment;
    this.commentFormService.resetForm(this.editForm, comment);

    this.webArticlesSharedCollection = this.webArticleService.addWebArticleToCollectionIfMissing<IWebArticle>(
      this.webArticlesSharedCollection,
      comment.webArticle
    );
  }

  protected loadRelationshipsOptions(): void {
    this.webArticleService
      .query()
      .pipe(map((res: HttpResponse<IWebArticle[]>) => res.body ?? []))
      .pipe(
        map((webArticles: IWebArticle[]) =>
          this.webArticleService.addWebArticleToCollectionIfMissing<IWebArticle>(webArticles, this.comment?.webArticle)
        )
      )
      .subscribe((webArticles: IWebArticle[]) => (this.webArticlesSharedCollection = webArticles));
  }
}
