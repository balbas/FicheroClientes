package motor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.log4j.Logger;

/**
 * Clase que implementa la generación e impresión de un report
 * @author jose
 */
public class ImprimirReport {
    private String rutaRelativa = System.getProperty("user.dir");
    private String carpetaReportes = Configuracion.getInstance().getProperty(Configuracion.REPORTS_FOLDER);
    private String nombreReporte = Configuracion.getInstance().getProperty(Configuracion.REPORT_NAME);
    private String carpetaPdf = Configuracion.getInstance().getProperty(Configuracion.PDF_FOLDER);
    private Logger log;
    
    public ImprimirReport(String idCliente, String tipoPagina) {         
        JasperReport reporte;
        JasperPrint reporte_view;
        
        // Iniciamos el log, si tenemos activo el debug
        if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) {
            Main iniciarlog = new Main();
            log = iniciarlog.getLog();
        }
        
        if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) log.info("[ImprimirReport.java (ImprimirReport)] Entrada: " + idCliente + ", " + tipoPagina);
 
        Conexion con = new Conexion();
     
        try {
            //if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) log.info("[ImprimirReport.java (ImprimirReport)] Compilar: " + rutaRelativa + File.separator + carpetaReportes + File.separator + nombreReporte + tipoPagina + ".jrxml");
            if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) log.info("[ImprimirReport.java (ImprimirReport)] Compilado: " + rutaRelativa + File.separator + carpetaReportes + File.separator + nombreReporte + tipoPagina + ".jasper");
            
            // Reporte sin compilar
            //reporte = JasperCompileManager.compileReport(rutaRelativa + File.separator + carpetaReportes + File.separator + nombreReporte + tipoPagina + ".jrxml");
            
            // Reporte compilado
            reporte = (JasperReport) JRLoader.loadObjectFromFile(rutaRelativa + File.separator + carpetaReportes + File.separator + nombreReporte + tipoPagina + ".jasper");
            
            Map <String,Object> parametros = new HashMap<String,Object>();
            parametros.clear();
            
            parametros.put("ID_CLIENTE", idCliente);

            reporte_view = JasperFillManager.fillReport(reporte, parametros, con.getConnection());
            
            if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) log.info("[ImprimirReport.java (ImprimirReport)] Salida: " + carpetaPdf + File.separator + nombreReporte + tipoPagina + ".pdf");
            
            JasperExportManager.exportReportToPdfFile(reporte_view, carpetaPdf + File.separator + nombreReporte + tipoPagina + ".pdf");
            //JasperViewer.viewReport(reporte_view, false);
        } catch(JRException ex) {
            if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) log.error("[ImprimirReport.java (ImprimirReport)] JRException: " + ex.getMessage());
        }
        con.desconectar();
    }
}