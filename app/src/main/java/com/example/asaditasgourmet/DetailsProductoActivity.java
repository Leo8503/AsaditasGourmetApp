package com.example.asaditasgourmet;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.asaditasgourmet.database.DatabaseHelper;
import com.travijuu.numberpicker.library.NumberPicker;

public class DetailsProductoActivity extends AppCompatActivity {

    String id, foto, producto , categoria , precio, descripcion , estado, fkcategoria;
    private ImageView imagen;
    private Button boton;
    DatabaseHelper db;
    private TextView xproducto, xcategoria, xprecio, xdescripcion;
    String path = GlobalInfo.PATH_IP;

    TextView textCartItemCount;
    int mCartItemCount = 0;

    @Override
    public void onResume(){
        super.onResume();
        mCartItemCount = db.getCountCart();
        setupBadge();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.producto_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(getApplicationContext());
        imagen = (ImageView) findViewById(R.id.resImage);
        boton = (Button) findViewById(R.id.add_cart);
        mCartItemCount = db.getCountCart();

        xproducto = (TextView) findViewById(R.id.resName);
        xcategoria = (TextView) findViewById(R.id.resCuisine);
        xprecio = (TextView) findViewById(R.id.average_price);
        xdescripcion = (TextView) findViewById(R.id.description);


        Bundle param = this.getIntent().getExtras();
        if(param != null){
              id = String.valueOf(param.getString("idproducto"));
              foto = String.valueOf(param.getString("foto"));
              producto = String.valueOf(param.getString("producto"));
              precio = String.valueOf(param.getString("precio"));
              descripcion = String.valueOf(param.getString("descripcion"));
              estado = String.valueOf(param.getString("estado"));
              categoria = String.valueOf(param.getString("categoria"));
              fkcategoria = String.valueOf(param.getString("fkcategoria"));

              Glide.with(this).load(path+"pages/platos/upload/" + foto).into(imagen);
              xproducto.setText(""+producto);
              xcategoria.setText(""+categoria);
              xprecio.setText("Precio: "+precio);
              xdescripcion.setText(""+descripcion);
        }

        NumberPicker numberPicker = (NumberPicker) findViewById(R.id.number_picker);
        numberPicker.setMax(999);
        numberPicker.setMin(0);
        numberPicker.setUnit(1);
        numberPicker.setValue(0);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if(numberPicker.getValue() == 0){
                Toast.makeText(getApplicationContext(), "Agregue la cantidad ", Toast.LENGTH_LONG).show();
              }else{
                 db.insertCart(getApplicationContext(), foto, precio, producto, String.valueOf(numberPicker.getValue()), String.valueOf(Integer.parseInt(precio)*numberPicker.getValue()), categoria, fkcategoria);
                 setupBadge();
                  numberPicker.setValue(0);
              }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem menuItem = menu.findItem(R.id.action_cart);
        View actionView = menuItem.getActionView();
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);
        setupBadge();
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart: {
                Intent intent = new Intent(DetailsProductoActivity.this, CardItemActivity.class );
                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupBadge() {
        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
