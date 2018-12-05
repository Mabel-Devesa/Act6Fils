package com.mdevesa.activitats.miembrosdelequipo;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        //Localizar los controles
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        TextView tv_nom = (TextView) findViewById(R.id.tv_nom);
        TextView tv_desc = (TextView) findViewById(R.id.tv_desc);
        TextView tv_aptituds = (TextView) findViewById(R.id.tv_aptituds);

        //Recuperamos la información pasada en el intent
        Bundle bundle = this.getIntent().getExtras();

        //Mostrar la informacion por pantalla
        tv_nom.setText(bundle.getString("NOMBRE"));
        tv_desc.setText("Descripcio: "+bundle.getString("DESC"));
        tv_aptituds.setText("Aptituds: "+bundle.getString("APT"));
        String imatge = bundle.getString("IMAGEN");
        //Convertir el nom de la imatge en un recurs
        int res_imatge = getResources().getIdentifier(imatge,"drawable", getPackageName());
        imageView.setImageResource(res_imatge);
    }
}
