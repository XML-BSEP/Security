export class CreateCertificate {
    public subjectId: number;
    public issuerId: number;
    public startDate: string;
    public endDate: string;
    public signatureAlgorithm: string;
    public keyUsage: string[];
    public extendedKeyUsage: string[];
    public issuerSerialNumber: string;

    constructor(subjectId: number, issuerId: number, startDate: string, endDate: string, signatureAlgorithm: string, keyUsage: string[], extendedKeyUsage: string[], issuerSerialNumber: string) {
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