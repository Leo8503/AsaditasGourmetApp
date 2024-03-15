package com.example.asaditasgourmet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.asaditasgourmet.Adapter.CartAdapter;
import com.example.asaditasgourmet.Adapter.CountriesListAdapter;
import com.example.asaditasgourmet.database.DatabaseHelper;
import com.example.asaditasgourmet.modelo.Cart;
import java.util.ArrayList;
import java.util.List;

public class CardItemActivity extends AppCompatActivity {


    private CartAdapter mAdapter;
    DatabaseHelper db;
    private TextView subtotal, envio, total;
    private TextView texto, texto2;
    private Button boton;
    private RelativeLayout re1, re2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_cart);

        re1 = (RelativeLayout) findViewById(R.id.relative1);
        re2 = (RelativeLayout) findViewById(R.id.relative2);
        subtotal = (TextView) findViewById(R.id.changeAddressText);
        envio = (TextView) findViewById(R.id.changeAddressText);
        total = (TextView) findViewById(R.id.changeAddressText);

        db = new DatabaseHelper(this);
        subtotal.setText("$ "+db.getSumCart());
        cargar();

        boton = (Button) findViewById(R.id.payAmountBtn);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UbicacionActivity.class);
                startActivity(intent);
            }
        });
    }

    public void cargar() {
        List<Cart> notesList = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.cartItemRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        notesList.addAll(db.getAllCart());
        mAdapter = new CartAdapter(this, notesList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        if (recyclerView.getAdapter() != null) {
            if (recyclerView.getAdapter().getItemCount() == 0) {
                recyclerView.setVisibility(View.GONE);
                re1.setVisibility(View.GONE);
                re2.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                re1.setVisibility(View.VISIBLE);
                re2.setVisibility(View.GONE);
            }
        }
    }
}
