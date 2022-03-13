package ma.tp04.ratechiefs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import ma.tp04.ratechiefs.adapter.ChiefAdapter;
import ma.tp04.ratechiefs.adapter.PlatAdapter;
import ma.tp04.ratechiefs.beans.Chief;
import ma.tp04.ratechiefs.beans.Plat;
import ma.tp04.ratechiefs.databinding.ActivityMainBinding;
import ma.tp04.ratechiefs.service.ChiefService;
import ma.tp04.ratechiefs.service.PlatService;

public class ListActivity2 extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private ListView liste;
    ArrayList<Plat> platsDuChef = new ArrayList<Plat>();
    private ChiefService cs;
    private Chief chef;
    private Button noter;
    private ImageView image;
    private TextView name;
    private PlatService service;
    private RecyclerView recyclerView;
    private PlatAdapter platAdapter = null;
    private boolean alreadyExecuted = false;
    ActivityMainBinding binding;
    Handler mainHandler = new Handler();
    ProgressDialog progressDialog;
    RelativeLayout swipelayout;

    GestureDetector gesturedetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list2);
        liste  =  findViewById(R.id.liste);
        //service = PlatService.getInstance();
        service = new PlatService();

       // if(!alreadyExecuted) {
            init();
          //  alreadyExecuted = true;
       // }

        image = findViewById(R.id.img);
        name = findViewById(R.id.name);
        String url;
        Intent intent = getIntent();
        int id = Integer.parseInt(intent.getStringExtra("idChief"));
        cs = ChiefService.getInstance();
        chef = cs.findById(id);
        name.setText(chef.getName());
        url = chef.getImg();
        new ListActivity2.FetchImage(url,image).start();
       // platsDuChef.clear();
        for (Plat p: service.findAll()
             ) {
            if(id == p.getChef()){
                platsDuChef.add(p);
            }
        }
        liste.setAdapter(new PlatAdapter(this, platsDuChef));
        liste.setOnItemClickListener(this);
        //
        swipelayout = findViewById(R.id.parent);
        swipelayout.setOnTouchListener(new OnSwipeTouchListener(ListActivity2.this) {
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                Intent intent = new Intent(ListActivity2.this, ListActivity.class);
                ListActivity2.this.startActivity(intent);
                ListActivity2.this.finish();
            }
        });
    }

    public void init(){
        service.create(new Plat("TAGLIATELLES AU SAUMON FRAIS", R.mipmap.plat1, 1, "https://youtu.be/lo35JAl6rDQ"));
        service.create(new Plat("CARBONADE FLAMANDE", R.mipmap.plat2, 1, "https://youtu.be/0TdFDv4dJqs"));
        service.create(new Plat("TARTE TATIN AUX COINGS", R.mipmap.plat3, 1, "https://youtu.be/RsIXTwtu7nA"));
        service.create(new Plat("Fricassée de volaille aux morilles", R.mipmap.plat4, 2, "https://youtu.be/9fxFLC8xZgk"));
        service.create(new Plat("Pavé de bar et huître", R.mipmap.plat5, 2, "https://www.likeachef.fr/recette/pave-de-bar-et-huitre-sur-une-fondue-de-poireaux-bouillon-iode"));
        service.create(new Plat("LA FRAMBOISE ET LE CAFÉ BOURBON POINTU", R.mipmap.plat6, 3, "https://www.academiedugout.fr/recettes/la-framboise-et-le-cafe_4496_2"));
        service.create(new Plat("YAOURTS MAISON", R.mipmap.plat7, 3, "https://www.academiedugout.fr/recettes/yaourts-maison_4503_2"));
        service.create(new Plat("Tatin d'oignon rouge au miel", R.mipmap.plat8, 4, "https://youtu.be/pvdcDgse6u4"));
        service.create(new Plat("Soufflés de topinambours à la vanille et au chocolat chaud", R.mipmap.plat9, 4, "https://youtu.be/dbn6QEvBWg8"));
        service.create(new Plat("Tarte de brocoli", R.mipmap.plat10, 5, "https://youtu.be/Dtx6XO9RSdE"));
        service.create(new Plat("MOELLEUX CHOCOLAT", R.mipmap.plat11, 6, "https://youtu.be/qN6bSCFrhpU"));
        service.create(new Plat("TOUT PETITS POIS", R.mipmap.plat12, 6, "https://www.academiedugout.fr/recettes/tout-petits-pois_4473_2"));
        service.create(new Plat("Risotto à la citrouille et à la scamorza fumée", R.mipmap.plat13, 7, "https://youtu.be/6A_c6MPsZko"));
        service.create(new Plat("Cottage pie au boeuf et à la bière", R.mipmap.plat14, 7, "https://youtu.be/M_GNznvIN1E"));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        int id = Integer.parseInt(((TextView)view.findViewById(R.id.idplat)).getText().toString());
        String lien = service.findById(id).getRecette();
       // System.out.println("lien: "+ id);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(lien));
        startActivity(browserIntent);
    }

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

                    progressDialog = new ProgressDialog(ListActivity2.this);
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
        }
    }
}