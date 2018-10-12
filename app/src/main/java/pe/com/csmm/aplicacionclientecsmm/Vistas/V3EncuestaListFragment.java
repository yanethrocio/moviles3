package pe.com.csmm.aplicacionclientecsmm.Vistas;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import pe.com.csmm.aplicacionclientecsmm.Beans.AlternativaAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class V3EncuestaListFragment extends ListFragment {

    AlternativaAdapter oAlternativaAdapter;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MostrarListaRespuestas();
    }

    private void MostrarListaRespuestas(){
        // mostrando la lista de preguntas en pantalla
        oAlternativaAdapter = new AlternativaAdapter(getActivity(), V3EncuestaMain.alternativaList);
        setListAdapter(oAlternativaAdapter);

        //retocando la la linea divisora de los elementos
        getListView().setDivider(ContextCompat.getDrawable(getActivity(),android.R.color.white));
        getListView().setDividerHeight(1);

        // registrando para el menu contextual para la edicion de un elemento de la lista
        registerForContextMenu(getListView());
    }

    // metodo para seleccionar un elemento de la lista para ser visualizado
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        //LaunchRespuestaDetailActivity(RespuestaMain.fragment_to_load.EDIT, position);
        //RespuestaMain.oRespuesta = (Respuesta) getListAdapter().getItem(position);
        //Toast.makeText(getActivity(),"Respuesta Seleccionada: "+RespuestaMain.oRespuesta.getTitle(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


}