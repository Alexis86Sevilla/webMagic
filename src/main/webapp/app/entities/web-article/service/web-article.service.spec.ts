import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IWebArticle } from '../web-article.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../web-article.test-samples';

import { WebArticleService, RestWebArticle } from './web-article.service';

const requireRestSample: RestWebArticle = {
  ...sampleWithRequiredData,
  date: sampleWithRequiredData.date?.toJSON(),
};

describe('WebArticle Service', () => {
  let service: WebArticleService;
  let httpMock: HttpTestingController;
  let expectedResult: IWebArticle | IWebArticle[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(WebArticleService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a WebArticle', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const webArticle = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(webArticle).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a WebArticle', () => {
      const webArticle = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(webArticle).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a WebArticle', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of WebArticle', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a WebArticle', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addWebArticleToCollectionIfMissing', () => {
      it('should add a WebArticle to an empty array', () => {
        const webArticle: IWebArticle = sampleWithRequiredData;
        expectedResult = service.addWebArticleToCollectionIfMissing([], webArticle);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(webArticle);
      });

      it('should not add a WebArticle to an array that contains it', () => {
        const webArticle: IWebArticle = sampleWithRequiredData;
        const webArticleCollection: IWebArticle[] = [
          {
            ...webArticle,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addWebArticleToCollectionIfMissing(webArticleCollection, webArticle);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WebArticle to an array that doesn't contain it", () => {
        const webArticle: IWebArticle = sampleWithRequiredData;
        const webArticleCollection: IWebArticle[] = [sampleWithPartialData];
        expectedResult = service.addWebArticleToCollectionIfMissing(webArticleCollection, webArticle);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(webArticle);
      });

      it('should add only unique WebArticle to an array', () => {
        const webArticleArray: IWebArticle[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const webArticleCollection: IWebArticle[] = [sampleWithRequiredData];
        expectedResult = service.addWebArticleToCollectionIfMissing(webArticleCollection, ...webArticleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const webArticle: IWebArticle = sampleWithRequiredData;
        const webArticle2: IWebArticle = sampleWithPartialData;
        expectedResult = service.addWebArticleToCollectionIfMissing([], webArticle, webArticle2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(webArticle);
        expect(expectedResult).toContain(webArticle2);
      });

      it('should accept null and undefined values', () => {
        const webArticle: IWebArticle = sampleWithRequiredData;
        expectedResult = service.addWebArticleToCollectionIfMissing([], null, webArticle, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(webArticle);
      });

      it('should return initial array if no WebArticle is added', () => {
        const webArticleCollection: IWebArticle[] = [sampleWithRequiredData];
        expectedResult = service.addWebArticleToCollectionIfMissing(webArticleCollection, undefined, null);
        expect(expectedResult).toEqual(webArticleCollection);
      });
    });

    describe('compareWebArticle', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareWebArticle(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareWebArticle(entity1, entity2);
        const compareResult2 = service.compareWebArticle(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareWebArticle(entity1, entity2);
        const compareResult2 = service.compareWebArticle(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareWebArticle(entity1, entity2);
        const compareResult2 = service.compareWebArticle(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
