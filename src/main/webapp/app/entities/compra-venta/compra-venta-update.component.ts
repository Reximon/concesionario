import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { ICompraVenta, CompraVenta } from 'app/shared/model/compra-venta.model';
import { CompraVentaService } from './compra-venta.service';

@Component({
  selector: 'jhi-compra-venta-update',
  templateUrl: './compra-venta-update.component.html'
})
export class CompraVentaUpdateComponent implements OnInit {
  compraVenta: ICompraVenta;
  isSaving: boolean;

  editForm = this.fb.group({
    id: []
  });

  constructor(protected compraVentaService: CompraVentaService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ compraVenta }) => {
      this.updateForm(compraVenta);
      this.compraVenta = compraVenta;
    });
  }

  updateForm(compraVenta: ICompraVenta) {
    this.editForm.patchValue({
      id: compraVenta.id
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const compraVenta = this.createFromForm();
    if (compraVenta.id !== undefined) {
      this.subscribeToSaveResponse(this.compraVentaService.update(compraVenta));
    } else {
      this.subscribeToSaveResponse(this.compraVentaService.create(compraVenta));
    }
  }

  private createFromForm(): ICompraVenta {
    const entity = {
      ...new CompraVenta(),
      id: this.editForm.get(['id']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompraVenta>>) {
    result.subscribe((res: HttpResponse<ICompraVenta>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
