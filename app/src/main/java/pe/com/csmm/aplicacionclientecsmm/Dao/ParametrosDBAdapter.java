package pe.com.csmm.aplicacionclientecsmm.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import pe.com.csmm.aplicacionclientecsmm.Beans.Parametros;

/**
 * Created by RICHARD on 09/10/2016.
 */
public class ParametrosDBAdapter {

    /**
     AlumnoDBAdapter dbAdapter = new AlumnoDBAdapter(getActivity().getBaseContext());
     dbAdapter.open();
     dbAdapter.deleteAllAlumno();
     dbAdapter.close();
     * */

    public static final String DATABASE_NAME = "encuestapaciente.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_PARAMETROS = "parametros";

    public static final String COL_LLAVE = "llave";
    public static final String COL_VALOR = "valor";

    public String[] allColumns = {COL_LLAVE, COL_VALOR};

    public static final String CREATE_TABLE_PARAMETROS = "create table "+ TABLE_PARAMETROS + " ("
            + COL_LLAVE + " text, "
            + COL_VALOR + " text); ";

    private SQLiteDatabase sqlDB;
    private Context context;

    private ParametrosDBHelper parametrosDBHelper;

    public ParametrosDBAdapter(Context ctx){
        context = ctx;
    }

    public ParametrosDBAdapter open() throws android.database.SQLException{

        parametrosDBHelper = new ParametrosDBHelper(context);
        sqlDB = parametrosDBHelper.getWritableDatabase();

        return this;
    }

    public void close(){
        parametrosDBHelper.close();
    }

    public long deleteAllParametros(){
        return sqlDB.delete(TABLE_PARAMETROS, null, null);
    }

    public ArrayList<Parametros> getAllParametros(){

        ArrayList<Parametros> categoriaSubs = new ArrayList<>();

        Cursor cursor = sqlDB.query(TABLE_PARAMETROS, allColumns, null, null, null, null,null,null);

        for(cursor.moveToLast();!cursor.isBeforeFirst();cursor.moveToPrevious()){
            Parametros cateSub = cursorToParametros(cursor);
            categoriaSubs.add(cateSub);
        }

        cursor.close();

        return categoriaSubs;
    }

    public ArrayList<Parametros> getAllParametrosbyLLave(String llave){

        ArrayList<Parametros> categoriaSubs = new ArrayList<>();

        //realizar un query
        try{
            Cursor cursor = sqlDB.query(TABLE_PARAMETROS, allColumns, COL_LLAVE + " = '" + llave+"'", null, null, null,null,null);

            for(cursor.moveToLast();!cursor.isBeforeFirst();cursor.moveToPrevious()){
                Parametros cateSub = cursorToParametros(cursor);
                categoriaSubs.add(cateSub);
            }

            if(categoriaSubs.isEmpty()){
                categoriaSubs.add(new Parametros("",""));
            }

            cursor.close();
        }catch (Exception e){
            categoriaSubs.add(new Parametros("",""));
        }


        return categoriaSubs;
    }

    private Parametros cursorToParametros(Cursor cursor) {

        String llave = "";
        String valor= "";

        try{
            llave = cursor.getString(0);
        }catch(Exception e){
            llave = "";
        }

        try{
            valor = cursor.getString(1);
        }catch(Exception e){
            valor = "";
        }

        Parametros newCateSub = new Parametros(llave,valor);

        /*
        * Alumno(int idLista(0), Long idSchedule(1), String nombres(2), String apePaterno(3),
                  String apeMaterno(4), String adicional(5), String asistencia(6))*/

        return newCateSub;
    }

    public Long insertParametros(Parametros parametros){

        ContentValues values = new ContentValues();
        values.put(COL_LLAVE, parametros.getLlave());
        values.put(COL_VALOR, parametros.getValor());

        ArrayList<Parametros> listaParametros = getAllParametrosbyLLave(parametros.getLlave());

        Long insertId;
        if(listaParametros.get(0).getLlave().equalsIgnoreCase("")){
            insertId = sqlDB.insert(TABLE_PARAMETROS,null,values);
        }else{
            insertId = updateParametros(parametros);
        }

        return insertId;
    }

    public long updateParametros(Parametros parametros){
        ContentValues values = new ContentValues();
        values.put(COL_LLAVE, parametros.getLlave());
        values.put(COL_VALOR, parametros.getValor());

        return sqlDB.update(TABLE_PARAMETROS, values, COL_LLAVE + " = '" + parametros.getLlave()+"'", null);
    }

    private static class ParametrosDBHelper extends SQLiteOpenHelper {

        ParametrosDBHelper(Context ctx){
            super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        }

        // este metodo se ejecuta cuando la base de datos se va ha crear
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_PARAMETROS);
            db.execSQL(RespuestaDBAdapter.CREATE_TABLE_RESPUESTA);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE " + RespuestaDBAdapter.RESPUESTA_TABLE + ";");
            db.execSQL(RespuestaDBAdapter.CREATE_TABLE_RESPUESTA);

            db.execSQL("DROP TABLE " + TABLE_PARAMETROS + ";");
            db.execSQL(CREATE_TABLE_PARAMETROS);
        }
    }
}
