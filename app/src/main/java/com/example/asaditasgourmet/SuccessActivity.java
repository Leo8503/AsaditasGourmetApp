package com.example.asaditasgourmet;

import static android.content.Context.MODE_PRIVATE;
import static com.example.asaditasgourmet.fragment.Fragment4.MY_PREFS_NAME;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.View;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.animation.ValueAnimator;
import android.view.animation.*;
import android.widget.Button;
import android.util.*;
import android.media.*;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.asaditasgourmet.Adapter.SucessAdapter;
import com.example.asaditasgourmet.AppController;
import com.example.asaditasgourmet.Domiciliario;
import com.example.asaditasgourmet.GlobalInfo;
import com.example.asaditasgourmet.MainActivity;
import com.example.asaditasgourmet.R;
import com.example.asaditasgourmet.RegistrarActivity;
import com.example.asaditasgourmet.database.DatabaseHelper;
import com.example.asaditasgourmet.modelo.Cart;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SuccessActivity extends AppCompatActivity {

    private TextView total;
    private Button boton;
    private RecyclerView recyclerView;
    private SucessAdapter mAdapter;
    DatabaseHelper db;
    private List<Cart> notesList = new ArrayList<>();
    private TextView direccion;
    private RelativeLayout re1, re2;
    private ProgressDialog pDialog;
    private TextView telefono;
    String latitud, longitud;
    String path = GlobalInfo.PATH_IP;
    public static final String idkey = "id";
  	public static final String MY_PREFS_NAME = "MySession";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_restaurant_page);
        recyclerView = (RecyclerView) findViewById(R.id.menuItemRecylerView);

        direccion = (TextView) findViewById(R.id.changeTelefono);
        total = (TextView) findViewById(R.id.total);
        telefono = (TextView) findViewById(R.id.changeAddressText);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        SharedPreferences shared = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String idchannel = (shared.getString(idkey, ""));

        Bundle parametros = this.getIntent().getExtras();
        if(parametros != null){
            String datos = parametros.getString("ubicacion");
            latitud = parametros.getString("latitud");
            longitud = parametros.getString("longitud");

            //Toast.makeText(getApplicationContext(), ""+datos, Toast.LENGTH_LONG).show();
            direccion.setText(datos);
        }


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        db = new DatabaseHelper(this);
        total.setText("Total: $ "+db.getSumCart());
        notesList.addAll(db.getAllCart());
        mAdapter = new SucessAdapter(this, notesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        if(recyclerView.getAdapter() !=  null){
            if(recyclerView.getAdapter().getItemCount() == 0){
                recyclerView.setVisibility(View.GONE);
            }else{
                recyclerView.setVisibility(View.VISIBLE);
            }
        }

        boton = (Button) findViewById(R.id.payAmountBtn);
        boton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             registerPedido(total.getText().toString() , direccion.getText().toString(), telefono.getText().toString(), latitud, longitud, idchannel);
            }
         });
    }

/*
    private void  registerPedido(String stotal, String sdireccion, String stelefono, String  slatitud, String slongitud, String sidcliente) {
        String tag_string_req = "req_register";
        pDialog.setMessage("Cargando Red ...");
        String url = path+"Api/registrarpedido.php";

        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                Toast.makeText(getApplicationContext(), ""+response, Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(getApplicationContext(), ""+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("total", ""+stotal);
                params.put("direccion", ""+sdireccion);
                params.put("telefono", ""+stelefono);
                params.put("latitud", ""+slatitud);
                params.put("longitud", ""+slongitud);
                params.put("idcliente", ""+sidcliente);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }*/
