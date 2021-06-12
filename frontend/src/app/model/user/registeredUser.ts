
export class RegisteredUser{
  public name : String;
  public surname : String;
  public email : String;
  public password : String;
  public confirmPassword : String;
  public city : String;
  public state : String;
  public organization : String;
  public organizationUnit : String;
  public commonName : String;
  constructor(name : String, surname : String, email : String, password : String, confirmedpassword : String, state : String, city : String, org : String, orgUnit : String, commonName :String){
    this.name = name;
    this.surname = surname;
    this.email = email;
    this.password = password;
    this.confirmPassword = confirmedpassword;
    this.state = state;
    this.organization = org;
    this.organizationUnit = orgUnit
    this.city = city
    this.commonName = commonName
  }
}
