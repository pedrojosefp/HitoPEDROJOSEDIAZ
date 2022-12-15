package com.example.hitopedrojosediaz;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


public class Opciones extends AppCompatActivity{

    private Spinner mSpinner;
    private BaseDatos db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opciones);

        mSpinner = (Spinner) findViewById(R.id.diff_spinner);

        db = new BaseDatos(this);
        Button bGuardar = (Button) findViewById(R.id.btn_save);
        Button bLimpiar = (Button) findViewById(R.id.button_clear);
        Button bMain = (Button) findViewById(R.id.btn_main);

        final SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();

        // Carga la configuración de dificultad actual, si la hay, por defecto es Media
        String currentDiff = sharedPref.getString("saved_difficulty", "Medium");

        // ArrayAdapter maneja nuestra variedad de dificultades (Fácil, Medio, Difícil)
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.difficulties, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if (mSpinner != null) {
            mSpinner.setAdapter(adapter);
            mSpinner.setSelection(adapter.getPosition(currentDiff));
        } else {
            Log.i("OptionsActivity", "mSpinner failed - null pointer");
        }

        // Botón para borrar base de datos, te muestra alerta y te pregunta sí o no
        if (bLimpiar != null) {
            bLimpiar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    db.deleteAll();
                                    Toast.makeText(Opciones.this, R.string.str_clearscore, Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage(R.string.str_clear_scores_alert).setPositiveButton(R.string.str_yes, dialogClickListener)
                            .setNegativeButton(R.string.str_no, dialogClickListener).show();
                }
            });
        }

        // El botón  que guarda los cambios(dificultad, etc)
        if (bGuardar != null) {
            bGuardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String diffString = mSpinner.getSelectedItem().toString();

                    //Almacene la cadena de dificultad en las preferencias, muestre el toast exitoso
                    editor.putString("saved_difficulty", diffString);
                    editor.apply();
                    Toast.makeText(Opciones.this, R.string.str_savedmsg, Toast.LENGTH_SHORT).show();

                }
            });
        }

        // Botón del menú principal
        if (bMain != null) {
            bMain.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick (View v){

                    Intent intent = new Intent(getApplicationContext(), EmpezarJuego.class);
                    startActivity(intent);

                }
            });
        }

    }
}

