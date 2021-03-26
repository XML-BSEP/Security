import { User } from "./user";

export class newUser{
  public id : number;
  public givenName : String;
  public surname : String;
  public commonName : String;
  public organization : String;
  public organizationUnit : String;
  public state : String;
  public city : String;
  public email : String;
  public password : String;

  constructor(givenName : String, surname : String, commonName : String, organization:String, organizationUnit : String, state :String, city:String, email : String, id:number, password:String){
    this.givenName = givenName;
    this.surname = surname;
    this.commonName = commonName;
    this.organization = organization;
    this.organizationUnit = organizationUnit;
    this.state = state;
    this.city = city;
    this.email = email;
    this.password = password;
    this.id = id;
  }
}
