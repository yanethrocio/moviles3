package pe.com.csmm.aplicacionclientecsmm.Vistas;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import pe.com.csmm.aplicacionclientecsmm.Beans.Alternativa;
import pe.com.csmm.aplicacionclientecsmm.Beans.Atencion;
import pe.com.csmm.aplicacionclientecsmm.Beans.Pregunta;
import pe.com.csmm.aplicacionclientecsmm.Dao.RespuestaDBAdapter;
import pe.com.csmm.aplicacionclientecsmm.R;
import pe.com.csmm.aplicacionclientecsmm.Utiles.Constantes;

public class V3EncuestaMain extends AppCompatActivity {

    List<Alternativa> listaAlternativas = new ArrayList<>();
    public static int numeroPregunta = 0;
    private AlertDialog confirmDialogObject;
    public static Context contexto;
    TextView tituloAlternativas;
    public static ArrayList<Pregunta> preguntaList = new ArrayList<>();
    public static ArrayList<Alternativa> alternativaList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v3_encuesta_main_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contexto = V3EncuestaMain.this;

        // danto titulo al cuadro de alternativas
        tituloAlternativas = findViewById(R.id.titulo_pregunta_alternativa_txt);

        buildTerminarEncuestaDialog("Que Decea Terminar y Guardar?");

