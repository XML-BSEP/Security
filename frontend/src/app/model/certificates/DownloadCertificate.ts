
export class DownloadCertificate{
  public subjectEmail : String;
  public serialNumber : String;
  public commonName :String;

  constructor(subjectEmail : String, serialNumber : String, commonName : String){
    this.subjectEmail = subjectEmail;
    this.serialNumber = serialNumber;
    this.commonName = commonName;
  }
}
