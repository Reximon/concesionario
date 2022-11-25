import { Moment } from 'moment';
import { IVehiculo } from 'app/shared/model/vehiculo.model';
import { ITrabajador } from 'app/shared/model/trabajador.model';
import { ICliente } from 'app/shared/model/cliente.model';

export interface ICompraVenta {
  id?: number;
  fechaVenta?: Moment;
  garantia?: number;
  precioTotal?: number;
  vehiculo?: IVehiculo;
  vendedor?: ITrabajador;
  cliente?: ICliente;
}

export class CompraVenta implements ICompraVenta {
  constructor(
    public id?: number,
    public fechaVenta?: Moment,
    public garantia?: number,
    public precioTotal?: number,
    public vehiculo?: IVehiculo,
    public vendedor?: ITrabajador,
    public cliente?: ICliente
  ) {}
}