/*
    private void registerPedido(String total, String direccion, String telefono, String latitud, String longitud, String idcliente) {
  		String tag_string_req = "req_register";
  		pDialog.setMessage("Validando usuario ...");
  		showDialog();
      String url = path+"Api/registrarpedido.php";
      Toast.makeText(getApplicationContext(), ""+url, Toast.LENGTH_LONG).show();

  		StringRequest strReq = new StringRequest(Request.Method.POST,
  						url , new Response.Listener<String>() {
  				@Override
  				public void onResponse(String response) {
  					hideDialog();
  					 try {
  							 JSONArray respObj = new JSONArray(response);
  							 for(int i = 0; i < respObj.length(); i++){
  									 JSONObject c = respObj.getJSONObject(i);
  									 String resultado = c.getString("resultado");
  									 if(resultado.equals("1")){
                          Toast.makeText(getApplicationContext(), "Pedido Creado con exito", Toast.LENGTH_LONG).show();
  									 }
  									 if(resultado.equals("0")){
  										 Toast.makeText(getApplicationContext(), "Pedido 0", Toast.LENGTH_LONG).show();
  									 }
  									 if(resultado.equals("2")){
  										 Toast.makeText(getApplicationContext(), "Pedido 2", Toast.LENGTH_LONG).show();
  									 }
  							 }
  					 } catch (Exception e){
  							 e.printStackTrace();
  							 Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_LONG).show();
  					 }
  				}
  			}, new Response.ErrorListener() {
  				@Override
  				public void onErrorResponse(VolleyError error) {
  					hideDialog();
  					Toast.makeText(getApplicationContext(), "Error: " +error.getMessage(), Toast.LENGTH_LONG).show();
  				}
  			}) {
  				@Override
  				protected Map<String, String> getParams() {
  						Map<String, String> params = new HashMap<String, String>();
              params.put("total", total);
              params.put("direccion", direccion);
              params.put("telefono", telefono);
              params.put("latitud", latitud);
              params.put("longitud", longitud);
              params.put("idcliente", idcliente);
  						return params;
  				}
  		};
  		AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }*/


    private void registerPedido(String stotal, String sdireccion, String stelefono, String slatitud, String slongitud, String sidcliente) {
        String tag_string_req = "req_register";
        pDialog.setMessage("Cargando ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                path+"Api/registrarpedido.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                Toast.makeText(getApplicationContext(), ""+response, Toast.LENGTH_LONG).show();

                try {
                    JSONArray respObj = new JSONArray(response);
                    for(int i = 0; i < respObj.length(); i++){
                        JSONObject c = respObj.getJSONObject(i);
                        String resultado = c.getString("insertado");
                        if(!resultado.equals("0")){
                            Cursor cur = db.getAllCart(getApplicationContext());
                            ArrayList temp = new ArrayList();
                            if (cur != null) {
                                if (cur.moveToFirst()) {
                                    do {
                                        @SuppressLint("Range") String id = cur.getString(cur.getColumnIndex("id"));
                                        @SuppressLint("Range") String foto = cur.getString(cur.getColumnIndex("foto"));
                                        @SuppressLint("Range") String precio = cur.getString(cur.getColumnIndex("precio"));
                                        @SuppressLint("Range") String producto = cur.getString(cur.getColumnIndex("producto"));
                                        @SuppressLint("Range") String cantidad = cur.getString(cur.getColumnIndex("cantidad"));
                                        @SuppressLint("Range") String subtotal = cur.getString(cur.getColumnIndex("subtotal"));
                                        @SuppressLint("Range") String categoria = cur.getString(cur.getColumnIndex("categoria"));
                                        @SuppressLint("Range") String fkcategoria = cur.getString(cur.getColumnIndex("fkcategoria"));

                                        registerDetalle(id, foto, precio, producto, cantidad, subtotal, categoria, fkcategoria, resultado);
                                    } while (
                                        cur.moveToNext());
                                }
                            }
                            db.deleteAllCart();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);

                        }else{
                            Toast.makeText(getApplicationContext(), "No se pudo crear el usuario", Toast.LENGTH_LONG).show();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(getApplicationContext(), "error: " +error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("total", String.valueOf(stotal));
                params.put("direccion", String.valueOf(sdireccion));
                params.put("telefono", String.valueOf(stelefono));
                params.put("latitud", String.valueOf(slatitud));
                params.put("longitud", String.valueOf(slongitud));
                params.put("idcliente", String.valueOf(sidcliente));
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void registerDetalle(String sid, String sfoto, String sprecio, String sproducto, String scantidad, String ssubtotal, String scategoria, String sfkcategoria, String sfkpedido) {
        String tag_string_req = "req_register";
        pDialog.setMessage("Cargando ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                path+"Api/registradetallepedido.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                Toast.makeText(getApplicationContext(), "response: " +response, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(getApplicationContext(), "error: " +error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", String.valueOf(sid));
                params.put("foto", String.valueOf(sfoto));
                params.put("precio", String.valueOf(sprecio));
                params.put("producto", String.valueOf(sproducto));
                params.put("cantidad", String.valueOf(scantidad));
                params.put("subtotal", String.valueOf(ssubtotal));
                params.put("categoria", String.valueOf(scategoria));
                params.put("fkcategoria", String.valueOf(sfkcategoria));
                params.put("fkpedido", String.valueOf(sfkpedido));
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
