import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { IVehiculo, Vehiculo } from 'app/shared/model/vehiculo.model';
import { VehiculoService } from './vehiculo.service';

@Component({
  selector: 'jhi-vehiculo-update',
  templateUrl: './vehiculo-update.component.html'
})
export class VehiculoUpdateComponent implements OnInit {
  vehiculo: IVehiculo;
  isSaving: boolean;
  dateDp: any;

  editForm = this.fb.group({
    id: [],
    modelo: [],
    tipo: [],
    precio: [],
    matricula: [],
    marca: [],
    date: []
  });

  constructor(protected vehiculoService: VehiculoService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ vehiculo }) => {
      this.updateForm(vehiculo);
      this.vehiculo = vehiculo;
    });
  }

  updateForm(vehiculo: IVehiculo) {
    this.editForm.patchValue({
      id: vehiculo.id,
      modelo: vehiculo.modelo,
      tipo: vehiculo.tipo,
      precio: vehiculo.precio,
      matricula: vehiculo.matricula,
      marca: vehiculo.marca,
      date: vehiculo.date
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const vehiculo = this.createFromForm();
    if (vehiculo.id !== undefined) {
      this.subscribeToSaveResponse(this.vehiculoService.update(vehiculo));
    } else {
      this.subscribeToSaveResponse(this.vehiculoService.create(vehiculo));
    }
  }

  private createFromForm(): IVehiculo {
    const entity = {
      ...new Vehiculo(),
      id: this.editForm.get(['id']).value,
      modelo: this.editForm.get(['modelo']).value,
      tipo: this.editForm.get(['tipo']).value,
      precio: this.editForm.get(['precio']).value,
      matricula: this.editForm.get(['matricula']).value,
      marca: this.editForm.get(['marca']).value,
      date: this.editForm.get(['date']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVehiculo>>) {
    result.subscribe((res: HttpResponse<IVehiculo>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
