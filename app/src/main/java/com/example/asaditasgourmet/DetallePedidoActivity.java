package com.example.asaditasgourmet;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.asaditasgourmet.Adapter.PedidoDetailstAdapter;
import com.example.asaditasgourmet.modelo.detalle;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.asaditasgourmet.GlobalInfo;
public class DetallePedidoActivity extends AppCompatActivity {

    private TextView subtotal, envio, total;
    private ProgressDialog pDialog;
    private List<detalle> listAnimation = new ArrayList<>();
    private PedidoDetailstAdapter myadapter;
    private RecyclerView recyclerView;
    String path = GlobalInfo.PATH_IP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_pedido);

        subtotal =  findViewById(R.id.changeAddressText);
        envio =  findViewById(R.id.valorEnvio);
        total =  findViewById(R.id.valorSubtotal);


        recyclerView =  findViewById(R.id.menuItemRecylerView);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        Bundle param = this.getIntent().getExtras();
        if(param != null){
            String stotal = String.valueOf(param.getString("total"));
            String sestado = String.valueOf(param.getString("estado"));
            String ssubtotal = String.valueOf(param.getString("subtotal"));
            String stelefono = String.valueOf(param.getString("telefono"));
            String sid = String.valueOf(param.getString("id"));

            subtotal.setText(ssubtotal);

           Pedido(sid);
           //Toast.makeText(getApplicationContext(), ""+id,Toast.LENGTH_LONG).show();
        }
    }

    private void Pedido(String id) {
        String tag_string_req = "req_register";
        pDialog.setMessage("Cargando ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                path+"Api/ListaIdPpedido.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    hideDialog();
                    JSONArray myJsonArray = new JSONArray(response);
                    for(int i = 0; i<myJsonArray.length(); i++){
                        detalle ob = new detalle();
                        ob.setId(myJsonArray.getJSONObject(i).getInt("id"));
                        ob.setFoto(myJsonArray.getJSONObject(i).getString("foto"));
                        ob.setPrecio(myJsonArray.getJSONObject(i).getString(("precio")));
                        ob.setProducto(myJsonArray.getJSONObject(i).getString(("plato")));
                        ob.setCantidad(myJsonArray.getJSONObject(i).getString("cantidad"));
                        ob.setSubtotal(myJsonArray.getJSONObject(i).getString("subtotal"));
                        ob.setCategoria(myJsonArray.getJSONObject(i).getString("categoria"));
                        listAnimation.add(ob);
                    }
                    setupRecyclerView(listAnimation);
                } catch (Exception e) {
                    e.printStackTrace();
                    hideDialog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
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

    private void setupRecyclerView(List<detalle> listAnimation) {
        myadapter = new PedidoDetailstAdapter(this,listAnimation, this) ;
        int numItems =  myadapter.getItemCount();
        if(numItems == 0){
            recyclerView.setVisibility(View.GONE);
        } else{
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(myadapter);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }



}
