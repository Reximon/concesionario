import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Coche } from 'app/shared/model/coche.model';
import { CocheService } from './coche.service';
import { CocheComponent } from './coche.component';
import { CocheDetailComponent } from './coche-detail.component';
import { CocheUpdateComponent } from './coche-update.component';
import { CocheDeletePopupComponent } from './coche-delete-dialog.component';
import { ICoche } from 'app/shared/model/coche.model';

@Injectable({ providedIn: 'root' })
export class CocheResolve implements Resolve<ICoche> {
  constructor(private service: CocheService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ICoche> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Coche>) => response.ok),
        map((coche: HttpResponse<Coche>) => coche.body)
      );
    }
    return of(new Coche());
  }
}

export const cocheRoute: Routes = [
  {
    path: '',
    component: CocheComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'concesionarioApp.coche.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CocheDetailComponent,
    resolve: {
      coche: CocheResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'concesionarioApp.coche.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CocheUpdateComponent,
    resolve: {
      coche: CocheResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'concesionarioApp.coche.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CocheUpdateComponent,
    resolve: {
      coche: CocheResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'concesionarioApp.coche.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const cochePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: CocheDeletePopupComponent,
    resolve: {
      coche: CocheResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'concesionarioApp.coche.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
