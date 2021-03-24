export class SigningCertificate {

    public issuerCommonName: string;
    public issuerEmail: string;
    public subjectEmail: string;
    public issuerId: number;
    public serialNum: string;
    public validFrom: string;
    public validTo: string;
    public keyUsage: string[];
    public extendedKeyUsage: string[];
    public signatureAlgorithm : string;
    constructor(issuerCommonName: string, issuerEmail: string, subjectEmail: string, issuerId: number, serialNum: string, validFrom: string,
        validTo: string, keyUsage: string[], extendedKeyUsage: string[], signatureAlgorithm : string) {
        this.issuerCommonName = issuerCommonName;
        this.subjectEmail = subjectEmail;
        this.issuerEmail = issuerEmail;
        this.issuerId = issuerId;
        this.serialNum = serialNum;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.keyUsage = keyUsage;
        this.extendedKeyUsage = extendedKeyUsage;
        this.signatureAlgorithm = signatureAlgorithm;
    }

}
