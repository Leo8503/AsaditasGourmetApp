package com.example.asaditasgourmet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.asaditasgourmet.GlobalInfo;
import com.example.asaditasgourmet.R;
import com.example.asaditasgourmet.database.DatabaseHelper;
import com.example.asaditasgourmet.modelo.Cart;
import com.travijuu.numberpicker.library.NumberPicker;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class SucessAdapter extends RecyclerView.Adapter<SucessAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<Cart> notesList;
    private List<Cart> movieListFiltered;
    private DatabaseHelper mDatabase;
    String path = GlobalInfo.PATH_IP;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView note;
        public TextView timestamp;
        public TextView subtotal;
        public TextView  precio;
        public ImageView imagen;
        public RelativeLayout linear1;

        public MyViewHolder(View view) {
            super(view);
            note = view.findViewById(R.id.favResName);
            timestamp = view.findViewById(R.id.favResCategories);
            linear1 = view.findViewById(R.id.rowFG);
            precio = view.findViewById(R.id.favoriteResPrice);
            subtotal = view.findViewById(R.id.subtotal);
            imagen = view.findViewById(R.id.favResImage);
        }
    }


    public SucessAdapter(Context context, List<Cart> notesList) {
        this.context = context;
        this.notesList = notesList;
        this.movieListFiltered = notesList;
        mDatabase = new DatabaseHelper(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.succes_res_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Cart note = notesList.get(position);
        Glide.with(context).load(path+"pages/platos/upload/"+note.getFoto()).into(holder.imagen);
        holder.note.setText(""+note.getProducto());
        holder.timestamp.setText(""+note.getCategoria());
        holder.subtotal.setText("Cantidad "+note.getCantidad()+" x "+note.getPrecio());
        holder.precio.setText("Subtotal "+(Integer.parseInt(note.getCantidad()) * Integer.parseInt(note.getPrecio())));

    }

    public void filtrado(final String txtBuscar) {
        int longitud = txtBuscar.length();
        if (longitud == 0) {
            movieListFiltered.clear();
            movieListFiltered.addAll(notesList);
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                List<Cart> collecion = movieListFiltered.stream()
                        .filter(i -> i.getProducto().toLowerCase().contains(txtBuscar.toLowerCase()))
                        .collect(Collectors.toList());
                movieListFiltered.clear();
                movieListFiltered.addAll(collecion);
            } else {
                for (Cart c : notesList) {
                    if (c.getProducto().toLowerCase().contains(txtBuscar.toLowerCase())) {
                        movieListFiltered.add(c);
                    }
                }
            }
        }
        notifyDataSetChanged();
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
                    List<Cart> filteredList = new ArrayList<>();
                    for (Cart movie : notesList) {
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
                notesList = (ArrayList<Cart>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

}
