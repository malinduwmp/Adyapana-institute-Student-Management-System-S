package gui;

import com.mysql.cj.protocol.Resultset;
import static gui.Home.subjectMap;
import static gui.Home.teacherMap;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;
import model.MySQL;

public class subjectEnroll extends javax.swing.JFrame {


    public subjectEnroll(HashMap<String, String> subinfo) {
        initComponents();

        String nic = subinfo.get("nic");

        NICLabel.setText(subinfo.get("nic"));
        NAMELabel.setText(subinfo.get("name"));
        loadSubject();
        subjetcTableLoad(nic);
    }

    private void loadSubject() {
        try {
            ResultSet resultSet = MySQL.execute("SELECT * FROM `subject`");

            Vector<String> v = new Vector<>();
            v.add("Select Subject");

            while (resultSet.next()) {
                String subject_name = resultSet.getString("subject_name");
                int subno = resultSet.getInt("subno");
                v.add(subject_name);
                subjectMap.put(subject_name, subno);
            }

            DefaultComboBoxModel<String> model1 = new DefaultComboBoxModel<>(v);
            subjectEnrollCombo.setModel(model1);

        } catch (Exception e) {
        }
    }

    private void subjetcTableLoad(String nic) {

        try {

            ResultSet resultSet = MySQL.execute("SELECT * FROM `student` "
                    + "INNER JOIN `subject_has_student` ON `student`.`sno` = `subject_has_student`.`student_sno` "
                    + "INNER JOIN `subject` ON `subject`.`subno` = `subject_has_student"
                    + "`.`subject_subno` "
                    + "WHERE `student`.`nic` = '" + nic + "'");

            DefaultTableModel model = (DefaultTableModel) subjectEnrollmentTable.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                Vector<Object> v = new Vector<>();

                v.add(resultSet.getString("subno"));
                v.add(resultSet.getString("subject_name"));

                model.addRow(v);
                subjectEnrollmentTable.setModel(model);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkSubjectExistsForStudent(int subjectId, String sno) {
        ResultSet resultSet;
        try {
            resultSet = MySQL.execute("SELECT COUNT(*) FROM `subject_has_student` WHERE `subject_subno` = '" + subjectId + "' AND `student_sno` = '" + sno + "'");

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (Exception ex) {
            Logger.getLogger(subjectEnroll.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        subjectEnrollmentTable = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        NICLabel = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        NAMELabel = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        subjectEnrollCombo = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jPanel30 = new javax.swing.JPanel();
        teacherEnrollCombo = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        subdelete = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(900, 600));
        setResizable(false);

        jPanel1.setPreferredSize(new java.awt.Dimension(800, 80));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Subject Enrollment");
        jPanel1.add(jLabel1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setPreferredSize(new java.awt.Dimension(200, 345));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel4.setPreferredSize(new java.awt.Dimension(200, 40));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jLabel2.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Current Subject");
        jPanel4.add(jLabel2, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel4, java.awt.BorderLayout.PAGE_START);

        subjectEnrollmentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Subject No.", "Subject"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        subjectEnrollmentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                subjectEnrollmentTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(subjectEnrollmentTable);

        jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.LINE_START);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel5.setPreferredSize(new java.awt.Dimension(578, 70));
        jPanel5.setLayout(new java.awt.GridLayout(1, 2));

        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel22.setPreferredSize(new java.awt.Dimension(50, 70));

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 70, Short.MAX_VALUE)
        );

        jPanel7.add(jPanel22, java.awt.BorderLayout.LINE_START);

        jPanel24.setLayout(new java.awt.GridLayout(2, 1));

        jLabel4.setFont(new java.awt.Font("Quicksand", 0, 14)); // NOI18N
        jLabel4.setText("NIC");
        jPanel24.add(jLabel4);

        NICLabel.setFont(new java.awt.Font("Quicksand Medium", 1, 14)); // NOI18N
        NICLabel.setText("............................................");
        jPanel24.add(NICLabel);

        jPanel7.add(jPanel24, java.awt.BorderLayout.CENTER);

        jPanel5.add(jPanel7);

        jPanel8.setLayout(new java.awt.BorderLayout());

        jPanel23.setPreferredSize(new java.awt.Dimension(50, 70));

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 70, Short.MAX_VALUE)
        );

        jPanel8.add(jPanel23, java.awt.BorderLayout.LINE_START);

        jPanel25.setLayout(new java.awt.GridLayout(2, 1));

        jLabel5.setFont(new java.awt.Font("Quicksand", 0, 14)); // NOI18N
        jLabel5.setText("Name");
        jPanel25.add(jLabel5);

        NAMELabel.setFont(new java.awt.Font("Quicksand Medium", 1, 14)); // NOI18N
        NAMELabel.setText("...................................................");
        jPanel25.add(NAMELabel);

        jPanel8.add(jPanel25, java.awt.BorderLayout.CENTER);

        jPanel5.add(jPanel8);

        jPanel3.add(jPanel5, java.awt.BorderLayout.PAGE_START);

        jPanel6.setLayout(new java.awt.BorderLayout());

        jPanel9.setMinimumSize(new java.awt.Dimension(300, 150));
        jPanel9.setPreferredSize(new java.awt.Dimension(645, 150));
        jPanel9.setLayout(new java.awt.BorderLayout());

        jPanel11.setPreferredSize(new java.awt.Dimension(50, 150));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );

