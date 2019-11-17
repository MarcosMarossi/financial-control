package com.example.controlefinanceiro.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.controlefinanceiro.R;

public class LucrosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.act_lucros );

        getSupportActionBar().hide();
    }
}
