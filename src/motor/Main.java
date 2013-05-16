package motor;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.apache.log4j.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import ventanas.*;

/**
 * Clase Main.
 * @author jose
 */
public class Main {
    Debug logObject = null;
    Logger log;
    
    // Punto de partida de la aplicación.
    public static void main(String[] args) {                
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                if(Configuracion.getInstance().getProperty(Configuracion.MULTIPLE_SESSION).equals("si")) {
                    this.iniciarAplicacion();
                } else {
                    if(Configuracion.getInstance().getProperty(Configuracion.ACTIVE_SESSION).equals("no")) {
                        this.iniciarAplicacion();
                    } else {
                        JOptionPane.showMessageDialog(null, "Ya ha iniciado sesión", "Error", JOptionPane.ERROR_MESSAGE, new ImageIcon("src/iconos/delete.png"));
                        System.exit(0);
                    }
                }
            }

            private void iniciarAplicacion() {
                // Formato fecha
                SimpleDateFormat formatoFecha = new SimpleDateFormat("dd'/'MM'/'yyyy", new Locale("es_ES"));
                // Fecha actual
                Date ahora = new Date();
                // Fecha actual con nuestro formato
                String fechaActual = formatoFecha.format(ahora);
                // Obtenemos la fecha de última copia
                String fechaUltimaCopia = Configuracion.getInstance().getProperty(Configuracion.LAST_DATE_BACKUP);

                if(!fechaUltimaCopia.equals("") && !fechaUltimaCopia.equals(fechaActual)){
                    CopiaSeguridad copia = new CopiaSeguridad();
                    copia.realizarCopia(fechaActual);
                    Configuracion.getInstance().setProperty("fechaUltimaCopia", fechaActual);
                }                 

                Configuracion.getInstance().setProperty("sesionActiva", "si");                    
                // Iniciamos el login.
                new Login().setVisible(true);
            }
        });
    }
    
    public Main() {
        if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) { 
            this.iniciarLog();
        }
    }
    
    private void iniciarLog() {
        // Creamos el log
        try {
            this.logObject = new Debug(System.getProperty("user.dir"));  
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.log = this.logObject.getLog();
    }
    
    public Logger getLog() {
        return this.log;
    }
}
