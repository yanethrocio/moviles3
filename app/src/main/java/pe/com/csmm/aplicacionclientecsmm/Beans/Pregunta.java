package pe.com.csmm.aplicacionclientecsmm.Beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by RICHARD on 25/08/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pregunta {

    private Long id;
    private String pregunta;
    private String descripcion;
    private String adicional;


    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getPregunta() {
        return pregunta;
    }
    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getAdicional() {
        return adicional;
    }

    public void setAdicional(String adicional) {
        this.adicional = adicional;
    }
}
