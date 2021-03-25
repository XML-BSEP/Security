export class User{
  // private Long id;
  // private String givenName;
  // private String surname;
  // private String commonName;
  // private String organization;
  // private String organizationUnit;
  // private String state;
  // private String city;
  // private String email;
  // private boolean isCA;
  // private Long certificateCount;

  public id : number;
  public givenName : String;
  public surname : String;
  public commonName : String;
  public organization : String;
  public organizationUnit : String;
  public state : String;
  public city : String;
  public email : String;
  public isCA : boolean;
  public certificateCount : Number;

  constructor(givenName : String, surname : String, commonName : String, organization:String, organizationUnit : String, state :String, city:String, email : String, isCA:boolean, certificaCount:Number, id:number){
    this.givenName = givenName;
    this.surname = surname;
    this.commonName = commonName;
    this.organization = organization;
    this.organizationUnit = organizationUnit;
    this.state = state;
    this.city = city;
    this.email = email;
    this.isCA = isCA;
    this.certificateCount = certificaCount;
    this.id = id;
  }

}
