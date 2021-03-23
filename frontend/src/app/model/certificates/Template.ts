export class Template{
  public id :  Number;
  public signatureAlgorithm : String;
  public keyAlgorithm : String;
  public name : String;
  public timestamp : Date;
  public keyUsage : String[];
  public extendedKeyUsage : String[];

  constructor(id : Number , signatureAlgorithm : String, keyAlgorithm : String, name : String, timestamp : Date, keyUsage: String[], extendedKeyUsage: String[]){
    this.id = id;
    this.signatureAlgorithm = signatureAlgorithm;
    this.keyAlgorithm = keyAlgorithm;
    this.name = name;
    this.timestamp = timestamp;
    this.keyUsage = keyUsage;
    this.extendedKeyUsage = extendedKeyUsage;
  }
}
