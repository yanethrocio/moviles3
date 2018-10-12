package pe.com.csmm.aplicacionclientecsmm.Vistas;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import pe.com.csmm.aplicacionclientecsmm.Beans.Atencion;
import pe.com.csmm.aplicacionclientecsmm.Dao.ParametrosDBAdapter;
import pe.com.csmm.aplicacionclientecsmm.Notificacion.AtencionNotificationService;
import pe.com.csmm.aplicacionclientecsmm.R;
import pe.com.csmm.aplicacionclientecsmm.Utiles.Constantes;

public class V2AtencionMain extends AppCompatActivity {

    public static Atencion oAtencion = null;
    public static Context contexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v2_atencion_main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contexto = V2AtencionMain.this;
    }

    @Override
    public void onBackPressed() {
        if (true) {
            //doSomething();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!AtencionNotificationService.isRunin) {
            // iniciando el servicio de notificaciones en caso de que no este ejecutandose
            startService(new Intent(this, AtencionNotificationService.class));
        }

        // obteniendo los credenciales desde los datos guardados de la app
        ParametrosDBAdapter dbAdapter = new ParametrosDBAdapter(getApplicationContext());
        dbAdapter.open();
        String estadoSesion = dbAdapter.getAllParametrosbyLLave(Constantes.estado_sesion).get(0).getValor();
        dbAdapter.close();

        if(estadoSesion.equalsIgnoreCase(Constantes.sesion_cerrada)){
            Intent Intent = new Intent(getApplicationContext(),V1Login.class);
            startActivity(Intent);
        }

        // utilizado para iniciar las preguntas desde el inicio
        V3EncuestaMain.numeroPregunta = 0;
    }
}
