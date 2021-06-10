export class ResetPass {
    email : String;
	password : String;
	confirmedpassword : String;
	code : String;
 
    constructor(email : String, password : String, confirmedPassword : String, code : String) {
        this.email = email;
        this.password = password;
        this.confirmedpassword = confirmedPassword;
        this.code = code;
    }
}