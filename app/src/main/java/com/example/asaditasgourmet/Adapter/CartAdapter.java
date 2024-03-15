package com.example.asaditasgourmet.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.asaditasgourmet.CardItemActivity;
import com.example.asaditasgourmet.R;
import com.example.asaditasgourmet.database.DatabaseHelper;
import com.example.asaditasgourmet.modelo.Cart;
import com.travijuu.numberpicker.library.NumberPicker;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.example.asaditasgourmet.GlobalInfo;
/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<Cart> notesList;
    private List<Cart> movieListFiltered;
    private DatabaseHelper mDatabase;
    CardItemActivity activity;
    String path = GlobalInfo.PATH_IP;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView note;
        public TextView timestamp;
        public TextView stock;
        public TextView  precio;
        public ImageView imagen;
        public RelativeLayout linear1;
        public TextView fecha;
        public Button add, minus;

        public MyViewHolder(View view) {
            super(view);
            note = view.findViewById(R.id.favResName);
            timestamp = view.findViewById(R.id.favResCategories);
            fecha = view.findViewById(R.id.display);
            linear1 = view.findViewById(R.id.rowFG);
            precio = view.findViewById(R.id.favoriteResPrice);
            imagen = view.findViewById(R.id.favResImage);
            add = view.findViewById(R.id.increment);
            minus = view.findViewById(R.id.decrement);

        }
    }


    public CartAdapter(Context context, List<Cart> notesList, CardItemActivity activity) {
        this.context = context;
        this.notesList = notesList;
        this.movieListFiltered = notesList;
        mDatabase = new DatabaseHelper(context);
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_favorite_res_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Cart note = notesList.get(position);
        Glide.with(context).load(path+"pages/platos/upload/"+note.getFoto()).into(holder.imagen);
        holder.note.setText(""+note.getProducto());
        holder.timestamp.setText(""+note.getCategoria());
        holder.fecha.setText(""+Integer.parseInt(note.getCantidad()));
        holder.precio.setText("Precio "+note.getPrecio());

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valor = holder.fecha.getText().toString();
                int campo = Integer.parseInt(valor)+1;
                holder.fecha.setText(""+campo);

            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valor = holder.fecha.getText().toString();
                int campo = Integer.parseInt(valor);
                if(holder.fecha.getText().toString().equals("1")){
                    //holder.fecha.setText("0");
                    mDatabase.deleteCart(note.getId());
                    activity.cargar();
                }
                if(campo > 0){
                    campo = campo-1;
                    holder.fecha.setText(""+campo);
                }else{
                    holder.fecha.setText("0");
                    mDatabase.deleteCart(note.getId());
                    activity.cargar();
                }
            }
        });

        /*
        holder.note.setText(""+note.getNombre());
        holder.timestamp.setText(""+note.getTelefonoMovil());
        holder.fecha.setText(""+note.getTelefonoFijo());
        holder.linear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsClienteActivity.class);
                intent.putExtra("idcliente",""+note.getId());
                context.startActivity(intent);
            }
        });
*/
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
