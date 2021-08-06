package br.com.apoo2021.farm.util;

import br.com.apoo2021.farm.Farmaple;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Cripto {

    public static String MD5Converter(String userData){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(userData.getBytes());
            byte[] digest = md.digest();
            return DatatypeConverter.printHexBinary(digest).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            Farmaple.logger.error("Erro ao converter para MD5!", e);
        }
        return null;
    }

}
