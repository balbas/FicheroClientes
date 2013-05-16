package motor;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

/**
 * Clase que implementa acciones con las visitas
 * @author jose
 */
public class Visitas {
    private Logger log;
    
    public Visitas() {
        // Iniciamos el log, si tenemos activo el debug
        if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) {
            Main iniciarlog = new Main();
            log = iniciarlog.getLog();
        }
    }
    
    public boolean grabarVisita(String consulta) {
        Boolean resultado = false;       

        Conexion con = new Conexion();        
        if(con.insertarActualizarEliminar(consulta) != 0) resultado = true; 
        con.desconectar();
        
        return resultado;
    }
    
    public int obtenerContador() {
        int contador = 0;
        Conexion con = new Conexion();
        
        try {
            ResultSet rs = con.consultar("SELECT MAX(visitas.contador) FROM visitas");
        
            while(rs.next()) { 
                contador = rs.getInt(1);
            }
        } catch (SQLException ex) {
            if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) log.error("[Visitas.java (obtenerContador)] SQLException: " + ex.getMessage());
        }
        
        con.desconectar();        
        return contador;
    }
}
