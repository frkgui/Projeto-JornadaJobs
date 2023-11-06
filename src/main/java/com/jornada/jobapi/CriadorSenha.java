package com.jornada.jobapi;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CriadorSenha {
    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

//          -- Criptografia de senha --
//           String senhaCriptografada = bCryptPasswordEncoder.encode("123");
//          System.out.println(senhaCriptografada);

//          -- Senhas salvas no banco  --
//        $2a$10$4pJEttY2LieCN5wOXQGnzOc/qMT2yB3c40XTx0/hupCtgY5LO0uk. (?   )
//        $2a$10$D9RzY/9t8Ef3mRVjUIe7R.OJ7LTneoGO7ZltQ7BrpwKmMFcQD8o6K (?)
//        $10$v6lphh0tajjyYKj.Tflxge3fKPWDrwjP2AOlvA3wkMoGQoIrauZFO (12345)


//         -- Verificar se a senha é igual a senha criptografada --
        boolean senhaCorreta =bCryptPasswordEncoder.matches("12345", "$2a$10$D9RzY/9t8Ef3mRVjUIe7R.OJ7LTneoGO7ZltQ7BrpwKmMFcQD8o6K");
        System.out.println(senhaCorreta);
    }
}
