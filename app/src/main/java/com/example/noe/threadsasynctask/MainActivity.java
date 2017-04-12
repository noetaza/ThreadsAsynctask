package com.example.noe.threadsasynctask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button btnHilo;
    private Button btnAsyncTask;
    private Button btnDescargar1;
    private Button btnDescargar2;
    private Button btnLimpiar;
    private ProgressBar pbarProgreso1;
    private ProgressBar pbarProgreso2;

    private ImageView imageView1;
    private ImageView imageView2;
    private String URL1 = "http://www.stickpng.com/assets/thumbs/584df34d6a5ae41a83ddee02.png";
    private String URL2 = "https://upload.wikimedia.org/wikipedia/en/d/d1/Toad_3D_Land.png";

    private Async tarea1;
    private DownloadI tarea2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnHilo = (Button)findViewById(R.id.btnHilo);
        btnAsyncTask = (Button)findViewById(R.id.btnAsyncTask);
        btnDescargar1 = (Button) findViewById(R.id.btnDescargarT);
        btnDescargar2 = (Button) findViewById(R.id.btnDescargarA);
        btnLimpiar = (Button)findViewById(R.id.btnLimpiar);
        pbarProgreso1 = (ProgressBar)findViewById(R.id.pbarProgreso1);
        pbarProgreso2 = (ProgressBar)findViewById(R.id.pbarProgreso2);

        pbarProgreso1.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        pbarProgreso2.getProgressDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);

        imageView1 = (ImageView) findViewById(R.id.image_view1);
        imageView2 = (ImageView) findViewById(R.id.image_view2);

        //Descargar Imagen con Thread
        btnDescargar1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        final Bitmap imagen = descargarImagen(URL1);

                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(MainActivity.this, "DescargaT finalizada!", Toast.LENGTH_SHORT).show();
                                imageView1.setImageBitmap(imagen);
                            }
                        });
                    }


                }).start();
            }
        });

        //Descargar Imagen con Asyntask
        btnDescargar2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tarea2 = new DownloadI();
                tarea2.execute(URL2);
            }
        });

        //Hilo
        btnHilo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        pbarProgreso1.post(new Runnable() {
                            public void run() {
                                pbarProgreso1.setProgress(0);
                            }
                        });

                        for(int i=1; i<=100; i++) {
                            Working();

                            pbarProgreso1.post(new Runnable() {
                                public void run() {
                                    pbarProgreso1.incrementProgressBy(1);
                                }
                            });
                        }

                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(MainActivity.this, "Thread finalizado!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
            }
        });

        //AsyncTask
        btnAsyncTask.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tarea1 = new Async();
                tarea1.execute();
            }

        });

        //Limpiar
        btnLimpiar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pbarProgreso1.setProgress(0);
                pbarProgreso2.setProgress(0);
                imageView1.setImageBitmap(null);
                imageView2.setImageBitmap(null);
            }
        });
    }

    public Bitmap descargarImagen(String imageHttpAddress) {
<<<<<<< HEAD
        URL imageUrl = null;
=======
        URL imageUrl;
>>>>>>> Initial commit
        Bitmap img = null;
        try {
            imageUrl = new URL(imageHttpAddress);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();
            img = BitmapFactory.decodeStream(conn.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return img;
    }

    private void Working() {
        try {
            Thread.sleep(100);
        } catch(InterruptedException e) {}
    }

    private class Async extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {

            for(int i=1; i<=100; i++) {
                Working();
                publishProgress(i);

                if(isCancelled())
                    break;
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progreso = values[0].intValue();
            pbarProgreso2.setProgress(progreso);
        }

        @Override
        protected void onPreExecute() {
            pbarProgreso2.setMax(100);
            pbarProgreso2.setProgress(0);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result)
                Toast.makeText(MainActivity.this, "Asynctask finalizada!", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(MainActivity.this, "Tarea cancelada!", Toast.LENGTH_SHORT).show();
        }
    }

    private class DownloadI extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            Bitmap imagen = descargarImagen(url);
            return imagen;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            imageView2.setImageBitmap(result);
            Toast.makeText(MainActivity.this, "DescargaA finalizada!", Toast.LENGTH_SHORT).show();
        }

    }

}
