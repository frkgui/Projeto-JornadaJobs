package com.jornada.jobapi;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CriadorSenha {
    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

//           String senhaCriptografada = bCryptPasswordEncoder.encode("123");
//          System.out.println(senhaCriptografada);


        //$2a$10$4pJEttY2LieCN5wOXQGnzOc/qMT2yB3c40XTx0/hupCtgY5LO0uk.
//        $2a$10$D9RzY/9t8Ef3mRVjUIe7R.OJ7LTneoGO7ZltQ7BrpwKmMFcQD8o6K


//
        boolean senhaCorreta =bCryptPasswordEncoder.matches("12345", "$2a$10$D9RzY/9t8Ef3mRVjUIe7R.OJ7LTneoGO7ZltQ7BrpwKmMFcQD8o6K");
        System.out.println(senhaCorreta);
    }
}
