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
    
    // WAJIB DIPANGGIL DI SINI
    kondisiAwal(); 
    isiTable();
}
    
    public void isiTable(){
        lstMhs = iMahasiswa.getAll();
        TabelModelMahasiswa tabelMhs = new TabelModelMahasiswa(lstMhs);
        frmMahasiswa.getTabelData().setModel(tabelMhs);
    }
    
    public void kondisiAwal() {
    reset(); // Mengosongkan semua TextField
    
    // Pastikan tombol Simpan yang AKTIF, sisanya MATI
    frmMahasiswa.getbuttonInsert().setEnabled(true);  // Ini harus TRUE agar bisa input lagi
    frmMahasiswa.getbuttonUpdate().setEnabled(true);   // MATI
    frmMahasiswa.getbuttonDelete().setEnabled(true);  // MATI
    frmMahasiswa.getbuttonReset().setEnabled(true);   // NYALA
    
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
            javax.swing.JOptionPane.showMessageDialog(frmMahasiswa, 
                "Data masih ada yang kosong", 
                "Peringatan", 
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return false; // Validasi gagal
        }
        return true; // Validasi berhasil
    }
    
    public void insert() {
    // --- TAMBAHKAN PENGAMAN INI ---
    // Cek apakah txtID sedang mati. Kalau mati, berarti user lagi klik data di tabel (mode edit).
    // Kita cegah jangan sampai mereka malah klik Simpan/Insert.
    if (!frmMahasiswa.gettxtID().isEnabled()) {
        JOptionPane.showMessageDialog(frmMahasiswa, 
            "Data ini sudah ada! Gunakan tombol UBAH jika ingin mengupdate.", 
            "Peringatan", JOptionPane.WARNING_MESSAGE);
        return; 
    }

    // 1. Cek validasi dulu
    if (!cekValidasi()) {
        // Teks tidak akan hilang karena return di sini
        return;  
    }

    // 2. Jika validasi lolos, baru jalankan proses insert
    try {
        Mahasiswa b = new Mahasiswa();
        b.setId(Integer.valueOf(frmMahasiswa.gettxtID().getText()));
        b.setNim(frmMahasiswa.gettxtNim().getText());
        b.setNama(frmMahasiswa.gettxtNama().getText());
        b.setJk(frmMahasiswa.getJKel().getSelectedItem().toString());
        b.setAlamat(frmMahasiswa.gettxtAlamat().getText());

        iMahasiswa.insert(b);
        
        // 3. Tampilkan pesan berhasil
        JOptionPane.showMessageDialog(null, "Input berhasil");
        
        // 4. Baru panggil reset dan refresh tabel setelah BENAR-BENAR berhasil
        isiTable();      // Refresh tabel dulu agar data baru muncul
        kondisiAwal();   // kondisiAwal biasanya sudah memanggil reset() dan mengatur tombol
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Gagal simpan: " + e.getMessage());
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
            JOptionPane.showMessageDialog(frmMahasiswa, 
                "Tidak ada perubahan data yang dideteksi!", 
                "Informasi", JOptionPane.INFORMATION_MESSAGE);
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
        JOptionPane.showMessageDialog(frmMahasiswa, 
            "Data belum dipilih dari tabel! Anda tidak bisa menghapus data yang sedang diinput baru.", 
            "Peringatan", JOptionPane.WARNING_MESSAGE);
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
}