/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DAOInterface.IDAOMahasiswa;
import Helper.KoneksiDB;
import Model.Mahasiswa;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

/**
 * @author ASUS
 */
public class DAOMahasiswa implements IDAOMahasiswa {

    Connection con;
    //SQL Query
    String strRead = "select * from tblMahasiswa order by id desc;";
    String strInsert = "insert into tblMahasiswa(id,nim,nama,jk,alamat) values (?,?,?,?,?);";
    String strUpdate = "update tblMahasiswa set nim=?, nama=?, jk=?, alamat=? where id=? ";
    String strDelete = "delete from tblMahasiswa where id=?";
    String strSearch = "select * from tblMahasiswa where nama like ?;";
    

    public DAOMahasiswa() throws SQLException {
        con = KoneksiDB.getConnection();
    }

    @Override
    public List<Mahasiswa> getAll() {
        List<Mahasiswa> lstMhs = new ArrayList<>();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(strRead);
            
            while(rs.next()){
                Mahasiswa mhs = new Mahasiswa();
                mhs.setId(rs.getInt("id"));
                mhs.setNim(rs.getString("nim")); 
                mhs.setNama(rs.getString("nama"));
                mhs.setJk(rs.getString("jk"));
                mhs.setAlamat(rs.getString("alamat"));
                lstMhs.add(mhs);
            }
        } catch(SQLException e) {
            System.out.println("Error saat getAll: " + e.getMessage());
        }
        return lstMhs;
    }

    @Override
    public boolean insert(Mahasiswa b) {
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(strInsert);
            statement.setInt(1, b.getId());
            statement.setString(2, b.getNim());
            statement.setString(3, b.getNama());
            statement.setString(4, b.getJk());
            statement.setString(5, b.getAlamat());

            int rowsAffected = statement.executeUpdate();

            System.out.println("Data Berhasil Disimpan");
            return rowsAffected > 0; // Mengembalikan true jika ada data masuk

        } catch (SQLException ex) {
            // Kita tetap print di console untuk kita baca sendiri kalau ada error
            System.out.println("Gagal Input (DAO): " + ex.getMessage());
            return false; // Mengembalikan false agar Controller tahu ada masalah
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.out.println("Gagal menutup statement: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void update(Mahasiswa b) {
        PreparedStatement statement = null;
    try {
        statement = con.prepareStatement(strUpdate, java.sql.Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, b.getNim());
        statement.setString(2, b.getNama());
        statement.setString(3, b.getJk());
        statement.setString(4, b.getAlamat());
        statement.setInt(5, b.getId());
        statement.executeUpdate();
        
        ResultSet rs = statement.getGeneratedKeys();
        while (rs.next()) {
            b.setId(rs.getInt(1));
        }
        
        System.out.println("Data Berhasil Disimpan");

    } catch (SQLException ex) {
        System.out.println("Gagal Update" + ex.getMessage());
    } finally {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                System.out.println("Gagal menutup statement: " + e.getMessage());
            }
        }
    }
    }

    @Override
    public void delete(int id) {
        PreparedStatement statement = null;
    try {
        statement = con.prepareStatement(strDelete, java.sql.Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, id);
        statement.executeUpdate();
       
    } catch (SQLException ex) {
        System.out.println("Berhasil Delete");
    } finally {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                System.out.println("Gagal Delete: " + e.getMessage());
            }
        }
    }
    
    }

    @Override
    public List<Mahasiswa> getAllByName(String nama) {
        List<Mahasiswa> lstMhs = new ArrayList<>();
        try {
            PreparedStatement st = con.prepareStatement(strSearch);
            st.setString(1, "%"+nama+"%");
            ResultSet rs = st.executeQuery();
            
            while(rs.next()){
                Mahasiswa mhs = new Mahasiswa();
                mhs.setId(rs.getInt("id"));
                mhs.setNim(rs.getString("nim")); 
                mhs.setNama(rs.getString("nama"));
                mhs.setJk(rs.getString("jk"));
                mhs.setAlamat(rs.getString("alamat"));
                lstMhs.add(mhs);
            }
        } catch(SQLException e) {
            System.out.println("Error saat getAll: " + e.getMessage());
        }
        return lstMhs;
    }
}
