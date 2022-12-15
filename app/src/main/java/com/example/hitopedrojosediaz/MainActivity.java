package com.example.hitopedrojosediaz;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {


    // Declaraciones iniciales relativamente estáticas
    int varDemonioRndom;
    private TextView mVistaTiempo;
    private TextView mVistaPuntuaje;
    public int varPuntuacion = 0;
    private int varVidas = 5;
    final Handler handler = new Handler();
    public boolean varCerrar = false;

    // Esta es la duración de nuestro juego
    private final int maxTiempo = 60 * 1000;
    // Dejar calc redundante que permite establecer segundos usando alguna lógica/preferencias
    private long pasoTiempo =  1000;

    // Este es el delay por cada demonio que aparece (dificultad)
    public int intervaloTiempo = 2000;//cada tres segundos sale uno y desaparece otro
    // Este es el tiempo en el que el golpeable va subiendo hasta durar los 2 segundos de timeInterval
    public int DemonioUpTime = 1000;

    // Cuenta regresiva del juego principal
    public CountDownTimer mTimer = new myTimer(maxTiempo, pasoTiempo);


    public String currentDiff;

    public ImageView demonioClick [] = new ImageView [9];

    public int yValor;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            mVistaTiempo = (TextView) findViewById(R.id.textTimeVal);
            mVistaPuntuaje = (TextView) findViewById(R.id.textScoreVal);

            // Obtenga la dificultad guardada, predeterminada en "Medium" si no existe preferencia
            final SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            currentDiff = sharedPref.getString("saved_difficulty", "Medium");

            // Empieza el juego
            mTimer.start();
            handler.post(moleLoop);

            varCerrar = false;


            demonioClick [0] = (ImageView) findViewById(R.id.imageMole1);
            demonioClick [1] = (ImageView) findViewById(R.id.imageMole2);
            demonioClick [2] = (ImageView) findViewById(R.id.imageMole3);
            demonioClick [3] = (ImageView) findViewById(R.id.imageMole4);
            demonioClick [4] = (ImageView) findViewById(R.id.imageMole5);
            demonioClick [5] = (ImageView) findViewById(R.id.imageMole6);
            demonioClick [6] = (ImageView) findViewById(R.id.imageMole7);
            demonioClick [7] = (ImageView) findViewById(R.id.imageMole8);
            demonioClick [8] = (ImageView) findViewById(R.id.imageMole9);

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);

            // Escala la traducción de moles basada en las dimensiones del dispositivo
            int sHeight = metrics.heightPixels;
            yValor = (sHeight/12)*-1;

        }

        @Override
        public void onPause() {
            super.onPause();

            varCerrar = true;
            mTimer.cancel();


        }

        @Override
        public void onStop() {
            super.onStop();

            varCerrar = true;
            mTimer.cancel();


        }

        @Override
        public void onResume(){
            super.onResume();

            varCerrar = false;

        }

        // Clase temporizador pública que maneja el reloj del juego
        public class myTimer extends CountDownTimer {
            public myTimer(int maxTime, long stepTime) {
                super(maxTime, stepTime);

            }
            @Override

            // Llamado cuando el contador termina
            public void onFinish() {

                // Llamar a la clase FinJuego y al puntaje , motivo (debido al tiempo de espera)
                this.cancel();
                String messageTime = getString(R.string.str_end_time);
                FinJuego(varPuntuacion, messageTime);

                // Restablece variables de dificultad
                intervaloTiempo = 2000;//1000
                DemonioUpTime = 1000;//350

            }

            //Ticker llamado cada "x" milisegundos hasta que termine
            public void onTick(long millisUntilFinished) {

                // Se usa para establecer el valor de tiempo cada segundo o (1000ms)
                mVistaTiempo.setText(String.valueOf(millisUntilFinished / 1000));

                // Aumenta la dificultad cada 15 segundos
                if (((millisUntilFinished/1000)%15 == 0) && (millisUntilFinished/1000) != 60){
                    aumentarDificultad();
                }

            }
        }

        // Funciones para aumentar gradualmente la dificultad
        public void aumentarDificultad(){

            String diff1 = getString(R.string.diff1);
            String diff3 = getString(R.string.diff3);

            // Cuando se llama aumento de dificultad, se disminuye el tiempo entre el golpeable
            // y el tiempo en la superficie que se basará en la dificultad
            if (currentDiff.equals(diff1)){
                intervaloTiempo *= 0.99;
                DemonioUpTime *= 0.95;
            } else if (currentDiff.equals(diff3)) {
                intervaloTiempo *= 1;
                DemonioUpTime *= 1;
            } else {
                intervaloTiempo *= 0.9;
                DemonioUpTime *= 0.9;
            }

        }

        //Se termina el juego.
        public void FinJuego(int EndScore, String Reason) {

            Intent intent = new Intent(getApplicationContext(), FinJuego.class);
            intent.putExtra("score", EndScore);
            intent.putExtra("reason", Reason);

            mTimer.cancel();
            startActivity(intent);
            this.finish();

        }

        // Game loop es un ejecutable que se llama a sí mismo cada intervalo de tiempo (millis)
        public Runnable moleLoop = new Runnable() {

            int varPrevRandMole = 10;

            @Override
            public void run () {

                //Elige un demonio al azar, si obtienes el mismo dos veces, vuelve a tirar hasta que sea diferente
                varDemonioRndom = new Random().nextInt(8);

                if (varDemonioRndom == varPrevRandMole){
                    do
                        varDemonioRndom = new Random().nextInt(8);
                    while (varDemonioRndom == varPrevRandMole);
                }

                varPrevRandMole = varDemonioRndom;

                // Muestra al demonio
                demonioClick[varDemonioRndom].animate().translationY(yValor).setDuration(DemonioUpTime);

                // Temporizador para volver a bajar al demonio si el jugador no lo golpea
                new Timer().schedule(new TimerTask() {
                    public void run() {

                        if (!varCerrar) {

                            for (int i = 0; i < 9; i++) {
                                if (demonioClick[i].getTranslationY() == yValor) {

                                    final int j = i;

                                    // Devuelve el demonio a su posición inicial
                                    //  se ejecuta esta actualización en el subproceso de la interfaz de usuario ya que necesitamos un subproceso "looper"
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            demonioClick[j].animate().translationY(0).setDuration(5);
                                        }
                                    });


                                    // Quita una vida si fallamos o no le damos a tiempo
                                    varVidas -= 1;
                                    actualizarVidas(varVidas);

                                }
                            }
                        }
                    }
                }, intervaloTiempo);

                if (!varCerrar) {
                    handler.postDelayed(moleLoop, intervaloTiempo);
                }
            }
        };

        // Manejo de los indicadores de vida
        public void actualizarVidas(int Lives){

            final ImageView vida1= (ImageView) findViewById(R.id.vida1);
            final ImageView vida2= (ImageView) findViewById(R.id.vida2);
            final ImageView vida3= (ImageView) findViewById(R.id.vida3);
            final ImageView vida4= (ImageView) findViewById(R.id.vida4);
            final ImageView vida5= (ImageView) findViewById(R.id.vida5);

            // Comienza a quitar vidas, cuando no quede ninguna, llama a nuestro método de FinJuego
            if (Lives == 4){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (vida5 != null){
                            vida5.setImageResource(R.drawable.vida2);
                        }
                    }
                });
            } else if (Lives == 3){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (vida4 != null) {
                            vida4.setImageResource(R.drawable.vida2);
                        }
                    }
                });
            } else if (Lives == 2) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (vida3 != null){
                            vida3.setImageResource(R.drawable.vida2);
                        }
                    }
                });
            } else if (Lives == 1){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (vida2 != null) {
                            vida2.setImageResource(R.drawable.vida2);
                        }
                    }
                });
            } else if (Lives == 0){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (vida1 != null) {
                            vida1.setImageResource(R.drawable.vida2);
                        }
                    }
                });
                String messageLives = getString(R.string.str_end_lives);
                if (!varCerrar) {
                    FinJuego(varPuntuacion, messageLives);
                }
            }

        }

        // Actualiza el texto de puntuación
        public void actualizarPuntuacion(int Score){
            mVistaPuntuaje.setText(String.valueOf(Score));
        }

        // OnClick para objetos demonio cuando los golpeamos
        public void onClick(View v) {



            // Switch para buscar el demonio correcto y bajarlo hacia abajo
            switch(v.getId()) {
                case R.id.imageMole1:
                    if (demonioClick[0].getTranslationY() < 0) {
                        demonioClick[0].animate().translationY(0).setDuration(20);
                        golpeDirecto();
                    }
                    break;
                case R.id.imageMole2:
                    if (demonioClick[1].getTranslationY() < 0) {
                        demonioClick[1].animate().translationY(0).setDuration(20);
                        golpeDirecto();
                    }
                    break;
                case R.id.imageMole3:
                    if (demonioClick[2].getTranslationY() < 0) {
                        demonioClick[2].animate().translationY(0).setDuration(20);
                        golpeDirecto();
                    }
                    break;
                case R.id.imageMole4:
                    if (demonioClick[3].getTranslationY() < 0) {
                        demonioClick[3].animate().translationY(0).setDuration(20);
                        golpeDirecto();
                    }
                    break;
                case R.id.imageMole5:
                    if (demonioClick[4].getTranslationY() < 0) {
                        demonioClick[4].animate().translationY(0).setDuration(20);
                        golpeDirecto();
                    }
                    break;
                case R.id.imageMole6:
                    if (demonioClick[5].getTranslationY() < 0) {
                        demonioClick[5].animate().translationY(0).setDuration(20);
                        golpeDirecto();
                    }
                    break;
                case R.id.imageMole7:
                    if (demonioClick[6].getTranslationY() < 0) {
                        demonioClick[6].animate().translationY(0).setDuration(20);
                        golpeDirecto();
                    }
                    break;
                case R.id.imageMole8:
                    if (demonioClick[7].getTranslationY() < 0) {
                        demonioClick[7].animate().translationY(0).setDuration(20);
                        golpeDirecto();
                    }
                    break;
                case R.id.imageMole9:
                    if (demonioClick[8].getTranslationY() < 0) {
                        demonioClick[8].animate().translationY(0).setDuration(20);
                        golpeDirecto();
                    }
                    break;
            }
        }

        //  actualiza la puntuación.
        public void golpeDirecto(){

            // Se otorgar¡n puntos, y se actualiza la puntuacion
            varPuntuacion += 250;
            actualizarPuntuacion(varPuntuacion);
        }
    }


















