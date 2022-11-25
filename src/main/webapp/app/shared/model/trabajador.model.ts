export interface ITrabajador {
  id?: number;
  dni?: string;
  nombre?: string;
  apellido?: string;
  cargo?: string;
  telefono?: number;
}

export class Trabajador implements ITrabajador {
  constructor(
    public id?: number,
    public dni?: string,
    public nombre?: string,
    public apellido?: string,
    public cargo?: string,
    public telefono?: number
  ) {}
}
