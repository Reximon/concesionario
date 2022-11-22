import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { ConcesionarioSharedModule } from 'app/shared';
import {
  CocheComponent,
  CocheDetailComponent,
  CocheUpdateComponent,
  CocheDeletePopupComponent,
  CocheDeleteDialogComponent,
  cocheRoute,
  cochePopupRoute
} from './';

const ENTITY_STATES = [...cocheRoute, ...cochePopupRoute];

@NgModule({
  imports: [ConcesionarioSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [CocheComponent, CocheDetailComponent, CocheUpdateComponent, CocheDeleteDialogComponent, CocheDeletePopupComponent],
  entryComponents: [CocheComponent, CocheUpdateComponent, CocheDeleteDialogComponent, CocheDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ConcesionarioCocheModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
