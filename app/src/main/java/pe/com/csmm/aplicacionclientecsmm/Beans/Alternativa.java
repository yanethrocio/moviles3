package pe.com.csmm.aplicacionclientecsmm.Beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by RICHARD on 25/08/2016.
 */
// modificado el 2018/10/10
@JsonIgnoreProperties(ignoreUnknown = true)
public class Alternativa {

    private Long id;
    private Long idPregunta;
    private String respuesta;
    private String otros;
    private String audio;
    private String video;
    private String adicional;
    private boolean esMarcado;
    private String tipoResp;

    public Alternativa() {
    }

    public Alternativa(Long id, Long idPregunta, String respuesta, String esMarcado , String tipoRespuesta, String otros) {
        this.respuesta = respuesta;
        this.tipoResp = tipoRespuesta;
        this.idPregunta = idPregunta;
        this.otros = otros;
        this.id = id;

        if(esMarcado.equalsIgnoreCase("1")){
            this.esMarcado = true;
        }else{
            this.esMarcado = false;
        }
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getOtros() {
        return otros;
    }

    public void setOtros(String otros) {
        this.otros = otros;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getAdicional() {
        return adicional;
    }

    public void setAdicional(String adicional) {
        this.adicional = adicional;
    }

    public boolean isEsMarcado() {
        return esMarcado;
    }

    public void setEsMarcado(boolean esMarcado) {
        this.esMarcado = esMarcado;
    }

    public String getTipoResp() {
        return tipoResp;
    }

    public void setTipoResp(String tipoResp) {
        this.tipoResp = tipoResp;
    }

    public Long getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(Long idPregunta) {
        this.idPregunta = idPregunta;
    }
}
