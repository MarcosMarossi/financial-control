package com.example.controlefinanceiro.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.controlefinanceiro.Configurações.ConfigFirebase;
import com.example.controlefinanceiro.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

public class PainelActitvity extends AppCompatActivity {

    private FirebaseAuth autenticacao = ConfigFirebase.getFireBaseAutenticacao();
    private MaterialCalendarView calendario;
    private DatabaseReference reference = ConfigFirebase.getDataBaseFirebase();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.act_painel );
        Toolbar toolbar = findViewById( R.id.toolbar );
        toolbar.setTitle( "" );
        setSupportActionBar( toolbar );

        calendario = findViewById(R.id.calendarView);
        calendario.state().edit()
                .setMaximumDate(CalendarDay.from(2019,1,1))
                .setMaximumDate( CalendarDay.from(2020,6,1))
                .commit();



        calendario.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                Log.i( "data: ", "valor: " + (date.getMonth() + 1) + "/" + date.getYear() );
            }
        });
    }

    public void recuperarResumo(){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_toolbar, menu );
        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.MenuSair:
                autenticacao.signOut();
                startActivity( new Intent( this, MainActivity.class ) );
                finish();
                break;
        }
        return super.onOptionsItemSelected( item );
    }

    public void criarDespesa(View view){
        startActivity( new Intent( getApplicationContext(), DespesasActivity.class ) );

    }
    public void criarLucro(View view){
        startActivity( new Intent( getApplicationContext(), LucrosActivity.class ) );
    }

}
