export class PossibleKeyUsages{
    public possibleKeyUsages : string[];
    public possibleExtendedKeyUsages : string[];

    constructor(possibleKeyUsages : string[], possibleExtendedKeyUsages : string[]){
      this.possibleKeyUsages = possibleKeyUsages;
      this.possibleExtendedKeyUsages = possibleExtendedKeyUsages;
    }
}
