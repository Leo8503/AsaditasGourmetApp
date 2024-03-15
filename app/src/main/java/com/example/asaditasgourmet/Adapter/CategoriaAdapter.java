package com.example.asaditasgourmet.Adapter;

import com.example.asaditasgourmet.GlobalInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.asaditasgourmet.R;
import com.example.asaditasgourmet.modelo.Categoria;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<Categoria> notesList;
    private List<Categoria> movieListFiltered;
    String path = GlobalInfo.PATH_IP;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView cuisineName;
        public RelativeLayout linear1;
        public ImageView img;


        public MyViewHolder(View view) {
            super(view);
            cuisineName = view.findViewById(R.id.cuisineName);
            linear1 = view.findViewById(R.id.cuisineImageCard);
            img = view.findViewById(R.id.cuisineImage);

        }
    }


    public CategoriaAdapter(Context context, List<Categoria> notesList) {
        this.context = context;
        this.notesList = notesList;
        this.movieListFiltered = notesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categoria_list_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Categoria note = notesList.get(position);

        Glide.with(context).load(path+"pages/categoria/upload/"+note.getFoto()).into(holder.img);
        holder.cuisineName.setText(""+note.getCategoria());
        holder.linear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent intent = new Intent(context, DetailsCategoriaActivity.class);
                intent.putExtra("idcategoria",""+note.getIdCategoria());
                context.startActivity(intent);*/
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
                    List<Categoria> filteredList = new ArrayList<>();
                    for (Categoria movie : notesList) {
                        if (movie.getCategoria().toLowerCase().contains(charString.toLowerCase())) {
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
                notesList = (ArrayList<Categoria>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

}
