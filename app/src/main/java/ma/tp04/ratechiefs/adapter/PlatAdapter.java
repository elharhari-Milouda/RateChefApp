package ma.tp04.ratechiefs.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import ma.tp04.ratechiefs.ListActivity2;
import ma.tp04.ratechiefs.R;
import ma.tp04.ratechiefs.beans.Chief;
import ma.tp04.ratechiefs.beans.Plat;

public class PlatAdapter extends BaseAdapter{
    private List<Plat> plats;
    private LayoutInflater inflater;

    public PlatAdapter(Activity activity, List<Plat> plats) {
        this.plats = plats;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return plats.size();
    }

    @Override
    public Object getItem(int i) {
        return plats.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i+1;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null)
            view = inflater.inflate(R.layout.plat_item, null);

        TextView idplat = view.findViewById(R.id.idplat);
        TextView nom = view.findViewById(R.id.nom);
        ImageView photo = view.findViewById(R.id.photo);

        idplat.setText(plats.get(i).getId()+"");
        nom.setText(plats.get(i).getNom()+"");
        photo.setImageResource(plats.get(i).getImg());

        return view;
    }
}
