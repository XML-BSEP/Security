package com.example.DukeStrategicTechnologies.pki.model.enums;

public enum SignatureAlgorithm {
    SHA2_256_RSA,
    SHA2_384_RSA,
    SHA2_512_RSA;

    public String toString(){

      switch(this){
          case SHA2_256_RSA: return "SHA256WithRSAEncryption";
          case SHA2_384_RSA: return "SHA384WithRSAEncryption";
          case SHA2_512_RSA: return "SHA512WithRSAEncryption";
          default: return "SHA256WithRSAEncryption";
      }

    };
}
