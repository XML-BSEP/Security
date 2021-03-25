package com.example.DukeStrategicTechnologies.pki.util.properties;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class KeyStoreProperties {
    public static final String ROOT_FILE = "self_signed";
    public static final String CA_FILE = "ca";
    public static final String END_ENTITY_FILE = "end_entity";
    public static final String PROPERTIES_FILE = "keystore-passwords.properties";

    public KeyStoreProperties() {
    }

    public String readKeyStorePass(String file) throws IOException {
        InputStream inputStream = null;
        String result = "";
        try {
            Properties prop = new Properties();

            inputStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("No such file found");
            }

            result = prop.getProperty(file);

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            inputStream.close();
        }
        return result;
    }


}
