import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITrabajador } from 'app/shared/model/trabajador.model';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-trabajador-detail',
  templateUrl: './trabajador-detail.component.html'
})
export class TrabajadorDetailComponent implements OnInit {
  trabajador?: ITrabajador;

  constructor(protected modal: NgbActiveModal) {}

  ngOnInit() {}
  close(msg) {
    this.modal.close(msg);
  }
}
