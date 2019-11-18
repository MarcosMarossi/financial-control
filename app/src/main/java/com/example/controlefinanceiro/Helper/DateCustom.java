package com.example.controlefinanceiro.Helper;

import java.text.SimpleDateFormat;

public class DateCustom {

    public static String dataAtual(){
        long data = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "dd/MM/yyyy" );
        String dataFormatada = simpleDateFormat.format( data );
        return dataFormatada;
    }

    public static String mesAno(String data){
        String returnoData[] = data.split( "/" );
        String dia = returnoData[0]; // dia 17
        String mes = returnoData[1]; // mes 11
        String ano = returnoData[2]; // 2019

        String mesAno = mes + ano;
        return mesAno;
    }
}
