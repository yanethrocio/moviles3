/*
* Archivo modificado por HackerTeban
* */
package pe.com.csmm.aplicacionclientecsmm.Beans;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import pe.com.csmm.aplicacionclientecsmm.R;

/**
 * Created by RICHARD on 25/08/2016.
 */
public class AtencionAdapter extends ArrayAdapter<Atencion> {

    public AtencionAdapter(Context context, ArrayList<Atencion> atencions){
        super(context,0, atencions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Atencion pregunta = getItem(position);
        final String descripcion = pregunta.getDescripcion();
        SimpleDateFormat formatFecha = new SimpleDateFormat("'Atencion el' EEEE dd 'de' MMMM");

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.atencion_list_row,parent,
                    false);
        }

        TextView oNoteTitle = convertView.findViewById(R.id.List_item_note_title);
        TextView oNoteBody = convertView.findViewById(R.id.List_item_note_body);
        ImageView oNoteImage = convertView.findViewById(R.id.noteIcon);

        oNoteTitle.setText(pregunta.getDescripcion());
        oNoteBody.setText(formatFecha.format(new Date()));
        oNoteImage.setImageResource(R.drawable.fondo_pregunta);

        oNoteImage.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(),descripcion, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        return convertView;
    }
}
