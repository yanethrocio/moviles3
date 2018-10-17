
/**
 * Archivo modificado por Tudela.
 */

package pe.com.csmm.aplicacionclientecsmm.Beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by RICHARD on 25/08/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Parametros {

    private String llave;
    private String valor;

    public Parametros() {
    }

    public Parametros(String llave, String valor) {
        this.llave = llave;
        this.valor = valor;
    }

    public String getLlave() {
        return llave;
    }

    public void setLlave(String llave) {
        this.llave = llave;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
