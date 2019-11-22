package com.example.controlefinanceiro.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.controlefinanceiro.Configurações.ConfigFirebase;
import com.example.controlefinanceiro.Helper.Base64Custom;
import com.example.controlefinanceiro.Helper.DateCustom;
import com.example.controlefinanceiro.Model.Movimentacao;
import com.example.controlefinanceiro.Model.Usuario;
import com.example.controlefinanceiro.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ReceitaActivity extends AppCompatActivity {

    private EditText etValor, etData, etTitulo, etDesc;
    private Movimentacao movimentacao;
    private Double receitaTotal;
    private DatabaseReference referencia = ConfigFirebase.getDataBaseFirebase();
    private FirebaseAuth autenticacao =  ConfigFirebase.getFireBaseAutenticacao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.act_receita );

        getSupportActionBar().hide();

        etValor = findViewById( R.id.etValor );
        etData = findViewById( R.id.etData );
        etTitulo = findViewById( R.id.etTitulo );
        etDesc = findViewById( R.id.etDesc );

        etData.setText( DateCustom.dataAtual() );

        recuperarReceitaTotal();
    }

    public void addReceita(View v){

        if(validar()){
            movimentacao = new Movimentacao();
            String data = etData.getText().toString();
            Double valorRecuperado = Double.parseDouble( etValor.getText().toString() );

            movimentacao.setValor( valorRecuperado );
            movimentacao.setData( data );
            movimentacao.setCategoria( etTitulo.getText().toString() );
            movimentacao.setDescricao( etDesc.getText().toString() );
            movimentacao.setTipo( "r" );

            Double receitaAtual = receitaTotal + valorRecuperado;
            atualizarReceita( receitaAtual );

            movimentacao.salvar(data);

            Toast.makeText( this, "Movimentação criada com sucesso", Toast.LENGTH_SHORT ).show();
            finish();

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
                        Toast.makeText( this, "Você não descreveu o lucro!", Toast.LENGTH_SHORT ).show();
                        return false;
                    }
                } else {
                    Toast.makeText( this, "Você não categorizou o lucro!", Toast.LENGTH_SHORT ).show();
                    return false;
                }

            } else {
                Toast.makeText( this, "Você não colocou nenhuma data.", Toast.LENGTH_SHORT ).show();
                return false;
            }
        } else {
            Toast.makeText( this, "Você não colocou nenhum valor.", Toast.LENGTH_SHORT ).show();
            return false;
        }

        return true;
    }


    public void recuperarReceitaTotal(){

        String email = autenticacao.getCurrentUser().getEmail();
        String id = Base64Custom.codificarBase64( email );
        DatabaseReference usuarioReferencia = referencia.child( "usuarios" ).child( id );

        usuarioReferencia.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                receitaTotal = usuario.getReceitaTotal();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void atualizarReceita(Double receita){

        String email = autenticacao.getCurrentUser().getEmail();
        String id = Base64Custom.codificarBase64( email );
        DatabaseReference usuarioReferencia = referencia.child( "usuarios" )
                .child( id );

        usuarioReferencia.child( "receitaTotal" ).setValue( receita );

    }
}
