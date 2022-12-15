package com.example.hitopedrojosediaz;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class FinJuego extends AppCompatActivity {

    private String defaultName = String.valueOf(R.string.default_name);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fin_juego);

        TextView compPuntuacion = (TextView) findViewById(R.id.mScoreValue);
        TextView compReason = (TextView) findViewById(R.id.mReasonDisplay);

        Button mEnviar = (Button) findViewById(R.id.button_Submit);
        Button mMenu = (Button) findViewById(R.id.button_Menu);

        final EditText nombreCaja = (EditText)findViewById(R.id.mNameEntry);
        final Jugador nuevoJugador = new Jugador();

        // Se obtiene los extras que envió nuestro INTENT
        Intent intent = getIntent();
        final int ScoreValue = intent.getExtras().getInt("score");
        String ReasonValue = intent.getExtras().getString("reason");

        final BaseDatos db = new BaseDatos(this);

        // Rellena el nombre y la puntuación de nuestro nuevo objeto Jugador
        nuevoJugador.setVarNombre(defaultName);
        nuevoJugador.setVarPuntuacion(ScoreValue);

        if (compPuntuacion != null){
            compPuntuacion.setText(String.valueOf(ScoreValue));
        }

        if (compReason != null){
            compReason.setText(ReasonValue);
        }

        // El botón enviar que guarda la puntuación del jugador
        if (mEnviar != null){
            mEnviar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Comprueba si el texta esta vacio o no, aunque no debería estar vacio
                    if (nombreCaja != null) {
                        if ((nombreCaja.getText().toString()).equals("")) {
                            Toast.makeText(FinJuego.this, R.string.str_empty_name, Toast.LENGTH_SHORT).show();
                        } else {

                            //Al enviar, nos agrega el jugador a la base de datos y nos lleva al marcador
                            String playerName = nombreCaja.getText().toString();

                            nuevoJugador.setVarNombre(playerName);
                            nuevoJugador.setVarPuntuacion(ScoreValue);

                            db.addPlayer(nuevoJugador);

                            Intent intent = new Intent(getApplicationContext(), Puntuaciones.class);
                            startActivity(intent);
                        }
                    }
                }
            });
        }

        // El botón de menú que nos devuelve al menú principal
        if (mMenu != null){
            mMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), EmpezarJuego.class);
                    startActivity(intent);
                }
            });
        }
    }
}

