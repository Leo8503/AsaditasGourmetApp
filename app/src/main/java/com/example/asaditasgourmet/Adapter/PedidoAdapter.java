package com.example.asaditasgourmet.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.asaditasgourmet.DetailsProductoActivity;
import com.example.asaditasgourmet.DetallePedidoActivity;
import com.example.asaditasgourmet.R;
import com.example.asaditasgourmet.modelo.Pedido;
import com.example.asaditasgourmet.modelo.Producto;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<Pedido> notesList;
    private List<Pedido> movieListFiltered;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView pedido;
        public TextView estado;
        public TextView total;
        public TextView fecha;
        public CardView linear1;
        public ImageView foto;

        public MyViewHolder(View view) {
            super(view);
            pedido = view.findViewById(R.id.orderedResName);
            estado = view.findViewById(R.id.orderedResAddress);
            total = view.findViewById(R.id.orderedItemsText);
            fecha = view.findViewById(R.id.orderedTimeStamp);
            linear1 = view.findViewById(R.id.imageContainer);
        }

    }


    public PedidoAdapter(Context context, List<Pedido> notesList) {
        this.context = context;
        this.notesList = notesList;
        this.movieListFiltered = notesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pedido_list_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Pedido note = notesList.get(position);
        holder.pedido.setText("Pedido Nª: "+note.getId());
        holder.estado.setText("Estado: "+note.getEstado());
        holder.total.setText("Fecha: "+note.getFecha());
        holder.fecha.setText(""+note.getTotal());
        // Glide.with(context).load("http://192.168.0.55/GestorPedidos/pages/platos/upload/"+note.getFoto()).into(holder.foto);
        holder.linear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetallePedidoActivity.class);
                intent.putExtra("id",""+note.getId());
                intent.putExtra("total",note.getTotal());
                intent.putExtra("estado",note.getEstado());
                intent.putExtra("subtotal",note.getSubtotal());
                intent.putExtra("telefono",note.getTelefono());
                context.startActivity(intent);
            }
        });
    }



    private void EliminarProducto(int id, int position) {
      //  mDatabase.deleteProducto(id);
        notesList.remove(position);
        this.notifyItemRemoved(position);
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    notesList = movieListFiltered;
                } else {
                    List<Pedido> filteredList = new ArrayList<>();
                    for (Pedido movie : notesList) {
                        if (movie.getTelefono().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(movie);
                         //   Log.e(" moview tet rre"," Cliente "+movie.getNombre()+" "+movie.getNombre().toString());
                        }
                    }
                    notesList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = notesList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                notesList = (ArrayList<Pedido>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

}
