package com.example.asaditasgourmet.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.asaditasgourmet.Adapter.CategoriaAdapter;
import com.example.asaditasgourmet.Adapter.MainSliderAdapter;
import com.example.asaditasgourmet.Adapter.PicassoImageLoadingService;
import com.example.asaditasgourmet.Adapter.ProductoAdapter;
import com.example.asaditasgourmet.R;
import com.example.asaditasgourmet.modelo.Categoria;
import com.example.asaditasgourmet.modelo.Producto;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.List;
import ss.com.bannerslider.Slider;

import com.example.asaditasgourmet.GlobalInfo;
public class Fragment1 extends Fragment {

    private ProgressDialog pDialog;
    private List<Producto> listAnimation = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProductoAdapter myadapter;
    private List<Categoria> listAnimationc = new ArrayList<>();
    private RecyclerView recyclerViewc;
    private CategoriaAdapter myadapterc;
    private Slider slider;
    private RelativeLayout rcategoria, rproductos;
    String path = GlobalInfo.PATH_IP;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_1, container, false);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        recyclerView = view.findViewById(R.id.lstvPlatos);
        recyclerViewc = view.findViewById(R.id.lstvCategorias);
        recyclerView.setNestedScrollingEnabled(false);

        //  recyclerView.setNestedScrollingEnabled(false);
        rcategoria = view.findViewById(R.id.bottomSheet);
        rproductos = view.findViewById(R.id.bottomPlatos);

        Slider.init(new PicassoImageLoadingService(getActivity()));
        setupViews(view);

        CargarCategoria();
        CargarData();

        return view;
    }



    private void setupViews(View v) {
        setupPageIndicatorChooser();
        slider = v.findViewById(R.id.banner_slider1);
        //delay for testing empty view functionality
        slider.postDelayed(new Runnable() {
            @Override
            public void run() {
                slider.setAdapter(new MainSliderAdapter(getContext()));
                slider.setSelectedSlide(0);
            }
        }, 1500);
    }


    private void setupPageIndicatorChooser() {
        String[] pageIndicatorsLabels = getResources().getStringArray(R.array.page_indicators);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                pageIndicatorsLabels
        );
    }




    private void CargarData() {
        String url = path+"Api/ListaPlatos.php";
        showDialog();
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void setupRecyclerView(List<Producto> listAnimation) {
        myadapter = new ProductoAdapter(getActivity(),listAnimation) ;
        int numItems =  myadapter.getItemCount();
        if(numItems == 0){
            recyclerView.setVisibility(View.GONE);
            rproductos.setVisibility(View.GONE);
            // linear1.setVisibility(View.VISIBLE);
        } else{
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
            //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), 2));
            recyclerView.setAdapter(myadapter);
            recyclerView.setVisibility(View.VISIBLE);
            rproductos.setVisibility(View.VISIBLE);
            // linear1.setVisibility(View.GONE);
        }
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
        myadapterc = new CategoriaAdapter(getActivity(),listAnimation) ;
        int numItems =  myadapterc.getItemCount();
        if(numItems == 0){
            recyclerViewc.setVisibility(View.GONE);
            rcategoria.setVisibility(View.GONE);
        } else{
            LinearLayoutManager linearLayoutmanager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            recyclerViewc.setLayoutManager(linearLayoutmanager);
            recyclerViewc.setAdapter(myadapterc);
            recyclerViewc.setVisibility(View.VISIBLE);
            rcategoria.setVisibility(View.VISIBLE);
        }
    }
}
