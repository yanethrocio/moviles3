package pe.com.csmm.aplicacionclientecsmm.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import pe.com.csmm.aplicacionclientecsmm.Beans.Alternativa;

/**
 * Created by RICHARD on 09/10/2016.
 */
public class RespuestaDBAdapter {

    public static final String DATABASE_NAME = ParametrosDBAdapter.DATABASE_NAME;
    public static final int DATABASE_VERSION = ParametrosDBAdapter.DATABASE_VERSION;

    public static final String RESPUESTA_TABLE = "alternativa";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_IDPREGUNTA = "idPregunta";
    public static final String COLUMN_RESPUESTA = "respuesta";
    public static final String COLUMN_ESMARCADO = "esmarcado";
    public static final String COLUMN_TIPORESPUESTA = "tipoRespuesta";
    public static final String COLUMN_OTROS = "otros";

    public String[] allColumns = {COLUMN_ID, COLUMN_IDPREGUNTA, COLUMN_RESPUESTA, COLUMN_ESMARCADO, COLUMN_TIPORESPUESTA, COLUMN_OTROS};

    public static final String CREATE_TABLE_RESPUESTA = "create table "+ RESPUESTA_TABLE + " ("
            + COLUMN_ID + " integer primary key, "
            + COLUMN_IDPREGUNTA + " integer, "
            + COLUMN_RESPUESTA + " text not null, "
            + COLUMN_ESMARCADO + " text not null, "
            + COLUMN_TIPORESPUESTA + " text not null, "
            + COLUMN_OTROS + " text); ";

    private SQLiteDatabase sqlDB;
    private Context context;

    private AlternativaDBHelper noteBookDBHelper;

    public RespuestaDBAdapter(Context ctx){
        context = ctx;
    }

    public RespuestaDBAdapter open() throws android.database.SQLException{

        noteBookDBHelper = new AlternativaDBHelper(context);
        sqlDB = noteBookDBHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        noteBookDBHelper.close();
    }

    public Alternativa saveRespuesta(Long id, Long idPregunta, String respuesta, String esMarcado , String tipoRespuesta, String otros){

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, id);
        values.put(COLUMN_IDPREGUNTA, idPregunta);
        values.put(COLUMN_RESPUESTA, respuesta);
        values.put(COLUMN_ESMARCADO, esMarcado);
        values.put(COLUMN_TIPORESPUESTA, tipoRespuesta);
        values.put(COLUMN_OTROS, otros);

        Long insertId = sqlDB.insert(RESPUESTA_TABLE,null,values);

        Cursor cursor = sqlDB.query(RESPUESTA_TABLE,allColumns,COLUMN_ID +" = "+id
                ,null,null,null,null);

        cursor.moveToFirst();
        Alternativa newAlternativa = cursorToNote(cursor);
        cursor.close();

        return newAlternativa;
    }

    /*
    public Alternativa createAlternativa(){

        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, Calendar.getInstance().getTimeInMillis()+"");
        values.put(COLUMN_RESPUESTA,AlternativaMain.oAlternativa.getTitle());
        values.put(COLUMN_MESSAGE, AlternativaMain.oAlternativa.getMessage());
        values.put(COLUMN_ESMARCADO, AlternativaMain.oAlternativa.getMedia());
        values.put(COLUMN_PREGUNTA, AlternativaMain.oAlternativa.getPregId());

        Long insertId = sqlDB.insert(RESPUESTA_TABLE,null,values);

        Cursor cursor = sqlDB.query(RESPUESTA_TABLE,allColumns,COLUMN_ID +" = "+insertId
                ,null,null,null,null);

        cursor.moveToFirst();
        Alternativa newPregunta = cursorToNote(cursor);
        cursor.close();

        return newPregunta;
    }
    */

    // actualizando la pregunta
    /*
    public long updateAlternativa(long idToUpdate, String title, String message, String rutaMedia){
        ContentValues values = new ContentValues();
        values.put(COLUMN_RESPUESTA,title);
        values.put(COLUMN_DESCRIPCION, message);
        values.put(COLUMN_ESMARCADO, rutaMedia);

        return sqlDB.update(RESPUESTA_TABLE,values,COLUMN_ID + " = " + idToUpdate,null);
    }
    */

    public long updateRespuesta(Long idRespuesta, String esMarcado){
        ContentValues values = new ContentValues();
        //values.put(COLUMN_RESPUESTA, AlternativaMain.oAlternativa.getTitle());
        values.put(COLUMN_ESMARCADO, esMarcado);

        return sqlDB.update(RESPUESTA_TABLE,values,COLUMN_ID + " = " + idRespuesta,null);
    }

    public void deleteAllRespuesta(){
        //return sqlDB.delete(RESPUESTA_TABLE, COLUMN_ID + " = " + idToDelete, null);
        sqlDB.delete(RESPUESTA_TABLE, null, null);
    }

    public ArrayList<Alternativa> getAllRespuestas(){
        ArrayList<Alternativa> alternativas = new ArrayList<>();

        Cursor cursor = sqlDB.query(RESPUESTA_TABLE,allColumns,null,null,null,null,null);

        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            Alternativa alternativa = cursorToNote(cursor);
            alternativas.add(alternativa);
        }

        cursor.close();

        return alternativas;
    }

    public ArrayList<Alternativa> BuscarRespuesta(Long idPregunta){
        ArrayList<Alternativa> preguntas = new ArrayList<>();

        Cursor cursor = sqlDB.query(RESPUESTA_TABLE,allColumns, COLUMN_IDPREGUNTA +" = "+idPregunta
                ,null,null,null,null);

        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            Alternativa oAlternativa = cursorToNote(cursor);
            preguntas.add(oAlternativa);
        }

        cursor.close();

        return preguntas;
    }

    private Alternativa cursorToNote(Cursor cursor) {
        //COLUMN_ID, COLUMN_RESPUESTA, COLUMN_ESMARCADO, COLUMN_TIPORESPUESTA, COLUMN_OTROS

        //Long id, Long idPregunta, String respuesta, String esMarcado , String tipoRespuesta, String otros

        Alternativa newAlternativa = new Alternativa(cursor.getLong(0),cursor.getLong(1),cursor.getString(2),cursor.getString(3),
                cursor.getString(4),cursor.getString(5));

        return newAlternativa;
    }

    private static class AlternativaDBHelper extends SQLiteOpenHelper {

        AlternativaDBHelper(Context ctx){
            super(ctx,DATABASE_NAME,null,DATABASE_VERSION);
        }

        // este metodo se ejecuta cuando la base de datos se va ha crear
        @Override
        public void onCreate(SQLiteDatabase db) {

            //db.execSQL(CREATE_TABLE_RESPUESTA);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            /*
            db.execSQL("DROP TABLE " + RESPUESTA_TABLE + ";");
            db.execSQL(CREATE_TABLE_RESPUESTA);
            */
        }
    }
}
