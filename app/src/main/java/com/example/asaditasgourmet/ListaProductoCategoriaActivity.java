package com.example.asaditasgourmet;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.asaditasgourmet.Adapter.ProductoAdapter;
import com.example.asaditasgourmet.database.DatabaseHelper;
import com.example.asaditasgourmet.modelo.Producto;
import com.travijuu.numberpicker.library.NumberPicker;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListaProductoCategoriaActivity extends AppCompatActivity {

    String id, categoria;
    private TextView text;
    private ProgressDialog pDialog;
    private List<Producto> listAnimation = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProductoAdapter myadapter;
    String path = GlobalInfo.PATH_IP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_producto_categoria);
        text = findViewById(R.id.deliveringAtText);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        recyclerView = findViewById(R.id.lstvPlatos);

        Bundle param = this.getIntent().getExtras();
        if(param != null){
            categoria = String.valueOf(param.getString("categoria"));
            id = String.valueOf(param.getString("idcategoria"));
            text.setText("Busqueda para '"+categoria+"'");
            CargarData(id);
        }
    }


    private void CargarData(final String sid) {
      String tag_string_req = "req_register";
      //pDialog.setMessage("Validando usuario ...");
      showDialog();
      StringRequest strReq = new StringRequest(Request.Method.POST,
              path+"Api/ListaPlatosPorCategoria.php", new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
            try {
                hideDialog();
                JSONArray myJsonArray = new JSONArray(response);
                for(int i = 0; i<myJsonArray.length(); i++){
                    Producto ob = new Producto();
                    ob.setIdProducto(myJsonArray.getJSONObject(i).getInt("id"));
                    ob.setProducto(myJsonArray.getJSONObject(i).getString("nombre"));
                    ob.setPrecio(myJsonArray.getJSONObject(i).getString(("precio")));
                    ob.setFoto(myJsonArray.getJSONObject(i).getString(("foto")));
                    ob.setDescripcion(myJsonArray.getJSONObject(i).getString("descripcion"));
                    ob.setEstado(myJsonArray.getJSONObject(i).getString("estado"));
                    ob.setCategoria(myJsonArray.getJSONObject(i).getString("categoria"));
                    ob.setFkCategoria(myJsonArray.getJSONObject(i).getString("fkcategoria"));
                    listAnimation.add(ob);
                }
                setupRecyclerView(listAnimation);
            } catch (Exception e) {
                e.printStackTrace();
                hideDialog();
                Toast.makeText(getApplicationContext(), "e "+e.getMessage(), Toast.LENGTH_LONG).show();
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
              params.put("id", sid);
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

    private void setupRecyclerView(List<Producto> listAnimation) {
        myadapter = new ProductoAdapter(this,listAnimation) ;
        int numItems =  myadapter.getItemCount();
        if(numItems == 0){
            recyclerView.setVisibility(View.GONE);
        } else{
            recyclerView.setLayoutManager(new GridLayoutManager(this,2));
            //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), 2));
            recyclerView.setAdapter(myadapter);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

}
