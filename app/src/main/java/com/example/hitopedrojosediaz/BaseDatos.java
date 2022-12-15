package com.example.hitopedrojosediaz;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;



public class BaseDatos extends SQLiteOpenHelper {

    //Algunas estaticos(statics) para usar variables para la información de la base de datos, lo que facilita la actualización
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "highscores.db";
    private static final String TABLE_NAME = "scoreboard";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_SCORE = "score";

    // Sentencia SQL para crear nuestra tabla principal
    private static final String TABLE_CREATE = "CREATE TABLE " +
            TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY NOT NULL, " +
            COLUMN_NAME + " TEXT NOT NULL, "+
            COLUMN_SCORE + " TEXT NOT NULL);";

    SQLiteDatabase db;

    public BaseDatos(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // onCreate bundle ejecuta nuestra consulta y hace la tabla
        db.execSQL(TABLE_CREATE);

    }

    //Método que usa nuestra clase Jugador para agregar un jugador al momento del envío
    public void addPlayer(Jugador newPlayer) {

        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //Se usa el cursor DB para insertar nuestro nuevo jugador
        String query = "SELECT * FROM " +  TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();

        values.put(COLUMN_ID, count);
        values.put(COLUMN_NAME, newPlayer.getVarNombre());
        values.put(COLUMN_SCORE, newPlayer.getVarPuntuacion());

        //Se inserta el nuevo jugador y se cierra el cursor
        db.insert(TABLE_NAME, null, values);
        db.close();

    }

    //Método para devolver nuestra lista de jugadores para completar el marcador
    public List<Jugador> getAll() {

        // Construye el Arraylist de jugadores
        List<Jugador> dataList = new ArrayList<>();
        db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            do {
                Jugador data = new Jugador();
                data.setVarNombre(cursor.getString(1));
                data.setVarPuntuacion(Integer.parseInt(cursor.getString(2)));
                dataList.add(data);
            }
            while(cursor.moveToNext());
        }
        return dataList;
    }

    // Método para truncar la base de datos.
    public void deleteAll(){

        db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();

    }

    //Stub dejado, en caso de actualizar DB entre versiones(ni idea)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String Upgrade = "DROP TABLE IF EXISTS" + TABLE_NAME;
        db.execSQL(Upgrade);
        this.onCreate(db);

    }
}
