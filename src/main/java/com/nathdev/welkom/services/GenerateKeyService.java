package com.nathdev.welkom.services;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class GenerateKeyService {

//    Générer une courte id de 7 chiffre
//    la clé d'en-tête, le header. À envoyer dans le header de chaque requette pour retrouver les données correspondantes des chaque événement.

    public String generateShortNumberKey(){
        String shortNumberKey = "0123456789";
        SecureRandom random = new SecureRandom();
        int numberKeyLength = 7;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < numberKeyLength; i++){
            int number = random.nextInt(shortNumberKey.length());
            sb.append(shortNumberKey.charAt(number));
        }
        return sb.toString();
    }


//    Générer un clé secrète
//    cette clé c'est pour pour la sécurité lors du checking.

    public String generateSecureKey() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        int keyLength = 14;
        StringBuilder sb = new StringBuilder("wlk-");

        for (int i = 0; i < keyLength; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString(); // Retourne un code du style : wlk-jNw2i89Dw1dn2J
    }
}
