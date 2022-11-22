import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICompraVenta } from 'app/shared/model/compra-venta.model';

type EntityResponseType = HttpResponse<ICompraVenta>;
type EntityArrayResponseType = HttpResponse<ICompraVenta[]>;

@Injectable({ providedIn: 'root' })
export class CompraVentaService {
  public resourceUrl = SERVER_API_URL + 'api/compra-ventas';

  constructor(protected http: HttpClient) {}

  create(compraVenta: ICompraVenta): Observable<EntityResponseType> {
    return this.http.post<ICompraVenta>(this.resourceUrl, compraVenta, { observe: 'response' });
  }

  update(compraVenta: ICompraVenta): Observable<EntityResponseType> {
    return this.http.put<ICompraVenta>(this.resourceUrl, compraVenta, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICompraVenta>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICompraVenta[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
