/*
*Archivo modificado por Maycol
 */
package pe.com.csmm.aplicacionclientecsmm.Notificacion;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import pe.com.csmm.aplicacionclientecsmm.Beans.Atencion;
import pe.com.csmm.aplicacionclientecsmm.Beans.UserRolPersona;
import pe.com.csmm.aplicacionclientecsmm.Dao.ParametrosDBAdapter;
import pe.com.csmm.aplicacionclientecsmm.Utiles.Constantes;
import pe.com.csmm.aplicacionclientecsmm.Vistas.V2AtencionMain;
import pe.com.csmm.aplicacionclientecsmm.R;
/**
 * Created by RICHARD on 01/03/2017.
 */
public class AtencionNotificationService extends Service {

    public static boolean isRunin = false;
    public static ArrayList<Atencion> AtencionesList;

    @Override
    public void onCreate() {

        System.out.println("onCreate ok...");
        super.onCreate();
    }

    @Override
    public boolean stopService(Intent name) {

        System.out.println("stopService ok...");
        return super.stopService(name);
    }

    @Override
    protected void finalize() throws Throwable {

        System.out.println("finalize ok...");
        super.finalize();
        isRunin = false;
    }

    @Override
    public IBinder onBind(Intent arg0) {

        System.out.println("onBind ok...");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        System.out.println("onStartCommand ok...");
        isRunin = true;

        registerReceiver(new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent) {
                //This happens when the screen is switched off
                System.out.println("onReceive Intent.ACTION_SCREEN_OFF ok...");
            }
        }, new IntentFilter(Intent.ACTION_SCREEN_OFF));

        registerReceiver(new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent) {
                //This happens when the screen is turned on and screen lock deactivated
                //Toast.makeText(getApplication(),"notificacion",Toast.LENGTH_LONG).show();

                // obteniendo los datos de autenticacion
                // obteniendo los credenciales desde los datos guardados de la app
                ParametrosDBAdapter dbAdapter = new ParametrosDBAdapter(getApplicationContext());
                dbAdapter.open();
                String estadoSesion = dbAdapter.getAllParametrosbyLLave(Constantes.estado_sesion).get(0).getValor();
                dbAdapter.close();

                if(estadoSesion.equalsIgnoreCase(Constantes.sesion_activa)){

                    // comprobando la conectividad a internet
                    ConnectivityManager connectivityManage = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetworkInfo = connectivityManage.getActiveNetworkInfo();

                    if(!(activeNetworkInfo != null && activeNetworkInfo.isConnected())){
                        //Toast.makeText(this,"Red No Disponible",Toast.LENGTH_SHORT).show();
                    }else{
                        // obteniendo los datos del webservice para mostrar la lista de preguntas
                        new ObtenerAtenciones().execute();
                    }
                }

                System.out.println("onReceive Intent.ACTION_USER_PRESENT ok...");
            }
        }, new IntentFilter(Intent.ACTION_USER_PRESENT));

        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        System.out.println("onDestroy ok...");
        super.onDestroy();
    }


    private void InvoxStyleNotification() {

        System.out.println("InvoxStyleNotification ok...");

        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        style.setBigContentTitle("Clinica Santa Maria Magdalena");

        for(Atencion itemAtenci : AtencionesList){
            style.addLine(itemAtenci.getDescripcion());
        }

        ParametrosDBAdapter dbAdapter = new ParametrosDBAdapter(getApplicationContext());
        dbAdapter.open();
        String nombre_per = dbAdapter.getAllParametrosbyLLave(Constantes.persona_nombre).get(0).getValor();
        dbAdapter.close();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Clinica Santa Maria Magdalena");
        builder.setContentText("Hola " + nombre_per + ", ¿como te fue con nosotros?");
        builder.setSmallIcon(R.drawable.logo_clinica_notification_fw);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo_clinica_fw);
        builder.setLargeIcon(largeIcon);

        builder.setTicker("Hola " + nombre_per);
        builder.setAutoCancel(true);
        builder.setStyle(style);

        // determinando el activity a donde sera dirigido cuando haga click en la notificacion
        Intent intent = new Intent(this, V2AtencionMain.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        builder.setContentIntent(pIntent);
        builder.setVibrate(new long[]{1000,1000});

        Notification notificacion = builder.build();
        NotificationManager manager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        manager.notify(1236, notificacion);
    }

    private class ObtenerAtenciones extends AsyncTask<Void, Void, Atencion[]> {
        @Override
        protected Atencion[] doInBackground(Void... params) {
            try {

                final String url = Constantes.urlServer+"/lista_atenciones_by_iduserrol";

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

                ParametrosDBAdapter dbAdapter = new ParametrosDBAdapter(getApplicationContext());
                dbAdapter.open();
                Long idUserRol = Long.parseLong(dbAdapter.getAllParametrosbyLLave(Constantes.userrol_id).get(0).getValor());
                UserRolPersona userRolPersona = new UserRolPersona();
                userRolPersona.setUserRolId(idUserRol);
                dbAdapter.close();

                ResponseEntity<Atencion[]> responseEntity = restTemplate.postForEntity(url, userRolPersona, Atencion[].class);

                return responseEntity.getBody();

            } catch (Exception e) {
                return new Atencion[0];
            }
        }

        @Override
        protected void onPostExecute(Atencion[] listaAtenciones) {

            AtencionesList = new ArrayList<>();

            if(listaAtenciones != null){
                for(Atencion item: listaAtenciones){
                    //Toast.makeText(getApplicationContext(),item.getTitulo(),Toast.LENGTH_SHORT).show();
                    Atencion oAtencion = new Atencion();
                    oAtencion.setDescripcion(item.getDescripcion());
                    oAtencion.setId(item.getId());
                    oAtencion.setFecha(item.getFecha());
                    oAtencion.setEntrevistado_id(item.getEntrevistado_id());
                    oAtencion.setPersona_id(item.getPersona_id());

                    AtencionesList.add(oAtencion);
                }
            }else{
                //Toast.makeText(getApplicationContext(), "No hay Contenido", Toast.LENGTH_LONG).show();
            }

            if(!AtencionesList.isEmpty()){
                InvoxStyleNotification();
            }
        }
    }

}
