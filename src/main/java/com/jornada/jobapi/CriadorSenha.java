package com.jornada.jobapi;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CriadorSenha {
    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

//           String senhaCriptografada = bCryptPasswordEncoder.encode("Pedro123!");
//          System.out.println(senhaCriptografada);


        //$2a$10$4pJEttY2LieCN5wOXQGnzOc/qMT2yB3c40XTx0/hupCtgY5LO0uk.


        boolean senhaCorreta =bCryptPasswordEncoder.matches("Pedro123!", "$2a$10$vDeJYEZot8B/HgDabNMXHuvQzValQzUiLmYX0O11y85nSqRQEXr/O");
        System.out.println(senhaCorreta);
    }
}
