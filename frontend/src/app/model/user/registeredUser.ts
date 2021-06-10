
export class RegisteredUser{
  public name : String;
  public surname : String;
  public email : String;
  public password : String;
  public confirmedpassword : String;
  public username : String;

  constructor(name : String, surname : String, email : String, password : String, confirmedpassword : String, username : String){
    this.name = name;
    this.surname = surname;
    this.email = email;
    this.password = password;
    this.confirmedpassword = confirmedpassword;
    this.username = username;
  }
}
