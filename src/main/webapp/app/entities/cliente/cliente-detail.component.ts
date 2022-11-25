import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICliente } from 'app/shared/model/cliente.model';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-cliente-detail',
  templateUrl: './cliente-detail.component.html'
})
export class ClienteDetailComponent implements OnInit {
  cliente?: ICliente;

  constructor(protected modal: NgbActiveModal) {}

  ngOnInit() {}

  close(msg) {
    this.modal.close(msg);
  }
  previousState() {
    window.history.back();
  }
}
