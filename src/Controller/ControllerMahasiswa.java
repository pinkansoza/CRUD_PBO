/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

// Pastikan huruf besar/kecilnya sama dengan nama folder package kamu
import View.FormMahasiswa; 
import DAO.DAOMahasiswa;
import DAOInterface.IDAOMahasiswa;
import Model.Mahasiswa;
import Model.TabelModelMahasiswa;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;

public class ControllerMahasiswa {
    
    // Pindahkan deklarasi ke atas (sebagai field)
    FormMahasiswa frame; 
    IDAOMahasiswa iMahasiswa;
    List<Mahasiswa> lstMhs;
    private final FormMahasiswa frmMahasiswa;

    public ControllerMahasiswa(FormMahasiswa frmMahasiswa) throws SQLException {
        this.frmMahasiswa = frmMahasiswa;
        iMahasiswa = new DAOMahasiswa();
    }
    
    public void isiTable(){
        lstMhs = iMahasiswa.getAll();
        TabelModelMahasiswa tabelMhs = new TabelModelMahasiswa(lstMhs);
        frmMahasiswa.getTabelData().setModel(tabelMhs);
    }
    
    public void insert(){
        if (!cekValidasi()) {
        return; 
        }
        Mahasiswa b = new Mahasiswa();
        b.setId(Integer.valueOf(frmMahasiswa.gettxtID().getText()));
        b.setNim(frmMahasiswa.gettxtNim().getText());
        b.setNama(frmMahasiswa.gettxtNama().getText());
        b.setJk(frmMahasiswa.getJKel().getSelectedItem().toString());
        b.setAlamat(frmMahasiswa.gettxtAlamat().getText());
        iMahasiswa.insert(b);
        JOptionPane.showMessageDialog(null, "input berhasil");
    }
    
    public void reset(){
        frmMahasiswa.gettxtID().setText("");
        frmMahasiswa.gettxtNim().setText("");
        frmMahasiswa.gettxtNama().setText("");
        frmMahasiswa.getJKel().setSelectedItem("");
        frmMahasiswa.gettxtAlamat().setText("");
    }

    public boolean cekValidasi() {
        // Ambil data dari View (Frame)
        String nim = frmMahasiswa.gettxtNim().getText();
        String nama = frmMahasiswa.gettxtNama().getText();
        String alamat = frmMahasiswa.gettxtAlamat().getText();

        // Cek apakah ada yang kosong
        if (nim.trim().isEmpty() || nama.trim().isEmpty() || alamat.trim().isEmpty()) {
            // Tampilkan Pesan Peringatan
            javax.swing.JOptionPane.showMessageDialog(frmMahasiswa, 
                "Data belum lengkap! Harap isi semua kolom.", 
                "Peringatan", 
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return false; // Validasi gagal
        }
        return true; // Validasi berhasil
    }
}
