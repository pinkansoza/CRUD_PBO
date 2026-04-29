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
    
    FormMahasiswa frame; 
    IDAOMahasiswa iMahasiswa;
    List<Mahasiswa> lstMhs;
    private final FormMahasiswa frmMahasiswa;

    public ControllerMahasiswa(FormMahasiswa frmMahasiswa) throws SQLException {
        this.frmMahasiswa = frmMahasiswa;
        iMahasiswa = new DAOMahasiswa();

        kondisiAwal(); 
        isiTable();
    }
    
    public void isiTable(){
        lstMhs = iMahasiswa.getAll();
        TabelModelMahasiswa tabelMhs = new TabelModelMahasiswa(lstMhs);
        frmMahasiswa.getTabelData().setModel(tabelMhs);
    }
    
    public void kondisiAwal() {
        reset();

        frmMahasiswa.getbuttonInsert().setEnabled(true);
        frmMahasiswa.getbuttonUpdate().setEnabled(true);
        frmMahasiswa.getbuttonDelete().setEnabled(true);
        frmMahasiswa.getbuttonReset().setEnabled(true);

        // Aktifkan kembali input ID
        frmMahasiswa.gettxtID().setEnabled(true);
    }
    
    public boolean cekValidasi() {
        // Ambil data dari View (Frame)
        String nim = frmMahasiswa.gettxtNim().getText();
        String nama = frmMahasiswa.gettxtNama().getText();
        String alamat = frmMahasiswa.gettxtAlamat().getText();

        // Cek apakah ada yang kosong
        if (nim.trim().isEmpty() || nama.trim().isEmpty() || alamat.trim().isEmpty()) {
            // Tampilkan Pesan Peringatan
            javax.swing.JOptionPane.showMessageDialog(null, "Data Masih ada yang Kosong");
            return false; // Validasi gagal
        }
        return true; // Validasi berhasil
    }
    
    public void insert() {
        // 1. Cek apakah ID sedang mati (mode edit)
        if (!frmMahasiswa.gettxtID().isEnabled()) {
            JOptionPane.showMessageDialog(frmMahasiswa, "Data sudah ada! Gunakan tombol UBAH.");
            return;
        }

        // 2. Cek validasi input kosong
        if (!cekValidasi()) return; 

        // 3. Siapkan objek mahasiswa
        Mahasiswa b = new Mahasiswa();
        b.setId(Integer.valueOf(frmMahasiswa.gettxtID().getText()));
        b.setNim(frmMahasiswa.gettxtNim().getText());
        b.setNama(frmMahasiswa.gettxtNama().getText());
        b.setJk(frmMahasiswa.getJKel().getSelectedItem().toString());
        b.setAlamat(frmMahasiswa.gettxtAlamat().getText());

        // 4. Panggil DAO dan langsung cek hasilnya
        boolean isSuccess = iMahasiswa.insert(b);

        if (isSuccess) {
            // Jika TRUE (Berhasil)
            JOptionPane.showMessageDialog(null, "Input berhasil");
            kondisiAwal(); 
        } else {
            // Jika FALSE (Gagal, biasanya karena ID Duplikat)
            JOptionPane.showMessageDialog(null, "Gaga Input/Duplikat");
        }
    }

        public void reset(){
            if(!frmMahasiswa.gettxtID().isEnabled())
                frmMahasiswa.gettxtID().setEnabled(true);
            frmMahasiswa.gettxtID().setText("");
            frmMahasiswa.gettxtNim().setText("");
            frmMahasiswa.gettxtNama().setText("");
            frmMahasiswa.getJKel().setSelectedItem("");
            frmMahasiswa.gettxtAlamat().setText("");

            frmMahasiswa.gettxtID().setEnabled(true);  
            frmMahasiswa.getbuttonInsert().setEnabled(true);
        }

        public void isiField(int row) {
            // 1. Ambil data dan tampilkan di textfield
            frmMahasiswa.gettxtID().setText(lstMhs.get(row).getId().toString());
            frmMahasiswa.gettxtNim().setText(lstMhs.get(row).getNim());
            frmMahasiswa.gettxtNama().setText(lstMhs.get(row).getNama());
            frmMahasiswa.getJKel().setSelectedItem(lstMhs.get(row).getJk());
            frmMahasiswa.gettxtAlamat().setText(lstMhs.get(row).getAlamat());

            // 2. Kunci Field ID (agar Primary Key tidak diubah-ubah)
            frmMahasiswa.gettxtID().setEnabled(false);

            // 3. ATUR TOMBOL (Pastikan logikanya begini)
            frmMahasiswa.getbuttonInsert().setEnabled(false);
            frmMahasiswa.getbuttonUpdate().setEnabled(true); // MATIKAN simpan (karena bukan input baru)
            frmMahasiswa.getbuttonDelete().setEnabled(true);    // NYALAKAN ubah
            frmMahasiswa.getbuttonReset().setEnabled(true);   // NYALAKAN hapus
        }
    
    public void update() {
        // 1. Cek apakah sudah pilih data (ID harus mati)
        if (frmMahasiswa.gettxtID().isEnabled()) {
            JOptionPane.showMessageDialog(frmMahasiswa, "Pilih data di tabel dulu baru klik Ubah!");
            return;
        }

        // 2. AMBIL DATA ASLI (Data sebelum diotak-atik)
        // Kita cari data di list yang ID-nya sama dengan di TextField
        int idSekarang = Integer.parseInt(frmMahasiswa.gettxtID().getText());
        Mahasiswa dataLama = null;
        for (Mahasiswa m : lstMhs) {
            if (m.getId() == idSekarang) {
                dataLama = m;
                break;
            }
        }

        // 3. LOGIKA PERBANDINGAN
        if (dataLama != null) {
            boolean isSama = 
                frmMahasiswa.gettxtNim().getText().equals(dataLama.getNim()) &&
                frmMahasiswa.gettxtNama().getText().equals(dataLama.getNama()) &&
                frmMahasiswa.getJKel().getSelectedItem().toString().equals(dataLama.getJk()) &&
                frmMahasiswa.gettxtAlamat().getText().equals(dataLama.getAlamat());

            if (isSama) {
                JOptionPane.showMessageDialog(null, "Tidak ada data yang berubah");
                return; // Berhenti, tidak perlu akses database
            }
        }

        // 4. Validasi kelengkapan (seperti NIM kosong dll)
        if (!cekValidasi()) return;

        try {
            Mahasiswa b = new Mahasiswa();
            b.setId(idSekarang);
            b.setNim(frmMahasiswa.gettxtNim().getText());
            b.setNama(frmMahasiswa.gettxtNama().getText());
            b.setJk(frmMahasiswa.getJKel().getSelectedItem().toString());
            b.setAlamat(frmMahasiswa.gettxtAlamat().getText());

            iMahasiswa.update(b);
            JOptionPane.showMessageDialog(null, "Update berhasil!");
            kondisiAwal();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal update: " + e.getMessage());
        }
    }
    
    public void delete() {
        // 1. Ambil ID dari textfield
        String idStr = frmMahasiswa.gettxtID().getText();

        // 2. Cek apakah ID kosong
        if (idStr.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frmMahasiswa, "Pilih data yang akan dihapus terlebih dahulu!");
            return;
        }

        // --- PENGAMAN TAMBAHAN ---
        // Jika txtID masih AKTIF (Enabled), berarti user mengetik ID sendiri (Mode Insert).
        // Kita cegah karena data yang sedang diketik belum tentu ada di database.
        if (frmMahasiswa.gettxtID().isEnabled()) {
            JOptionPane.showMessageDialog(null, 
                "Data belum dipilih dari tabel! tidak bisa menghapus data yang sedang diinput baru.");
            return;
        }

        // 3. Konfirmasi hapus
        int confirm = JOptionPane.showConfirmDialog(frmMahasiswa, "Yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                iMahasiswa.delete(Integer.parseInt(idStr));
                JOptionPane.showMessageDialog(null, "Data berhasil dihapus");

                isiTable();      
                kondisiAwal();   

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Gagal hapus: " + e.getMessage());
            }
        }
    }
    
    public void cari(){
        lstMhs= iMahasiswa.getAllByName(frmMahasiswa.gettxtCariNama().getText());
        TabelModelMahasiswa tabelMhs = new TabelModelMahasiswa(lstMhs);
        frmMahasiswa.getTabelData().setModel(tabelMhs);
    }
}