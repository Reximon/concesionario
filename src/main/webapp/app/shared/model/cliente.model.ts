import { Moment } from 'moment';

export interface ICliente {
  id?: number;
  dni?: string;
  nombre?: string;
  apellido?: string;
  fechaNacimiento?: Moment;
}

export class Cliente implements ICliente {
  constructor(public id?: number, public dni?: string, public nombre?: string, public apellido?: string, public fechaNacimiento?: Moment) {}
}
