package com.example.asaditasgourmet.fragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.asaditasgourmet.Adapter.BuscarAdapter;
import com.example.asaditasgourmet.R;
import com.example.asaditasgourmet.modelo.Categoria;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.List;
import com.example.asaditasgourmet.GlobalInfo;

public class Fragment2  extends Fragment {

    private ProgressDialog pDialog;
    private List<Categoria> listAnimationc = new ArrayList<>();
    private RecyclerView recyclerViewc;
    private BuscarAdapter myadapterc;
    String path = GlobalInfo.PATH_IP;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_2, container, false);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        recyclerViewc = root.findViewById(R.id.lstvCategorias);

        CargarCategoria();

        return root;
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }




    private void CargarCategoria() {
        String url = path+"Api/ListaCategoria.php";
        showDialog();
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    hideDialog();
                    JSONArray myJsonArray = new JSONArray(response);
                    for(int i = 0; i<myJsonArray.length(); i++){
                        Categoria ob = new Categoria();
                        ob.setIdCategoria(myJsonArray.getJSONObject(i).getInt("id"));
                        ob.setCategoria(myJsonArray.getJSONObject(i).getString("categoria"));
                        ob.setFoto(myJsonArray.getJSONObject(i).getString("foto"));
                        listAnimationc.add(ob);
                    }
                    setupRecyclerViewC(listAnimationc);
                } catch (Exception e) {
                    e.printStackTrace();
                    hideDialog();
                    Toast.makeText(getActivity(), "e "+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(getActivity(), "error "+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


    private void setupRecyclerViewC(List<Categoria> listAnimation) {
        myadapterc = new BuscarAdapter(getActivity(),listAnimation) ;
        int numItems =  myadapterc.getItemCount();
        if(numItems == 0){
            recyclerViewc.setVisibility(View.GONE);
        } else{
            recyclerViewc.setLayoutManager(new GridLayoutManager(getActivity(),2));
            recyclerViewc.setAdapter(myadapterc);
            recyclerViewc.setVisibility(View.VISIBLE);
        }
    }
}
