export interface ICoche {
  id?: number;
  audi?: string;
}

export class Coche implements ICoche {
  constructor(public id?: number, public audi?: string) {}
}
