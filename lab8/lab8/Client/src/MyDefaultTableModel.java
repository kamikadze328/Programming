import javax.swing.table.DefaultTableModel;

class MyDefaultTableModel extends DefaultTableModel {

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public Class getColumnClass(int col) {
        if (col == 2 || col ==5|| col ==6|| col ==7)
            return Integer.class;
        else return String.class;
    }
}