        new ObtenerPreguntas((V3EncuestaMain) V3EncuestaMain.contexto).execute();
    }

    public void ActualizarAlternativas(){
        Pregunta oPregunta = preguntaList.get(numeroPregunta);

        tituloAlternativas.setText(oPregunta.getPregunta());

        RespuestaDBAdapter dbAdapter = new RespuestaDBAdapter(V3EncuestaMain.contexto);
        dbAdapter.open();
        alternativaList = dbAdapter.BuscarRespuesta(oPregunta.getId());
        dbAdapter.close();

        //actualizar el fragment de alternativas
        Fragment frg = getSupportFragmentManager().findFragmentByTag("EVENTO_LIST_FRAGMENT");
        if (frg != null) {

            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.detach(frg);
            ft.attach(frg);
            ft.commit();

        } else {

            FragmentManager oFragManager = getSupportFragmentManager();
            FragmentTransaction oFragTransaction = oFragManager.beginTransaction();
            V3EncuestaListFragment oEventoTodoListFragment = new V3EncuestaListFragment();
            setTitle("Eventos");

            oFragTransaction.add(R.id.alternativa_main_content_frag, oEventoTodoListFragment,
                    "EVENTO_LIST_FRAGMENT");
            oFragTransaction.commit();
        }
    }

    public void NavegarPregTerminar(View view){

        switch (view.getId()){
            case R.id.btn_anterior:

                if(numeroPregunta == 0){
                    Toast.makeText(getApplication(),"Es la Primera Pregunta",Toast.LENGTH_SHORT).show();
                }else{
                    numeroPregunta --;
                    ActualizarAlternativas();
                }

                break;
            case R.id.btn_siguiente:

                if(numeroPregunta == (preguntaList.size()-1)){
                    //Toast.makeText(getApplication(),"Es la Ultima Pregunta",Toast.LENGTH_SHORT).show();
                    buildTerminarEncuestaDialog("Es la Ultima Pregunta Desea Terminar y Guardar?");
                    confirmDialogObject.show();
                }else{
                    numeroPregunta ++;
                    ActualizarAlternativas();
                }

                break;
            case R.id.btn_terminar:

                confirmDialogObject.show();

                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alternativa_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    private class GuardarRespuestas extends AsyncTask<Void, Void, Boolean> {

        private ProgressDialog progressDialog;
        V3EncuestaMain contexto;

        public GuardarRespuestas(V3EncuestaMain v1Login) {
            contexto = v1Login;
            progressDialog = new ProgressDialog(contexto);
            progressDialog.setMessage("Guardando...");
            //progressDialog.setTitle("Guardando...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {

                final String url = Constantes.urlServer+"/modificar_guardar_lista_respuesta";

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

                if(!listaAlternativas.isEmpty()){
                    Alternativa oAlternativa = listaAlternativas.get(0);
                    oAlternativa.setAdicional(V2AtencionMain.oAtencion.getId()+"");
                    listaAlternativas.add(0, oAlternativa);
                }

                ResponseEntity<Boolean> responseEntity = restTemplate.postForEntity(url, listaAlternativas
                        , Boolean.class);

                return responseEntity.getBody();

            } catch (Exception e) {
                progressDialog.dismiss();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean seGuardo) {

            progressDialog.dismiss();

            if(seGuardo){
                Toast.makeText(getApplication(),"Se Han Guardado las Respuestas",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplication(),V2AtencionMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }else{
                Toast.makeText(getApplication(),"Ha Fallado La Operacion",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void buildTerminarEncuestaDialog(String titulo){
        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(this);

        confirmBuilder.setTitle(titulo);

        confirmBuilder.setPositiveButton("Confirmar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        RespuestaDBAdapter dbAdapter = new RespuestaDBAdapter(getApplication());
                        dbAdapter.open();
                        listaAlternativas = dbAdapter.getAllRespuestas();
                        dbAdapter.close();

                        new GuardarRespuestas(V3EncuestaMain.this).execute();
                    }
                });
        //When you click Cancel, Leaves PopUp.
        confirmBuilder.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        confirmDialogObject = confirmBuilder.create();
    }

    private class ObtenerPreguntas extends AsyncTask<Void, Void, Pregunta[]> {

        private android.app.ProgressDialog ProgressDialog;
        V3EncuestaMain contexto;

        public ObtenerPreguntas(V3EncuestaMain login) {
            contexto = login;
            ProgressDialog = new ProgressDialog(contexto);
            ProgressDialog.setMessage("Obteniendo Preguntas ...");
            //ProgressDialog.setTitle("");
            ProgressDialog.setCancelable(false);
            ProgressDialog.show();
        }

        @Override
        protected Pregunta[] doInBackground(Void... params) {
            try {

                final String url = Constantes.urlServer+"/lista_preguntas_paciente";

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

                // creando el objeto para enviarlo
                Atencion oAtencion = V2AtencionMain.oAtencion;

                ResponseEntity<Pregunta[]> responseEntity = restTemplate.postForEntity(url, oAtencion, Pregunta[].class);

                return responseEntity.getBody();

            } catch (Exception e) {
                return new Pregunta[0];
            }

            //return new Pregunta[0];
        }

        @Override
        protected void onPostExecute(Pregunta[] listaPreguntas) {

            preguntaList = new ArrayList<>();

            if(listaPreguntas != null){
                for(Pregunta item: listaPreguntas){
                    //Toast.makeText(getApplicationContext(),item.getTitulo(),Toast.LENGTH_SHORT).show();
                    Pregunta oPregunta = new Pregunta();
                    oPregunta.setDescripcion(item.getDescripcion());
                    oPregunta.setId(item.getId());
                    oPregunta.setPregunta(item.getPregunta());

                    preguntaList.add(oPregunta);
                }
            }else{
                Toast.makeText(getApplication(),"No hay Preguntas"
                        , Toast.LENGTH_LONG).show();
            }

            ProgressDialog.dismiss();

            new ObtenerAlternativas((V3EncuestaMain) V3EncuestaMain.contexto).execute();
        }
    }


    private class ObtenerAlternativas extends AsyncTask<Void, Void, Alternativa[]> {

        private android.app.ProgressDialog ProgressDialog;
        V3EncuestaMain contexto;

        public ObtenerAlternativas(V3EncuestaMain context) {
            contexto = context;
            ProgressDialog = new ProgressDialog(contexto);
            ProgressDialog.setMessage("Obteniendo Preguntas ...");
            //ProgressDialog.setTitle("");
            ProgressDialog.setCancelable(false);
            ProgressDialog.show();
        }

        @Override
        protected Alternativa[] doInBackground(Void... params) {
            try {

                final String url = Constantes.urlServer+"/listar_respuestas_de_lista_preguntas";

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

                ResponseEntity<Alternativa[]> responseEntity = restTemplate.postForEntity(url, preguntaList, Alternativa[].class);

                return responseEntity.getBody();

            } catch (Exception e) {
                ProgressDialog.dismiss();
                return new Alternativa[0];
            }

            //return new Respuesta[0];
        }

        @Override
        protected void onPostExecute(Alternativa[] listaAlternativas) {

            if(listaAlternativas == null)
                return;

            // insertando en la base de datos
            RespuestaDBAdapter dbAdapter = new RespuestaDBAdapter(getApplication());
            dbAdapter.open();

            dbAdapter.deleteAllRespuesta();

            String esMarcad = "0";
            for(Alternativa item: listaAlternativas){

                esMarcad = "0";
                if(item.isEsMarcado()){
                    esMarcad = "1";
                }

                dbAdapter.saveRespuesta(item.getId(), item.getIdPregunta(), item.getRespuesta()
                        , esMarcad, item.getTipoResp(), item.getOtros());
            }

            dbAdapter.close();

            ProgressDialog.dismiss();

            ActualizarAlternativas();
        }
    }
}
