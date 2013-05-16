package motor;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

/**
 * Clase que implementa acciones con los clientes
 * @author jose
 */
public class Clientes { 
    private Logger log;
    
    public Clientes() {
        // Iniciamos el log, si tenemos activo el debug
        if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) {
            Main iniciarlog = new Main();
            log = iniciarlog.getLog();
        }
    }
    
    public int grabarCliente(String consulta) {
        int idCliente = 0;        
        
        Conexion con = new Conexion();        
        
        if(con.insertarActualizarEliminar(consulta) != 0) {
            try {
                ResultSet rs = con.consultar("SELECT LAST_INSERT_ROWID(), CHANGES()");
                while(rs.next()) {
                    idCliente = rs.getInt(1);
                }
            } catch(SQLException ex) {
                if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) log.error("[Clientes.java (grabarCliente)] SQLException: " + ex.getMessage());
            }
        }
        con.desconectar();
        
        return idCliente;
    }
    
    public boolean actualizarCliente(String consulta) {
        boolean resultado = false;
        Conexion con = new Conexion();        
        
        if(con.insertarActualizarEliminar(consulta) != 0) resultado = true;
        con.desconectar();
        
        return resultado;        
    }
}
