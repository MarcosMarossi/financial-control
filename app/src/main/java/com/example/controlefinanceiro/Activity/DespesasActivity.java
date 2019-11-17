package com.example.controlefinanceiro.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.controlefinanceiro.R;

public class DespesasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.act_despesas );

        getSupportActionBar().hide();
    }
}
