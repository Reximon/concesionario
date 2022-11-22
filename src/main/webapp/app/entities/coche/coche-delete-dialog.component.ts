import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICoche } from 'app/shared/model/coche.model';
import { CocheService } from './coche.service';

@Component({
  selector: 'jhi-coche-delete-dialog',
  templateUrl: './coche-delete-dialog.component.html'
})
export class CocheDeleteDialogComponent {
  coche: ICoche;

  constructor(protected cocheService: CocheService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.cocheService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'cocheListModification',
        content: 'Deleted an coche'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-coche-delete-popup',
  template: ''
})
export class CocheDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ coche }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(CocheDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.coche = coche;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/coche', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/coche', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
