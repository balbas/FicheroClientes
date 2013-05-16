package motor;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Rectangle;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Clase para unir pdfs
 * @author jmbalbas
 */
public class MergePDF {
    private Logger log;
    
    public MergePDF(String[] pdfFiles, String outputFilePath) {
        // Iniciamos el log, si tenemos activo el debug
        if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) {
            Main iniciarlog = new Main();
            log = iniciarlog.getLog();
        }
        
        try {      
            List<InputStream> pdfs = new ArrayList<InputStream>();
            for(int i = 0; i < pdfFiles.length; i++) {
                if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) log.info("[MergePDF.java (MergePDF)] Entrada:" + pdfFiles[i]);
                pdfs.add(new FileInputStream(pdfFiles[i]));
            }
            
            if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) log.info("[MergePDF.java (MergePDF)] Salida:" + outputFilePath);
            
            OutputStream output = new FileOutputStream(outputFilePath);
            concatPDFs((ArrayList<InputStream>) pdfs, output, true);
        } catch (Exception ex) {
            if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) log.error("[MergePDF.java (MergePDF)] Exception: " + ex.getMessage());
        }
    }

    public void concatPDFs(List<InputStream> streamOfPDFFiles, OutputStream outputStream, boolean paginate) {
        Rectangle pageSize = new Rectangle(419, 629);
        Document document = new Document(pageSize);
        try {
            List<InputStream> pdfs = streamOfPDFFiles;
            List<PdfReader> readers = new ArrayList<PdfReader>();
            Iterator<InputStream> iteratorPDFs = pdfs.iterator();

            // Create Readers for the pdfs.
            while(iteratorPDFs.hasNext()) {
                InputStream pdf = iteratorPDFs.next();
                PdfReader pdfReader = new PdfReader(pdf);
                readers.add(pdfReader);
                pdf.close();
            }
            // Create a writer for the outputstream
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            document.open();            
            PdfContentByte cb = writer.getDirectContent(); // Holds the PDF
            // data
            PdfImportedPage page;
            int pageOfCurrentReaderPDF = 0;
            Iterator<PdfReader> iteratorPDFReader = readers.iterator();
            
            // Loop through the PDF files and add to the output.
            while(iteratorPDFReader.hasNext()) {
                PdfReader pdfReader = iteratorPDFReader.next();
                
                // Create a new page in the target for each source page.
                while(pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
                    document.newPage();
                    pageOfCurrentReaderPDF++;
                    page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
                    cb.addTemplate(page, 0, 0);
                }
                pageOfCurrentReaderPDF = 0;
            }
            outputStream.flush();            
            document.close();
            outputStream.close();
        } catch(Exception ex) {
            if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) log.error("[MergePDF.java (concatPDFs)] Exception: " + ex.getMessage());
        } finally {
            if(document.isOpen())
                document.close();
            try {
                if(outputStream != null) outputStream.close();
            } catch(IOException ex) {
               if(Configuracion.getInstance().getProperty(Configuracion.DEBUG).equals("si")) log.error("[MergePDF.java (concatPDFs)] IOException: " + ex.getMessage());
            }
        }
    }
}
