// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  baseUrl: 'http://localhost:8088',
  login:"login",
  auth:"auth",
  api: "api",
  template : "template",
  add : "add",
  findAll : "all",
  users : "users",
  getCaCertificates : "getCaCertificates",
  getRootCertificates : "getRootCertificates",
  certificate : "certificate",
  getCertificatesByUser : "getCertificatesByUser",
  createCertificate : "createCertificate",
  all : "getAll",
  getEndEntityCertificates : "getEndEntityCertificates",
  getAllForSigning : "getAllForSigning",
  getAllForSigningByUser : "getAllForSigningByUser",
  getCaCertificatesByUser: "getCaCertificatesByUser",
  getEndEntityCertificatesByUser: "getEndEntityCertificatesByUser",
  getPossibleKeyUsages : "getPossibleKeyUsages",
  downloadCertificate : "downloadCertificate",
  revokeCertificate: "revokeCertificate",
  createRootCertificate : "createRootCertificate"
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
