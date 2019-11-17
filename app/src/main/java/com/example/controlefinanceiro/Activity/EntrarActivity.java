package com.example.controlefinanceiro.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.controlefinanceiro.Configurações.ConfigFirebase;
import com.example.controlefinanceiro.Model.Usuario;
import com.example.controlefinanceiro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class EntrarActivity extends AppCompatActivity {

    private EditText etEmail, etSenha;
    private Button btEntrar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.act_entrar );


        etEmail = findViewById( R.id.edtEmail );
        etSenha = findViewById( R.id.edtSenha );
        btEntrar = findViewById( R.id.btEntrar );

        btEntrar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = etEmail.getText().toString();
                String senha = etSenha.getText().toString();

                    if(!email.isEmpty()){
                        if(!senha.isEmpty()){

                            usuario = new Usuario();
                            usuario.setEmail( email );
                            usuario.setSenha( senha );
                            validarEntrada();

                        } else{
                            Toast.makeText( EntrarActivity.this, "Por favor, digite a senha.", Toast.LENGTH_SHORT ).show();
                        }
                    } else{
                        Toast.makeText( EntrarActivity.this, "Por favor, digite o email.", Toast.LENGTH_SHORT ).show();
                    }
                }
        });
    }

    public void validarEntrada(){

        autenticacao = ConfigFirebase.getFireBaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),usuario.getSenha()
        ).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity( new Intent( getApplicationContext(), PainelActitvity.class ) );
                    finish();
                } else{

                    String excecao = "";

                    try{
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e){
                        excecao = "Usuário não está cadastrado.";
                    }
                    catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "E-mail e senha incompatíveis. Tente novamente.";
                    }
                    catch (Exception e){
                        excecao = "Erro ao cadastrar o usuário.";
                        e.printStackTrace();
                    }
                    Toast.makeText( EntrarActivity.this, excecao, Toast.LENGTH_SHORT ).show();
                }
            }
        } );

    }
}
