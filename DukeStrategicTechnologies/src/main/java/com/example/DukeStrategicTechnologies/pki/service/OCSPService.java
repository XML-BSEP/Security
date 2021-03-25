package com.example.DukeStrategicTechnologies.pki.service;

import com.example.DukeStrategicTechnologies.pki.model.enums.SignatureAlgorithm;
import com.example.DukeStrategicTechnologies.pki.repository.RevokedCertificateRepository;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.ocsp.*;
import org.bouncycastle.cert.ocsp.jcajce.JcaBasicOCSPRespBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DigestCalculatorProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Date;
@Service
public class OCSPService {

    @Autowired
    private RevokedCertificateRepository revokedCertificateRepository;

    public OCSPService() {}

    public boolean isCertificateRevoked(X509Certificate certificate, PrivateKey privateKey) throws OperatorCreationException, CertificateEncodingException, IOException, OCSPException {
        OCSPReqBuilder reqBuilder = new OCSPReqBuilder();

        DigestCalculatorProvider digestCalculatorProvider = new JcaDigestCalculatorProviderBuilder().setProvider("BC").build();
        CertificateID certificateId = new CertificateID(digestCalculatorProvider.get(CertificateID.HASH_SHA1), new X509CertificateHolder(certificate.getEncoded()), certificate.getSerialNumber());

        reqBuilder.addRequest(certificateId);

        if ((ocspResp(reqBuilder.build(), certificate, privateKey).getResponses()[0].getCertStatus() == CertificateStatus.GOOD))
            return false;
        return true;
    }

    public BasicOCSPResp ocspResp(OCSPReq req, X509Certificate certificate, PrivateKey privateKey) throws OperatorCreationException, OCSPException, CertificateEncodingException, IOException {
        DigestCalculatorProvider provider = new JcaDigestCalculatorProviderBuilder().setProvider("BC").build();

        BasicOCSPRespBuilder response = new JcaBasicOCSPRespBuilder(certificate.getPublicKey(), provider.get(RespID.HASH_SHA1));

        if (isRevoked(certificate.getSerialNumber().toString())) {
            response.addResponse(req.getRequestList()[0].getCertID(), new RevokedStatus(new Date(), 0));
        } else {
            response.addResponse(req.getRequestList()[0].getCertID(), CertificateStatus.GOOD);
        }

        ContentSigner contentSigner = new JcaContentSignerBuilder(SignatureAlgorithm.SHA2_256_RSA.toString()).setProvider("BC").build(privateKey);

        X509CertificateHolder[] responseList = new X509CertificateHolder[1];

        responseList[0] = new X509CertificateHolder(certificate.getEncoded());

        return response.build(contentSigner,responseList, new Date());
    }

    public boolean isRevoked(String serialNumber) {
        return revokedCertificateRepository.findAll().stream().anyMatch(c -> c.getSerialNumber().equals(serialNumber));
    }


}
