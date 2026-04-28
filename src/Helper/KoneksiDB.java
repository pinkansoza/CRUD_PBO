/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Helper;

import com.mysql.cj.jdbc.MysqlDataSource; // Atau driver lain yang kamu pakai
import java.sql.Connection;
import java.sql.SQLException;

public class KoneksiDB {
    static Connection con;

    // METHOD INI YANG DICARI OLEH DAOMahasiswa
    public static Connection getConnection() throws SQLException {
        if (con == null) {
                MysqlDataSource data = new MysqlDataSource();
                data.setDatabaseName("db_crud");
                data.setUser("root");
                data.setPassword("");
                data.setServerName("localhost"); // Tambahkan baris ini
                data.setPortNumber(3307);        // Tambahkan baris ini (port default MySQL)
                data.setServerTimezone("UTC");   // Tambahkan ini jika ada error terkait Timezone
                try {
                   con = data.getConnection();
                   System.out.println("koneksi berhasil");
                }catch(SQLException e){
                    System.out.println("tidak konek");
                }
                   
        }
        return con;
    }

}
