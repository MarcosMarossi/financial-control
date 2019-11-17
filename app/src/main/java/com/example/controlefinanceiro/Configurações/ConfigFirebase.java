package com.example.controlefinanceiro.Configurações;

import com.google.firebase.auth.FirebaseAuth;

public class ConfigFirebase {

    private static FirebaseAuth autenticacao;

    public static FirebaseAuth getFireBaseAutenticacao(){

        if(autenticacao==null){
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }
}
