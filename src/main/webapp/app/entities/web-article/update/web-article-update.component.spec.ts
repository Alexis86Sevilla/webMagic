import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { WebArticleFormService } from './web-article-form.service';
import { WebArticleService } from '../service/web-article.service';
import { IWebArticle } from '../web-article.model';
import { ITag } from 'app/entities/tag/tag.model';
import { TagService } from 'app/entities/tag/service/tag.service';

import { WebArticleUpdateComponent } from './web-article-update.component';

describe('WebArticle Management Update Component', () => {
  let comp: WebArticleUpdateComponent;
  let fixture: ComponentFixture<WebArticleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let webArticleFormService: WebArticleFormService;
  let webArticleService: WebArticleService;
  let tagService: TagService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [WebArticleUpdateComponent],
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
      .overrideTemplate(WebArticleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WebArticleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    webArticleFormService = TestBed.inject(WebArticleFormService);
    webArticleService = TestBed.inject(WebArticleService);
    tagService = TestBed.inject(TagService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Tag query and add missing value', () => {
      const webArticle: IWebArticle = { id: 456 };
      const tags: ITag[] = [{ id: 73971 }];
      webArticle.tags = tags;

      const tagCollection: ITag[] = [{ id: 66013 }];
      jest.spyOn(tagService, 'query').mockReturnValue(of(new HttpResponse({ body: tagCollection })));
      const additionalTags = [...tags];
      const expectedCollection: ITag[] = [...additionalTags, ...tagCollection];
      jest.spyOn(tagService, 'addTagToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ webArticle });
      comp.ngOnInit();

      expect(tagService.query).toHaveBeenCalled();
      expect(tagService.addTagToCollectionIfMissing).toHaveBeenCalledWith(tagCollection, ...additionalTags.map(expect.objectContaining));
      expect(comp.tagsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const webArticle: IWebArticle = { id: 456 };
      const tags: ITag = { id: 55528 };
      webArticle.tags = [tags];

      activatedRoute.data = of({ webArticle });
      comp.ngOnInit();

      expect(comp.tagsSharedCollection).toContain(tags);
      expect(comp.webArticle).toEqual(webArticle);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWebArticle>>();
      const webArticle = { id: 123 };
      jest.spyOn(webArticleFormService, 'getWebArticle').mockReturnValue(webArticle);
      jest.spyOn(webArticleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ webArticle });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: webArticle }));
      saveSubject.complete();

      // THEN
      expect(webArticleFormService.getWebArticle).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(webArticleService.update).toHaveBeenCalledWith(expect.objectContaining(webArticle));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWebArticle>>();
      const webArticle = { id: 123 };
      jest.spyOn(webArticleFormService, 'getWebArticle').mockReturnValue({ id: null });
      jest.spyOn(webArticleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ webArticle: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: webArticle }));
      saveSubject.complete();

      // THEN
      expect(webArticleFormService.getWebArticle).toHaveBeenCalled();
      expect(webArticleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWebArticle>>();
      const webArticle = { id: 123 };
      jest.spyOn(webArticleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ webArticle });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(webArticleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTag', () => {
      it('Should forward to tagService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(tagService, 'compareTag');
        comp.compareTag(entity, entity2);
        expect(tagService.compareTag).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
