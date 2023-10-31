package gui;

import java.awt.List;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import model.MySQL;
import model.classAttendece;

public class Home extends javax.swing.JFrame {

    public static HashMap<String, Integer> subjectMap = new HashMap<>();
    public static HashMap<String, Integer> teacherMap = new HashMap<>();
    public static HashMap<String, classAttendece> classtMap = new HashMap<>();

    public Home() {
        initComponents();
        loadDate();
        loadStudents();
        loadSubject();
        loadteacher();
        loadSubjectTable();
        loadClass();
        loadAttendenceClass();
    }

    private void resetTeacher() {
        teaFnameT.setText("");
        teaLnameT.setText("");
        teaAddressT.setText("");
        teaMobileT.setText("");
        teaNicT.setText("");
        teaEmailT.setText("");
        teaSubT.setSelectedIndex(0);
        TstudentFname.grabFocus();
    }

    private void reset() {
        TstudentFname.setText("");
        TstudentLname.setText("");
        TstudentAddress.setText("");
        TstudentMobile.setText("");
        TstudentNIC.setText("");
        TstuEmail.setText("");
        TstudentFname.grabFocus();
    }

    private void resetSubject() {
        subjectName.setText("");
        subDes.setText("");
        subPrice.setText("");
        subPrice.setText("");
    }

    private void loadDate() {
        Date currentDate = new Date();
        TstudentBday.setDate(currentDate);
    }

    private void loadStudents() {
        try {

            ResultSet resultSet = MySQL.execute("SELECT * FROM `student` " + "INNER JOIN `gender` ON `student`.`gender_gid` = `gender`.`gid` ORDER BY `sno` ASC ");

            DefaultTableModel model = (DefaultTableModel) stuTable.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                Vector<String> v = new Vector();
                v.add(resultSet.getString("sno"));
                v.add(resultSet.getString("f_name") + " " + resultSet.getString("l_name"));
                v.add(resultSet.getString("nic"));
                v.add(resultSet.getString("mobile"));
                v.add(resultSet.getString("email"));
                v.add(resultSet.getString("address"));
                v.add(resultSet.getString("dob"));
                v.add(resultSet.getString("gender.type"));

                model.addRow(v);
                stuTable.setModel(model);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void loadteacher() {
        try {

            ResultSet resultSet = MySQL.execute("SELECT * FROM `teacher` " + "INNER JOIN `gender` ON `teacher`.`gender_gid` = `gender`.`gid` " 
                    + "INNER JOIN `subject` ON `teacher`.`subject_subno` = `subject`.`subno` ORDER BY `tno` ASC ");

            DefaultTableModel model = (DefaultTableModel) teaTable.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                Vector<String> v = new Vector();
                v.add(resultSet.getString("tno"));
                v.add(resultSet.getString("f_name") + " " + resultSet.getString("l_name"));
                v.add(resultSet.getString("nic"));
                v.add(resultSet.getString("mobile"));
                v.add(resultSet.getString("email"));
                v.add(resultSet.getString("address"));
                v.add(resultSet.getString("subject.description"));
                v.add(resultSet.getString("gender.type"));

                model.addRow(v);
                teaTable.setModel(model);
            }

        } catch (Exception e) {
        }
    }

    private void loadClass() {

        try {

            ResultSet resultSet = MySQL.execute("SELECT * FROM `class` "
                    + "INNER JOIN `subject` ON `subject`.`subno` = `class`.`subject_subno` "
                    + "INNER JOIN `teacher` ON `teacher`.`tno` = `class`.`teacher_tno` WHERE `timeslot_start` > CURRENT_TIMESTAMP");

            DefaultTableModel model = (DefaultTableModel) classTable.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                Vector<String> v = new Vector();
                v.add(resultSet.getString("classno"));
                v.add(resultSet.getString("teacher.f_name") + " " + resultSet.getString("teacher.l_name"));
                v.add(resultSet.getString("subject.subject_name"));
                v.add(resultSet.getString("timeslot_start"));
                v.add(resultSet.getString("timeslot_end"));

                model.addRow(v);
                classTable.setModel(model);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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
            teaSubT.setModel(model1);

            DefaultComboBoxModel<String> model2 = new DefaultComboBoxModel<>(v);
            clsRegSubCombo.setModel(model2);

        } catch (Exception e) {
        }
    }

    private void loadAttendenceClass() {
        try {
            ResultSet resultSet = MySQL.execute("SELECT * FROM `class` "
                    + "INNER JOIN `subject` ON `subject`.`subno` = `class`.`subject_subno` "
                    + "INNER JOIN `teacher` ON `teacher`.`tno` = `class`.`teacher_tno` WHERE `timeslot_start` > CURRENT_TIMESTAMP");

            Vector<String> v = new Vector<>();
            v.add("Select Class");

            while (resultSet.next()) {
                classAttendece classInfo = new classAttendece();
                String class_name = resultSet.getString("teacher.f_name") + " " + resultSet.getString("teacher.l_name") + "-" + resultSet.getString("subject.subject_name");
                int classno = resultSet.getInt("classno");
                v.add(class_name);

                classInfo.setTeacherName(resultSet.getString("teacher.f_name") + " " + resultSet.getString("teacher.l_name"));
                classInfo.setSubjectName(resultSet.getString("subject.subject_name"));
                classInfo.setMobile(resultSet.getString("teacher.mobile"));
                classInfo.setEmail(resultSet.getString("teacher.email"));
                classInfo.setClassNo(classno);
                classInfo.setSubjectNo(resultSet.getInt("subject.subno"));

                classtMap.put(class_name, classInfo);
            }

            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(v);
            attendenceClassCombo.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSubjectTable() {

        try {

            ResultSet resultSet = MySQL.execute("SELECT * FROM `subject` ");

            DefaultTableModel model = (DefaultTableModel) subjectTable.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                Vector<Object> v = new Vector<>();

                v.add(resultSet.getString("subno"));
                v.add(resultSet.getString("subject_name"));
                v.add(resultSet.getInt("price"));
                v.add(resultSet.getString("subject_duration"));
                v.add(resultSet.getString("description"));

                model.addRow(v);
                subjectTable.setModel(model);
            }

        } catch (Exception e) {
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        dashBoard = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jPanel35 = new javax.swing.JPanel();
        jPanel36 = new javax.swing.JPanel();
        jPanel37 = new javax.swing.JPanel();
        jPanel38 = new javax.swing.JPanel();
        jPanel34 = new javax.swing.JPanel();
        studentregPanal = new javax.swing.JPanel();
        studentMainPanal = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        studentInputPanal = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        StudentSearchInput = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        studentInputSubPanal = new javax.swing.JPanel();
        inputField1 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        TstudentFname = new javax.swing.JTextField();
        jPanel17 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        TstudentNIC = new javax.swing.JTextField();
        jPanel20 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        TstudentAddress = new javax.swing.JTextField();
        jPanel21 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        rStuMale = new javax.swing.JRadioButton();
        RStuFemale = new javax.swing.JRadioButton();
        inputField2 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        TstudentLname = new javax.swing.JTextField();
        jPanel27 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        TstudentMobile = new javax.swing.JTextField();
        jPanel30 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        TstuEmail = new javax.swing.JTextField();
        jPanel31 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        TstudentBday = new com.toedter.calendar.JDateChooser();
        inputField3 = new javax.swing.JPanel();
        jPanel32 = new javax.swing.JPanel();
        jPanel33 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        stuAddButton = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        studentTablePanal = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        stuTable = new javax.swing.JTable();
        teacherRegPanal = new javax.swing.JPanel();
        studentMainPanal2 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        studentInputPanal2 = new javax.swing.JPanel();
        jPanel89 = new javax.swing.JPanel();
        teaSerchInput = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        studentInputSubPanal2 = new javax.swing.JPanel();
        inputField7 = new javax.swing.JPanel();
        jPanel90 = new javax.swing.JPanel();
        jPanel91 = new javax.swing.JPanel();
        jPanel92 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        teaFnameT = new javax.swing.JTextField();
        jPanel93 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        teaNicT = new javax.swing.JTextField();
        jPanel94 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        teaSubT = new javax.swing.JComboBox<>();
        jPanel95 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        jPanel96 = new javax.swing.JPanel();
        teaMaleR = new javax.swing.JRadioButton();
        teaFemaleR = new javax.swing.JRadioButton();
        inputField8 = new javax.swing.JPanel();
        jPanel97 = new javax.swing.JPanel();
        jPanel98 = new javax.swing.JPanel();
        jPanel99 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        teaLnameT = new javax.swing.JTextField();
        jPanel100 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        teaMobileT = new javax.swing.JTextField();
        jPanel101 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        teaEmailT = new javax.swing.JTextField();
        jPanel102 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        teaAddressT = new javax.swing.JTextField();
        inputField9 = new javax.swing.JPanel();
        jPanel103 = new javax.swing.JPanel();
        jPanel104 = new javax.swing.JPanel();
        jPanel105 = new javax.swing.JPanel();
        jPanel106 = new javax.swing.JPanel();
        jPanel107 = new javax.swing.JPanel();
        teaAddButton = new javax.swing.JButton();
        jPanel108 = new javax.swing.JPanel();
        jPanel109 = new javax.swing.JPanel();
        jButton8 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        studentTablePanal2 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        teaTable = new javax.swing.JTable();
        classRegistration = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel50 = new javax.swing.JPanel();
        jPanel51 = new javax.swing.JPanel();
        jPanel52 = new javax.swing.JPanel();
        jPanel53 = new javax.swing.JPanel();
        jPanel55 = new javax.swing.JPanel();
        jPanel57 = new javax.swing.JPanel();
        jPanel63 = new javax.swing.JPanel();
        jPanel65 = new javax.swing.JPanel();
        jPanel69 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        clsRegSubCombo = new javax.swing.JComboBox<>();
        jPanel70 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        clsReg_TeaCombo = new javax.swing.JComboBox<>();
        jPanel58 = new javax.swing.JPanel();
        jPanel64 = new javax.swing.JPanel();
        jPanel66 = new javax.swing.JPanel();
        jPanel71 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        start_Time = new javax.swing.JSpinner();
        jPanel72 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        End_Time = new javax.swing.JSpinner();
        jPanel56 = new javax.swing.JPanel();
        jPanel67 = new javax.swing.JPanel();
        jPanel68 = new javax.swing.JPanel();
        classAddButton = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jPanel54 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel39 = new javax.swing.JPanel();
        StudentSearchInput1 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        classTable = new javax.swing.JTable();
        subjectRegistration = new javax.swing.JPanel();
        jPanel59 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel60 = new javax.swing.JPanel();
        jPanel61 = new javax.swing.JPanel();
        jPanel73 = new javax.swing.JPanel();
        jPanel74 = new javax.swing.JPanel();
        jPanel75 = new javax.swing.JPanel();
        jPanel76 = new javax.swing.JPanel();
        jPanel77 = new javax.swing.JPanel();
        jPanel78 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        subjectName = new javax.swing.JTextField();
        subDescription = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        subDes = new javax.swing.JTextField();
        jPanel80 = new javax.swing.JPanel();
        jPanel81 = new javax.swing.JPanel();
        jPanel82 = new javax.swing.JPanel();
        jPanel83 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        subPrice = new javax.swing.JTextField();
        jPanel84 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        subDuration = new javax.swing.JTextField();
        jPanel85 = new javax.swing.JPanel();
        jPanel86 = new javax.swing.JPanel();
        jPanel87 = new javax.swing.JPanel();
        subAddButton = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jPanel88 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jPanel43 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        subSearch = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        subjectTable = new javax.swing.JTable();
        jPanel28 = new javax.swing.JPanel();
        jPanel62 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        jPanel40 = new javax.swing.JPanel();
        jPanel42 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        attendenceClassCombo = new javax.swing.JComboBox<>();
        jPanel44 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        attSubject = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        attMobile = new javax.swing.JLabel();
        attEmail = new javax.swing.JLabel();
        attTeacher_name = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jPanel41 = new javax.swing.JPanel();
        jPanel47 = new javax.swing.JPanel();
        attStuSearch = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        attSubmitButton = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        attTable = new javax.swing.JTable();
        jPanel14 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1366, 768));

        jTabbedPane2.setFont(new java.awt.Font("Quicksand", 0, 14)); // NOI18N
        jTabbedPane2.setMinimumSize(new java.awt.Dimension(1366, 768));
        jTabbedPane2.setPreferredSize(new java.awt.Dimension(1366, 768));

        dashBoard.setLayout(new java.awt.BorderLayout());

        jPanel9.setPreferredSize(new java.awt.Dimension(50, 680));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 636, Short.MAX_VALUE)
        );

