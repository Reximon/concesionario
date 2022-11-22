export interface IVehiculo {
  id?: number;
}

export class Vehiculo implements IVehiculo {
  constructor(public id?: number) {}
}
