import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { ConcesionarioSharedModule } from 'app/shared';
import {
  CompraVentaComponent,
  CompraVentaDetailComponent,
  CompraVentaUpdateComponent,
  CompraVentaDeletePopupComponent,
  CompraVentaDeleteDialogComponent,
  compraVentaRoute,
  compraVentaPopupRoute
} from './';

import { CompraVentaShowPopupComponent, CompraVentaShowDialogComponent } from '../compra-venta/compra-venta-show-dialog.component';

const ENTITY_STATES = [...compraVentaRoute, ...compraVentaPopupRoute];

@NgModule({
  imports: [ConcesionarioSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    CompraVentaComponent,
    CompraVentaDetailComponent,
    CompraVentaUpdateComponent,
    CompraVentaDeleteDialogComponent,
    CompraVentaDeletePopupComponent,
    CompraVentaShowDialogComponent,
    CompraVentaShowPopupComponent
  ],
  entryComponents: [
    CompraVentaComponent,
    CompraVentaUpdateComponent,
    CompraVentaDeleteDialogComponent,
    CompraVentaDeletePopupComponent,
    CompraVentaShowDialogComponent,
    CompraVentaShowPopupComponent
  ],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ConcesionarioCompraVentaModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
