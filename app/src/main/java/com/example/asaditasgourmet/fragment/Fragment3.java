package com.example.asaditasgourmet.fragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.asaditasgourmet.Adapter.PedidoAdapter;
import com.example.asaditasgourmet.Adapter.ProductoAdapter;
import com.example.asaditasgourmet.AppController;
import com.example.asaditasgourmet.R;
import com.example.asaditasgourmet.modelo.Pedido;
import com.example.asaditasgourmet.modelo.Producto;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static android.content.Context.MODE_PRIVATE;
import static com.example.asaditasgourmet.fragment.Fragment4.MY_PREFS_NAME;
import com.example.asaditasgourmet.GlobalInfo;

public class Fragment3 extends Fragment {


    private ProgressDialog pDialog;
    private List<Pedido> listAnimation = new ArrayList<>();
    private RecyclerView recyclerView;
    private RelativeLayout rcategoria, rproductos;
    private ImageView imagen;
    private TextView text1, text2;

    public static final String idkey = "id";
  	public static final String MY_PREFS_NAME = "MySession";
    String path = GlobalInfo.PATH_IP;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_3, container, false);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        imagen = view.findViewById(R.id.emptyCartImg);
        text1 = view.findViewById(R.id.emptyCartText);
        text2 = view.findViewById(R.id.addItemText);
        recyclerView = view.findViewById(R.id.favoriteResRecyclerView);

        SharedPreferences shared = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String idchannel = (shared.getString(idkey, ""));

        CargarData(idchannel);
        return view;
    }




        private void  CargarData(String id) {
            String tag_string_req = "req_register";
            pDialog.setMessage("Cargando ...");
            String url = path+"API/ListaPedidos.php";
            listAnimation.clear();
            showDialog();
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    hideDialog();
                    if(response.equals("[]")){

                    }else{
                          try{
                            JSONArray myJsonArray = new JSONArray(response);
                            for(int i = 0; i<myJsonArray.length(); i++){
                                Pedido ob = new Pedido();
                                ob.setId(myJsonArray.getJSONObject(i).getInt("id"));
                                ob.serTelefono(myJsonArray.getJSONObject(i).getString("telefono"));
                                ob.setFecha(myJsonArray.getJSONObject(i).getString(("fecha")));
                                ob.setTotal(myJsonArray.getJSONObject(i).getString(("total")));
                                ob.setSubtotal(myJsonArray.getJSONObject(i).getString(("subtotal")));
                                ob.setDireccion(myJsonArray.getJSONObject(i).getString("direccion"));
                                ob.setEstado(myJsonArray.getJSONObject(i).getString("estado"));
                                listAnimation.add(ob);
                            }
                            setupRecyclerView(listAnimation);
                          } catch (JSONException e) {
                              e.printStackTrace();
                              Toast.makeText(requireActivity().getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_LONG).show();
                          }
                      }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideDialog();
                    Toast.makeText(getActivity().getApplicationContext(), ""+error.getMessage(), Toast.LENGTH_LONG).show();
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

    private void setupRecyclerView(List<Pedido> listAnimation) {
        PedidoAdapter myadapter = new PedidoAdapter(getActivity(),listAnimation) ;
        int numItems =  myadapter.getItemCount();
        if(numItems == 0){
            recyclerView.setVisibility(View.GONE);
            imagen.setVisibility(View.VISIBLE);
            text1.setVisibility(View.VISIBLE);
            text2.setVisibility(View.VISIBLE);
        } else{
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(myadapter);
            recyclerView.setVisibility(View.VISIBLE);
            imagen.setVisibility(View.GONE);
            text1.setVisibility(View.GONE);
            text2.setVisibility(View.GONE);
        }
    }
}
