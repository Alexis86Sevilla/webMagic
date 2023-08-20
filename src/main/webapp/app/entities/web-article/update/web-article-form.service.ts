import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IWebArticle, NewWebArticle } from '../web-article.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWebArticle for edit and NewWebArticleFormGroupInput for create.
 */
type WebArticleFormGroupInput = IWebArticle | PartialWithRequiredKeyOf<NewWebArticle>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IWebArticle | NewWebArticle> = Omit<T, 'date'> & {
  date?: string | null;
};

type WebArticleFormRawValue = FormValueOf<IWebArticle>;

type NewWebArticleFormRawValue = FormValueOf<NewWebArticle>;

type WebArticleFormDefaults = Pick<NewWebArticle, 'id' | 'date' | 'tags'>;

type WebArticleFormGroupContent = {
  id: FormControl<WebArticleFormRawValue['id'] | NewWebArticle['id']>;
  title: FormControl<WebArticleFormRawValue['title']>;
  body: FormControl<WebArticleFormRawValue['body']>;
  image: FormControl<WebArticleFormRawValue['image']>;
  imageContentType: FormControl<WebArticleFormRawValue['imageContentType']>;
  author: FormControl<WebArticleFormRawValue['author']>;
  date: FormControl<WebArticleFormRawValue['date']>;
  tags: FormControl<WebArticleFormRawValue['tags']>;
};

export type WebArticleFormGroup = FormGroup<WebArticleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WebArticleFormService {
  createWebArticleFormGroup(webArticle: WebArticleFormGroupInput = { id: null }): WebArticleFormGroup {
    const webArticleRawValue = this.convertWebArticleToWebArticleRawValue({
      ...this.getFormDefaults(),
      ...webArticle,
    });
    return new FormGroup<WebArticleFormGroupContent>({
      id: new FormControl(
        { value: webArticleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      title: new FormControl(webArticleRawValue.title),
      body: new FormControl(webArticleRawValue.body),
      image: new FormControl(webArticleRawValue.image),
      imageContentType: new FormControl(webArticleRawValue.imageContentType),
      author: new FormControl(webArticleRawValue.author),
      date: new FormControl(webArticleRawValue.date),
      tags: new FormControl(webArticleRawValue.tags ?? []),
    });
  }

  getWebArticle(form: WebArticleFormGroup): IWebArticle | NewWebArticle {
    return this.convertWebArticleRawValueToWebArticle(form.getRawValue() as WebArticleFormRawValue | NewWebArticleFormRawValue);
  }

  resetForm(form: WebArticleFormGroup, webArticle: WebArticleFormGroupInput): void {
    const webArticleRawValue = this.convertWebArticleToWebArticleRawValue({ ...this.getFormDefaults(), ...webArticle });
    form.reset(
      {
        ...webArticleRawValue,
        id: { value: webArticleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): WebArticleFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      date: currentTime,
      tags: [],
    };
  }

  private convertWebArticleRawValueToWebArticle(
    rawWebArticle: WebArticleFormRawValue | NewWebArticleFormRawValue
  ): IWebArticle | NewWebArticle {
    return {
      ...rawWebArticle,
      date: dayjs(rawWebArticle.date, DATE_TIME_FORMAT),
    };
  }

  private convertWebArticleToWebArticleRawValue(
    webArticle: IWebArticle | (Partial<NewWebArticle> & WebArticleFormDefaults)
  ): WebArticleFormRawValue | PartialWithRequiredKeyOf<NewWebArticleFormRawValue> {
    return {
      ...webArticle,
      date: webArticle.date ? webArticle.date.format(DATE_TIME_FORMAT) : undefined,
      tags: webArticle.tags ?? [],
    };
  }
}
