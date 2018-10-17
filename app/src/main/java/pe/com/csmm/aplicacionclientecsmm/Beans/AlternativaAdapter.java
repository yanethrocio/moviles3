/*
*archivo modificado por Leonel
 **/

package pe.com.csmm.aplicacionclientecsmm.Beans;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pe.com.csmm.aplicacionclientecsmm.Dao.RespuestaDBAdapter;
import pe.com.csmm.aplicacionclientecsmm.R;

/**
 * Created by RICHARD on 25/08/2016.
 */
public class AlternativaAdapter extends ArrayAdapter<Alternativa> {

    public static Map<ImageView,Alternativa> listaRespuestas = new HashMap<>();
    public static boolean estaLlenando = true;

    public AlternativaAdapter(Context context, ArrayList<Alternativa> alternativas){
        super(context,0, alternativas);
        this.listaRespuestas = new HashMap<>();
        estaLlenando = true;

        // inicializando el array de las alternativas para el check o uncheck
        for(Alternativa item: alternativas){
            /*
            if(PreguntaMain.oPregunta.getRespuesta().indexOf(item.getTitle()) > 0){
                this.selectNoSelect.put(item.getTitle(),true);
            }else {
                this.selectNoSelect.put(item.getTitle(),false);
            }
            */
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Alternativa pregunta = getItem(position);
        final String audio = pregunta.getAudio();

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.alternativa_list_row,parent,false);
        }

        TextView oNoteTitle = (TextView) convertView.findViewById(R.id.List_item_note_title);
        TextView oNoteBody = (TextView) convertView.findViewById(R.id.List_item_note_body);
        ImageView oNoteImage = (ImageView) convertView.findViewById(R.id.noteIcon);

        oNoteBody.setText(pregunta.getOtros());
        oNoteTitle.setText(pregunta.getRespuesta());

        if(pregunta.isEsMarcado()){
            oNoteImage.setImageResource(R.drawable.alternativa_check);
        }else{
            oNoteImage.setImageResource(R.drawable.alternativa_uncheck);
        }

        if(estaLlenando){
            this.listaRespuestas.put(oNoteImage, pregunta);
        }


        oNoteImage.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Alternativa oAlternativa = AlternativaAdapter.listaRespuestas.get(view);
                        //Toast.makeText(getContext(), oRespuesta.getRespuesta(), Toast.LENGTH_LONG).show();
                        estaLlenando = false;

                        ImageView oNoteImage = (ImageView) view;
                        oNoteImage.setImageResource(R.drawable.alternativa_check);

                        // el tipo de respuesta es 1 - unico o 2 - multiple
                        if(oAlternativa.getTipoResp().equalsIgnoreCase("1")){

                            for (Map.Entry<ImageView, Alternativa> entry : AlternativaAdapter.listaRespuestas.entrySet()) {
                                ImageView oNoteTmpImage = entry.getKey();
                                Alternativa oRespuTmp = entry.getValue();
                                if (oNoteImage != oNoteTmpImage) {
                                    oNoteTmpImage.setImageResource(R.drawable.alternativa_uncheck);
                                    oRespuTmp.setEsMarcado(false);
                                } else {
                                    oRespuTmp.setEsMarcado(true);
                                }
                            }
                        }else{
                            for (Map.Entry<ImageView, Alternativa> entry : AlternativaAdapter.listaRespuestas.entrySet()) {
                                ImageView oNoteTmpImage = entry.getKey();
                                Alternativa oRespuTmp = entry.getValue();

                                if ((oNoteImage == oNoteTmpImage)&&(oRespuTmp.isEsMarcado())) {
                                    oNoteTmpImage.setImageResource(R.drawable.alternativa_uncheck);
                                    oRespuTmp.setEsMarcado(false);

                                } else if(oNoteImage == oNoteTmpImage) {
                                    oRespuTmp.setEsMarcado(true);
                                }
                            }
                        }

                        // haciendo permanente la eleccion en la base de datos
                        RespuestaDBAdapter dbAdapter = new RespuestaDBAdapter(getContext());
                        dbAdapter.open();

                        String esMarcado = "0";
                        for (Map.Entry<ImageView, Alternativa> entry : AlternativaAdapter.listaRespuestas.entrySet()) {
                            Alternativa oRespuTmp = entry.getValue();

                            esMarcado = "0";
                            if(oRespuTmp.isEsMarcado()){
                                esMarcado = "1";
                            }

                            dbAdapter.updateRespuesta(oRespuTmp.getId(),esMarcado);
                        }

                        dbAdapter.close();
                    }
                }
        );

        return convertView;
    }
}
