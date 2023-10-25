package com.jornada.jobapi;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CriadorSenha {
    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

//           String senhaCriptografada = bCryptPasswordEncoder.encode("Senhasegura123!");
//          System.out.println(senhaCriptografada);
//       $2a$10$81UcjrTp0OzVsG5aXaZHcefOtS0EuzCaXTMPa6ik3XRhMUobrCLqm



        boolean senhaCorreta =bCryptPasswordEncoder.matches("Senhasegura123!", "$2a$10$81UcjrTp0OzVsG5aXaZHcefOtS0EuzCaXTMPa6ik3XRhMUobrCLqm");
        System.out.println(senhaCorreta);
    }
}
