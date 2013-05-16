package motor;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * Clase para gestionar las acciones sobre un grid (seleccionar, filtrar, formatear, etc.).
 * @author jose
 */
public class Grid extends DefaultTableCellRenderer {
    
    public void establecerAnchoMinimo(JTable grid, String columna, int ancho) {
        TableColumn columnaId = grid.getColumn(columna);
        columnaId.setMinWidth(ancho);
    }
    
    public void establecerAnchoMaximo(JTable grid, String columna, int ancho) {
        TableColumn columnaId = grid.getColumn(columna);
        columnaId.setMaxWidth(ancho);
    }
    
    public void aplicarFiltro(int indexColumn, JTable grid, String cadenafiltro) {
        TableRowSorter<TableModel> orden;
        orden = new TableRowSorter<TableModel>(grid.getModel());
        grid.setRowSorter(orden);
        RowFilter<TableModel, Object> filtro;
        try {
            filtro = RowFilter.regexFilter(cadenafiltro, indexColumn);
        }catch(java.util.regex.PatternSyntaxException e){
            return;
        }
        orden.setRowFilter(filtro);
    }    
    
    public String seleccionarFila(JTable grid, ModeloGrid modelo,int columna, int campo) {
        int seleccion = grid.getSelectedRow();    
        int filaSel = grid.convertRowIndexToModel(seleccion);
        int columnaSel = columna;
        
        if ((filaSel > -1) && (columnaSel > -1)) {
            return modelo.getValueAt(filaSel, campo).toString();
        } else {
            return "";
        }
    }
    
    // Componente sobreescrito: getTableCellRendererComponent (altenar color de la fila)
    @Override 
    public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
        if(row % 2 != 0) {
            this.setBackground(new Color(225, 237, 251));
            this.setForeground(Color.black);
        } else {    
            this.setBackground(Color.white);
            this.setForeground(Color.black);
        }        
        super.getTableCellRendererComponent(table, value, selected, focused, row, column);
        
        return this;
    }
}
