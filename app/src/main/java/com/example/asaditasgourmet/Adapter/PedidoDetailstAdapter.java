package com.example.asaditasgourmet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.asaditasgourmet.CardItemActivity;
import com.example.asaditasgourmet.DetallePedidoActivity;
import com.example.asaditasgourmet.R;
import com.example.asaditasgourmet.database.DatabaseHelper;
import com.example.asaditasgourmet.modelo.Cart;
import com.example.asaditasgourmet.modelo.detalle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.example.asaditasgourmet.GlobalInfo;
/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class PedidoDetailstAdapter extends RecyclerView.Adapter<PedidoDetailstAdapter.MyViewHolder>  {
    private Context context;
    private List<detalle> notesList;
    private List<detalle> movieListFiltered;
    private DatabaseHelper mDatabase;
    DetallePedidoActivity activity;
    String path = GlobalInfo.PATH_IP;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView producto;
        public TextView  categoria;
        public TextView  cantidad;
        public TextView  subtotal;
        public ImageView imagen;
        public Button add;


        public MyViewHolder(View view) {
            super(view);
            producto = view.findViewById(R.id.favResName);
            categoria = view.findViewById(R.id.favResCategories);
            cantidad = view.findViewById(R.id.favResLocation);
            subtotal = view.findViewById(R.id.favoriteResPrice);
            imagen = view.findViewById(R.id.favResImage);
        }
    }


    public PedidoDetailstAdapter(Context context, List<detalle> notesList, DetallePedidoActivity activity) {
        this.context = context;
        this.notesList = notesList;
        this.movieListFiltered = notesList;
        mDatabase = new DatabaseHelper(context);
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_pedido_res_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final detalle note = notesList.get(position);
        Glide.with(context).load(path+"pages/platos/upload/"+note.getFoto()).into(holder.imagen);
        holder.categoria.setText(""+note.getCategoria());
        holder.cantidad.setText("Cantidad: "+note.getCantidad());
        holder.subtotal.setText("Total: "+note.getSubtotal());
        holder.producto.setText(""+note.getProducto());

    }


    @Override
    public int getItemCount() {
        return notesList.size();
    }

}
