package pe.com.csmm.aplicacionclientecsmm.Beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

/**
 * Created by RICHARD on 25/08/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Atencion {

    private Long id;
    private Long persona_id;
    private Long entrevistado_id;
    private String descripcion;
    private String adicional;
    private Date fecha;


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
    public String getAdicional() {
        return adicional;
    }
    public void setAdicional(String adicional) {
        this.adicional = adicional;
    }

    public Long getPersona_id() {
        return persona_id;
    }
    public void setPersona_id(Long persona_id) {
        this.persona_id = persona_id;
    }
    public Long getEntrevistado_id() {
        return entrevistado_id;
    }
    public void setEntrevistado_id(Long entrevistado_id) {
        this.entrevistado_id = entrevistado_id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