        dashBoard.add(jPanel9, java.awt.BorderLayout.LINE_START);

        jPanel11.setPreferredSize(new java.awt.Dimension(50, 680));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 636, Short.MAX_VALUE)
        );

        dashBoard.add(jPanel11, java.awt.BorderLayout.LINE_END);

        jPanel12.setPreferredSize(new java.awt.Dimension(1361, 50));

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1366, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        dashBoard.add(jPanel12, java.awt.BorderLayout.PAGE_START);

        jPanel13.setMinimumSize(new java.awt.Dimension(100, 50));
        jPanel13.setPreferredSize(new java.awt.Dimension(1361, 50));

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1366, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        dashBoard.add(jPanel13, java.awt.BorderLayout.PAGE_END);

        jPanel18.setLayout(new java.awt.BorderLayout());

        jPanel29.setPreferredSize(new java.awt.Dimension(1261, 300));
        jPanel29.setLayout(new java.awt.GridLayout(1, 4));

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 316, Short.MAX_VALUE)
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jPanel29.add(jPanel35);

        javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 316, Short.MAX_VALUE)
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jPanel29.add(jPanel36);

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 316, Short.MAX_VALUE)
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jPanel29.add(jPanel37);

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 316, Short.MAX_VALUE)
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jPanel29.add(jPanel38);

        jPanel18.add(jPanel29, java.awt.BorderLayout.PAGE_START);

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1266, Short.MAX_VALUE)
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 336, Short.MAX_VALUE)
        );

        jPanel18.add(jPanel34, java.awt.BorderLayout.CENTER);

        dashBoard.add(jPanel18, java.awt.BorderLayout.CENTER);

        jTabbedPane2.addTab("  DashBoard  ", dashBoard);

        studentregPanal.setLayout(new java.awt.GridLayout(1, 1));

        studentMainPanal.setPreferredSize(new java.awt.Dimension(1366, 768));
        studentMainPanal.setLayout(new javax.swing.BoxLayout(studentMainPanal, javax.swing.BoxLayout.PAGE_AXIS));

        jPanel3.setPreferredSize(new java.awt.Dimension(1361, 100));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jLabel5.setFont(new java.awt.Font("Quicksand", 1, 24)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Student Registration");
        jPanel3.add(jLabel5, java.awt.BorderLayout.CENTER);

        studentMainPanal.add(jPanel3);

        studentInputPanal.setPreferredSize(new java.awt.Dimension(1361, 400));
        studentInputPanal.setLayout(new java.awt.BorderLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(1361, 60));

        StudentSearchInput.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        StudentSearchInput.setPreferredSize(new java.awt.Dimension(7, 25));
        StudentSearchInput.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                StudentSearchInputInputMethodTextChanged(evt);
            }
        });
        StudentSearchInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StudentSearchInputActionPerformed(evt);
            }
        });
        StudentSearchInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                StudentSearchInputKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                StudentSearchInputKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                StudentSearchInputKeyTyped(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Quicksand", 0, 14)); // NOI18N
        jLabel1.setText("Search Student :");
        jLabel1.setToolTipText("");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(StudentSearchInput, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(917, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(StudentSearchInput, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        studentInputPanal.add(jPanel1, java.awt.BorderLayout.PAGE_END);

        studentInputSubPanal.setLayout(new java.awt.GridLayout(1, 3, 0, 10));

        inputField1.setLayout(new java.awt.BorderLayout());

        jPanel10.setPreferredSize(new java.awt.Dimension(75, 386));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 75, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 332, Short.MAX_VALUE)
        );

        inputField1.add(jPanel10, java.awt.BorderLayout.LINE_START);

        jPanel15.setLayout(new java.awt.GridLayout(4, 1, 0, 10));

        jPanel16.setLayout(new java.awt.GridLayout(2, 1));

        jLabel10.setFont(new java.awt.Font("Quicksand", 0, 14)); // NOI18N
        jLabel10.setText("First Name");
        jPanel16.add(jLabel10);

        TstudentFname.setFont(new java.awt.Font("Quicksand", 1, 14)); // NOI18N
        jPanel16.add(TstudentFname);

        jPanel15.add(jPanel16);

        jPanel17.setLayout(new java.awt.GridLayout(2, 1));

        jLabel11.setFont(new java.awt.Font("Quicksand", 0, 14)); // NOI18N
        jLabel11.setText("NIC");
        jPanel17.add(jLabel11);

        TstudentNIC.setFont(new java.awt.Font("Quicksand", 1, 14)); // NOI18N
        TstudentNIC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TstudentNICActionPerformed(evt);
            }
        });
        jPanel17.add(TstudentNIC);

        jPanel15.add(jPanel17);

        jPanel20.setLayout(new java.awt.GridLayout(2, 1));

        jLabel12.setFont(new java.awt.Font("Quicksand", 0, 14)); // NOI18N
        jLabel12.setText("Address");
        jPanel20.add(jLabel12);

        TstudentAddress.setFont(new java.awt.Font("Quicksand", 1, 14)); // NOI18N
        jPanel20.add(TstudentAddress);

        jPanel15.add(jPanel20);

        jPanel21.setLayout(new java.awt.GridLayout(2, 1));

        jLabel13.setFont(new java.awt.Font("Quicksand", 0, 14)); // NOI18N
        jLabel13.setText("Gender");
        jPanel21.add(jLabel13);

        jPanel2.setLayout(new java.awt.GridLayout(1, 2));

        buttonGroup1.add(rStuMale);
        rStuMale.setFont(new java.awt.Font("Quicksand", 0, 14)); // NOI18N
        rStuMale.setSelected(true);
        rStuMale.setText("Male");
        rStuMale.setActionCommand("1");
        rStuMale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rStuMaleActionPerformed(evt);
            }
        });
        jPanel2.add(rStuMale);

        buttonGroup1.add(RStuFemale);
        RStuFemale.setFont(new java.awt.Font("Quicksand", 0, 14)); // NOI18N
        RStuFemale.setText("Female");
        RStuFemale.setActionCommand("2");
        RStuFemale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RStuFemaleActionPerformed(evt);
            }
        });
        jPanel2.add(RStuFemale);

        jPanel21.add(jPanel2);

        jPanel15.add(jPanel21);

        inputField1.add(jPanel15, java.awt.BorderLayout.CENTER);

        studentInputSubPanal.add(inputField1);

        inputField2.setLayout(new java.awt.BorderLayout());

        jPanel23.setPreferredSize(new java.awt.Dimension(75, 386));

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 75, Short.MAX_VALUE)
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 332, Short.MAX_VALUE)
        );

        inputField2.add(jPanel23, java.awt.BorderLayout.LINE_START);

        jPanel25.setLayout(new java.awt.GridLayout(4, 1, 0, 10));

        jPanel26.setLayout(new java.awt.GridLayout(2, 1));

        jLabel14.setFont(new java.awt.Font("Quicksand", 0, 14)); // NOI18N
        jLabel14.setText("Last Name");
        jPanel26.add(jLabel14);

        TstudentLname.setFont(new java.awt.Font("Quicksand", 1, 14)); // NOI18N
        jPanel26.add(TstudentLname);

        jPanel25.add(jPanel26);

        jPanel27.setLayout(new java.awt.GridLayout(2, 1));

        jLabel15.setFont(new java.awt.Font("Quicksand", 0, 14)); // NOI18N
        jLabel15.setText("Mobile");
        jPanel27.add(jLabel15);

        TstudentMobile.setFont(new java.awt.Font("Quicksand", 1, 14)); // NOI18N
        TstudentMobile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TstudentMobileActionPerformed(evt);
            }
        });
        jPanel27.add(TstudentMobile);

        jPanel25.add(jPanel27);

        jPanel30.setLayout(new java.awt.GridLayout(2, 1));

        jLabel16.setFont(new java.awt.Font("Quicksand", 0, 14)); // NOI18N
        jLabel16.setText("Email");
        jPanel30.add(jLabel16);

        TstuEmail.setFont(new java.awt.Font("Quicksand", 1, 14)); // NOI18N
        jPanel30.add(TstuEmail);

        jPanel25.add(jPanel30);

        jPanel31.setLayout(new java.awt.GridLayout(2, 1));

        jLabel17.setFont(new java.awt.Font("Quicksand", 0, 14)); // NOI18N
        jLabel17.setText("Date of Birth");
        jPanel31.add(jLabel17);
        jPanel31.add(TstudentBday);

        jPanel25.add(jPanel31);

        inputField2.add(jPanel25, java.awt.BorderLayout.CENTER);

        studentInputSubPanal.add(inputField2);

        inputField3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel32.setPreferredSize(new java.awt.Dimension(75, 386));

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 75, Short.MAX_VALUE)
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 331, Short.MAX_VALUE)
        );

        inputField3.add(jPanel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 331));

        jPanel33.setLayout(new java.awt.BorderLayout());

        jPanel4.setMinimumSize(new java.awt.Dimension(75, 100));
        jPanel4.setPreferredSize(new java.awt.Dimension(75, 390));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 75, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 331, Short.MAX_VALUE)
        );

        jPanel33.add(jPanel4, java.awt.BorderLayout.LINE_END);

        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        stuAddButton.setBackground(new java.awt.Color(0, 51, 102));
        stuAddButton.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        stuAddButton.setForeground(new java.awt.Color(255, 255, 255));
        stuAddButton.setText("Add Student");
        stuAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stuAddButtonActionPerformed(evt);
            }
        });
        jPanel6.add(stuAddButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 20, 310, 60));

        jPanel5.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, -1, 90));

        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel5.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, -1, 90));

        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel5.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(-37, 200, 340, -1));

        jButton2.setFont(new java.awt.Font("Quicksand", 0, 14)); // NOI18N
        jButton2.setText("Remove");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 220, 90, 40));

        jButton7.setFont(new java.awt.Font("Quicksand", 0, 14)); // NOI18N
        jButton7.setText("Update");
        jButton7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton7MouseReleased(evt);
            }
        });
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 220, -1, 40));

        jButton1.setFont(new java.awt.Font("Quicksand", 0, 14)); // NOI18N
        jButton1.setText("Set Subejct");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 220, 120, 40));

        jPanel33.add(jPanel5, java.awt.BorderLayout.CENTER);

        inputField3.add(jPanel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 0, -1, 331));

        studentInputSubPanal.add(inputField3);

        studentInputPanal.add(studentInputSubPanal, java.awt.BorderLayout.CENTER);

        studentMainPanal.add(studentInputPanal);

        studentTablePanal.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        studentTablePanal.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setPreferredSize(new java.awt.Dimension(0, 500));

        stuTable.setFont(new java.awt.Font("Quicksand", 0, 14)); // NOI18N
        stuTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Student ID", "Full Name", "NIC", "Moibile", "Email", "Address", "Date of Birth", "Gender"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        stuTable.setShowGrid(false);
        stuTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                stuTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(stuTable);

        studentTablePanal.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        studentMainPanal.add(studentTablePanal);

        studentregPanal.add(studentMainPanal);

        jTabbedPane2.addTab("  Student Registration  ", studentregPanal);

        teacherRegPanal.setLayout(new java.awt.GridLayout(1, 1));

        studentMainPanal2.setLayout(new javax.swing.BoxLayout(studentMainPanal2, javax.swing.BoxLayout.PAGE_AXIS));

        jPanel22.setPreferredSize(new java.awt.Dimension(1361, 100));
        jPanel22.setLayout(new java.awt.BorderLayout());

        jLabel34.setFont(new java.awt.Font("Quicksand", 1, 24)); // NOI18N
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel34.setText("Teacher Registration");
        jPanel22.add(jLabel34, java.awt.BorderLayout.CENTER);

        studentMainPanal2.add(jPanel22);

        studentInputPanal2.setPreferredSize(new java.awt.Dimension(1361, 400));
        studentInputPanal2.setLayout(new java.awt.BorderLayout());

        jPanel89.setPreferredSize(new java.awt.Dimension(1361, 60));

        teaSerchInput.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        teaSerchInput.setPreferredSize(new java.awt.Dimension(7, 25));
        teaSerchInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                teaSerchInputActionPerformed(evt);
            }
        });
        teaSerchInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                teaSerchInputKeyReleased(evt);
            }
        });

        jLabel35.setFont(new java.awt.Font("Quicksand", 1, 14)); // NOI18N
        jLabel35.setText("Search Student :");
        jLabel35.setToolTipText("");

        javax.swing.GroupLayout jPanel89Layout = new javax.swing.GroupLayout(jPanel89);
        jPanel89.setLayout(jPanel89Layout);
        jPanel89Layout.setHorizontalGroup(
            jPanel89Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel89Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(teaSerchInput, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(917, Short.MAX_VALUE))
        );
        jPanel89Layout.setVerticalGroup(
            jPanel89Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel89Layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(jPanel89Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(teaSerchInput, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        studentInputPanal2.add(jPanel89, java.awt.BorderLayout.PAGE_END);

        studentInputSubPanal2.setLayout(new java.awt.GridLayout(1, 3, 0, 10));

        inputField7.setLayout(new java.awt.BorderLayout());

        jPanel90.setPreferredSize(new java.awt.Dimension(75, 386));

        javax.swing.GroupLayout jPanel90Layout = new javax.swing.GroupLayout(jPanel90);
        jPanel90.setLayout(jPanel90Layout);
        jPanel90Layout.setHorizontalGroup(
            jPanel90Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 75, Short.MAX_VALUE)
        );
        jPanel90Layout.setVerticalGroup(
            jPanel90Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 333, Short.MAX_VALUE)
        );

        inputField7.add(jPanel90, java.awt.BorderLayout.LINE_START);

        jPanel91.setLayout(new java.awt.GridLayout(4, 1, 0, 10));

        jPanel92.setLayout(new java.awt.GridLayout(2, 1));

        jLabel36.setFont(new java.awt.Font("Quicksand", 0, 14)); // NOI18N
        jLabel36.setText("First Name");
        jPanel92.add(jLabel36);

        teaFnameT.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jPanel92.add(teaFnameT);

        jPanel91.add(jPanel92);

        jPanel93.setLayout(new java.awt.GridLayout(2, 1));

        jLabel37.setFont(new java.awt.Font("Quicksand", 0, 14)); // NOI18N
        jLabel37.setText("NIC");
        jPanel93.add(jLabel37);

        teaNicT.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        teaNicT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                teaNicTActionPerformed(evt);
            }
        });
        jPanel93.add(teaNicT);

        jPanel91.add(jPanel93);

        jPanel94.setLayout(new java.awt.GridLayout(2, 1));

        jLabel38.setFont(new java.awt.Font("Quicksand", 0, 14)); // NOI18N
        jLabel38.setText("Subject");
        jPanel94.add(jLabel38);

        teaSubT.setFont(new java.awt.Font("Quicksand", 0, 16)); // NOI18N
        teaSubT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                teaSubTActionPerformed(evt);
            }
        });
        jPanel94.add(teaSubT);

        jPanel91.add(jPanel94);

        jPanel95.setLayout(new java.awt.GridLayout(2, 1));

        jLabel39.setFont(new java.awt.Font("Quicksand", 0, 15)); // NOI18N
        jLabel39.setText("Gender");
        jPanel95.add(jLabel39);

        jPanel96.setLayout(new java.awt.GridLayout(1, 2));

        buttonGroup1.add(teaMaleR);
        teaMaleR.setFont(new java.awt.Font("Quicksand", 0, 14)); // NOI18N
        teaMaleR.setText("Male");
        teaMaleR.setActionCommand("1");
        teaMaleR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                teaMaleRActionPerformed(evt);
            }
        });
        jPanel96.add(teaMaleR);

        buttonGroup1.add(teaFemaleR);
        teaFemaleR.setFont(new java.awt.Font("Quicksand", 0, 14)); // NOI18N
        teaFemaleR.setText("Female");
        teaFemaleR.setActionCommand("2");
        jPanel96.add(teaFemaleR);

        jPanel95.add(jPanel96);

        jPanel91.add(jPanel95);

        inputField7.add(jPanel91, java.awt.BorderLayout.CENTER);

        studentInputSubPanal2.add(inputField7);

        inputField8.setLayout(new java.awt.BorderLayout());

        jPanel97.setPreferredSize(new java.awt.Dimension(75, 386));

        javax.swing.GroupLayout jPanel97Layout = new javax.swing.GroupLayout(jPanel97);
        jPanel97.setLayout(jPanel97Layout);
        jPanel97Layout.setHorizontalGroup(
            jPanel97Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 75, Short.MAX_VALUE)
        );
        jPanel97Layout.setVerticalGroup(
            jPanel97Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 333, Short.MAX_VALUE)
        );

        inputField8.add(jPanel97, java.awt.BorderLayout.LINE_START);

        jPanel98.setLayout(new java.awt.GridLayout(4, 1, 0, 10));

        jPanel99.setLayout(new java.awt.GridLayout(2, 1));

        jLabel40.setFont(new java.awt.Font("Quicksand", 0, 14)); // NOI18N
        jLabel40.setText("Last Name");
        jPanel99.add(jLabel40);

        teaLnameT.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jPanel99.add(teaLnameT);

        jPanel98.add(jPanel99);

        jPanel100.setLayout(new java.awt.GridLayout(2, 1));

        jLabel41.setFont(new java.awt.Font("Quicksand", 0, 14)); // NOI18N
        jLabel41.setText("Mobile");
        jPanel100.add(jLabel41);

        teaMobileT.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        teaMobileT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                teaMobileTActionPerformed(evt);
            }
        });
        jPanel100.add(teaMobileT);

        jPanel98.add(jPanel100);

        jPanel101.setLayout(new java.awt.GridLayout(2, 1));

        jLabel42.setFont(new java.awt.Font("Quicksand", 0, 14)); // NOI18N
        jLabel42.setText("Email");
        jPanel101.add(jLabel42);

        teaEmailT.setFont(new java.awt.Font("Quicksand", 0, 16)); // NOI18N
        jPanel101.add(teaEmailT);

        jPanel98.add(jPanel101);

        jPanel102.setLayout(new java.awt.GridLayout(2, 1));

        jLabel43.setFont(new java.awt.Font("Quicksand", 0, 14)); // NOI18N
        jLabel43.setText("Address");
        jPanel102.add(jLabel43);
        jPanel102.add(teaAddressT);

        jPanel98.add(jPanel102);

        inputField8.add(jPanel98, java.awt.BorderLayout.CENTER);

        studentInputSubPanal2.add(inputField8);

        inputField9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel103.setPreferredSize(new java.awt.Dimension(75, 386));

        javax.swing.GroupLayout jPanel103Layout = new javax.swing.GroupLayout(jPanel103);
        jPanel103.setLayout(jPanel103Layout);
        jPanel103Layout.setHorizontalGroup(
            jPanel103Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 75, Short.MAX_VALUE)
        );
        jPanel103Layout.setVerticalGroup(
            jPanel103Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 331, Short.MAX_VALUE)
        );

        inputField9.add(jPanel103, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 331));

        jPanel104.setLayout(new java.awt.BorderLayout());

        jPanel105.setMinimumSize(new java.awt.Dimension(75, 100));
        jPanel105.setPreferredSize(new java.awt.Dimension(75, 390));

        javax.swing.GroupLayout jPanel105Layout = new javax.swing.GroupLayout(jPanel105);
        jPanel105.setLayout(jPanel105Layout);
        jPanel105Layout.setHorizontalGroup(
            jPanel105Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 75, Short.MAX_VALUE)
        );
        jPanel105Layout.setVerticalGroup(
            jPanel105Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 331, Short.MAX_VALUE)
        );

        jPanel104.add(jPanel105, java.awt.BorderLayout.LINE_END);

        jPanel106.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel107.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        teaAddButton.setBackground(new java.awt.Color(0, 51, 102));
        teaAddButton.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        teaAddButton.setForeground(new java.awt.Color(255, 255, 255));
        teaAddButton.setText("Add Teacher");
        teaAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                teaAddButtonActionPerformed(evt);
            }
        });
        jPanel107.add(teaAddButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 20, 310, 60));

        jPanel106.add(jPanel107, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, -1, 90));

        jPanel108.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel106.add(jPanel108, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, -1, 90));

        jPanel109.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel106.add(jPanel109, new org.netbeans.lib.awtextra.AbsoluteConstraints(-37, 200, 340, -1));

        jButton8.setFont(new java.awt.Font("Quicksand", 0, 14)); // NOI18N
        jButton8.setText("Remove");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel106.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 220, 130, 40));

        jButton13.setFont(new java.awt.Font("Quicksand", 0, 14)); // NOI18N
        jButton13.setText("Update");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        jPanel106.add(jButton13, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 220, 130, 40));

        jPanel104.add(jPanel106, java.awt.BorderLayout.CENTER);

        inputField9.add(jPanel104, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 0, -1, 331));

        studentInputSubPanal2.add(inputField9);

        studentInputPanal2.add(studentInputSubPanal2, java.awt.BorderLayout.CENTER);

        studentMainPanal2.add(studentInputPanal2);

        studentTablePanal2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        studentTablePanal2.setLayout(new java.awt.GridLayout(1, 1));

        teaTable.setFont(new java.awt.Font("Quicksand", 0, 14)); // NOI18N
        teaTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Teacher No.", "Full Name", "NIC", "Moibile", "Email", "Address", "Subject", "Gender"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, false, false, true, true, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        teaTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                teaTableMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(teaTable);

        studentTablePanal2.add(jScrollPane5);

        studentMainPanal2.add(studentTablePanal2);

        teacherRegPanal.add(studentMainPanal2);

        jTabbedPane2.addTab("  Teacher Registration  ", teacherRegPanal);

        classRegistration.setLayout(new java.awt.BorderLayout());

        jPanel19.setPreferredSize(new java.awt.Dimension(1361, 70));
        jPanel19.setLayout(new java.awt.BorderLayout());

        jLabel3.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Class Registration");
        jPanel19.add(jLabel3, java.awt.BorderLayout.CENTER);

        classRegistration.add(jPanel19, java.awt.BorderLayout.PAGE_START);

        jPanel50.setLayout(new java.awt.BorderLayout());

        jPanel51.setPreferredSize(new java.awt.Dimension(1361, 300));
        jPanel51.setLayout(new java.awt.BorderLayout());

        jPanel52.setPreferredSize(new java.awt.Dimension(1361, 30));

        javax.swing.GroupLayout jPanel52Layout = new javax.swing.GroupLayout(jPanel52);
        jPanel52.setLayout(jPanel52Layout);
        jPanel52Layout.setHorizontalGroup(
            jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel52Layout.setVerticalGroup(
            jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel51.add(jPanel52, java.awt.BorderLayout.PAGE_END);

        jPanel53.setPreferredSize(new java.awt.Dimension(900, 320));
        jPanel53.setLayout(new java.awt.BorderLayout());

        jPanel55.setPreferredSize(new java.awt.Dimension(900, 180));
        jPanel55.setLayout(new java.awt.GridLayout(1, 2));

        jPanel57.setLayout(new java.awt.BorderLayout());

        jPanel63.setPreferredSize(new java.awt.Dimension(50, 200));

        javax.swing.GroupLayout jPanel63Layout = new javax.swing.GroupLayout(jPanel63);
        jPanel63.setLayout(jPanel63Layout);
        jPanel63Layout.setHorizontalGroup(
            jPanel63Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        jPanel63Layout.setVerticalGroup(
            jPanel63Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 180, Short.MAX_VALUE)
        );

        jPanel57.add(jPanel63, java.awt.BorderLayout.LINE_END);

        jPanel65.setLayout(new java.awt.GridLayout(2, 1, 0, 10));

        jPanel69.setLayout(new java.awt.GridLayout(2, 1));

        jLabel9.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel9.setText("Select Subject");
        jPanel69.add(jLabel9);

        clsRegSubCombo.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        clsRegSubCombo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                clsRegSubComboMouseClicked(evt);
            }
        });
        clsRegSubCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clsRegSubComboActionPerformed(evt);
            }
        });
        jPanel69.add(clsRegSubCombo);

        jPanel65.add(jPanel69);

        jPanel70.setLayout(new java.awt.GridLayout(2, 1));

        jLabel26.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel26.setText("Select Teacher");
        jPanel70.add(jLabel26);

        clsReg_TeaCombo.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        clsReg_TeaCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clsReg_TeaComboActionPerformed(evt);
            }
        });
        jPanel70.add(clsReg_TeaCombo);

        jPanel65.add(jPanel70);

        jPanel57.add(jPanel65, java.awt.BorderLayout.CENTER);

        jPanel55.add(jPanel57);

        jPanel58.setLayout(new java.awt.BorderLayout());

        jPanel64.setPreferredSize(new java.awt.Dimension(50, 200));

        javax.swing.GroupLayout jPanel64Layout = new javax.swing.GroupLayout(jPanel64);
        jPanel64.setLayout(jPanel64Layout);
        jPanel64Layout.setHorizontalGroup(
            jPanel64Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        jPanel64Layout.setVerticalGroup(
            jPanel64Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 180, Short.MAX_VALUE)
        );

        jPanel58.add(jPanel64, java.awt.BorderLayout.LINE_END);

        jPanel66.setLayout(new java.awt.GridLayout(2, 1));

        jPanel71.setLayout(new java.awt.GridLayout(2, 1));

        jLabel27.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel27.setText("Class Start Time");
        jPanel71.add(jLabel27);

        start_Time.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        start_Time.setModel(new javax.swing.SpinnerDateModel());
        jPanel71.add(start_Time);

        jPanel66.add(jPanel71);

        jPanel72.setLayout(new java.awt.GridLayout(2, 1));

        jLabel28.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel28.setText("Class End Time");
        jPanel72.add(jLabel28);

        End_Time.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        End_Time.setModel(new javax.swing.SpinnerDateModel());
        jPanel72.add(End_Time);

        jPanel66.add(jPanel72);

        jPanel58.add(jPanel66, java.awt.BorderLayout.CENTER);

        jPanel55.add(jPanel58);

        jPanel53.add(jPanel55, java.awt.BorderLayout.PAGE_START);

        jPanel56.setLayout(new java.awt.BorderLayout());

        jPanel67.setMinimumSize(new java.awt.Dimension(100, 50));
        jPanel67.setPreferredSize(new java.awt.Dimension(900, 20));

        javax.swing.GroupLayout jPanel67Layout = new javax.swing.GroupLayout(jPanel67);
        jPanel67.setLayout(jPanel67Layout);
        jPanel67Layout.setHorizontalGroup(
            jPanel67Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 900, Short.MAX_VALUE)
        );
        jPanel67Layout.setVerticalGroup(
            jPanel67Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        jPanel56.add(jPanel67, java.awt.BorderLayout.PAGE_START);

        jPanel68.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        classAddButton.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        classAddButton.setText("ADD CLASS");
        classAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                classAddButtonActionPerformed(evt);
            }
        });
        jPanel68.add(classAddButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 0, 230, 50));

        jButton12.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton12.setText("REMOVE CLASS");
        jButton12.setMargin(new java.awt.Insets(2, 20, 2, 20));
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        jPanel68.add(jButton12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 170, 50));

        jPanel56.add(jPanel68, java.awt.BorderLayout.PAGE_END);

        jPanel53.add(jPanel56, java.awt.BorderLayout.CENTER);

        jPanel51.add(jPanel53, java.awt.BorderLayout.LINE_END);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/presentation.png"))); // NOI18N

        javax.swing.GroupLayout jPanel54Layout = new javax.swing.GroupLayout(jPanel54);
        jPanel54.setLayout(jPanel54Layout);
        jPanel54Layout.setHorizontalGroup(
            jPanel54Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel54Layout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(103, Short.MAX_VALUE))
        );
        jPanel54Layout.setVerticalGroup(
            jPanel54Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel51.add(jPanel54, java.awt.BorderLayout.CENTER);

        jPanel39.setPreferredSize(new java.awt.Dimension(1361, 45));

        StudentSearchInput1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        StudentSearchInput1.setPreferredSize(new java.awt.Dimension(7, 25));
        StudentSearchInput1.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                StudentSearchInput1InputMethodTextChanged(evt);
            }
        });
        StudentSearchInput1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StudentSearchInput1ActionPerformed(evt);
            }
        });
        StudentSearchInput1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                StudentSearchInput1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                StudentSearchInput1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                StudentSearchInput1KeyTyped(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Quicksand", 0, 14)); // NOI18N
        jLabel7.setText("Search Class :");
        jLabel7.setToolTipText("");

        javax.swing.GroupLayout jPanel39Layout = new javax.swing.GroupLayout(jPanel39);
        jPanel39.setLayout(jPanel39Layout);
        jPanel39Layout.setHorizontalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel39Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(StudentSearchInput1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(917, Short.MAX_VALUE))
        );
        jPanel39Layout.setVerticalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel39Layout.createSequentialGroup()
                .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(StudentSearchInput1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 11, Short.MAX_VALUE))
        );

        jPanel51.add(jPanel39, java.awt.BorderLayout.PAGE_END);

        jPanel50.add(jPanel51, java.awt.BorderLayout.PAGE_START);

        classTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Class No", "Teacher", "Subject", "Start Time", "End Time"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        classTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                classTableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(classTable);

        jPanel50.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        classRegistration.add(jPanel50, java.awt.BorderLayout.CENTER);

        jTabbedPane2.addTab("  Class Registration  ", classRegistration);

        subjectRegistration.setLayout(new java.awt.BorderLayout());

        jPanel59.setPreferredSize(new java.awt.Dimension(1361, 70));
        jPanel59.setLayout(new java.awt.BorderLayout());

        jLabel4.setFont(new java.awt.Font("Quicksand", 1, 24)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Subject Registration");
        jPanel59.add(jLabel4, java.awt.BorderLayout.CENTER);

        subjectRegistration.add(jPanel59, java.awt.BorderLayout.PAGE_START);

        jPanel60.setLayout(new java.awt.BorderLayout());

        jPanel61.setPreferredSize(new java.awt.Dimension(1361, 300));
        jPanel61.setLayout(new java.awt.BorderLayout());

        jPanel73.setPreferredSize(new java.awt.Dimension(900, 320));
        jPanel73.setLayout(new java.awt.BorderLayout());

        jPanel74.setPreferredSize(new java.awt.Dimension(900, 180));
        jPanel74.setLayout(new java.awt.GridLayout(1, 2));

        jPanel75.setLayout(new java.awt.BorderLayout());

        jPanel76.setPreferredSize(new java.awt.Dimension(50, 200));

        javax.swing.GroupLayout jPanel76Layout = new javax.swing.GroupLayout(jPanel76);
        jPanel76.setLayout(jPanel76Layout);
        jPanel76Layout.setHorizontalGroup(
            jPanel76Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        jPanel76Layout.setVerticalGroup(
            jPanel76Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 180, Short.MAX_VALUE)
        );

        jPanel75.add(jPanel76, java.awt.BorderLayout.LINE_END);

        jPanel77.setLayout(new java.awt.GridLayout(2, 1, 0, 10));

        jPanel78.setLayout(new java.awt.GridLayout(2, 1));

        jLabel29.setFont(new java.awt.Font("Quicksand", 1, 14)); // NOI18N
        jLabel29.setText("Subject Name");
        jPanel78.add(jLabel29);

        subjectName.setFont(new java.awt.Font("Quicksand", 0, 16)); // NOI18N
        jPanel78.add(subjectName);

        jPanel77.add(jPanel78);

        subDescription.setLayout(new java.awt.GridLayout(2, 1));

        jLabel30.setFont(new java.awt.Font("Quicksand", 1, 14)); // NOI18N
        jLabel30.setText("Description");
        subDescription.add(jLabel30);

        subDes.setFont(new java.awt.Font("Quicksand", 0, 16)); // NOI18N
        subDescription.add(subDes);

        jPanel77.add(subDescription);

        jPanel75.add(jPanel77, java.awt.BorderLayout.CENTER);

        jPanel74.add(jPanel75);

        jPanel80.setLayout(new java.awt.BorderLayout());

        jPanel81.setPreferredSize(new java.awt.Dimension(50, 200));

        javax.swing.GroupLayout jPanel81Layout = new javax.swing.GroupLayout(jPanel81);
        jPanel81.setLayout(jPanel81Layout);
        jPanel81Layout.setHorizontalGroup(
            jPanel81Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        jPanel81Layout.setVerticalGroup(
            jPanel81Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 180, Short.MAX_VALUE)
        );

        jPanel80.add(jPanel81, java.awt.BorderLayout.LINE_END);

        jPanel82.setLayout(new java.awt.GridLayout(2, 1));

        jPanel83.setLayout(new java.awt.GridLayout(2, 1));

        jLabel31.setFont(new java.awt.Font("Quicksand", 1, 14)); // NOI18N
        jLabel31.setText("Price ");
        jPanel83.add(jLabel31);

        subPrice.setFont(new java.awt.Font("Quicksand", 0, 16)); // NOI18N
        jPanel83.add(subPrice);

        jPanel82.add(jPanel83);

        jPanel84.setLayout(new java.awt.GridLayout(2, 1));

        jLabel32.setFont(new java.awt.Font("Quicksand", 1, 14)); // NOI18N
        jLabel32.setText("Subject Duration (Hrs)");
        jPanel84.add(jLabel32);

        subDuration.setFont(new java.awt.Font("Quicksand", 0, 16)); // NOI18N
        jPanel84.add(subDuration);

        jPanel82.add(jPanel84);

        jPanel80.add(jPanel82, java.awt.BorderLayout.CENTER);

        jPanel74.add(jPanel80);

        jPanel73.add(jPanel74, java.awt.BorderLayout.PAGE_START);

        jPanel85.setLayout(new java.awt.BorderLayout());

        jPanel86.setMinimumSize(new java.awt.Dimension(100, 50));
        jPanel86.setPreferredSize(new java.awt.Dimension(900, 20));

        javax.swing.GroupLayout jPanel86Layout = new javax.swing.GroupLayout(jPanel86);
        jPanel86.setLayout(jPanel86Layout);
        jPanel86Layout.setHorizontalGroup(
            jPanel86Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 900, Short.MAX_VALUE)
        );
        jPanel86Layout.setVerticalGroup(
            jPanel86Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        jPanel85.add(jPanel86, java.awt.BorderLayout.PAGE_START);

        jPanel87.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        subAddButton.setFont(new java.awt.Font("Quicksand", 1, 14)); // NOI18N
        subAddButton.setText("ADD SUBJECT");
        subAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subAddButtonActionPerformed(evt);
            }
        });
        jPanel87.add(subAddButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 0, 260, 50));

        jButton14.setFont(new java.awt.Font("Quicksand", 1, 14)); // NOI18N
        jButton14.setText("UPDATE SUBJECT");
        jButton14.setMargin(new java.awt.Insets(2, 20, 2, 20));
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });
        jPanel87.add(jButton14, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 0, 180, 50));

        jButton15.setFont(new java.awt.Font("Quicksand", 1, 14)); // NOI18N
        jButton15.setText("REMOVE SUBJECT");
        jButton15.setMargin(new java.awt.Insets(2, 20, 2, 20));
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });
        jPanel87.add(jButton15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 180, 50));

        jPanel85.add(jPanel87, java.awt.BorderLayout.CENTER);

        jPanel73.add(jPanel85, java.awt.BorderLayout.CENTER);

        jPanel61.add(jPanel73, java.awt.BorderLayout.LINE_END);

        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/books.png"))); // NOI18N

        javax.swing.GroupLayout jPanel88Layout = new javax.swing.GroupLayout(jPanel88);
        jPanel88.setLayout(jPanel88Layout);
        jPanel88Layout.setHorizontalGroup(
            jPanel88Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel88Layout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(103, Short.MAX_VALUE))
        );
        jPanel88Layout.setVerticalGroup(
            jPanel88Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel61.add(jPanel88, java.awt.BorderLayout.CENTER);

        jPanel43.setPreferredSize(new java.awt.Dimension(1361, 45));

        jLabel19.setFont(new java.awt.Font("Quicksand", 0, 14)); // NOI18N
        jLabel19.setText("Search Subject :");
        jLabel19.setToolTipText("");

        subSearch.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        subSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subSearchActionPerformed(evt);
            }
        });
        subSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                subSearchKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel43Layout = new javax.swing.GroupLayout(jPanel43);
        jPanel43.setLayout(jPanel43Layout);
        jPanel43Layout.setHorizontalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel43Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(917, Short.MAX_VALUE))
        );
        jPanel43Layout.setVerticalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel43Layout.createSequentialGroup()
                .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(subSearch, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                .addGap(0, 12, Short.MAX_VALUE))
        );

        jPanel61.add(jPanel43, java.awt.BorderLayout.PAGE_END);

        jPanel60.add(jPanel61, java.awt.BorderLayout.PAGE_START);

        subjectTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Subject No", "Subject Name", "Price", "Duration", "Description"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        subjectTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                subjectTableMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(subjectTable);

        jPanel60.add(jScrollPane4, java.awt.BorderLayout.CENTER);

        subjectRegistration.add(jPanel60, java.awt.BorderLayout.CENTER);

        jTabbedPane2.addTab("  Insert Subject  ", subjectRegistration);

        jPanel28.setLayout(new java.awt.BorderLayout());

        jPanel62.setPreferredSize(new java.awt.Dimension(1361, 70));
        jPanel62.setLayout(new java.awt.BorderLayout());

        jLabel6.setFont(new java.awt.Font("Quicksand", 1, 24)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Student Attendence");
        jPanel62.add(jLabel6, java.awt.BorderLayout.CENTER);

        jPanel28.add(jPanel62, java.awt.BorderLayout.PAGE_START);

        jPanel24.setLayout(new java.awt.BorderLayout());

        jPanel40.setPreferredSize(new java.awt.Dimension(1361, 150));
        jPanel40.setLayout(new java.awt.BorderLayout());

        jPanel42.setPreferredSize(new java.awt.Dimension(500, 150));

        jLabel20.setFont(new java.awt.Font("Quicksand", 1, 16)); // NOI18N
        jLabel20.setText("Select Class :");

        attendenceClassCombo.setFont(new java.awt.Font("Quicksand", 0, 16)); // NOI18N
        attendenceClassCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                attendenceClassComboItemStateChanged(evt);
            }
        });
        attendenceClassCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                attendenceClassComboActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel42Layout = new javax.swing.GroupLayout(jPanel42);
        jPanel42.setLayout(jPanel42Layout);
        jPanel42Layout.setHorizontalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel42Layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(attendenceClassCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel42Layout.setVerticalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel42Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(attendenceClassCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(44, Short.MAX_VALUE))
        );

        jPanel40.add(jPanel42, java.awt.BorderLayout.LINE_START);

        jLabel21.setFont(new java.awt.Font("Quicksand", 1, 16)); // NOI18N
        jLabel21.setText("Class Details");

        attSubject.setFont(new java.awt.Font("Quicksand", 1, 16)); // NOI18N
        attSubject.setText(".....................");

        jLabel25.setFont(new java.awt.Font("Quicksand", 1, 16)); // NOI18N
        jLabel25.setText("Subject :");

        jLabel44.setFont(new java.awt.Font("Quicksand", 1, 16)); // NOI18N
        jLabel44.setText("Email");

        jLabel45.setFont(new java.awt.Font("Quicksand", 1, 16)); // NOI18N
        jLabel45.setText("Mobile");

        attMobile.setFont(new java.awt.Font("Quicksand", 1, 16)); // NOI18N
        attMobile.setText(".....................");

        attEmail.setFont(new java.awt.Font("Quicksand", 1, 16)); // NOI18N
        attEmail.setText(".....................");

        attTeacher_name.setFont(new java.awt.Font("Quicksand", 1, 16)); // NOI18N
        attTeacher_name.setText(".....................");

        jLabel49.setFont(new java.awt.Font("Quicksand", 1, 16)); // NOI18N
        jLabel49.setText("Teacher Name :");

        javax.swing.GroupLayout jPanel44Layout = new javax.swing.GroupLayout(jPanel44);
        jPanel44.setLayout(jPanel44Layout);
        jPanel44Layout.setHorizontalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel44Layout.createSequentialGroup()
                .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel44Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel44Layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel49)
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(36, 36, 36)
                        .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(attSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(attTeacher_name, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel45, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel44, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(36, 36, 36)
                        .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(attEmail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(attMobile, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(118, Short.MAX_VALUE))
        );
        jPanel44Layout.setVerticalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel44Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel44Layout.createSequentialGroup()
                        .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel44)
                            .addComponent(attEmail))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel45)
                            .addComponent(attMobile)))
                    .addGroup(jPanel44Layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel49)
                            .addComponent(attTeacher_name))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(attSubject))))
                .addGap(32, 32, 32))
        );

        jPanel40.add(jPanel44, java.awt.BorderLayout.CENTER);

        jPanel24.add(jPanel40, java.awt.BorderLayout.PAGE_START);

        jPanel41.setPreferredSize(new java.awt.Dimension(1361, 400));
        jPanel41.setLayout(new java.awt.BorderLayout());

        jPanel47.setPreferredSize(new java.awt.Dimension(1361, 70));

        attStuSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                attStuSearchKeyReleased(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Quicksand", 1, 14)); // NOI18N
        jLabel18.setText("Search Student :");

        attSubmitButton.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        attSubmitButton.setText(" Submit");
        attSubmitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                attSubmitButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel47Layout = new javax.swing.GroupLayout(jPanel47);
        jPanel47.setLayout(jPanel47Layout);
        jPanel47Layout.setHorizontalGroup(
            jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel47Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(attStuSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 749, Short.MAX_VALUE)
                .addComponent(attSubmitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        jPanel47Layout.setVerticalGroup(
            jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel47Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel47Layout.createSequentialGroup()
                        .addComponent(attSubmitButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(5, 5, 5))
                    .addGroup(jPanel47Layout.createSequentialGroup()
                        .addGap(0, 17, Short.MAX_VALUE)
                        .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(attStuSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18))))
                .addGap(11, 11, 11))
        );

        jPanel41.add(jPanel47, java.awt.BorderLayout.PAGE_START);

        attTable.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        attTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Student ID", "NIC", "Student Name", "Date", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(attTable);
        if (attTable.getColumnModel().getColumnCount() > 0) {
            attTable.getColumnModel().getColumn(1).setResizable(false);
            attTable.getColumnModel().getColumn(4).setResizable(false);
        }

        jPanel41.add(jScrollPane6, java.awt.BorderLayout.CENTER);

        jPanel24.add(jPanel41, java.awt.BorderLayout.CENTER);

        jPanel28.add(jPanel24, java.awt.BorderLayout.CENTER);

        jTabbedPane2.addTab("  Student Attendence  ", jPanel28);

        jPanel14.setLayout(new java.awt.BorderLayout());

        jLabel2.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("ADYAPANA INSTITUTE  -  STUDENT MANAGEMENT SYSTEM");
        jLabel2.setPreferredSize(new java.awt.Dimension(639, 30));
        jPanel14.add(jLabel2, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 712, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        if (!stuTable.isEnabled()) {
            int selectRow = stuTable.getSelectedRow();

            if (selectRow == -1) {
                JOptionPane.showMessageDialog(null, "Please Select Student", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {

                int result = JOptionPane.showOptionDialog(
                        null,
                        "Do you want to continue?",
                        "Confirmation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        null,
                        null);
                if (result == JOptionPane.YES_OPTION) {
                    String stu_id = String.valueOf(stuTable.getValueAt(selectRow, 0));
                    try {

                        MySQL.execute(" DELETE FROM `s_attendance` WHERE `student_sno` = '" + stu_id + "'");
                        MySQL.execute(" DELETE FROM `subject_has_student` WHERE `student_sno` = '" + stu_id + "'");
                        MySQL.execute(" DELETE FROM `student` WHERE `sno` = '" + stu_id + "'");

                        reset();
                        loadStudents();

                        stuTable.setEnabled(true);
                        stuAddButton.setEnabled(true);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (result == JOptionPane.NO_OPTION) {
                    // User clicked No
                    stuTable.setEnabled(true);
                    stuAddButton.setEnabled(true);
                } else {
                    // User closed the dialog or clicked on the close button
                    stuTable.setEnabled(true);
                    stuAddButton.setEnabled(true);
                }
            }
        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void TstudentMobileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TstudentMobileActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TstudentMobileActionPerformed

    private void rStuMaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rStuMaleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rStuMaleActionPerformed

    private void TstudentNICActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TstudentNICActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TstudentNICActionPerformed

    private void StudentSearchInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StudentSearchInputActionPerformed


    }//GEN-LAST:event_StudentSearchInputActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed

        if (!stuTable.isEnabled()) {

            String stuFname = TstudentFname.getText();
            String stuLname = TstudentLname.getText();
            String stuMobile = TstudentMobile.getText();
            String stuNIC = TstudentNIC.getText();
            String stuAddress = TstudentAddress.getText();
            String stuEmail = TstuEmail.getText();

            java.util.Date selectedDate = TstudentBday.getDate();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String stuBday = formatter.format(selectedDate);

            ButtonModel Gender = buttonGroup1.getSelection();

            int selectRow = stuTable.getSelectedRow();

            if (selectRow == -1) {
                JOptionPane.showMessageDialog(null, "Please Select Student", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {

                if (stuFname.isEmpty()) {

                    JOptionPane.showMessageDialog(null, "Invalid First Name", "Warning", JOptionPane.WARNING_MESSAGE);
                } else if (stuLname.isEmpty()) {

                    JOptionPane.showMessageDialog(null, "Invalid Last Name", "Warning", JOptionPane.WARNING_MESSAGE);

                } else if (stuNIC.isEmpty()) {

                    JOptionPane.showMessageDialog(null, "Please Enter NIC", "Warning", JOptionPane.WARNING_MESSAGE);

                } else if (!stuNIC.matches("\\d{12}") && !stuNIC.matches("\\d{9}[VX]")) {

                    JOptionPane.showMessageDialog(null, "Invalid NIC", "Warning", JOptionPane.WARNING_MESSAGE);

                } else if (stuMobile.isEmpty()) {

                    JOptionPane.showMessageDialog(null, "Please Enter Student Mobile Number", "Warning", JOptionPane.WARNING_MESSAGE);

                } else if (!stuMobile.matches("^(0|94)\\d{9}$")) {
                    JOptionPane.showMessageDialog(null, "Invalid Mobile Number", "Warning", JOptionPane.WARNING_MESSAGE);

                } else if (stuAddress.isEmpty()) {

                    JOptionPane.showMessageDialog(null, "Please Enter Student Address", "Warning", JOptionPane.WARNING_MESSAGE);

                } else if (stuEmail.isEmpty()) {

                    JOptionPane.showMessageDialog(null, "Please Enter Student Email", "Warning", JOptionPane.WARNING_MESSAGE);

                } else if (!stuEmail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                    JOptionPane.showMessageDialog(null, "Invalid Email Address", "Warning", JOptionPane.WARNING_MESSAGE);

                } else {

                    String genderID = Gender.getActionCommand();

                    String stu_id = String.valueOf(stuTable.getValueAt(selectRow, 0));
                    try {
                        MySQL.execute("UPDATE `student` SET "
                                + "`f_name`='" + stuFname + "',"
                                + "`l_name` = '" + stuLname + "',"
                                + "`nic` = '" + stuNIC + "',"
                                + "`address` = '" + stuAddress + "',"
                                + "`dob`='" + stuBday + "',"
                                + "`email`='" + stuEmail + "',"
                                + "`gender_gid`='" + genderID + "',"
                                + "`mobile` = '" + stuMobile + "' "
                                + "WHERE `sno` ='" + stu_id + "'");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    reset();
                    loadStudents();
                    stuTable.setEnabled(true);
                    stuAddButton.setEnabled(true);
                    JOptionPane.showMessageDialog(null, "Done", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void teaSerchInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teaSerchInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_teaSerchInputActionPerformed

    private void teaNicTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teaNicTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_teaNicTActionPerformed

    private void teaMaleRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teaMaleRActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_teaMaleRActionPerformed

    private void teaMobileTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teaMobileTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_teaMobileTActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed

        if (!teaTable.isEnabled()) {
            int selectRow = teaTable.getSelectedRow();

            if (selectRow == -1) {
                JOptionPane.showMessageDialog(null, "Please Select Teacher", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {

                int result = JOptionPane.showOptionDialog(
                        null,
                        "Do you want to continue?",
                        "Confirmation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        null,
                        null);
                if (result == JOptionPane.YES_OPTION) {
                    String tea_id = String.valueOf(teaTable.getValueAt(selectRow, 0));
                    try {

                        MySQL.execute(" DELETE FROM `class` WHERE `teacher_tno` = '" + tea_id + "'");
                        MySQL.execute(" DELETE FROM `teacher` WHERE `tno` = '" + tea_id + "'");

                        resetTeacher();
                        loadteacher();

                        teaTable.setEnabled(true);
                        teaAddButton.setEnabled(true);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (result == JOptionPane.NO_OPTION) {
                    // User clicked No
                    teaTable.setEnabled(true);
                    teaAddButton.setEnabled(true);
                } else {
                    // User closed the dialog or clicked on the close button
                    teaTable.setEnabled(true);
                    teaAddButton.setEnabled(true);
                }
            }
        }

    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed

        String teaFname = teaFnameT.getText();
        String teaLname = teaLnameT.getText();
        String teaMobile = teaMobileT.getText();
        String teaNIC = teaNicT.getText();
        String teaAddress = teaAddressT.getText();
        String teaEmail = teaEmailT.getText();
        String subject = String.valueOf(teaSubT.getSelectedItem());
        ButtonModel Gender = buttonGroup1.getSelection();

        int selectRow = teaTable.getSelectedRow();

        if (selectRow == -1) {
            JOptionPane.showMessageDialog(null, "Please Select Teacher", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

            if (teaFname.isEmpty()) {

                JOptionPane.showMessageDialog(null, "Invalid First Name", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (teaLname.isEmpty()) {

                JOptionPane.showMessageDialog(null, "Invalid Last Name", "Warning", JOptionPane.WARNING_MESSAGE);

            } else if (teaNIC.isEmpty()) {

                JOptionPane.showMessageDialog(null, "Please Enter NIC", "Warning", JOptionPane.WARNING_MESSAGE);

            } else if (!teaNIC.matches("\\d{12}") && !teaNIC.matches("\\d{9}[VX]")) {

                JOptionPane.showMessageDialog(null, "Invalid NIC", "Warning", JOptionPane.WARNING_MESSAGE);

            } else if (subject.equals("Select Subject")) {

                JOptionPane.showMessageDialog(null, "Please Select Subject", "Warning", JOptionPane.WARNING_MESSAGE);

            } else if (teaMobile.isEmpty()) {

                JOptionPane.showMessageDialog(null, "Please Enter Teacher Mobile Number", "Warning", JOptionPane.WARNING_MESSAGE);

            } else if (!teaMobile.matches("^(0|94)\\d{9}$")) {
                JOptionPane.showMessageDialog(null, "Invalid Mobile Number", "Warning", JOptionPane.WARNING_MESSAGE);

            } else if (teaAddress.isEmpty()) {

                JOptionPane.showMessageDialog(null, "Please Enter Teacher Address", "Warning", JOptionPane.WARNING_MESSAGE);

            } else if (teaEmail.isEmpty()) {

                JOptionPane.showMessageDialog(null, "Please Enter Teacher Email", "Warning", JOptionPane.WARNING_MESSAGE);

            } else if (!teaEmail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                JOptionPane.showMessageDialog(null, "Invalid Email Address", "Warning", JOptionPane.WARNING_MESSAGE);

            } else {

                String genderID = Gender.getActionCommand();
                int subjectId = subjectMap.get(subject);

                String tea_id = String.valueOf(teaTable.getValueAt(selectRow, 0));
                try {
                    MySQL.execute("UPDATE `teacher` SET "
                            + "`f_name`='" + teaFname + "',"
                            + "`l_name` = '" + teaLname + "',"
                            + "`nic` = '" + teaNIC + "',"
                            + "`address` = '" + teaAddress + "',"
                            + "`email`='" + teaEmail + "',"
                            + "`subject_subno`='" + subjectId + "',"
                            + "`gender_gid`='" + genderID + "',"
                            + "`mobile` = '" + teaMobile + "' "
                            + "WHERE `tno` ='" + tea_id + "'");

                } catch (Exception e) {
                    e.printStackTrace();
                }

                resetTeacher();
                loadteacher();
                teaTable.setEnabled(true);
                teaAddButton.setEnabled(true);
                JOptionPane.showMessageDialog(null, "Done", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }

    }//GEN-LAST:event_jButton13ActionPerformed

    private void clsRegSubComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clsRegSubComboActionPerformed

        String subject = (String) clsRegSubCombo.getSelectedItem();
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
                clsReg_TeaCombo.setModel(model);

            } catch (Exception ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            clsReg_TeaCombo.removeAllItems();
        }


    }//GEN-LAST:event_clsRegSubComboActionPerformed

    private void stuAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stuAddButtonActionPerformed

        String stuFname = TstudentFname.getText();
        String stuLname = TstudentLname.getText();
        String stuMobile = TstudentMobile.getText();
        String stuNIC = TstudentNIC.getText();
        String stuAddress = TstudentAddress.getText();
        String stuEmail = TstuEmail.getText();

        java.util.Date selectedDate = TstudentBday.getDate();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String stuBday = formatter.format(selectedDate);

        ButtonModel Gender = buttonGroup1.getSelection();

        if (stuFname.isEmpty()) {

            JOptionPane.showMessageDialog(null, "Invalid First Name", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (stuLname.isEmpty()) {

            JOptionPane.showMessageDialog(null, "Invalid Last Name", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (stuNIC.isEmpty()) {

            JOptionPane.showMessageDialog(null, "Please Enter NIC", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (!stuNIC.matches("\\d{12}") && !stuNIC.matches("\\d{9}[VX]")) {

            JOptionPane.showMessageDialog(null, "Invalid NIC", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (stuMobile.isEmpty()) {

            JOptionPane.showMessageDialog(null, "Please Enter Student Mobile Number", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (!stuMobile.matches("^(0|94)\\d{9}$")) {
            JOptionPane.showMessageDialog(null, "Invalid Mobile Number", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (stuAddress.isEmpty()) {

            JOptionPane.showMessageDialog(null, "Please Enter Student Address", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (stuEmail.isEmpty()) {

            JOptionPane.showMessageDialog(null, "Please Enter Student Email", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (!stuEmail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            JOptionPane.showMessageDialog(null, "Invalid Email Address", "Warning", JOptionPane.WARNING_MESSAGE);

        } else {

            String genderID = Gender.getActionCommand();

            try {
                MySQL.execute("INSERT INTO `student` (`f_name`,`l_name`,`nic`,`address`,`dob`,`mobile`,`email`,`gender_gid`)"
                        + " VALUES('" + stuFname + "','" + stuLname + "','" + stuNIC + "','" + stuAddress + "','" + stuBday + "',' " + stuMobile + "',' " + stuEmail + "',' " + genderID + "') ");

                String fullName = stuFname + " " + stuLname;

                HashMap< String, String> subenroll = new HashMap<>();
                subenroll.put("nic", stuNIC);
                subenroll.put("name", fullName);

                subjectEnroll suben = new subjectEnroll(subenroll);
                suben.setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
            }

            reset();
            loadStudents();
            JOptionPane.showMessageDialog(null, "Done", "Warning", JOptionPane.WARNING_MESSAGE);
        }


    }//GEN-LAST:event_stuAddButtonActionPerformed

    private void stuTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stuTableMouseClicked

        if (evt.getClickCount() == 2) {

            stuTable.setEnabled(false);
            stuAddButton.setEnabled(false);

            int selectRow = stuTable.getSelectedRow();

            String fullname = String.valueOf(stuTable.getValueAt(selectRow, 1));

            String[] name = fullname.split(" ");

            if (name.length >= 2) {
                String firstName = name[0];
                String lastName = name[1];

                TstudentFname.setText(firstName);
                TstudentLname.setText(lastName);

            }

            String nic = String.valueOf(stuTable.getValueAt(selectRow, 2));
            TstudentNIC.setText(nic);

            String mobile = String.valueOf(stuTable.getValueAt(selectRow, 3));
            TstudentMobile.setText(mobile);

            String email = String.valueOf(stuTable.getValueAt(selectRow, 4));
            TstuEmail.setText(email);

            String address = String.valueOf(stuTable.getValueAt(selectRow, 5));
            TstudentAddress.setText(address);

            String bday = String.valueOf(stuTable.getValueAt(selectRow, 6));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date birthday = null;

            try {
                birthday = dateFormat.parse(bday);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            TstudentBday.setDate(birthday);

            String gender = String.valueOf(stuTable.getValueAt(selectRow, 7));

            if (gender.equals("Male")) {
                rStuMale.setSelected(true);
            }

            if (gender.equals("Female")) {
                RStuFemale.setSelected(true);
            }

        }


    }//GEN-LAST:event_stuTableMouseClicked

    private void StudentSearchInputInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_StudentSearchInputInputMethodTextChanged

    }//GEN-LAST:event_StudentSearchInputInputMethodTextChanged

    private void StudentSearchInputKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_StudentSearchInputKeyTyped

    }//GEN-LAST:event_StudentSearchInputKeyTyped

    private void StudentSearchInputKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_StudentSearchInputKeyPressed

    }//GEN-LAST:event_StudentSearchInputKeyPressed

    private void StudentSearchInputKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_StudentSearchInputKeyReleased

        DefaultTableModel table = (DefaultTableModel) stuTable.getModel();
        TableRowSorter<DefaultTableModel> obj = new TableRowSorter<>(table);
        stuTable.setRowSorter(obj);

        String searchText = StudentSearchInput.getText();
        RowFilter<DefaultTableModel, Object> filter = new RowFilter<DefaultTableModel, Object>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                for (int i = 0; i < entry.getValueCount(); i++) {
                    if (entry.getStringValue(i).toLowerCase().contains(searchText.toLowerCase())) {
                        return true;
                    }
                }
                return false;
            }
        };
        obj.setRowFilter(filter);

    }//GEN-LAST:event_StudentSearchInputKeyReleased

    private void teaAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teaAddButtonActionPerformed

        String teaFname = teaFnameT.getText();
        String teaLname = teaLnameT.getText();
        String teaMobile = teaMobileT.getText();
        String teaNIC = teaNicT.getText();
        String teaAddress = teaAddressT.getText();
        String teaEmail = teaEmailT.getText();
        String subject = String.valueOf(teaSubT.getSelectedItem());

        ButtonModel Gender = buttonGroup1.getSelection();

        if (teaFname.isEmpty()) {

            JOptionPane.showMessageDialog(null, "Invalid First Name", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (teaLname.isEmpty()) {

            JOptionPane.showMessageDialog(null, "Invalid Last Name", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (teaNIC.isEmpty()) {

            JOptionPane.showMessageDialog(null, "Please Enter NIC", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (!teaNIC.matches("\\d{12}") && !teaNIC.matches("\\d{9}[VX]")) {

            JOptionPane.showMessageDialog(null, "Invalid NIC", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (subject.equals("Select Subject")) {

            JOptionPane.showMessageDialog(null, "Please Select Subject", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (teaMobile.isEmpty()) {

            JOptionPane.showMessageDialog(null, "Please Enter Student Mobile Number", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (!teaMobile.matches("^(0|94)\\d{9}$")) {
            JOptionPane.showMessageDialog(null, "Invalid Mobile Number", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (teaAddress.isEmpty()) {

            JOptionPane.showMessageDialog(null, "Please Enter Student Address", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (teaEmail.isEmpty()) {

            JOptionPane.showMessageDialog(null, "Please Enter Student Email", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (!teaEmail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            JOptionPane.showMessageDialog(null, "Invalid Email Address", "Warning", JOptionPane.WARNING_MESSAGE);

        } else {

            String genderID = Gender.getActionCommand();
            int subjectId = subjectMap.get(subject);

            try {
                MySQL.execute("INSERT INTO `teacher` (`f_name`,`l_name`,`nic`,`address`,`mobile`,`email`,`gender_gid`,`subject_subno`)"
                        + " VALUES('" + teaFname + "','" + teaLname + "','" + teaNIC + "','" + teaAddress + "','" + teaMobile + "',' " + teaEmail + "',' " + genderID + "','" + subjectId + "') ");

            } catch (Exception e) {
                e.printStackTrace();
            }

            JOptionPane.showMessageDialog(null, "Done", "Warning", JOptionPane.WARNING_MESSAGE);

            resetTeacher();
            loadteacher();

        }

    }//GEN-LAST:event_teaAddButtonActionPerformed

    private void teaTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_teaTableMouseClicked
        if (evt.getClickCount() == 2) {

            teaTable.setEnabled(false);
            teaAddButton.setEnabled(false);

            int selectRow = teaTable.getSelectedRow();

            String fullname = String.valueOf(teaTable.getValueAt(selectRow, 1));

            String[] name = fullname.split(" ");

            if (name.length >= 2) {
                String firstName = name[0];
                String lastName = name[1];

                teaFnameT.setText(firstName);
                teaLnameT.setText(lastName);

            }

            String nic = String.valueOf(teaTable.getValueAt(selectRow, 2));
            teaNicT.setText(nic);

            String mobile = String.valueOf(teaTable.getValueAt(selectRow, 3));
            teaMobileT.setText(mobile);

            String email = String.valueOf(teaTable.getValueAt(selectRow, 4));
            teaEmailT.setText(email);

            String address = String.valueOf(teaTable.getValueAt(selectRow, 5));
            teaAddressT.setText(address);

            String subject = String.valueOf(teaTable.getValueAt(selectRow, 6));
            teaSubT.setSelectedItem(subject);

            String gender = String.valueOf(teaTable.getValueAt(selectRow, 7));

            if (gender.equals("Male")) {
                teaMaleR.setSelected(true);
            }

            if (gender.equals("Female")) {
                teaFemaleR.setSelected(true);
            }

        }

    }//GEN-LAST:event_teaTableMouseClicked

    private void teaSerchInputKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_teaSerchInputKeyReleased

        DefaultTableModel table = (DefaultTableModel) teaTable.getModel();
        TableRowSorter<DefaultTableModel> obj = new TableRowSorter<>(table);
        teaTable.setRowSorter(obj);

        String searchText = teaSerchInput.getText();
        RowFilter<DefaultTableModel, Object> filter = new RowFilter<DefaultTableModel, Object>() {
            @Override
            public boolean include(RowFilter.Entry<? extends DefaultTableModel, ? extends Object> entry) {
                for (int i = 0; i < entry.getValueCount(); i++) {
                    if (entry.getStringValue(i).toLowerCase().contains(searchText.toLowerCase())) {
                        return true;
                    }
                }
                return false;
            }
        };
        obj.setRowFilter(filter);
    }//GEN-LAST:event_teaSerchInputKeyReleased

    private void subAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subAddButtonActionPerformed

        String subject_name = subjectName.getText();
        String subject_des = subDes.getText();
        String subject_duration = subDuration.getText();
        String Subject_Price = subPrice.getText();

        if (subject_name.isEmpty()) {

            JOptionPane.showMessageDialog(null, "Insert Subject Name", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (subject_des.isEmpty()) {

            JOptionPane.showMessageDialog(null, "Insert Subject Description", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (subject_duration.isEmpty()) {

            JOptionPane.showMessageDialog(null, "Insert Subject Duration", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (Subject_Price.isEmpty() || !Subject_Price.matches("\\d+")) {

            JOptionPane.showMessageDialog(null, "Insert Subject Price", "Warning", JOptionPane.WARNING_MESSAGE);

        } else {

            double price = Double.parseDouble(Subject_Price);

            try {
                MySQL.execute("INSERT INTO `subject` (`subject_name`,`subject_duration`,`description`,`price`)"
                        + "VALUES('" + subject_name + "','" + subject_duration + "','" + subject_des + "','" + price + "')");

            } catch (Exception e) {
                e.printStackTrace();
            }

            resetSubject();
            loadSubjectTable();
            JOptionPane.showMessageDialog(null, "Done", "Warning", JOptionPane.WARNING_MESSAGE);
        }


    }//GEN-LAST:event_subAddButtonActionPerformed

    private void subjectTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_subjectTableMouseClicked

        if (evt.getClickCount() == 2) {

            subjectTable.setEnabled(false);
            subAddButton.setEnabled(false);

            int selectRow = subjectTable.getSelectedRow();

            String sub_name = String.valueOf(subjectTable.getValueAt(selectRow, 1));
            subjectName.setText(sub_name);

            String sub_price = String.valueOf(subjectTable.getValueAt(selectRow, 2));
            subPrice.setText(sub_price);

            String sub_duration = String.valueOf(subjectTable.getValueAt(selectRow, 3));
            subDuration.setText(sub_duration);

            String sub_des = String.valueOf(subjectTable.getValueAt(selectRow, 4));
            subDes.setText(sub_des);

        }
    }//GEN-LAST:event_subjectTableMouseClicked

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed

        String subject_name = subjectName.getText();
        String subject_des = subDes.getText();
        String subject_duration = subDuration.getText();
        String Subject_Price = subPrice.getText();

        int selectRow = subjectTable.getSelectedRow();

        if (selectRow == -1) {
            JOptionPane.showMessageDialog(null, "Please Select Subject", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

            if (subject_name.isEmpty()) {

                JOptionPane.showMessageDialog(null, "Insert Subject Name", "Warning", JOptionPane.WARNING_MESSAGE);

            } else if (subject_des.isEmpty()) {

                JOptionPane.showMessageDialog(null, "Insert Subject Description", "Warning", JOptionPane.WARNING_MESSAGE);

            } else if (subject_duration.isEmpty()) {

                JOptionPane.showMessageDialog(null, "Insert Subject Duration", "Warning", JOptionPane.WARNING_MESSAGE);

            } else if (Subject_Price.isEmpty() || !Subject_Price.matches("\\d+(\\.\\d{2})?")) {

                JOptionPane.showMessageDialog(null, "Insert Subject Price", "Warning", JOptionPane.WARNING_MESSAGE);

            } else {

                double price = Double.parseDouble(Subject_Price);

                String sub_id = String.valueOf(subjectTable.getValueAt(selectRow, 0));

                try {
                    MySQL.execute("UPDATE `subject` SET "
                            + "`subject_name` = '" + subject_name + "' ,"
                            + " `subject_duration` = '" + subject_duration + "',"
                            + "`description` = '" + subject_des + "' , "
                            + "`price` = '" + price + "' WHERE `subno`='" + sub_id + "'");

                } catch (Exception e) {
                    e.printStackTrace();
                }

                resetSubject();
                loadSubjectTable();

                subjectTable.setEnabled(true);
                subAddButton.setEnabled(true);

                loadSubjectTable();
                JOptionPane.showMessageDialog(null, "Done", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButton14ActionPerformed

    private void StudentSearchInput1InputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_StudentSearchInput1InputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_StudentSearchInput1InputMethodTextChanged

    private void StudentSearchInput1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StudentSearchInput1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_StudentSearchInput1ActionPerformed

    private void StudentSearchInput1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_StudentSearchInput1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_StudentSearchInput1KeyPressed

    private void StudentSearchInput1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_StudentSearchInput1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_StudentSearchInput1KeyReleased

    private void StudentSearchInput1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_StudentSearchInput1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_StudentSearchInput1KeyTyped

    private void subSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_subSearchActionPerformed

    private void subSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_subSearchKeyReleased
        DefaultTableModel table = (DefaultTableModel) subjectTable.getModel();
        TableRowSorter<DefaultTableModel> obj = new TableRowSorter<>(table);
        subjectTable.setRowSorter(obj);

        String searchText = subSearch.getText();
        RowFilter<DefaultTableModel, Object> filter = new RowFilter<DefaultTableModel, Object>() {
            @Override
            public boolean include(RowFilter.Entry<? extends DefaultTableModel, ? extends Object> entry) {
                for (int i = 0; i < entry.getValueCount(); i++) {
                    if (entry.getStringValue(i).toLowerCase().contains(searchText.toLowerCase())) {
                        return true;
                    }
                }
                return false;
            }
        };
        obj.setRowFilter(filter);
    }//GEN-LAST:event_subSearchKeyReleased

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed

        if(!subjectTable.isEnabled()){
                    int selectRow = subjectTable.getSelectedRow();

        if (selectRow == -1) {
            JOptionPane.showMessageDialog(null, "Please Select Teacher", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

            int result = JOptionPane.showOptionDialog(
                    null,
                    "Do you want to continue?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    null,
                    null);
            if (result == JOptionPane.YES_OPTION) {
                String sub_id = String.valueOf(subjectTable.getValueAt(selectRow, 0));
                try {
                    MySQL.execute("DELETE FROM `class` WHERE `subject_subno` = '" + sub_id + "' ");
                    MySQL.execute("DELETE FROM `subject_has_student` WHERE `subject_subno` = '" + sub_id + "' ");
                    MySQL.execute(" DELETE FROM `teacher` WHERE `subject_subno` = '" + sub_id + "'");
                    MySQL.execute(" DELETE FROM `subject` WHERE `subno` = '" + sub_id + "'");

                    loadSubjectTable();

                    subjectTable.setEnabled(true);
                    subAddButton.setEnabled(true);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (result == JOptionPane.NO_OPTION) {
                // User clicked No
                teaTable.setEnabled(true);
                teaAddButton.setEnabled(true);
            } else {
                // User closed the dialog or clicked on the close button
                teaTable.setEnabled(true);
                teaAddButton.setEnabled(true);
            }
        }
        }

    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        int selectRow = stuTable.getSelectedRow();

        if (selectRow == -1) {
            JOptionPane.showMessageDialog(null, "Please Select Student", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

            int result = JOptionPane.showOptionDialog(
                    null,
                    "Do you want to continue?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    null,
                    null);
            if (result == JOptionPane.YES_OPTION) {
                String fullName = String.valueOf(stuTable.getValueAt(selectRow, 1));
                String stuNIC = String.valueOf(stuTable.getValueAt(selectRow, 2));

                HashMap< String, String> subenroll = new HashMap<>();
                subenroll.put("nic", stuNIC);
                subenroll.put("name", fullName);

                subjectEnroll suben = new subjectEnroll(subenroll);
                suben.setVisible(true);

            } else if (result == JOptionPane.NO_OPTION) {
                // User clicked No
                stuTable.setEnabled(true);
                stuAddButton.setEnabled(true);
            } else {
                // User closed the dialog or clicked on the close button
                stuTable.setEnabled(true);
                stuAddButton.setEnabled(true);
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void clsRegSubComboMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clsRegSubComboMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_clsRegSubComboMouseClicked

    private void classAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_classAddButtonActionPerformed

        String teacher = (String) clsReg_TeaCombo.getSelectedItem();
        String subject = (String) clsRegSubCombo.getSelectedItem();

        Object StartTime = start_Time.getValue();
        Object EndTime = End_Time.getValue();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String sqlStartTime = sdf.format(StartTime);
        String sqlEndTime = sdf.format(EndTime);

        int teacherid = teacherMap.get(teacher);
        int subjectID = subjectMap.get(subject);

        if (subject.equals("Select Subject")) {

            JOptionPane.showMessageDialog(null, "Please Select Subject", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (teacher.equals("Select Teacher")) {

            JOptionPane.showMessageDialog(null, "Invalid Last Name", "Warning", JOptionPane.WARNING_MESSAGE);

        } else {

            try {

                MySQL.execute("INSERT INTO `class` (`timeslot_start`,`timeslot_end`,`subject_subno`,`teacher_tno`)"
                        + " VALUES('" + sqlStartTime + "','" + sqlEndTime + "','" + subjectID + "','" + teacherid + "') ");

            } catch (Exception e) {
                e.printStackTrace();
            }

            reset();
            loadClass();
            loadAttendenceClass();
            JOptionPane.showMessageDialog(null, "Done", "Warning", JOptionPane.WARNING_MESSAGE);
        }


    }//GEN-LAST:event_classAddButtonActionPerformed

    private void RStuFemaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RStuFemaleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RStuFemaleActionPerformed

    private void classTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_classTableMouseClicked
        if (evt.getClickCount() == 2) {

            classTable.setEnabled(false);
            teaAddButton.setEnabled(false);

        }
    }//GEN-LAST:event_classTableMouseClicked

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed

        if (!classTable.isEnabled()) {

            int selctedRow = classTable.getSelectedRow();

            if (selctedRow == -1) {
                JOptionPane.showMessageDialog(null, "Please Selctet Teacher", "Warning", JOptionPane.WARNING_MESSAGE);

            } else {

                int result = JOptionPane.showOptionDialog(
                        null,
                        "Do you want to continue?",
                        "Confirmation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        null,
                        null);
                if (result == JOptionPane.YES_OPTION) {

                    String class_id = String.valueOf(classTable.getValueAt(selctedRow, 0));

                    try {

                        MySQL.execute("DELETE FROM `class` WHERE classno = '" + class_id + "'");

                        JOptionPane.showMessageDialog(null, "Successful removal of the Class..!", "Success", JOptionPane.INFORMATION_MESSAGE);

                        loadClass();

                        classTable.setEnabled(true);
                        classAddButton.setEnabled(true);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (result == JOptionPane.NO_OPTION) {
                    // User clicked No
                    classTable.setEnabled(true);
                    classAddButton.setEnabled(true);

                } else {
                    // User closed the dialog or clicked on the close button
                    classTable.setEnabled(true);
                    classAddButton.setEnabled(true);
                }

            }
        }

    }//GEN-LAST:event_jButton12ActionPerformed

    private void attendenceClassComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_attendenceClassComboItemStateChanged

        String className = String.valueOf(attendenceClassCombo.getSelectedItem());

        if (!className.equals("Select Class")) {

            classAttendece classinfo = classtMap.get(className);

            attMobile.setText(classinfo.getMobile());
            attTeacher_name.setText(classinfo.getTeacherName());
            attEmail.setText(classinfo.getEmail());
            attSubject.setText(classinfo.getSubjectName());

            int subjectNo = classinfo.getSubjectNo();

            try {
                ResultSet resultSet = MySQL.execute("SELECT * FROM `student` INNER JOIN"
                        + " `subject_has_student` ON `student`.`sno` = `subject_has_student`.`student_sno` "
                        + "WHERE `subject_subno` = '" + subjectNo + "' ");

                DefaultTableModel model = (DefaultTableModel) attTable.getModel();
                model.setRowCount(0);

                while (resultSet.next()) {
                    Vector v = new Vector<>();
                    v.add(resultSet.getString("sno"));
                    v.add(resultSet.getString("nic"));
                    v.add(resultSet.getString("f_name") + " " + resultSet.getString("l_name"));
                    v.add("timeslot_start");

                    model.addRow(v);
                    attTable.setModel(model);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_attendenceClassComboItemStateChanged

    private void attendenceClassComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_attendenceClassComboActionPerformed


    }//GEN-LAST:event_attendenceClassComboActionPerformed

    private void attStuSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_attStuSearchKeyReleased
        DefaultTableModel table = (DefaultTableModel) attTable.getModel();
        TableRowSorter<DefaultTableModel> obj = new TableRowSorter<>(table);
        attTable.setRowSorter(obj);

        String searchText = attStuSearch.getText();
        RowFilter<DefaultTableModel, Object> filter = new RowFilter<DefaultTableModel, Object>() {
            @Override
            public boolean include(RowFilter.Entry<? extends DefaultTableModel, ? extends Object> entry) {
                for (int i = 0; i < entry.getValueCount(); i++) {
                    if (entry.getStringValue(i).toLowerCase().contains(searchText.toLowerCase())) {
                        return true;
                    }
                }
                return false;
            }
        };
        obj.setRowFilter(filter);
    }//GEN-LAST:event_attStuSearchKeyReleased

    private void attSubmitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_attSubmitButtonActionPerformed

        String className = String.valueOf(attendenceClassCombo.getSelectedItem());
        
        classAttendece classinfo = classtMap.get(className);
        
        int classId = classinfo.getClassNo();

        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO `s_attendance`(`student_sno`, `class_classno`, `status_s_id`) VALUES ");

        for (int i = 0; i < attTable.getRowCount(); i++) {
            Object value = attTable.getValueAt(i, 4);

            boolean isPresent = false;
            if (value != null && value instanceof Boolean) {
                isPresent = (boolean) value;
            }

            if (isPresent) {
                String studentID = (String) attTable.getValueAt(i, 0);
                sqlBuilder.append("(").append(studentID).append(", ").append(classId).append(", 2), ");
            } else {

            }
        }

        String sql = sqlBuilder.toString();
        if (sql.endsWith(", ")) {
            sql = sql.substring(0, sql.length() - 2);
        }
        try {
            MySQL.execute(sql);
        } catch (Exception ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_attSubmitButtonActionPerformed

    private void jButton7MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7MouseReleased

    private void teaSubTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teaSubTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_teaSubTActionPerformed

    private void clsReg_TeaComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clsReg_TeaComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clsReg_TeaComboActionPerformed

    public static void main(String[] args) {

        java.awt.EventQueue.invokeLater(() -> {
            new Home().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner End_Time;
    private javax.swing.JRadioButton RStuFemale;
    private javax.swing.JTextField StudentSearchInput;
    private javax.swing.JTextField StudentSearchInput1;
    private javax.swing.JTextField TstuEmail;
    private javax.swing.JTextField TstudentAddress;
    private com.toedter.calendar.JDateChooser TstudentBday;
    private javax.swing.JTextField TstudentFname;
    private javax.swing.JTextField TstudentLname;
    private javax.swing.JTextField TstudentMobile;
    private javax.swing.JTextField TstudentNIC;
    private javax.swing.JLabel attEmail;
    private javax.swing.JLabel attMobile;
    private javax.swing.JTextField attStuSearch;
    private javax.swing.JLabel attSubject;
    private javax.swing.JButton attSubmitButton;
    private javax.swing.JTable attTable;
    private javax.swing.JLabel attTeacher_name;
    private javax.swing.JComboBox<String> attendenceClassCombo;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton classAddButton;
    private javax.swing.JPanel classRegistration;
    private javax.swing.JTable classTable;
    private javax.swing.JComboBox<String> clsRegSubCombo;
    private javax.swing.JComboBox<String> clsReg_TeaCombo;
    private javax.swing.JPanel dashBoard;
    private javax.swing.JPanel inputField1;
    private javax.swing.JPanel inputField2;
    private javax.swing.JPanel inputField3;
    private javax.swing.JPanel inputField7;
    private javax.swing.JPanel inputField8;
    private javax.swing.JPanel inputField9;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel100;
    private javax.swing.JPanel jPanel101;
    private javax.swing.JPanel jPanel102;
    private javax.swing.JPanel jPanel103;
    private javax.swing.JPanel jPanel104;
    private javax.swing.JPanel jPanel105;
    private javax.swing.JPanel jPanel106;
    private javax.swing.JPanel jPanel107;
    private javax.swing.JPanel jPanel108;
    private javax.swing.JPanel jPanel109;
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
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel52;
    private javax.swing.JPanel jPanel53;
    private javax.swing.JPanel jPanel54;
    private javax.swing.JPanel jPanel55;
    private javax.swing.JPanel jPanel56;
    private javax.swing.JPanel jPanel57;
    private javax.swing.JPanel jPanel58;
    private javax.swing.JPanel jPanel59;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel60;
    private javax.swing.JPanel jPanel61;
    private javax.swing.JPanel jPanel62;
    private javax.swing.JPanel jPanel63;
    private javax.swing.JPanel jPanel64;
    private javax.swing.JPanel jPanel65;
    private javax.swing.JPanel jPanel66;
    private javax.swing.JPanel jPanel67;
    private javax.swing.JPanel jPanel68;
    private javax.swing.JPanel jPanel69;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel70;
    private javax.swing.JPanel jPanel71;
    private javax.swing.JPanel jPanel72;
    private javax.swing.JPanel jPanel73;
    private javax.swing.JPanel jPanel74;
    private javax.swing.JPanel jPanel75;
    private javax.swing.JPanel jPanel76;
    private javax.swing.JPanel jPanel77;
    private javax.swing.JPanel jPanel78;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel80;
    private javax.swing.JPanel jPanel81;
    private javax.swing.JPanel jPanel82;
    private javax.swing.JPanel jPanel83;
    private javax.swing.JPanel jPanel84;
    private javax.swing.JPanel jPanel85;
    private javax.swing.JPanel jPanel86;
    private javax.swing.JPanel jPanel87;
    private javax.swing.JPanel jPanel88;
    private javax.swing.JPanel jPanel89;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanel90;
    private javax.swing.JPanel jPanel91;
    private javax.swing.JPanel jPanel92;
    private javax.swing.JPanel jPanel93;
    private javax.swing.JPanel jPanel94;
    private javax.swing.JPanel jPanel95;
    private javax.swing.JPanel jPanel96;
    private javax.swing.JPanel jPanel97;
    private javax.swing.JPanel jPanel98;
    private javax.swing.JPanel jPanel99;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JRadioButton rStuMale;
    private javax.swing.JSpinner start_Time;
    private javax.swing.JButton stuAddButton;
    private javax.swing.JTable stuTable;
    private javax.swing.JPanel studentInputPanal;
    private javax.swing.JPanel studentInputPanal2;
    private javax.swing.JPanel studentInputSubPanal;
    private javax.swing.JPanel studentInputSubPanal2;
    private javax.swing.JPanel studentMainPanal;
    private javax.swing.JPanel studentMainPanal2;
    private javax.swing.JPanel studentTablePanal;
    private javax.swing.JPanel studentTablePanal2;
    private javax.swing.JPanel studentregPanal;
    private javax.swing.JButton subAddButton;
    private javax.swing.JTextField subDes;
    private javax.swing.JPanel subDescription;
    private javax.swing.JTextField subDuration;
    private javax.swing.JTextField subPrice;
    private javax.swing.JTextField subSearch;
    private javax.swing.JTextField subjectName;
    private javax.swing.JPanel subjectRegistration;
    private javax.swing.JTable subjectTable;
    private javax.swing.JButton teaAddButton;
    private javax.swing.JTextField teaAddressT;
    private javax.swing.JTextField teaEmailT;
    private javax.swing.JRadioButton teaFemaleR;
    private javax.swing.JTextField teaFnameT;
    private javax.swing.JTextField teaLnameT;
    private javax.swing.JRadioButton teaMaleR;
    private javax.swing.JTextField teaMobileT;
    private javax.swing.JTextField teaNicT;
    private javax.swing.JTextField teaSerchInput;
    private javax.swing.JComboBox<String> teaSubT;
    private javax.swing.JTable teaTable;
    private javax.swing.JPanel teacherRegPanal;
    // End of variables declaration//GEN-END:variables
}
