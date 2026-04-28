/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author ASUS
 */
public class TabelModelMahasiswa extends AbstractTableModel {
     public TabelModelMahasiswa(List<Mahasiswa> lstMhs){
        this.lstMhs = lstMhs;
    }
   
    @Override
    public int getRowCount() {
        return this.lstMhs.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }
    
    @Override
    public String getColumnName(int column){
         return switch (column) {
             case 0 -> "ID";
             case 1 -> "NIM";
             case 2 -> "Nama";
             case 3 -> "Jenis Kelamin";
             case 4 -> "Alamat";
             default -> null;
         };    
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
         return switch (columnIndex) {
             case 0 -> lstMhs.get(rowIndex).getId();
             case 1 -> lstMhs.get(rowIndex).getNim();
             case 2 -> lstMhs.get(rowIndex).getNama();
             case 3 -> lstMhs.get(rowIndex).getJk();
             case 4 -> lstMhs.get(rowIndex).getAlamat();
             default -> null;
         };
    }
    
    List<Mahasiswa> lstMhs ;
}
