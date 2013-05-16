package motor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.log4j.Logger;

/**
 * Clase que implementa las acciones de copias de seguridad
 * @author jmbalbas
 */
public class CopiaSeguridad {
    private String rutaRelativa = System.getProperty("user.dir") + File.separator;
    private Logger log;
    
    public CopiaSeguridad() {
        // Iniciamos el log, si tenemos activo el debug
        if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) {
            Main iniciarlog = new Main();
            log = iniciarlog.getLog();
        }
    }
    
    public boolean realizarCopia(String fecha) {
        return this.Copia(fecha, "0");
    }
    
    public boolean restaurarCopia(String fecha) {
        return this.Copia(fecha, "1");        
    }
    
    private boolean Copia(String fecha, String modo) {
        boolean resultado = false;
        InputStream in;
        OutputStream out;
        byte[] buf = new byte[1024];
        int len;        
        
        // Covertimos "/" en "." para contruir el nombre del archivo de destino
        String año = Funciones.Right(fecha, 4);
        String mes = Funciones.Right(Funciones.Left(fecha, 5), 2);
        String dia = Funciones.Left(fecha, 2);
        fecha = dia + "." + mes + "." + año;
        
        // Objetos File para el origen y destino
        File origen;
        File destino;
        
        if(modo.equals("0")) { // realizarCopia
            origen = new File(rutaRelativa + Configuracion.getInstance().getProperty(Configuracion.DATABASE_FOLDER) + File.separator + Configuracion.getInstance().getProperty(Configuracion.DATABASE_NAME));
            destino = new File(rutaRelativa + Configuracion.getInstance().getProperty(Configuracion.BACKUP_FOLDER) + File.separator + fecha + "_" + Configuracion.getInstance().getProperty(Configuracion.DATABASE_NAME));
        } else { // restaurarCopia
            origen = new File(rutaRelativa + Configuracion.getInstance().getProperty(Configuracion.DATABASE_FOLDER) + File.separator + Configuracion.getInstance().getProperty(Configuracion.DATABASE_NAME));
            origen.delete();
            origen = new File(rutaRelativa + Configuracion.getInstance().getProperty(Configuracion.BACKUP_FOLDER) + File.separator + fecha + "_" + Configuracion.getInstance().getProperty(Configuracion.DATABASE_NAME));
            destino = new File(rutaRelativa + Configuracion.getInstance().getProperty(Configuracion.DATABASE_FOLDER) + File.separator + Configuracion.getInstance().getProperty(Configuracion.DATABASE_NAME));
        }
        
        try {
            if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) log.info("[CopiaSeguridad.java (Copia)] Origen: " + origen);
            in = new FileInputStream(origen);
            if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) log.info("[CopiaSeguridad.java (Copia)] Destino: " + destino);
            out = new FileOutputStream(destino);
            try {
                while((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                    resultado = true;
                }
            } catch (IOException ex) {
                if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) log.error("[CopiaSeguridad.java (Copia)] IOException: " + ex.getMessage());
            }
        } catch (FileNotFoundException ex) {
            if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) log.error("[CopiaSeguridad.java (Copia)] FileNotFoundException: " + ex.getMessage());
        }
        
        return resultado;
    }
}
