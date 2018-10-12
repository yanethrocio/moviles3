package pe.com.csmm.aplicacionclientecsmm.Vistas;


import android.app.AlertDialog;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import pe.com.csmm.aplicacionclientecsmm.Beans.Atencion;
import pe.com.csmm.aplicacionclientecsmm.Beans.AtencionAdapter;
import pe.com.csmm.aplicacionclientecsmm.Beans.UserRolPersona;
import pe.com.csmm.aplicacionclientecsmm.Dao.ParametrosDBAdapter;
import pe.com.csmm.aplicacionclientecsmm.Utiles.Constantes;

/**
 * A simple {@link Fragment} subclass.
 */
public class V2AtencionListFragment extends ListFragment {

    public static ArrayList<Atencion> listAtenciones;
    AtencionAdapter oPreguntaAdapter;
    private AlertDialog confirmDialogObject;
    List<Atencion> listaAtenEliminar = new ArrayList<>();
    int OpcionSelect;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // obteniendo los datos del webservice para mostrar la lista de preguntas
        new ObtenerAtenciones((V2AtencionMain) V2AtencionMain.contexto).execute();

        // alert para confirmar los cambios de la categoria
        buildConfirmDialog();
    }

    private void MostrarListaAtenciones(){
        // mostrando la lista de preguntas en pantalla
        oPreguntaAdapter = new AtencionAdapter(getActivity(), listAtenciones);
        setListAdapter(oPreguntaAdapter);

        //retocando la la linea divisora de los elementos
        getListView().setDivider(ContextCompat.getDrawable(getActivity(),android.R.color.black));
        getListView().setDividerHeight(1);

        // registrando para el menu contextual para la edicion de un elemento de la lista
        registerForContextMenu(getListView());
    }
    
    // metodo para seleccionar un elemento de la lista para ser visualizado
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        confirmDialogObject.show();
        V2AtencionMain.oAtencion = (Atencion) getListAdapter().getItem(position);
    }

    private void buildConfirmDialog(){
        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(getActivity());

        final String[] listaOpciones = {"Responder Encuesta", "Eliminar Encuesta"};

        confirmBuilder.setTitle("Que Desea Realizar?");
        confirmBuilder.setSingleChoiceItems(listaOpciones, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // The 'which' argument contains the index position
                // of the selected item
                OpcionSelect = which;
            }
        });

        confirmBuilder.setPositiveButton("Confirmar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if(listaOpciones[OpcionSelect] == "Responder Encuesta")
                        {
                            // procedemos a realizar la eliminacion de las atenciones que el
                            // usuario selecciono para eliminar
                            if(!listaAtenEliminar.isEmpty()){
                                new EliminarAtenciones((V2AtencionMain) V2AtencionMain.contexto).execute();
                            }

                            //Toast.makeText(getActivity(),"Responder Encuesta", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(),V3EncuestaMain.class);
                            startActivity(intent);
                        }
                        else if(listaOpciones[OpcionSelect] == "Eliminar Encuesta")
                        {
                            //Toast.makeText(getActivity(),"Eliminar Encuesta", Toast.LENGTH_SHORT).show();
                            listAtenciones.remove(V2AtencionMain.oAtencion);
                            listaAtenEliminar.add(V2AtencionMain.oAtencion);

                            MostrarListaAtenciones();

                            if(listAtenciones.isEmpty()){
                                new EliminarAtenciones((V2AtencionMain) V2AtencionMain.contexto).execute();
                            }
                        }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private class ObtenerAtenciones extends AsyncTask<Void, Void, Atencion[]> {

        private ProgressDialog progressDialog;
        V2AtencionMain contexto;

        public ObtenerAtenciones(V2AtencionMain login) {
            contexto = login;
            progressDialog = new ProgressDialog(contexto);
            progressDialog.setMessage("Procesando");
            progressDialog.setTitle("Obteniendo Atenciones ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Atencion[] doInBackground(Void... params) {
            try {

                final String url = Constantes.urlServer+"/lista_atenciones_by_iduserrol";

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

                // creando el objeto para enviarlo
                ParametrosDBAdapter dbAdapter = new ParametrosDBAdapter(getActivity());
                dbAdapter.open();
                Long idUserRol = Long.parseLong(dbAdapter.getAllParametrosbyLLave(Constantes.userrol_id).get(0).getValor());
                UserRolPersona userRolPersona = new UserRolPersona();
                userRolPersona.setUserRolId(idUserRol);
                dbAdapter.close();

                ResponseEntity<Atencion[]> responseEntity = restTemplate.postForEntity(url, userRolPersona, Atencion[].class);

                return responseEntity.getBody();

            } catch (Exception e) {
                progressDialog.dismiss();
                return new Atencion[0];
            }
        }

        @Override
        protected void onPostExecute(Atencion[] listaAtenciones) {

            listAtenciones = new ArrayList<>();

            if(listaAtenciones != null){
                for(Atencion item: listaAtenciones){
                    //Toast.makeText(getApplicationContext(),item.getTitulo(),Toast.LENGTH_SHORT).show();
                    Atencion oCateSub = new Atencion();
                    oCateSub.setDescripcion(item.getDescripcion());
                    oCateSub.setId(item.getId());
                    oCateSub.setFecha(item.getFecha());
                    oCateSub.setEntrevistado_id(item.getEntrevistado_id());
                    oCateSub.setPersona_id(item.getPersona_id());

                    listAtenciones.add(oCateSub);
                }
            }else{
                Toast.makeText(getActivity().getApplicationContext(),"No hay atenciones"
                        , Toast.LENGTH_LONG).show();
            }

            progressDialog.dismiss();

            MostrarListaAtenciones();
        }
    }

    private class EliminarAtenciones extends AsyncTask<Void, Void, Boolean> {

        private ProgressDialog progressDialog;
        V2AtencionMain contexto;

        public EliminarAtenciones(V2AtencionMain login) {
            contexto = login;
            progressDialog = new ProgressDialog(contexto);
            progressDialog.setMessage("Procesando");
            progressDialog.setTitle("Obteniendo Atenciones ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {

                final String url = Constantes.urlServer+"/eliminar_atenciones_paciente";

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

                ResponseEntity<Boolean> responseEntity = restTemplate.postForEntity(url, listaAtenEliminar, Boolean.class);

                return responseEntity.getBody();

            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean fueEliminado) {

            progressDialog.dismiss();

            if(fueEliminado){
                Toast.makeText(getActivity().getApplicationContext(),"Las Atenciones Fueron Eliminadas"
                        , Toast.LENGTH_LONG).show();

                listaAtenEliminar = new ArrayList<>();
            }else{
                Toast.makeText(getActivity().getApplicationContext(),"Fallo La Eliminacion"
                        , Toast.LENGTH_LONG).show();
            }
        }
    }
}
