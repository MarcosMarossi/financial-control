package com.example.controlefinanceiro.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.example.controlefinanceiro.Activity.CadastrarActivity;
import com.example.controlefinanceiro.Activity.EntrarActivity;
import com.example.controlefinanceiro.Configurações.ConfigFirebase;
import com.example.controlefinanceiro.R;
import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

public class MainActivity extends IntroActivity {

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        //setContentView( R.layout.act_main );
        
        verificarUsuario();

        getSupportActionBar().hide();
        setButtonBackVisible(false);
        setButtonNextVisible( false );


        addSlide(new FragmentSlide.Builder()
                .background( R.color.mi_icon_color_dark)
                .fragment(R.layout.act_intro)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.mi_icon_color_dark)
                .fragment(R.layout.act_main)
                .canGoForward( false )
                .build());
    }

    private void verificarUsuario() {

        autenticacao = ConfigFirebase.getFireBaseAutenticacao();
        //autenticacao.signOut();
        if( autenticacao.getCurrentUser() != null){
            startActivity(new Intent( getApplicationContext(), PainelActitvity.class));
        }
    }

    public void fazerCadastro(View view){
        Intent it = new Intent( getApplicationContext(), CadastrarActivity.class  );
        startActivity( it );
    }

    public void entrar(View view){
        Intent it = new Intent( getApplicationContext(), EntrarActivity.class  );
        startActivity( it );
    }




}
