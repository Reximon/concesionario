import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ConcesionarioSharedLibsModule, ConcesionarioSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [ConcesionarioSharedLibsModule, ConcesionarioSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [ConcesionarioSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ConcesionarioSharedModule {
  static forRoot() {
    return {
      ngModule: ConcesionarioSharedModule
    };
  }
}
