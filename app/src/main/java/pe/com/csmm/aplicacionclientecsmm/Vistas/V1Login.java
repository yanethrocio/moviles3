package pe.com.csmm.aplicacionclientecsmm.Vistas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import pe.com.csmm.aplicacionclientecsmm.Beans.Parametros;
import pe.com.csmm.aplicacionclientecsmm.Beans.User;
import pe.com.csmm.aplicacionclientecsmm.Beans.UserRolPersona;
import pe.com.csmm.aplicacionclientecsmm.Dao.ParametrosDBAdapter;
import pe.com.csmm.aplicacionclientecsmm.Notificacion.AtencionNotificationService;
import pe.com.csmm.aplicacionclientecsmm.R;
import pe.com.csmm.aplicacionclientecsmm.Utiles.Constantes;

public class V1Login extends AppCompatActivity {

    ArrayList<UserRolPersona> listUserRolPersonas = new ArrayList<>();
    User oUser = new User();
    EditText usuario_txt;
    EditText password_txt;
    CheckBox guardar_user_cb;
    public static UserRolPersona oUserRolPersona = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v1_login_activity);

        guardar_user_cb = (CheckBox)findViewById(R.id.guardar_user_cb);
        usuario_txt = (EditText)findViewById(R.id.usuario_txt);
        password_txt = (EditText)findViewById(R.id.password_txt);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!AtencionNotificationService.isRunin) {
            // iniciando el servicio de notificaciones en caso de que no este ejecutandose
            startService(new Intent(getApplicationContext(), AtencionNotificationService.class));
        }

        ParametrosDBAdapter dbAdapter = new ParametrosDBAdapter(getApplicationContext());
        dbAdapter.open();
        String estadoSesion = dbAdapter.getAllParametrosbyLLave(Constantes.estado_sesion).get(0).getValor();
        dbAdapter.close();

        if(estadoSesion.equalsIgnoreCase(Constantes.sesion_activa)){
            Intent intent = new Intent(this,V2AtencionMain.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    public void EjecutarBoton(View view){

        switch (view.getId()){
            case R.id.entrar_btn:
                oUser.setNombre(usuario_txt.getText().toString());
                oUser.setPassword(password_txt.getText().toString());

                // comprobando la conectividad
                ConnectivityManager connectivityManage = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManage.getActiveNetworkInfo();

                if(!(activeNetworkInfo != null && activeNetworkInfo.isConnected())){
                    Toast.makeText(this,"Red No Disponible",Toast.LENGTH_SHORT).show();
                }else{
                    new AutenticarUsuarioRol(V1Login.this).execute();
                }

                break;
            case R.id.cancelar_btn:
                usuario_txt.setText("");
                password_txt.setText("");

                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_entrevistado, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        /*
        if(id == R.id.action_sessiones ){
            Intent Intent = new Intent(this,CategoriaSubMain.class);
            startActivity(Intent);
            return  true;
        }
        */

        return super.onOptionsItemSelected(item);
    }

    private void CargarVistadeRol(){

        if(listUserRolPersonas.size()>1){
            Toast.makeText(this,"Hay una inconsistencia de roles",Toast.LENGTH_LONG).show();
            return;
        }else if(listUserRolPersonas.isEmpty()){
            Toast.makeText(this,"Usuario o Password es incorrecto",Toast.LENGTH_LONG).show();
            return;
        }

        // guardando las preferencias
        if(true){
            ParametrosDBAdapter dbAdapter = new ParametrosDBAdapter(getApplicationContext());
            dbAdapter.open();

            UserRolPersona oUserRolPer = listUserRolPersonas.get(0);

            Parametros oParam1 = new Parametros();
            oParam1.setLlave(Constantes.paciente_id);
            oParam1.setValor(oUserRolPer.getPacienteId());
            dbAdapter.insertParametros(oParam1);

            Parametros oParam2 = new Parametros();
            oParam2.setLlave(Constantes.persona_ape_materno);
            oParam2.setValor(oUserRolPer.getPersApeMater());
            dbAdapter.insertParametros(oParam2);

            Parametros oParam3 = new Parametros();
            oParam3.setLlave(Constantes.persona_ape_paterno);
            oParam3.setValor(oUserRolPer.getPersApePater());
            dbAdapter.insertParametros(oParam3);

            Parametros oParam4 = new Parametros();
            oParam4.setLlave(Constantes.persona_nombre);
            oParam4.setValor(oUserRolPer.getPersNombre());
            dbAdapter.insertParametros(oParam4);

            Parametros oParam5 = new Parametros();
            oParam5.setLlave(Constantes.usuario_nombre);
            oParam5.setValor(oUserRolPer.getUserNombre());
            dbAdapter.insertParametros(oParam5);

            Parametros oParam6 = new Parametros();
            oParam6.setLlave(Constantes.persona_id);
            oParam6.setValor(oUserRolPer.getPersId()+"");
            dbAdapter.insertParametros(oParam6);

            Parametros oParam7 = new Parametros();
            oParam7.setLlave(Constantes.usuario_id);
            oParam7.setValor(oUserRolPer.getUserId()+"");
            dbAdapter.insertParametros(oParam7);

            Parametros oParam8 = new Parametros();
            oParam8.setLlave(Constantes.userrol_id);
            oParam8.setValor(oUserRolPer.getUserRolId()+"");
            dbAdapter.insertParametros(oParam8);

            Parametros oParam9 = new Parametros();
            oParam9.setLlave(Constantes.estado_sesion);
            oParam9.setValor(Constantes.sesion_activa);
            dbAdapter.insertParametros(oParam9);

            dbAdapter.close();
        }

        // realizando una evaluacion de la vista a cargar de acuerdo al rol seleccionado
        oUserRolPersona = listUserRolPersonas.get(0);

        switch (oUserRolPersona.getRolNombre().trim()){
            case "Paciente":
                Intent intent = new Intent(this,V2AtencionMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            default:
                Toast.makeText(getApplicationContext(),"Usuario no permitido",Toast.LENGTH_LONG);
        }
    }

    private class AutenticarUsuarioRol extends AsyncTask<Void, Void, UserRolPersona[]> {

        private ProgressDialog progressDialog;
        V1Login contexto;

        public AutenticarUsuarioRol(V1Login v1Login) {
            contexto = v1Login;
            progressDialog = new ProgressDialog(contexto);
            progressDialog.setMessage("Autenticando...");
            progressDialog.setTitle("Procesando");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected UserRolPersona[] doInBackground(Void... params) {
            try {

                final String url = Constantes.urlServer+"/autenticar_usuario_rol";

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

                User oUserSend = oUser;

                ResponseEntity<UserRolPersona[]> responseEntity = restTemplate.postForEntity(url, oUserSend, UserRolPersona[].class);

                return responseEntity.getBody();

            } catch (Exception e) {
                System.out.println("MainActivity"+ e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(UserRolPersona[] listTmpUserRolPersona) {

            listUserRolPersonas = new ArrayList<>();

            if(listTmpUserRolPersona != null){
                for(UserRolPersona item: listTmpUserRolPersona){
                    UserRolPersona oUserRoltmpPersona = new UserRolPersona();
                    oUserRoltmpPersona.setRolId(item.getRolId());
                    oUserRoltmpPersona.setRolNombre(item.getRolNombre());
                    oUserRoltmpPersona.setUserId(item.getUserId());
                    oUserRoltmpPersona.setUserNombre(item.getUserNombre());
                    oUserRoltmpPersona.setPersApeMater(item.getPersApeMater());
                    oUserRoltmpPersona.setPersApePater(item.getPersApePater());
                    oUserRoltmpPersona.setPersNombre(item.getPersNombre());
                    oUserRoltmpPersona.setUserRolId(item.getUserRolId());
                    oUserRoltmpPersona.setPersId(item.getPersId());
                    oUserRoltmpPersona.setPacienteId(item.getPacienteId());

                    listUserRolPersonas.add(oUserRoltmpPersona);
                }
            }

            progressDialog.dismiss();

            CargarVistadeRol();
        }
    }
}
