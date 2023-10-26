package com.jornada.jobapi;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CriadorSenha {
    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

           String senhaCriptografada = bCryptPasswordEncoder.encode("123");
          System.out.println(senhaCriptografada);
//       $2a$10$6V3pi2WO2TYm745F7LYHQ.8BwSIjJRusNh05rvV3W1qbgzZTvchTS



//        boolean senhaCorreta =bCryptPasswordEncoder.matches("Senha-Segura123", "$2a$10$6V3pi2WO2TYm745F7LYHQ.8BwSIjJRusNh05rvV3W1qbgzZTvchTS");
//        System.out.println(senhaCorreta);
    }
}
