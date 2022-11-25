import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';

import { ICliente, Cliente } from 'app/shared/model/cliente.model';
import { AccountService } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { ClienteService } from './cliente.service';
import { ClienteDetailComponent } from './cliente-detail.component';

// @Component({
// 	selector: 'jhi-cliente',
//   templateUrl: './cliente.component.html'
// })
// export class NgbdModalBasic {
// 	closeResult = '';

// 	constructor(private modalService: NgbModal) {}

// 	open(content) {
// 		this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' }).result.then(
// 			(result) => {
// 				this.closeResult = `Closed with: ${result}`;
// 			},
// 			(reason) => {
// 				this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
// 			},
// 		);
// 	}

// 	private getDismissReason(reason: any): string {
// 		if (reason === ModalDismissReasons.ESC) {
// 			return 'by pressing ESC';
// 		} else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
// 			return 'by clicking on a backdrop';
// 		} else {
// 			return `with: ${reason}`;
// 		}
// 	}
// }

@Component({
  selector: 'jhi-cliente',
  templateUrl: './cliente.component.html'
})
export class ClienteComponent implements OnInit, OnDestroy {
  currentAccount: any;
  clientes: ICliente[];
  error: any;
  success: any;
  eventSubscriber: Subscription;
  routeData: any;
  links: any;
  totalItems: any;
  itemsPerPage: any;
  page: any;
  predicate: any;
  previousPage: any;
  reverse: any;
  closeResult = '';

  constructor(
    protected clienteService: ClienteService,
    protected parseLinks: JhiParseLinks,
    protected jhiAlertService: JhiAlertService,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    private modalService: NgbModal
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.routeData = this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.previousPage = data.pagingParams.page;
      this.reverse = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
    });
  }
  open(content) {
    const modalRef = this.modalService.open(ClienteDetailComponent, { ariaLabelledBy: 'modal-basic-title' });
    modalRef.componentInstance.cliente = content;
  }

  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }
  loadAll() {
    this.clienteService
      .query({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<ICliente[]>) => this.paginateClientes(res.body, res.headers),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/cliente'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    });
    this.loadAll();
  }

  clear() {
    this.page = 0;
    this.router.navigate([
      '/cliente',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.loadAll();
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInClientes();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ICliente) {
    return item.id;
  }

  registerChangeInClientes() {
    this.eventSubscriber = this.eventManager.subscribe('clienteListModification', response => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateClientes(data: ICliente[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.clientes = data;
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
