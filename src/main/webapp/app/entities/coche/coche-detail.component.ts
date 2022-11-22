import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICoche } from 'app/shared/model/coche.model';

@Component({
  selector: 'jhi-coche-detail',
  templateUrl: './coche-detail.component.html'
})
export class CocheDetailComponent implements OnInit {
  coche: ICoche;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ coche }) => {
      this.coche = coche;
    });
  }

  previousState() {
    window.history.back();
  }
}
