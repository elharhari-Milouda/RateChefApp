package ma.tp04.ratechiefs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ma.tp04.ratechiefs.adapter.ChiefAdapter;
import ma.tp04.ratechiefs.beans.Chief;
import ma.tp04.ratechiefs.databinding.ActivityMainBinding;
import ma.tp04.ratechiefs.service.ChiefService;

public class ListActivity extends AppCompatActivity {
    private List<Chief> chiefs;
    private RecyclerView recyclerView;
    private ChiefService service;
    private ChiefAdapter chiefAdapter = null;
    ActivityMainBinding binding;
    Handler mainHandler = new Handler();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        chiefs = new ArrayList<>();
        service = ChiefService.getInstance();
        init();
        recyclerView = findViewById(R.id.recycleList);
        chiefAdapter = new ChiefAdapter(this, service.findAll());
        recyclerView.setAdapter(chiefAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    public void init(){
        service.create(new Chief("Alain Ducasse", "https://www.ecoleducasse.com/application/files/6515/8507/1343/OUR_FOUNDER_Alain_Ducasse_c_Pierre_Monetta_HD.jpg", 3.5f));
        service.create(new Chief("Paule Bocuse", "https://img.cuisineaz.com/680x357/2021/09/17/i180594-paul-bocuse-biographie.png", 3));
        service.create(new Chief("Anne-Sophie Pic", "https://www.hublot.com/sites/default/files/styles/global_laptop_1x/public/2021-11/anne-sophie-pic-hublot-ambassador-l-1.jpg?itok=VbbQBYwC", 5));
        service.create(new Chief("Alain Passard", "https://cdn.radiofrance.fr/s3/cruiser-production/2019/06/690eaec6-ef85-4743-96e4-8fea8f984a2b/1136_000_8l06m.jpg", 1));
        service.create(new Chief("Pierre Gagnaire", "https://cdn-elle.ladmedia.fr/var/plain_site/storage/images/personnalites/pierre-gagnaire/78823337-3-fre-FR/Pierre-Gagnaire.jpg", 5));
        service.create(new Chief("Guy Savoy", "https://brandandcelebrities.com/wp-content/uploads/2017/05/guy-savoy-2.jpeg", 1));
        service.create(new Chief("Gordon Ramsay", "https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/host-judge-gordon-ramsay-in-the-family-reunion-episode-of-news-photo-1573674492.jpg", 1));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (chiefAdapter != null){
                    chiefAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });
        return true;
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            switch (direction){
                case ItemTouchHelper.LEFT:
                    //************************
                    service.delete(service.findById(position+1));
                    chiefAdapter.notifyItemRemoved(position);
                    //*******************
                    break;
                case ItemTouchHelper.RIGHT:
                    View popup = LayoutInflater.from(ListActivity.this).inflate(R.layout.star_edit_item, null,
                            false);
                    final ImageView img = popup.findViewById(R.id.img);
                    final RatingBar bar = popup.findViewById(R.id.ratingBar);
                    final TextView idss = popup.findViewById(R.id.idss);
                    String url = service.findById(position+1).getImg();
                    new FetchImage(url,img).start();
                    bar.setRating(service.findById(position+1).getStar());
                    idss.setText(((TextView)findViewById(R.id.ids)).getText().toString());
                    AlertDialog.Builder alert = new AlertDialog.Builder(ListActivity.this, R.style.AlertDialogCustom);
                    alert.setTitle("Notez : \n" + service.findById(position+1).getName());
                    //alert.setMessage("Chef " + service.findById(position+1).getName());
                    alert.setView(popup);
                    alert.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            float s = bar.getRating();
                           // int ids = Integer.parseInt(idss.getText().toString());
                            Chief chef = service.findById(position+1);
                            chef.setStar(s);
                            service.update(chef);
                            chiefAdapter.notifyItemChanged(position);
                        }
                    });
                    alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            chiefAdapter.notifyDataSetChanged();
                        }
                    });
                    alert.create();
                    alert.show();
                    break;
            }
        }
    };

    class FetchImage extends Thread{

        String URL;
        Bitmap bitmap;
        ImageView imageView;

        FetchImage(String URL, ImageView img){

            this.URL = URL;
            imageView = img;

        }

        @Override
        public void run() {

            mainHandler.post(new Runnable() {
                @Override
                public void run() {

                    progressDialog = new ProgressDialog(ListActivity.this);
                    progressDialog.setMessage("wait....");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            });

            InputStream inputStream = null;
            try {
                inputStream = new URL(URL).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

            mainHandler.post(new Runnable() {
                @Override
                public void run() {

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                        imageView.setImageBitmap(bitmap);
                }
            });
        }}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.share){
            String txt = "Get your app NOW : https://drive.google.com/file/d/1czT5A3uOJ6ijKKgfWfMJ6T4NxRQ6S8x2/view";
            String mimeType = "text/plain";
            ShareCompat.IntentBuilder
                    .from(this)
                    .setType(mimeType)
                    .setChooserTitle("Stars")
                    .setText(txt)
                    .startChooser();
        }
        return super.onOptionsItemSelected(item);
    }
}
