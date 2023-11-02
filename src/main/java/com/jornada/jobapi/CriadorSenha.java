package com.jornada.jobapi;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CriadorSenha {
    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

//           String senhaCriptografada = bCryptPasswordEncoder.encode("Senha123!");
//          System.out.println(senhaCriptografada);
//       $2a$10$Ig06.xyYseXKcTKKtjxmZONbC.PjxaO3D31OdGpgozKAilUZAPRUK

//        $2a$10$dn9nkenLDcY.lCIx.znXIOfjcupLzT.4SF8Mg1TYyQOk7KtZi6U1m CARLOS MEXEU AQUI



        boolean senhaCorreta =bCryptPasswordEncoder.matches("Senha123!", "$2a$10$Ig06.xyYseXKcTKKtjxmZONbC.PjxaO3D31OdGpgozKAilUZAPRUK");
        System.out.println(senhaCorreta);
    }
}
