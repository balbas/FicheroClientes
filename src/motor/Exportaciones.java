package motor;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.io.*;
import javax.swing.JTable;
import jxl.*;
import jxl.write.*;
import org.apache.log4j.Logger;

/**
 * Clase que implementa las exportaciones a las diferentes extensiones
 * @author jmbalbas
 */
public class Exportaciones {
    private File archivo;
    private FileWriter fichero;
    private JTable grid;
    private String nombreHojaExcel;
    private Logger log;
    
    public Exportaciones() {
        // Iniciamos el log, si tenemos activo el debug
        if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) {
            Main iniciarlog = new Main();
            log = iniciarlog.getLog();
        }
    }
    
    public Exportaciones(File archivo, JTable grid, String nombreHojaExcel) {
        // Iniciamos el log, si tenemos activo el debug
        if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) {
            Main iniciarlog = new Main();
            log = iniciarlog.getLog();
        }
        
        this.archivo = archivo;
        this.grid = grid;
        this.nombreHojaExcel = nombreHojaExcel;
    }
    
    public Exportaciones(FileWriter fichero, JTable grid, String nombreHojaExcel) {
        // Iniciamos el log, si tenemos activo el debug
        if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) {
            Main iniciarlog = new Main();
            log = iniciarlog.getLog();
        }
        
        this.fichero = fichero;
        this.grid = grid;
        this.nombreHojaExcel = nombreHojaExcel;
    }
    
    public boolean aExcel () {
        try {
            // Flujo de salida que apunta a donde vamos a escribir
            DataOutputStream flujoSalida = new DataOutputStream(new FileOutputStream(archivo));

            // Representa nuestro archivo en Excel y necesita un OutputStream para saber donde va a poner los datos
            WritableWorkbook workbook = Workbook.createWorkbook(flujoSalida);

            // Como Excel tiene muchas hojas, esta crea o toma la hoja
            // Coloca el nombre de la hoja y el indice
            WritableSheet sheet = workbook.createSheet(nombreHojaExcel, 0);
            
            // Recorremos la tabla
            for(int i = 0; i < grid.getColumnCount(); i++) {
                // Cabecera
                sheet.addCell(new Label(i, 0, grid.getColumnName(i)));
                for(int j = 1; j < grid.getRowCount()+1; j++) {
                    Object objeto = grid.getValueAt(j-1, i);
                    // Datos
                    sheet.addCell(new Label(i, j, String.valueOf(objeto)));
                }
            }
            // Escribir hoja Excel
            workbook.write();
            
            // Cerramos el WritableWorkbook y DataOutputStream
            workbook.close();
            flujoSalida.close();

            return true;
        } catch(IOException ex) { 
            if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) log.error("[Exportaciones.java (aExcel)] IOException: " + ex.getMessage());
        } catch(WriteException ex) { 
            if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) log.error("[Exportaciones.java (aExcel)] WriteException: " + ex.getMessage());
        }
        
        return false;
    }
    
    public boolean aPdf() {
        try{
            Document documento = new Document();
            PdfWriter.getInstance(documento,new FileOutputStream(archivo));
            documento.open();
            PdfPTable hoja = new PdfPTable(grid.getColumnCount());
            // Cabecera
            for(int i = 0; i < grid.getColumnCount(); i++) { hoja.addCell(grid.getColumnName(i)); }
            // Datos
            for(int j = 0; j < grid.getRowCount(); j++) {
                for(int k = 0; k < grid.getColumnCount(); k++) {
                    Object dato = GetData(grid, j, k);
                    String valor = dato.toString();
                    hoja.addCell(valor);
                }
            }
            documento.add(hoja);
            documento.close();
            
            return true;
        } catch(Exception ex) {
            if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) log.error("[Exportaciones.java (aPdf)] Exception: " + ex.getMessage());
        }
        
        return false;
    }
    
    public boolean aTextoPlano() { 
        try { 
            PrintWriter imprimir = new PrintWriter(fichero);
            
            imprimir.print("| ");
            for(int i = 0; i < grid.getColumnCount(); i++) {
                imprimir.print(grid.getColumnName(i).toString());
                imprimir.print(" | ");
            }
            imprimir.println("");
            // Datos
            for(int j = 0; j < grid.getRowCount(); j++) {
                imprimir.print("| ");
                for(int k = 0; k < grid.getColumnCount(); k++) {
                    Object dato = GetData(grid, j, k);
                    String valor = dato.toString();
                    imprimir.print(valor);
                    imprimir.print(" | ");
                }
                imprimir.println("");
            }           
            fichero.close();
            
            return true;
        } catch(Exception ex) { 
            if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) log.error("[Exportaciones.java (aTextoPlano)] Exception: " + ex.getMessage());
        }
        
        return false;
    }
    
    public Object GetData(JTable grid, int row_index, int col_index) {
        return grid.getModel().getValueAt(row_index, col_index);
    }
}
