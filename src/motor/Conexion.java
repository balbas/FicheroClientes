package motor;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;

/**
 * Clase que implementa la conexión con la base de datos
 * @author jose
 */
public class Conexion {
    private Connection con = null;
    private ResultSet rs = null;
    private Statement st = null;
    private PreparedStatement pst = null;
    private String rutaRelativa = System.getProperty("user.dir");
    private String dbFolder = Configuracion.getInstance().getProperty(Configuracion.DATABASE_FOLDER);
    private String db = Configuracion.getInstance().getProperty(Configuracion.DATABASE_NAME);
    private Logger log;
    
    // Constructor de clase que se conecta a la base de datos
    public Conexion() {
        // Iniciamos el log, si tenemos activo el debug
        if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) {
            Main iniciarlog = new Main();
            log = iniciarlog.getLog();
        }
        
        try {
            Class.forName("org.sqlite.JDBC");
        }catch(ClassNotFoundException ex) {
            if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) log.error("[Conexion.java (Conexion)] ClassNotFoundException: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }  
        
        try {            
            con = DriverManager.getConnection("jdbc:sqlite:"+ rutaRelativa + File.separator + dbFolder + File.separator + db);
        } catch(SQLException ex) {
            if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) log.error("[Conexion.java (Conexion)] SQLException: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, "No se ha podido establecer conexión con la base de datos", "Error", JOptionPane.ERROR_MESSAGE, new ImageIcon("src/iconos/delete.png"));
        }
    }
    
    /* Permite retornar la conexión */
    public Connection getConnection(){
        return con;
    }
    
    // Método para realizar una consulta. Devuelve el resultado (ResultSet).
    public ResultSet consultar(String consulta) {
        try {  
            if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) log.info("[Conexion.java (consultar)] Consulta: " + consulta);
            st = null;
            st = con.createStatement();           
            rs = st.executeQuery(consulta);
        } catch (SQLException ex) {
            if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) log.error("[Conexion.java (consultar)] SQLException: " + ex.getMessage());
        }     
        
        return rs;
    }
    
    // Método para insertar/actualizar/eliminar registros. Devuelve true/false.
    public int insertarActualizarEliminar(String consulta) {        
        int filasAfectadas = 0;
        
        try {
            if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) log.info("[Conexion.java (insertarActualizarEliminar)] Ejecutar consulta: " + consulta);
            pst = con.prepareStatement(consulta);
            filasAfectadas = pst.executeUpdate();
            pst.close();
        } catch(SQLException ex) {
            if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) log.error("[Conexion.java (insertarActualizarEliminar)] SQLException: " + ex.getMessage());
        }
        
        return filasAfectadas;
    }
    
    // Método para desconectar.
    public void desconectar() {
        try {
            con.close();
        } catch (SQLException ex) {
            if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) log.error("[Conexion.java (desconectar)] SQLException: " + ex.getMessage());
        }
    }
}