        jPanel9.add(jPanel11, java.awt.BorderLayout.LINE_START);

        jPanel12.setPreferredSize(new java.awt.Dimension(50, 150));

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );

        jPanel9.add(jPanel12, java.awt.BorderLayout.LINE_END);

        jPanel13.setPreferredSize(new java.awt.Dimension(445, 150));
        jPanel13.setLayout(new java.awt.BorderLayout());

        jPanel14.setPreferredSize(new java.awt.Dimension(445, 25));

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 545, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );

        jPanel13.add(jPanel14, java.awt.BorderLayout.PAGE_START);

        jPanel15.setPreferredSize(new java.awt.Dimension(445, 25));

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 545, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );

        jPanel13.add(jPanel15, java.awt.BorderLayout.PAGE_END);

        jPanel16.setPreferredSize(new java.awt.Dimension(485, 100));
        jPanel16.setLayout(new java.awt.GridLayout(1, 2));

        jPanel26.setLayout(new java.awt.GridLayout(1, 2));

        jPanel28.setPreferredSize(new java.awt.Dimension(220, 100));

        subjectEnrollCombo.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        subjectEnrollCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subjectEnrollComboActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Quicksand", 0, 14)); // NOI18N
        jLabel3.setText("Select Subject");

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(subjectEnrollCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 144, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel28Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(subjectEnrollCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel26.add(jPanel28);

        jPanel30.setPreferredSize(new java.awt.Dimension(220, 100));

        teacherEnrollCombo.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Quicksand", 0, 14)); // NOI18N
        jLabel6.setText("Select Teacher");

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(teacherEnrollCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 144, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel30Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(teacherEnrollCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel26.add(jPanel30);

        jPanel16.add(jPanel26);

        jPanel13.add(jPanel16, java.awt.BorderLayout.CENTER);

        jPanel9.add(jPanel13, java.awt.BorderLayout.CENTER);

        jPanel6.add(jPanel9, java.awt.BorderLayout.PAGE_START);

        jPanel10.setLayout(new java.awt.BorderLayout());

        jPanel17.setPreferredSize(new java.awt.Dimension(50, 85));

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 89, Short.MAX_VALUE)
        );

        jPanel10.add(jPanel17, java.awt.BorderLayout.LINE_START);

        jPanel18.setPreferredSize(new java.awt.Dimension(50, 85));

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 89, Short.MAX_VALUE)
        );

        jPanel10.add(jPanel18, java.awt.BorderLayout.LINE_END);

        jPanel19.setPreferredSize(new java.awt.Dimension(645, 20));

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 645, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jPanel10.add(jPanel19, java.awt.BorderLayout.PAGE_START);

        jPanel20.setPreferredSize(new java.awt.Dimension(645, 20));

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 645, Short.MAX_VALUE)
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jPanel10.add(jPanel20, java.awt.BorderLayout.PAGE_END);

        jPanel21.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton6.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jButton6.setText("Enroll Subject");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel21.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, 260, 50));

        subdelete.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        subdelete.setText("Remove");
        subdelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subdeleteActionPerformed(evt);
            }
        });
        jPanel21.add(subdelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 20, 120, 50));

        jPanel10.add(jPanel21, java.awt.BorderLayout.CENTER);

        jPanel6.add(jPanel10, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel6, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel3, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed

        String subject = (String) subjectEnrollCombo.getSelectedItem();
        String teacher = (String) teacherEnrollCombo.getSelectedItem();
        String nic = NICLabel.getText();

        try {
            ResultSet resultSet = MySQL.execute("SELECT * FROM `student` WHERE `nic` = '" + nic + "'");

            while (resultSet.next()) {
                String sno = resultSet.getString("sno");
                int subjectId = subjectMap.get(subject);
                int teacherId = teacherMap.get(teacher);

                boolean isSubjectAdded = checkSubjectExistsForStudent(subjectId, sno);

                if (!isSubjectAdded) {
                    MySQL.execute("INSERT INTO `subject_has_student` (`subject_subno`,`student_sno`,`teacher_tno`)"
                            + "VALUES('" + subjectId + "','" + sno + "','" + teacherId + "')");
                } else {

                }
            }
            subjetcTableLoad(nic);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_jButton6ActionPerformed


    private void subdeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subdeleteActionPerformed
        String nic = NICLabel.getText();
        int selectRow = subjectEnrollmentTable.getSelectedRow();
        String sub_id = String.valueOf(subjectEnrollmentTable.getValueAt(selectRow, 0));
        try {

            ResultSet resultSet = MySQL.execute("SELECT * FROM `student` WHERE `nic` = '" + nic + "'");

            while (resultSet.next()) {
                String sno = resultSet.getString("sno");
                MySQL.execute("DELETE FROM `subject_has_student` "
                        + "WHERE `subject_subno` = '" + sub_id + "' AND `student_sno` = '" + sno + "'");
            }

        } catch (Exception ex) {
            Logger.getLogger(subjectEnroll.class.getName()).log(Level.SEVERE, null, ex);
        }

        subjetcTableLoad(nic);

        subjectEnrollmentTable.setEnabled(true);
        jButton6.setEnabled(true);
    }//GEN-LAST:event_subdeleteActionPerformed

    private void subjectEnrollmentTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_subjectEnrollmentTableMouseClicked
        if (evt.getClickCount() == 2) {

            subjectEnrollmentTable.setEnabled(false);
            jButton6.setEnabled(false);

            int selectRow = subjectEnrollmentTable.getSelectedRow();

            String sub_name = String.valueOf(subjectEnrollmentTable.getValueAt(selectRow, 1));

            subjectEnrollCombo.setSelectedItem(sub_name);

        }
    }//GEN-LAST:event_subjectEnrollmentTableMouseClicked

    private void subjectEnrollComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subjectEnrollComboActionPerformed
        String subject = (String) subjectEnrollCombo.getSelectedItem();
        Integer subjectID = subjectMap.get(subject);

        if (subjectID != null) {
            try {

                ResultSet resultset = MySQL.execute("SELECT * FROM `teacher` WHERE `subject_subno`='" + subjectID + "' ");

                Vector<String> v = new Vector<>();
                v.add("Select Teacher");

                while (resultset.next()) {
                    v.add(resultset.getString("f_name") + " " + (resultset.getString("l_name")));

                    int teaNo = resultset.getInt("tno");
                    String teaName = resultset.getString("f_name") + " " + resultset.getString("l_name");
                    teacherMap.put(teaName, teaNo);
                }

                DefaultComboBoxModel model = new DefaultComboBoxModel(v);
                teacherEnrollCombo.setModel(model);

            } catch (Exception ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            teacherEnrollCombo.removeAllItems();
        }


    }//GEN-LAST:event_subjectEnrollComboActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel NAMELabel;
    private javax.swing.JLabel NICLabel;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton subdelete;
    private javax.swing.JComboBox<String> subjectEnrollCombo;
    private javax.swing.JTable subjectEnrollmentTable;
    private javax.swing.JComboBox<String> teacherEnrollCombo;
    // End of variables declaration//GEN-END:variables
}
