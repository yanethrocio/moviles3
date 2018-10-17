
/*
*Archivo modificado por Yaneth
 **/

package pe.com.csmm.aplicacionclientecsmm.Beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by RICHARD on 09/02/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private Long id;
    private String nombre;
    private String password;


    public User() {
    }
    public User(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
