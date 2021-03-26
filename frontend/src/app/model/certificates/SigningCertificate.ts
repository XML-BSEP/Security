export class SigningCertificate {

    public commonName: string;
    public issuerEmail: string;
    public subjectEmail: string;
    public issuerId: number;
    public serialNumber: string;
    public startDate: Date;
    public endDate: Date;
    public keyUsages: string[];
    public extendedKeyUsages: string[];
    public signatureAlgorithm : string;
    public isRevoked : boolean;
    constructor(issuerCommonName: string, issuerEmail: string, subjectEmail: string, issuerId: number, serialNumber: string, validFrom: Date,
        validTo: Date, keyUsage: string[], extendedKeyUsage: string[], signatureAlgorithm : string, isRevoked : boolean) {
        this.commonName = issuerCommonName;
        this.subjectEmail = subjectEmail;
        this.issuerEmail = issuerEmail;
        this.issuerId = issuerId;
        this.serialNumber = serialNumber;
        this.startDate = validFrom;
        this.endDate = validTo;
        this.keyUsages = keyUsage;
        this.extendedKeyUsages = extendedKeyUsage;
        this.signatureAlgorithm = signatureAlgorithm;
        this.isRevoked = isRevoked;
    }

}
