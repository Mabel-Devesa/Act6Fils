package com.mdevesa.activitats.fils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static com.mdevesa.activitats.fils.MainActivity.path_imatge;

public class MainActivity extends AppCompatActivity{

    // TODO Indiquem aquí la URL de la imatge que volem descarregar
    public static String img = "https://cdn.pixabay.com/photo/2018/06/14/22/50/nature-3475815_960_720.jpg";

    // Crearem el recurs a la memòria del telèfon, definim la ruta fins el nom del fitx
    public static String path_imatge = Environment.getExternalStorageDirectory().toString() + "/imatge.jpg";

    // Declarem els components de la UI
    private Button btn_descarrega;
    private ProgressBar barra_progres;
    private ImageView image_vista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_descarrega = (Button) findViewById(R.id.button);
        barra_progres = (ProgressBar) findViewById(R.id.progressBar);
        barra_progres.setMax(100);
        image_vista = (ImageView) findViewById(R.id.imageView);

        btn_descarrega.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                // Executem l'AsyncTask passant-li com a argument la ruta de la imatge.
                if (v == btn_descarrega) new TascaDescarrega().execute(img);
            }
        });
    }


    // AsyncTask que descarrega la imatge de la xarxa, rep strings com a paràmetre d'entra
    private  class TascaDescarrega extends AsyncTask<String, Integer, Void> {
        // OnPreExecute s'executa abans que doInBackground
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Eliminem la imatge i posem la barra de progrés a 0
            barra_progres.setProgress(0);
            image_vista.setImageDrawable(null);
        }
        // Tasca que realitza les operacions de xarxa, no es pot manipular l'UI des d'aqui
        @Override
        protected Void doInBackground(String... url) {
            try {
                // Agafem la URL que s'ha passat com argument
                URL imatge = new URL(url[0]);

                // Fem la connexió a la URL i mirem la mida de la imatge

                HttpURLConnection connection = (HttpURLConnection) imatge.openConnection();
                int totalImatge= connection.getContentLength();

                // Creem l'input i un buffer on anirem llegint la informació
                InputStream inputstream = (InputStream) imatge.getContent();
                byte[] bufferImatge = new byte[1024];

                // Creem la sortida, és a dir, un objecte on guardarem la informació (ruta de la imatge)
                OutputStream outputstream = new FileOutputStream(path_imatge);

                int descarregat = 0;
                int count;

                // Mentre hi hagi informació per llegir
                while ((count = inputstream.read(bufferImatge)) != -1) {

                    // Acumulem tot el que s'ha llegit
                    descarregat += count;

                    // Calculem el percentatge respecte el total i l'enviem a publishProgress
                    publishProgress(((descarregat * 100) / totalImatge));

                    // Guardem al disc el que hem descarregat
                    outputstream.write(bufferImatge, 0, count);
                }

                // Tanquem els "stream"
                inputstream.close();
                outputstream.close();

            } catch (IOException exception) {
                Log.d("ERR", "Alguna cosa no ha anat bé!");
                return null;
            }

            // No passem cap informació al onPostExecute
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            // Actualitzem la barra de progrés amb el valor que se'ns ha enviat des de doInBackground
            barra_progres.setProgress(values[0]);
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            // Quan acaba la tasca, inserim la imatge a l'ImageView
            image_vista.setImageDrawable(Drawable.createFromPath(path_imatge));
        }
    }
}
