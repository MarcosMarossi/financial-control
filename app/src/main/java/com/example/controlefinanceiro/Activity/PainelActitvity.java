package com.example.controlefinanceiro.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
    private Movimentacao movimentacao;
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
        recyclerView = findViewById( R.id.recyclerMovimentos );

        adapterMovimentacao = new AdapterMovimentacao( movimentacoes, this );
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( this );
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize( true );
        recyclerView.setAdapter(adapterMovimentacao);

        swipe();
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
                    //System.out.println( movimentacao.getCategoria());
                    movimentacao.setKey( dados.getKey() );
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
                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags( dragFlags, swipeFlags );
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                excluirMovimento( viewHolder );

            }
        };

        new ItemTouchHelper( item ).attachToRecyclerView( recyclerView );

    }

    public void excluirMovimento(final RecyclerView.ViewHolder viewHolder){

        AlertDialog.Builder alert = new AlertDialog.Builder( this );
        alert.setTitle( "Exlcuir movimentação" );
        alert.setMessage( "Tem certeza que deseja exlcuir a movimentação?" );
        alert.setCancelable( false );

        alert.setPositiveButton( "Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                int pos = viewHolder.getAdapterPosition();
                movimentacao = movimentacoes.get( pos );


                String email = autenticacao.getCurrentUser().getEmail();
                String id = Base64Custom.codificarBase64( email );
                movimentacaoref = referencia.child( "movimentacao" ).child( id ).child( mesAnoSelecionado );

                movimentacaoref.child( movimentacao.getKey() ).removeValue();
                adapterMovimentacao.notifyItemRemoved(pos);
                atualizarSaldo();
            }
        } );

        alert.setNegativeButton( "Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText( PainelActitvity.this, "Exclusão cancelada", Toast.LENGTH_SHORT ).show();
                adapterMovimentacao.notifyDataSetChanged();
            }
        } );

        AlertDialog alertDialog = alert.create();
        alertDialog.show();

    }

    public void atualizarSaldo(){

        String email = autenticacao.getCurrentUser().getEmail();
        String id = Base64Custom.codificarBase64( email );
        usuarioref = referencia.child( "usuarios" ).child( id );

        if(movimentacao.getTipo().equals( "r" )){
            receitaTotal = receitaTotal - movimentacao.getValor();
            usuarioref.child( "receitaTotal" ).setValue( receitaTotal );
        }

        if(movimentacao.getTipo().equals( "d" )){
            despesaTotal = despesaTotal - movimentacao.getValor();
            usuarioref.child( "despesaTotal" ).setValue( despesaTotal );
        }

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
