import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../web-article.test-samples';

import { WebArticleFormService } from './web-article-form.service';

describe('WebArticle Form Service', () => {
  let service: WebArticleFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WebArticleFormService);
  });

  describe('Service methods', () => {
    describe('createWebArticleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createWebArticleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            body: expect.any(Object),
            image: expect.any(Object),
            author: expect.any(Object),
            date: expect.any(Object),
            tags: expect.any(Object),
          })
        );
      });

      it('passing IWebArticle should create a new form with FormGroup', () => {
        const formGroup = service.createWebArticleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            body: expect.any(Object),
            image: expect.any(Object),
            author: expect.any(Object),
            date: expect.any(Object),
            tags: expect.any(Object),
          })
        );
      });
    });

    describe('getWebArticle', () => {
      it('should return NewWebArticle for default WebArticle initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createWebArticleFormGroup(sampleWithNewData);

        const webArticle = service.getWebArticle(formGroup) as any;

        expect(webArticle).toMatchObject(sampleWithNewData);
      });

      it('should return NewWebArticle for empty WebArticle initial value', () => {
        const formGroup = service.createWebArticleFormGroup();

        const webArticle = service.getWebArticle(formGroup) as any;

        expect(webArticle).toMatchObject({});
      });

      it('should return IWebArticle', () => {
        const formGroup = service.createWebArticleFormGroup(sampleWithRequiredData);

        const webArticle = service.getWebArticle(formGroup) as any;

        expect(webArticle).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IWebArticle should not enable id FormControl', () => {
        const formGroup = service.createWebArticleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewWebArticle should disable id FormControl', () => {
        const formGroup = service.createWebArticleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
