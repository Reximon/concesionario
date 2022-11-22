export interface ICompraVenta {
  id?: number;
}

export class CompraVenta implements ICompraVenta {
  constructor(public id?: number) {}
}
