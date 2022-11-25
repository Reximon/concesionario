import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IVehiculo } from 'app/shared/model/vehiculo.model';

type EntityResponseType = HttpResponse<IVehiculo>;
type EntityArrayResponseType = HttpResponse<IVehiculo[]>;

@Injectable({ providedIn: 'root' })
export class VehiculoService {
  public resourceUrl = SERVER_API_URL + 'api/vehiculos';

  constructor(protected http: HttpClient) {}

  create(vehiculo: IVehiculo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vehiculo);
    return this.http
      .post<IVehiculo>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(vehiculo: IVehiculo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vehiculo);
    return this.http
      .put<IVehiculo>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IVehiculo>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  getDisponble(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVehiculo[]>(`${this.resourceUrl}/disponible`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }
  getNoDisponble(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVehiculo[]>(`${this.resourceUrl}/no-disponible`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVehiculo[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(vehiculo: IVehiculo): IVehiculo {
    const copy: IVehiculo = Object.assign({}, vehiculo, {
      date: vehiculo.date != null && vehiculo.date.isValid() ? vehiculo.date.format(DATE_FORMAT) : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date != null ? moment(res.body.date) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((vehiculo: IVehiculo) => {
        vehiculo.date = vehiculo.date != null ? moment(vehiculo.date) : null;
      });
    }
    return res;
  }
}
