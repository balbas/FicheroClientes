package motor;

import javax.swing.table.DefaultTableModel;

/**
 * Clase que extiende de DefaultTableModel para los grid de la aplicación.
 * @author jose
 */
public class ModeloGrid extends DefaultTableModel {
    @Override
    public boolean isCellEditable (int row, int column) {
       return false;
   }
}
