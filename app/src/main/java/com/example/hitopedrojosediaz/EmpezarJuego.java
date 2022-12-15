package com.example.hitopedrojosediaz;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class EmpezarJuego extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empezar_juego);

        Button boton_inicio=(Button) findViewById(R.id.button_start);
        Button boton_puntuacion=(Button) findViewById(R.id.button_scores);
        Button boton_opciones=(Button) findViewById(R.id.button_options);

        //Listeners para cada botón (ir al main, ir a Puntuaciones, ir a Opciones)
        if (boton_inicio != null){
            boton_inicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            });
        }
        if (boton_puntuacion != null) {
            boton_puntuacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), Puntuaciones.class);
                    startActivity(intent);
                }
            });
        }
        if (boton_opciones != null) {
            boton_opciones.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), Opciones.class);
                    startActivity(intent);
                }
            });
        }
    }
}

