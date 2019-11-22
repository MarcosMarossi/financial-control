package com.example.controlefinanceiro.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

        if(validar()){
            movimentacao = new Movimentacao();
            String data = etData.getText().toString();

            movimentacao.setValor( Double.parseDouble( etValor.getText().toString() ) );
            movimentacao.setData( data );
            movimentacao.setCategoria( etTitulo.getText().toString() );
            movimentacao.setDescricao( etDesc.getText().toString() );
            movimentacao.setTipo( "r" );
            movimentacao.salvar(data);
        } else {
            Toast.makeText( this, "Movimentação não criada!", Toast.LENGTH_SHORT ).show();
        }
    }

    public Boolean validar(){

        String sValor = etValor.getText().toString();
        String sData = etData.getText().toString();
        String sCategoria = etTitulo.getText().toString();
        String sDescricao = etDesc.getText().toString();

        if(!sValor.isEmpty()){
            if(!sData.isEmpty()){
                if(!sCategoria.isEmpty()){
                    if(!sDescricao.isEmpty()){

                        } else {
                            Toast.makeText( this, "Você não descreveu a despesa!", Toast.LENGTH_SHORT ).show();
                            return false;
                        }
                    } else {
                        Toast.makeText( this, "Você não categorizou a despesa!", Toast.LENGTH_SHORT ).show();
                        return false;
                    }

                } else {
                    Toast.makeText( this, "Você não colocou nenhuma data na despesa.", Toast.LENGTH_SHORT ).show();
                    return false;
            }
        } else {
            Toast.makeText( this, "Você não colocou nenhum valor.", Toast.LENGTH_SHORT ).show();
            return false;
        }

        return true;
    }
}
