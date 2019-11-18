package com.example.controlefinanceiro.Model;

import com.example.controlefinanceiro.Configurações.ConfigFirebase;
import com.example.controlefinanceiro.Helper.Base64Custom;
import com.example.controlefinanceiro.Helper.DateCustom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Movimentacao {

    private String data;
    private String categoria;
    private String descricao;
    private String tipo;
    private double valor;


    public Movimentacao() {
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public void salvar(String dataSalva){
        FirebaseAuth autenticacao = ConfigFirebase.getFireBaseAutenticacao();
        String id = Base64Custom.codificarBase64( autenticacao.getCurrentUser().getEmail() );
        String dataEscolhida = DateCustom.mesAno( dataSalva );
        DatabaseReference firebase = ConfigFirebase.getDataBaseFirebase();
        firebase.child( "movimentacao" )
                .child( id )
                .child( dataEscolhida )
                .push()
                .setValue( this );
    }
}
