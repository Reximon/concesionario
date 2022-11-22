import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { ICoche, Coche } from 'app/shared/model/coche.model';
import { CocheService } from './coche.service';

@Component({
  selector: 'jhi-coche-update',
  templateUrl: './coche-update.component.html'
})
export class CocheUpdateComponent implements OnInit {
  coche: ICoche;
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    audi: []
  });

  constructor(protected cocheService: CocheService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ coche }) => {
      this.updateForm(coche);
      this.coche = coche;
    });
  }

  updateForm(coche: ICoche) {
    this.editForm.patchValue({
      id: coche.id,
      audi: coche.audi
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const coche = this.createFromForm();
    if (coche.id !== undefined) {
      this.subscribeToSaveResponse(this.cocheService.update(coche));
    } else {
      this.subscribeToSaveResponse(this.cocheService.create(coche));
    }
  }

  private createFromForm(): ICoche {
    const entity = {
      ...new Coche(),
      id: this.editForm.get(['id']).value,
      audi: this.editForm.get(['audi']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICoche>>) {
    result.subscribe((res: HttpResponse<ICoche>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
