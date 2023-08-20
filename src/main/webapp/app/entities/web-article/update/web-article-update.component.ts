import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { WebArticleFormService, WebArticleFormGroup } from './web-article-form.service';
import { IWebArticle } from '../web-article.model';
import { WebArticleService } from '../service/web-article.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ITag } from 'app/entities/tag/tag.model';
import { TagService } from 'app/entities/tag/service/tag.service';

@Component({
  selector: 'jhi-web-article-update',
  templateUrl: './web-article-update.component.html',
})
export class WebArticleUpdateComponent implements OnInit {
  isSaving = false;
  webArticle: IWebArticle | null = null;

  tagsSharedCollection: ITag[] = [];

  editForm: WebArticleFormGroup = this.webArticleFormService.createWebArticleFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected webArticleService: WebArticleService,
    protected webArticleFormService: WebArticleFormService,
    protected tagService: TagService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareTag = (o1: ITag | null, o2: ITag | null): boolean => this.tagService.compareTag(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ webArticle }) => {
      this.webArticle = webArticle;
      if (webArticle) {
        this.updateForm(webArticle);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('magicwebApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const webArticle = this.webArticleFormService.getWebArticle(this.editForm);
    if (webArticle.id !== null) {
      this.subscribeToSaveResponse(this.webArticleService.update(webArticle));
    } else {
      this.subscribeToSaveResponse(this.webArticleService.create(webArticle));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWebArticle>>): void {
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

  protected updateForm(webArticle: IWebArticle): void {
    this.webArticle = webArticle;
    this.webArticleFormService.resetForm(this.editForm, webArticle);

    this.tagsSharedCollection = this.tagService.addTagToCollectionIfMissing<ITag>(this.tagsSharedCollection, ...(webArticle.tags ?? []));
  }

  protected loadRelationshipsOptions(): void {
    this.tagService
      .query()
      .pipe(map((res: HttpResponse<ITag[]>) => res.body ?? []))
      .pipe(map((tags: ITag[]) => this.tagService.addTagToCollectionIfMissing<ITag>(tags, ...(this.webArticle?.tags ?? []))))
      .subscribe((tags: ITag[]) => (this.tagsSharedCollection = tags));
  }
}
