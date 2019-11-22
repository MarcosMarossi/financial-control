package com.example.controlefinanceiro.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.controlefinanceiro.Configurações.ConfigFirebase;
import com.example.controlefinanceiro.Helper.Base64Custom;
import com.example.controlefinanceiro.Model.Movimentacao;
import com.example.controlefinanceiro.Model.Usuario;
import com.example.controlefinanceiro.R;
import com.example.controlefinanceiro.adapter.AdapterMovimentacao;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PainelActitvity extends AppCompatActivity {

    private MaterialCalendarView calendario;
    private Double despesaTotal = 0.0, receitaTotal = 0.0, resultado;
    private TextView textUsuario, textValor;
    private RecyclerView recyclerView;
    private List<Movimentacao> movimentacoes = new ArrayList<>(  );
    private AdapterMovimentacao adapterMovimentacao;
    private DatabaseReference referencia = ConfigFirebase.getDataBaseFirebase();
    private FirebaseAuth autenticacao =  ConfigFirebase.getFireBaseAutenticacao();
    private DatabaseReference usuarioref, movimentacaoref = ConfigFirebase.getDataBaseFirebase();
    private ValueEventListener valEventListUsuario, valEventListMovimentacao;
    private String mesAnoSelecionado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.act_painel );
        Toolbar toolbar = findViewById( R.id.toolbar );
        toolbar.setTitle( "" );
        setSupportActionBar( toolbar );

        calendario = findViewById(R.id.calendarView);
        calendario.state().edit()
                .setMaximumDate(CalendarDay.from(2019,10,1))
                .setMaximumDate( CalendarDay.from(2020,2,1))
                .commit();

        CalendarDay calendarDay = calendario.getCurrentDate();

        String mesSelecionado = String.format( "%02d", (calendarDay.getMonth()+1) );
        mesAnoSelecionado = String.valueOf(mesSelecionado + "" +calendarDay.getYear());

        calendario.setOnMonthChangedListener( new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                String mesSelecionado = String.format( "%02d", (date.getMonth()+1) );
                mesAnoSelecionado = String.valueOf( (mesSelecionado + "" +date.getYear() ));
                System.out.println( mesAnoSelecionado );

                movimentacaoref.removeEventListener( valEventListMovimentacao );
                recuperarMovimentacao();
            }
        });

        textUsuario = findViewById( R.id.textUsuario );
        textValor = findViewById( R.id.textValor );

        swipe();

        recyclerView = findViewById( R.id.recyclerMovimentos );
        adapterMovimentacao = new AdapterMovimentacao( movimentacoes, this );
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( this );
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize( true );
        recyclerView.setAdapter(adapterMovimentacao);
    }


    public void recuperarMovimentacao(){

        String email = autenticacao.getCurrentUser().getEmail();
        String id = Base64Custom.codificarBase64( email );
        movimentacaoref = referencia.child( "movimentacao" ).child( id ).child( mesAnoSelecionado );

        System.out.println( "Movimentacao = " + mesAnoSelecionado );

        valEventListMovimentacao = movimentacaoref.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                movimentacoes.clear();
                for(DataSnapshot dados: dataSnapshot.getChildren()){
                    Movimentacao movimentacao = dados.getValue( Movimentacao.class);
                    System.out.println( movimentacao.getCategoria());
                    movimentacoes.add(movimentacao);
                }
                adapterMovimentacao.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );
    }

    public void swipe(){

        ItemTouchHelper.Callback item = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return 0;
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
        };

    }


    public void recuperarResumo(){

        String email = autenticacao.getCurrentUser().getEmail();
        String id = Base64Custom.codificarBase64( email );
        usuarioref = referencia.child( "usuarios" ).child( id );

        valEventListUsuario = usuarioref.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Usuario usuario = dataSnapshot.getValue( Usuario.class );

                despesaTotal = usuario.getDespesaTotal();
                receitaTotal = usuario.getReceitaTotal();

                resultado = receitaTotal - despesaTotal;

                DecimalFormat decimalFormat = new DecimalFormat( "0.##" );
                String format = decimalFormat.format( resultado );

                textUsuario.setText( "Olá, " + usuario.getNome()+".");
                textValor.setText("R$ "+ format);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
        startActivity( new Intent( getApplicationContext(), ReceitaActivity.class ) );
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarResumo();
        recuperarMovimentacao();
        System.out.println( "Evento iniciado" );
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println( "Evento removido" );
        usuarioref.removeEventListener( valEventListUsuario );
        movimentacaoref.removeEventListener( valEventListMovimentacao );
    }
}
