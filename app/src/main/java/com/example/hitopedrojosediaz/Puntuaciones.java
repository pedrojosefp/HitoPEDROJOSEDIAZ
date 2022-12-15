package com.example.hitopedrojosediaz;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



public class Puntuaciones extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puntuaciones);

        Button mMenu = (Button) findViewById(R.id.button_Menu);

        final BaseDatos db = new BaseDatos(this);
        final List<Jugador> dataList;

        TableLayout mTableLayout = (TableLayout)findViewById(R.id.scoreTable);

        dataList = db.getAll();

        // Clasifica la operación para ordenar a nuestros jugadores por puntuación alta descendente.
        Collections.sort(dataList, new Comparator<Jugador>() {
            @Override
            public int compare(Jugador playerOne, Jugador playerTwo) {
                return playerTwo.getVarPuntuacion() - playerOne.getVarPuntuacion();
            }

        });

        // Hace un bucle en nuestras filas y las agrega a la tabla usando LayoutInflater
        for (int i = 0; i < dataList.size(); i++) {

            View tableRow = LayoutInflater.from(this).inflate(R.layout.puntuaciones_jugador,null,false);

            TextView playerName = (TextView) tableRow.findViewById(R.id.player_name);
            TextView playerScore = (TextView) tableRow.findViewById(R.id.player_score);

            String varName = dataList.get(i).getVarNombre();
            int varScore = dataList.get(i).getVarPuntuacion();

            playerName.setText(varName);
            playerScore.setText(String.valueOf(varScore));

            if (mTableLayout != null){
                mTableLayout.addView(tableRow);
            }

        }

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
