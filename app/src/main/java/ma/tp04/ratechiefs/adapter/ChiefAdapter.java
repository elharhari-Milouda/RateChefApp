package ma.tp04.ratechiefs.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import ma.tp04.ratechiefs.ListActivity;
import ma.tp04.ratechiefs.ListActivity2;
import ma.tp04.ratechiefs.R;
import ma.tp04.ratechiefs.beans.Chief;
import ma.tp04.ratechiefs.service.ChiefService;

public class ChiefAdapter extends RecyclerView.Adapter<ChiefAdapter.ChiefViewHolder> implements Filterable {
    private static final String TAG = "ChiefAdapter";
    private List<Chief> chiefs;
    private List<Chief> chiefsFilter;
    private Context context;
    private NewFilter mfilter;

    public ChiefAdapter(Context context, List<Chief> chiefs) {
        this.chiefs = chiefs;
        this.context = context;
        chiefsFilter = new ArrayList<>();
        chiefsFilter.addAll(chiefs);
        mfilter = new NewFilter(this);
    }
    @NonNull
    @Override
    public ChiefAdapter.ChiefViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(this.context).inflate(R.layout.star_item, parent, false);
        final ChiefViewHolder holder = new ChiefViewHolder(v);
        holder.plat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View popup = LayoutInflater.from(context).inflate(R.layout.star_edit_item, null,
                        false);
                final ImageView img = popup.findViewById(R.id.img);
                final RatingBar bar = popup.findViewById(R.id.ratingBar);
                final TextView idss = popup.findViewById(R.id.idss);
                Bitmap bitmap = ((BitmapDrawable)((ImageView)v.findViewById(R.id.img)).getDrawable()).getBitmap();
                img.setImageBitmap(bitmap);
                bar.setRating(((RatingBar)v.findViewById(R.id.stars)).getRating());
                idss.setText(((TextView)v.findViewById(R.id.ids)).getText().toString());
                //int ids = Integer.parseInt(idss.getText().toString());
                Intent intent = new Intent(context, ListActivity2.class);
                intent.putExtra("idChief", idss.getText().toString());
                intent.putExtra("image", img.toString());
                context.startActivity(intent);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View popup = LayoutInflater.from(context).inflate(R.layout.star_edit_item, null,
                        false);
                final ImageView img = popup.findViewById(R.id.img);
                final RatingBar bar = popup.findViewById(R.id.ratingBar);
                final TextView idss = popup.findViewById(R.id.idss);
                Bitmap bitmap = ((BitmapDrawable)((ImageView)v.findViewById(R.id.img)).getDrawable()).getBitmap();
                img.setImageBitmap(bitmap);
                bar.setRating(((RatingBar)v.findViewById(R.id.stars)).getRating());
                idss.setText(((TextView)v.findViewById(R.id.ids)).getText().toString());
                /*AlertDialog dialog = new AlertDialog.Builder(context, R.style.AlertDialogStyle)
                        .setTitle("Notez : ")
                        .setMessage("Donner votre avis :")
                        .setView(popup)
                        .setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                float s = bar.getRating();
                                int ids = Integer.parseInt(idss.getText().toString());
                                Chief star = ChiefService.getInstance().findById(ids);
                                star.setStar(s);
                                ChiefService.getInstance().update(star);
                                notifyItemChanged(holder.getAdapterPosition());
                            }
                        })
                        .setNeutralButton("supprimer", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id)
                            {

                            }
                        })
                        .setNegativeButton("Annuler", null)
                        .create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));

                dialog.show();*/
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChiefAdapter.ChiefViewHolder holder, int position) {
        Log.d(TAG, "onBindView call ! "+ position);
        Glide.with(context)
                .asBitmap()
                .load(chiefsFilter.get(position).getImg())
                .apply(new RequestOptions().override(100, 100))
                .into(holder.img);
        holder.name.setText(chiefsFilter.get(position).getName().toUpperCase());
        holder.stars.setRating(chiefsFilter.get(position).getStar());
        holder.idss.setText(chiefsFilter.get(position).getId()+"");

    }

    @Override
    public int getItemCount() {
        return chiefsFilter.size();
    }

    @Override
    public Filter getFilter() {
        return mfilter;
    }

    public class ChiefViewHolder extends RecyclerView.ViewHolder {
        TextView idss;
        ImageView img;
        ImageView plat;
        TextView name;
        RatingBar stars;
        RelativeLayout parent;
        Button noter;
        public ChiefViewHolder(@NonNull View itemView) {
            super(itemView);
            idss = itemView.findViewById(R.id.ids);
            img = itemView.findViewById(R.id.img);
            plat = itemView.findViewById(R.id.plats);
            name = itemView.findViewById(R.id.name);
            stars = itemView.findViewById(R.id.stars);
            parent = itemView.findViewById(R.id.parent);
        }
    }
    public class NewFilter extends Filter {
        public RecyclerView.Adapter mAdapter;
        public NewFilter(RecyclerView.Adapter mAdapter) {
            super();
            this.mAdapter = mAdapter;
        }
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            chiefsFilter.clear();
            final FilterResults results = new FilterResults();
            if (charSequence.length() == 0) {
                chiefsFilter.addAll(chiefs);
            } else {
                final String filterPattern = charSequence.toString().toLowerCase().trim();
                for (Chief p : chiefs) {
                    if (p.getName().toLowerCase().startsWith(filterPattern)) {
                        chiefsFilter.add(p);
                    }
                }
            }
            results.values = chiefsFilter;
            results.count = chiefsFilter.size();
            return results;
        }
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            chiefsFilter = (List<Chief>) filterResults.values;
            this.mAdapter.notifyDataSetChanged();
        }
    }
}
