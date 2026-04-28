/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package DAOInterface;

import Model.Mahasiswa;
import java.util.List;

/**
 *
 * @author ASUS
 */
public interface IDAOMahasiswa {
    //READ DATA
    public List<Mahasiswa> getAll();
    //INSERT DATA
    public void insert(Mahasiswa b);
    //UPDATE DATA
    public void update(Mahasiswa b);
}
