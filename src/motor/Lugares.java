package motor;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

/**
 * Clase que implementa acciones con los lugares
 * @author jose
 */
public class Lugares {
    private Logger log;
    
    public Lugares() {
        // Iniciamos el log, si tenemos activo el debug
        if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) {
            Main iniciarlog = new Main();
            log = iniciarlog.getLog();
        }
    }
    
    public int grabarLugar(String consulta) {
        int idLugar = 0;        
        
        Conexion con = new Conexion();        
        
        if(con.insertarActualizarEliminar(consulta) != 0) {
            try {
                ResultSet rs = con.consultar("SELECT LAST_INSERT_ROWID(), CHANGES()");
                while(rs.next()) {
                    idLugar = rs.getInt(1);
                }
            } catch(SQLException ex) {
                if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) log.error("[Lugares.java (grabarLugar)] SQLException: " + ex.getMessage());
            }
        }
        con.desconectar();
        
        return idLugar;
    }
    
    public boolean actualizarLugar(String consulta) {
        boolean resultado = false;
        Conexion con = new Conexion();        
        
        if(con.insertarActualizarEliminar(consulta) != 0 ) resultado = true;
        con.desconectar();
        
        return resultado;        
    }
}
