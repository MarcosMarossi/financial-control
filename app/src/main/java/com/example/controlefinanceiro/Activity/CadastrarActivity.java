package com.example.controlefinanceiro.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.controlefinanceiro.Configurações.ConfigFirebase;
import com.example.controlefinanceiro.Helper.Base64Custom;
import com.example.controlefinanceiro.Model.Usuario;
import com.example.controlefinanceiro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastrarActivity extends AppCompatActivity {

    private EditText etNome, etEmail, etSenha;
    private Button btCadastrar;
    private FirebaseAuth autenticacao;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.act_cadastrar );

        etNome = findViewById( R.id.etNome );
        etEmail = findViewById( R.id.etEmail );
        etSenha = findViewById( R.id.etSenha );
        btCadastrar = findViewById( R.id.btCadastrar );


        btCadastrar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nome = etNome.getText().toString();
                String email = etEmail.getText().toString();
                String senha = etSenha.getText().toString();

                if(!nome.isEmpty()){
                    if(!email.isEmpty()){
                        if(!senha.isEmpty()){

                            usuario = new Usuario();
                            usuario.setNome( nome );
                            usuario.setEmail( email );
                            usuario.setSenha( senha );
                            cadastrarUsuario();


                        } else{
                            Toast.makeText( CadastrarActivity.this, "Por favor, digite a senha.", Toast.LENGTH_SHORT ).show();
                        }
                    } else{
                        Toast.makeText( CadastrarActivity.this, "Por favor, digite o email.", Toast.LENGTH_SHORT ).show();
                    }
                } else{
                    Toast.makeText( CadastrarActivity.this, "Por favor, digite o nome.", Toast.LENGTH_SHORT ).show();
                }
            }
        } );
    }

    public void cadastrarUsuario(){
        autenticacao = ConfigFirebase.getFireBaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),usuario.getSenha()
        ).addOnCompleteListener( this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    String idUsuario = Base64Custom.codificarBase64( usuario.getEmail() );
                    usuario.setIdUsuario( idUsuario );
                    usuario.salvar();
                    finish();
                }
                else{

                    String excecao = "";
                    try{
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha forte";
                    } catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "Por favor, digite um email válido!";
                    } catch (FirebaseAuthUserCollisionException e){
                        excecao = "Conta já cadastrada!";
                    } catch (Exception e){
                        excecao = "Erro ao cadastrar o usuário.";
                        e.printStackTrace();
                    }
                    Toast.makeText( CadastrarActivity.this, excecao , Toast.LENGTH_SHORT ).show();
                    Log.w("ERRO", "createUserWithEmail:failure", task.getException());
                }
            }
        } );
    }
}
