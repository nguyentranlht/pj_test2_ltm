/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test2;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Le Duy
 */
public class frmUser extends javax.swing.JFrame {
    /**
     * Creates new form frmUser
     */
    public frmUser() {
        initComponents();
        JLabel background = new JLabel(new ImageIcon("C:\\Users\\PC\\Documents\\NetBeansProjects\\pj_test2_ltm\\Test2\\src\\Img\\background.jpg"));

        // Đặt layout của JFrame là BorderLayout để có thể đặt hình ảnh làm nền
        setLayout(new BorderLayout());

        // Thêm hình ảnh vào vị trí CENTER của BorderLayout
        add(background, BorderLayout.CENTER);

        // Cài đặt thuộc tính của JFrame
        setTitle("Nhân sự");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        fillTableAdmin();
        fillTableStaff();
        addAdminRowSelectionListener();
        addStaffRowSelectionListener();
    }

    private void addAdminRowSelectionListener() {
        ListSelectionModel rowSelectionModel = tblAdmin.getSelectionModel();
        rowSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        rowSelectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = tblAdmin.getSelectedRow();
                    if (selectedRow != -1) {
                        // Lấy giá trị tại dòng đã chọn
                        Object selectedValue = tblAdmin.getValueAt(selectedRow, 0);
                        // Xử lý sự kiện khi chọn dòng
                        System.out.println("Selected Row: " + selectedRow + ", Value: " + selectedValue);
                        int confirm = JOptionPane.showConfirmDialog(frmUser.this, "Bạn có chắc chắn muốn hủy quyền quản lý", "Xác nhận", JOptionPane.YES_NO_OPTION);
                        if(confirm == JOptionPane.YES_OPTION){
                            // Thực hiện giảm cấp admin thành nhân viên
                        dataBase.MyConnection connection = new  dataBase.MyConnection ();
                        String updateQuery = "UPDATE NHAN_VIEN SET LAQUANLY = 0 WHERE TENDANGNHAP = '" + selectedValue + "'";
                        connection.ExcuteQueryUpdateDB(updateQuery);

                        // Cập nhật bảng
                        DefaultTableModel model = (DefaultTableModel) tblAdmin.getModel();
                        model.removeRow(selectedRow);
                        fillTableStaff();

                        }
                    }
                }
            }

        });
    }

    private void addStaffRowSelectionListener() {
        ListSelectionModel rowSelectionModel = tblStaff.getSelectionModel();
        rowSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        rowSelectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = tblStaff.getSelectedRow();
                    if (selectedRow != -1) {
                        // Lấy giá trị tại dòng đã chọn
                        Object selectedValue = tblStaff.getValueAt(selectedRow, 0);
                        // Xử lý sự kiện khi chọn dòng
                        System.out.println("Selected Row: " + selectedRow + ", Value: " + selectedValue);
                        int confirmed = JOptionPane.showConfirmDialog(frmUser.this, "Bạn có chắc chắn muốn cấp quyền quản lý?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                        if (confirmed == JOptionPane.YES_OPTION) {
                            // Hiển thị dialog xác nhận mật khẩu
                            authDialog.setSize(400, 200);
                            authDialog.setLocationRelativeTo(null);
                            lblWrongAuthPassword.setVisible(false);
                            txtPassword.setText("");
                            authDialog.setVisible(true);
                        }
                    }
                }
            }
        });
    }

    private void fillTableAdmin() {
        try {
            dataBase.MyConnection connection = new  dataBase.MyConnection ();
            String query = "SELECT TENDANGNHAP, TENNHANVIEN, SDT, GIOITINH, NGAYSINH FROM NHAN_VIEN WHERE LAQUANLY = 1";
            ResultSet rs = connection.ExcuteQueryGetTable(query);

            // Lấy thông tin về cấu trúc của ResultSet
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Tạo DefaultTableModel với các cột tương ứng
            DefaultTableModel tableModel = new DefaultTableModel();
            for (int i = 1; i <= columnCount; i++) {
                tableModel.addColumn(metaData.getColumnName(i));
            }

            // Thêm dữ liệu từ ResultSet vào DefaultTableModel
            while (rs.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = rs.getObject(i);
                }
                tableModel.addRow(rowData);
            }

            // Set model cho tblAdmin
            tblAdmin.setModel(tableModel);

            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillTableStaff() {
        try {
            dataBase.MyConnection connection = new  dataBase.MyConnection ();
            String query = "SELECT TENDANGNHAP, TENNHANVIEN, SDT, GIOITINH, NGAYSINH FROM NHAN_VIEN WHERE LAQUANLY = 0";
            ResultSet rs = connection.ExcuteQueryGetTable(query);

            // Lấy thông tin về cấu trúc của ResultSet
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Tạo DefaultTableModel với các cột tương ứng
            DefaultTableModel tableModel = new DefaultTableModel();
            for (int i = 1; i <= columnCount; i++) {
                tableModel.addColumn(metaData.getColumnName(i));
            }

            // Thêm dữ liệu từ ResultSet vào DefaultTableModel
            while (rs.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = rs.getObject(i);
                }
                tableModel.addRow(rowData);
            }

            // Set model cho tblAdmin
            tblStaff.setModel(tableModel);

            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        authDialog = new javax.swing.JDialog();
        txtPassword = new javax.swing.JPasswordField();
        jLabel4 = new javax.swing.JLabel();
        btnConfirm = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblWrongAuthPassword = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAdmin = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblStaff = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btnExit = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        authDialog.setTitle("Xác thực");

        jLabel4.setText("Nhập mật khẩu");

        btnConfirm.setText("Xác nhận");
        btnConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmActionPerformed(evt);
            }
        });

        btnCancel.setText("Hủy bỏ");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        lblWrongAuthPassword.setForeground(new java.awt.Color(255, 0, 0));
        lblWrongAuthPassword.setText("Mật khẩu không chính xác !");

        javax.swing.GroupLayout authDialogLayout = new javax.swing.GroupLayout(authDialog.getContentPane());
        authDialog.getContentPane().setLayout(authDialogLayout);
        authDialogLayout.setHorizontalGroup(
            authDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(authDialogLayout.createSequentialGroup()
                .addGroup(authDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(authDialogLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(authDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(authDialogLayout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(61, 61, 61)
                                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(authDialogLayout.createSequentialGroup()
                                .addGap(54, 54, 54)
                                .addComponent(btnConfirm)
                                .addGap(29, 29, 29)
                                .addComponent(btnCancel))))
                    .addGroup(authDialogLayout.createSequentialGroup()
                        .addGap(144, 144, 144)
                        .addComponent(lblWrongAuthPassword)))
                .addContainerGap(184, Short.MAX_VALUE))
        );
        authDialogLayout.setVerticalGroup(
            authDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(authDialogLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(lblWrongAuthPassword)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(authDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(36, 36, 36)
                .addGroup(authDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnConfirm)
                    .addComponent(btnCancel))
                .addContainerGap(122, Short.MAX_VALUE))
        );

        jLabel5.setText("jLabel5");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(800, 600));
        setSize(new java.awt.Dimension(800, 600));

        tblAdmin.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblAdmin);

        tblStaff.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(tblStaff);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("QUẢN LÝ NHÂN VIÊN");

        btnExit.setText("THOÁT");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Nhân viên quản lý");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("Nhân viên bán hàng");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(299, 299, 299)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(101, 101, 101)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(541, 541, 541)
                                .addComponent(btnExit))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 603, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 603, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(72, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addComponent(btnExit)
                .addContainerGap(117, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmActionPerformed
        // TODO add your handling code here:
        int selectedRow = tblStaff.getSelectedRow();
        Object selectedValue = tblStaff.getValueAt(selectedRow, 0);
        String password = new String(txtPassword.getPassword());
        try {
            dataBase.MyConnection access = new dataBase.MyConnection();
            ResultSet rs = access.ExcuteQueryGetTable("select TENDANGNHAP from NHAN_VIEN where LAQUANLY = 1 AND MATKHAU = '"+password+"'");
            if (rs.next()) {
                dataBase.MyConnection connection = new  dataBase.MyConnection ();
                String updateQuery = "UPDATE NHAN_VIEN SET LAQUANLY = 1 WHERE TENDANGNHAP = '" + selectedValue + "'";
                connection.ExcuteQueryUpdateDB(updateQuery);

                // Cập nhật bảng
                DefaultTableModel model = (DefaultTableModel) tblAdmin.getModel();
                model.removeRow(selectedRow);
                authDialog.dispose();
                fillTableAdmin();
                fillTableStaff();
            }
            else{
                lblWrongAuthPassword.setVisible(true);
                txtPassword.setText("");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnConfirmActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        authDialog.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmUser().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog authDialog;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnConfirm;
    private javax.swing.JButton btnExit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblWrongAuthPassword;
    private javax.swing.JTable tblAdmin;
    private javax.swing.JTable tblStaff;
    private javax.swing.JPasswordField txtPassword;
    // End of variables declaration//GEN-END:variables
}
