import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CommentFormService } from './comment-form.service';
import { CommentService } from '../service/comment.service';
import { IComment } from '../comment.model';
import { IWebArticle } from 'app/entities/web-article/web-article.model';
import { WebArticleService } from 'app/entities/web-article/service/web-article.service';

import { CommentUpdateComponent } from './comment-update.component';

describe('Comment Management Update Component', () => {
  let comp: CommentUpdateComponent;
  let fixture: ComponentFixture<CommentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let commentFormService: CommentFormService;
  let commentService: CommentService;
  let webArticleService: WebArticleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CommentUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CommentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CommentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    commentFormService = TestBed.inject(CommentFormService);
    commentService = TestBed.inject(CommentService);
    webArticleService = TestBed.inject(WebArticleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call WebArticle query and add missing value', () => {
      const comment: IComment = { id: 456 };
      const webArticle: IWebArticle = { id: 14190 };
      comment.webArticle = webArticle;

      const webArticleCollection: IWebArticle[] = [{ id: 86954 }];
      jest.spyOn(webArticleService, 'query').mockReturnValue(of(new HttpResponse({ body: webArticleCollection })));
      const additionalWebArticles = [webArticle];
      const expectedCollection: IWebArticle[] = [...additionalWebArticles, ...webArticleCollection];
      jest.spyOn(webArticleService, 'addWebArticleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ comment });
      comp.ngOnInit();

      expect(webArticleService.query).toHaveBeenCalled();
      expect(webArticleService.addWebArticleToCollectionIfMissing).toHaveBeenCalledWith(
        webArticleCollection,
        ...additionalWebArticles.map(expect.objectContaining)
      );
      expect(comp.webArticlesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const comment: IComment = { id: 456 };
      const webArticle: IWebArticle = { id: 73922 };
      comment.webArticle = webArticle;

      activatedRoute.data = of({ comment });
      comp.ngOnInit();

      expect(comp.webArticlesSharedCollection).toContain(webArticle);
      expect(comp.comment).toEqual(comment);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IComment>>();
      const comment = { id: 123 };
      jest.spyOn(commentFormService, 'getComment').mockReturnValue(comment);
      jest.spyOn(commentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ comment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: comment }));
      saveSubject.complete();

      // THEN
      expect(commentFormService.getComment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(commentService.update).toHaveBeenCalledWith(expect.objectContaining(comment));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IComment>>();
      const comment = { id: 123 };
      jest.spyOn(commentFormService, 'getComment').mockReturnValue({ id: null });
      jest.spyOn(commentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ comment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: comment }));
      saveSubject.complete();

      // THEN
      expect(commentFormService.getComment).toHaveBeenCalled();
      expect(commentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IComment>>();
      const comment = { id: 123 };
      jest.spyOn(commentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ comment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(commentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareWebArticle', () => {
      it('Should forward to webArticleService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(webArticleService, 'compareWebArticle');
        comp.compareWebArticle(entity, entity2);
        expect(webArticleService.compareWebArticle).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
