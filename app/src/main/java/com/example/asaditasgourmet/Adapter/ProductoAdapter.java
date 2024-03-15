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
import com.example.asaditasgourmet.R;
import com.example.asaditasgourmet.modelo.Producto;
import java.util.ArrayList;
import java.util.List;
import com.example.asaditasgourmet.GlobalInfo;
/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<Producto> notesList;
    private List<Producto> movieListFiltered;
    String path = GlobalInfo.PATH_IP;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView producto;
        public TextView precio;
        public CardView linear1;
        public ImageView foto;

        public MyViewHolder(View view) {
            super(view);
            producto = view.findViewById(R.id.resCuisine);
            precio = view.findViewById(R.id.average_price);
            linear1 = view.findViewById(R.id.card1);
            foto = view.findViewById(R.id.resImage);

        }

    }


    public ProductoAdapter(Context context, List<Producto> notesList) {
        this.context = context;
        this.notesList = notesList;
        this.movieListFiltered = notesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.producto_list_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Producto note = notesList.get(position);
        holder.producto.setText("Producto: "+note.getProducto());
        holder.precio.setText("Precio: "+note.getPrecio());
        Glide.with(context).load(path+"pages/platos/upload/"+note.getFoto()).into(holder.foto);
        holder.linear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsProductoActivity.class);
                intent.putExtra("idproducto",""+note.getIdProducto());
                intent.putExtra("foto",""+note.getFoto());
                intent.putExtra("producto",""+note.getProducto());
                intent.putExtra("precio",""+note.getPrecio());
                intent.putExtra("descripcion",""+note.getDescripcion());
                intent.putExtra("estado",""+note.getEstado());
                intent.putExtra("categoria",""+note.getCategoria());
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
                    List<Producto> filteredList = new ArrayList<>();
                    for (Producto movie : notesList) {
                        if (movie.getProducto().toLowerCase().contains(charString.toLowerCase())) {
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
                notesList = (ArrayList<Producto>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

}
