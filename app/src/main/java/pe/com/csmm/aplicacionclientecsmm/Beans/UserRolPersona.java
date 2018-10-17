/*
 *  Archivo modificado por Daniela
 * */

package pe.com.csmm.aplicacionclientecsmm.Beans;

/**
 * Created by RICHARD on 11/03/2017.
 */
public class UserRolPersona {

    private String userNombre;
    private Long userId;
    private Long userRolId;
    private Long rolId;
    private String rolNombre;
    private String persNombre;
    private String persApePater;
    private String persApeMater;
    private Long persId;
    private String pacienteId;


    public String getUserNombre() {
        return userNombre;
    }
    public void setUserNombre(String userNombre) {
        this.userNombre = userNombre;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getRolId() {
        return rolId;
    }
    public void setRolId(Long rolId) {
        this.rolId = rolId;
    }
    public String getRolNombre() {
        return rolNombre;
    }
    public void setRolNombre(String rolNombre) {
        this.rolNombre = rolNombre;
    }

    public String getPersNombre() {
        return persNombre;
    }

    public void setPersNombre(String persNombre) {
        this.persNombre = persNombre;
    }

    public String getPersApePater() {
        return persApePater;
    }

    public void setPersApePater(String persApePater) {
        this.persApePater = persApePater;
    }

    public String getPersApeMater() {
        return persApeMater;
    }

    public void setPersApeMater(String persApeMater) {
        this.persApeMater = persApeMater;
    }

    public Long getUserRolId() {
        return userRolId;
    }

    public void setUserRolId(Long userRolId) {
        this.userRolId = userRolId;
    }

    public Long getPersId() {
        return persId;
    }

    public void setPersId(Long persId) {
        this.persId = persId;
    }

    public String getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(String pacienteId) {
        this.pacienteId = pacienteId;
    }
}
