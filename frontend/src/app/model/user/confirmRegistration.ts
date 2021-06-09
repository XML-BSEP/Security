

export class ConfirmRegistration{
  public email : String;
  public code : String;


  constructor(email : String, code : String){
    this.email = email;
    this.code = code;

  }
}
