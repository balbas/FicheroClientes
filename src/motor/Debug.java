package motor;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.*;

/**
 * Clase para grabar el debug en un archivo log
 * @author jmbalbas
 */
 public class Debug {

    Date fecha = new Date();
    Logger log;

    public Debug(String workspace) throws IOException {
        log = Logger.getLogger(Debug.class);
        // Formato de fecha
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd.MM.yyyy");
        String fechaActual = formatoFecha.format(fecha);
        
        // Patrón que seguirá las lineas del log
        PatternLayout defaultLayout = new PatternLayout("%p: %d{HH:mm:ss} --> %m%n");
        RollingFileAppender rollingFileAppender = new RollingFileAppender();
        
        //Definimos el archivo dónde irá el log (la ruta)
        rollingFileAppender.setFile(workspace + "/logs/log_" + fechaActual + ".log", true, false, 0);
        rollingFileAppender.setLayout(defaultLayout);

        log.removeAllAppenders();
        log.addAppender(rollingFileAppender);
        log.setAdditivity(false);
    }
    
    public Logger getLog() {
        return log;
    }
    
    public void setLog(Logger  log) {
        this.log = log;
    }
}
