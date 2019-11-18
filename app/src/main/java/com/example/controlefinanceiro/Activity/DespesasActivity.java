package com.example.controlefinanceiro.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.controlefinanceiro.Helper.DateCustom;
import com.example.controlefinanceiro.Model.Movimentacao;
import com.example.controlefinanceiro.R;

public class DespesasActivity extends AppCompatActivity {

    private EditText etValor, etData, etTitulo, etDesc;
    private Movimentacao movimentacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.act_despesas );

        getSupportActionBar().hide();

        etValor = findViewById( R.id.etValor );
        etData = findViewById( R.id.etData );
        etTitulo = findViewById( R.id.etTitulo );
        etDesc = findViewById( R.id.etDesc );

        etData.setText( DateCustom.dataAtual() );
    }

    public void addDespesa(View view){
        movimentacao = new Movimentacao();

        String data = etData.getText().toString();

        movimentacao.setValor( Double.parseDouble( etValor.getText().toString() ) );
        movimentacao.setData( data );
        movimentacao.setCategoria( etTitulo.getText().toString() );
        movimentacao.setDescricao( etDesc.getText().toString() );
        movimentacao.setTipo( "r" );
        movimentacao.salvar(data);
    }
}
