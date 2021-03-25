export class CreateCertificate {
    public subjectId: number;
    public issuerId: number;
    public startDate: Date;
    public endDate: Date;
    public signatureAlgorithm: string;
    public keyUsage: string[];
    public extendedKeyUsage: string[];
    public issuerSerialNumber: string;

    constructor(subjectId: number, issuerId: number, startDate: Date, endDate: Date, signatureAlgorithm: string, keyUsage: string[], extendedKeyUsage: string[], issuerSerialNumber: string) {
        this.subjectId = subjectId;
        this.issuerId = issuerId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.signatureAlgorithm = signatureAlgorithm;
        this.keyUsage = keyUsage;
        this.extendedKeyUsage = extendedKeyUsage;
        this.issuerSerialNumber = issuerSerialNumber;
    }


}