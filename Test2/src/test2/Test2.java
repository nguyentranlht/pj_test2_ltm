/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test2;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author PC
 */
public class Test2 {
public static  frmMatHang frmMH=new frmMatHang();
     public static dataBase.MyConnection connection = new  dataBase.MyConnection();

    public static void main(String[] args) {
       frmMH.show();
    }
}
