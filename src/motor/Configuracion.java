package motor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * Clase para manejar el fichero properties de la aplicación
 * @author jose
 */
public class Configuracion {
    Properties properties;
    
    //public final static String CONFIG_FILE_NAME = "motor" + File.separator + "Configuracion.properties";
    public final static String CONFIG_FILE_NAME = "Configuracion.properties";
    public final static String ACTIVE_SESSION = "sesionActiva";
    public final static String USER = "usuarioPorDefecto";
    public final static String PASSWORD = "passwordPorDefecto";
    public final static String REMEMBER = "recordar";
    public final static String DATABASE_FOLDER = "carpetaBaseDatos";
    public final static String DATABASE_NAME = "nombreBaseDatos";
    public final static String TEMP_FOLDER = "carpetaTemporal";
    public final static String OUTPUT_FILE_NAME = "nombreArchivoSalida";
    public final static String REPORTS_FOLDER = "carpetaReportes";
    public final static String REPORT_NAME = "nombreReporte";
    public final static String PDF_FOLDER = "carpetaPdf";
    public final static String DEBUG = "debug";
    public final static String AUTO_BACKUP = "copiaAutomatica";
    public final static String LAST_DATE_BACKUP = "fechaUltimaCopia";
    public final static String MULTIPLE_SESSION = "inicioSesionMultiple";
    public final static String BACKUP_FOLDER = "carpetaCopias";
    // Ruta de acceso al fichero properties
    private final static String url = System.getProperty("user.dir") + File.separator + CONFIG_FILE_NAME;
    private Logger log;
 
    private Configuracion() {        
        this.properties = new Properties();
        
        // Iniciamos el log, si tenemos activo el debug
        if(DEBUG.equals("si")) {
            Main iniciarlog = new Main();
            log = iniciarlog.getLog();
        }

        try {
            this.properties.load(new FileInputStream(url));
        } catch (IOException ex) { 
            if(DEBUG.equals("si")) log.error("[Configuracion.java (Configuracion)] IOException: " + ex.getMessage());
        }
    }
 
    /**
     * Implementando Singleton
     * @return
     */
    public static Configuracion getInstance() {
        return ConfiguracionHolder.INSTANCE;
    }
 
    private static class ConfiguracionHolder { 
        private static final Configuracion INSTANCE = new Configuracion();
    }
 
    /**
     * Retorna la propiedad de configuración solicitada
     * @param key
     * @return
     */
    public String getProperty(String key) {
        return this.properties.getProperty(key);
    }
    
    /**
     * Nuevo valor a la propiedad de configuración solicitada. Graba en el fichero properties
     * @param key
     * @param value
     */    
    public void setProperty(String key, String value) {
        this.properties.setProperty(key, value);        
            
        try {
             this.properties.store(new FileOutputStream(new File(url)), null);               
        } catch (IOException ex) {
            if(DEBUG.equals("si")) log.error("[Conexion.java (setProperty)] IOException: " + ex.getMessage());
        }
    }
}