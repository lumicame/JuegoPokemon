package com.example.labsoftware09.parcial_iii;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView daño1,daño2;
    TextView pokemon1_nombre;
    TextView pokemon2_nombre;
    TextView vida1;
    TextView vida2;
    ImageView pokemon1_imagen;
    ImageView pokemon2_imagen;
    Button ataque;
    int jugador1_vida=100;
    int maquina_vida=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ataque= (Button) findViewById(R.id.atacar);
        pokemon1_nombre= (TextView) findViewById(R.id.pokemon1_nombre);
        pokemon2_nombre= (TextView) findViewById(R.id.pokemon2_nombre);
        pokemon1_imagen= (ImageView) findViewById(R.id.pokemon1);
        pokemon2_imagen= (ImageView) findViewById(R.id.pokemon2);
        vida1= (TextView) findViewById(R.id.pokemon1_vida);
        vida2= (TextView) findViewById(R.id.pokemon2_vida);
        daño1= (TextView) findViewById(R.id.textView_daño1);
        daño2= (TextView) findViewById(R.id.textView_daño2);
        vida1.setText("100");
        vida2.setText("100");
        ataque.setEnabled(false);
        ataque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ataque();
            }
        });
        int id1= (int) (Math.random()*721);
        int id2=(int) (Math.random()*721);
        traernombre(id1,pokemon1_nombre,pokemon1_imagen,false);
        traernombre(id2,pokemon2_nombre,pokemon2_imagen,true);



    }
    public AlphaAnimation FadeIn(){
        AlphaAnimation alphaAnimation=new AlphaAnimation(0.0f,1.0f);
        alphaAnimation.setDuration(1500);
        alphaAnimation.setFillAfter(true);
    return alphaAnimation;
    }

    public AlphaAnimation FadeOut(){
        AlphaAnimation alphaAnimation=new AlphaAnimation(1.0f,0.0f);
        alphaAnimation.setDuration(1500);
        alphaAnimation.setFillAfter(true);
        return alphaAnimation;
    }
    public boolean verificar(int vida){
        if (vida<=0){
            return true;
        }
        else return false;
    }
    public void ataque(){
        ataque.setEnabled(false);
        int ataque_jugador= (int) (Math.random()*50);
        final int ataque_maquina= (int) (Math.random()*50);
        //
        daño1.setText("-"+ataque_jugador+"");
        daño1.setAnimation(FadeIn());
        daño1.setAnimation(FadeOut());
        maquina_vida=(maquina_vida-ataque_jugador);
        if (verificar(maquina_vida)==true){
            vida1.setText("0");
            ataque.setEnabled(false);
            Toast.makeText(MainActivity.this, "GANASTES", Toast.LENGTH_SHORT).show();

        }else {
            vida1.setText(""+maquina_vida);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    jugador1_vida=(jugador1_vida-ataque_maquina);
                    daño2.setText("-"+ataque_maquina+"");
                    daño2.setAnimation(FadeIn());
                    daño2.setAnimation(FadeOut());
                    if (verificar(jugador1_vida)==true){
                        vida2.setText("0");
                        ataque.setEnabled(false);
                        Toast.makeText(MainActivity.this, "PERDISTES", Toast.LENGTH_SHORT).show();

                    }else {
                        vida2.setText(""+jugador1_vida);
                        ataque.setEnabled(true);
                    }

                }
            }, 1500);


        }



    }

    public void descargarimagen(ImageView imageView,String url){
        ImageLoader mImageLoader;
// The URL for the image that is being loaded.


// Get the ImageLoader through your singleton class.
        mImageLoader = MySingleton.getInstance(this).getImageLoader();
        mImageLoader.get(url, ImageLoader.getImageListener(imageView,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher));
        ataque.setEnabled(true);

    }

    public void traernombre(int id, final TextView t, final ImageView imageView, final boolean jugador){

        MySingleton.getInstance(this.getApplicationContext()).
                getRequestQueue();

// ...
        String url ="http://pokeapi.co/api/v2/pokemon/"+id+"/";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.i("response",response);
                        try{
                            JSONObject jsonObject=new JSONObject(response);
                            t.setText( jsonObject.getString("name"));
                            vida1.setText(""+maquina_vida);
                            vida2.setText(""+jugador1_vida);
                            JSONObject jsonObject1=new JSONObject(jsonObject.getString("sprites"));
                            Log.i("",jsonObject1.getString("back_default").toString());
                            String url;
                            if(jugador==true){
                                url=jsonObject1.getString("back_default").toString();
                                descargarimagen(imageView,url);
                            }else {
                                url=jsonObject1.getString("front_default").toString();
                                descargarimagen(imageView,url);
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("","That didn't work!");
            }
        });
// Add the request to the RequestQueue.

// Add a request (in this example, called stringRequest) to your RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            maquina_vida=100;
            jugador1_vida=100;
            int id1= (int) (Math.random()*721);
            int id2=(int) (Math.random()*721);
            traernombre(id1,pokemon1_nombre,pokemon1_imagen,false);
            traernombre(id2,pokemon2_nombre,pokemon2_imagen,true);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
