package com.Patikadev.View;

import com.Patikadev.Helper.Config;
import com.Patikadev.Helper.Helper;
import com.Patikadev.Helper.Item;
import com.Patikadev.Model.Content;
import com.Patikadev.Model.Course;
import com.Patikadev.Model.Patika;
import com.Patikadev.Model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StudentGUI extends JFrame {
    private JPanel wrapper;
    private JTable tbl_patika_list;
    private JScrollPane scrl_patika_list;
    private JComboBox cmb_course_list;
    private JButton btn_course_add;
    private JPanel pnl_content_list_left;
    private JTable tbl_content_list;
    private JPanel pnl_content_list_right;
    private JTextField fld_content_review;
    private JComboBox cmb_content_rating;
    private JButton btn_save_review;
    private DefaultTableModel mdl_patika_list;
    private Object[] row_patika_list;
    private DefaultTableModel mdl_content_list;
    private User student;
    private Object[] row_content_list;

    public StudentGUI(User u) {
        add(wrapper);
        setSize(900, 700);
        setLocation(Helper.screenCenterPoint("x", getSize()), Helper.screenCenterPoint("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);
        student = u;

        loadRatingComboBox();
        mdl_patika_list = new DefaultTableModel();
        Object[] col_patika_list = {"ID", "Patika Adı"};
        mdl_patika_list.setColumnIdentifiers(col_patika_list);
        row_patika_list = new Object[col_patika_list.length];
        LoadPatikaModel();
        tbl_patika_list.setModel(mdl_patika_list);  // amaç patikanın içine değerler yerleşssin
//        tbl_patika_list.setComponentPopupMenu(patikaMenu);
        tbl_patika_list.getTableHeader().setReorderingAllowed(false); // patikanın içindeki id ve name ksıımlarının yerlerinin manuel değişmesini önler
        tbl_patika_list.getColumnModel().getColumn(0).setMaxWidth(75); // id kısmı çok uzundu onu kısalttık


        mdl_content_list = new DefaultTableModel();
        Object[] col_contentList = {"ID", "Ders ID", "Adı", "Açıklama", "Link", "Quiz"};
        mdl_content_list.setColumnIdentifiers(col_contentList);
        row_content_list = new Object[col_contentList.length];
        loadContentModel();
        tbl_content_list.setModel(mdl_content_list);
        tbl_content_list.getColumnModel().getColumn(0).setMaxWidth(75);
        tbl_content_list.getTableHeader().setReorderingAllowed(false);

        tbl_patika_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                int selected_row = tbl_patika_list.rowAtPoint(point);
                tbl_patika_list.setRowSelectionInterval(selected_row, selected_row);

                cmb_course_list.removeAllItems();

                var selectedPatikaId = Integer.parseInt(tbl_patika_list.getValueAt(tbl_patika_list.getSelectedRow(), 0).toString());
                loadCourseComboBox(selectedPatikaId);

            }
        });

        tbl_content_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                int selected_row = tbl_content_list.rowAtPoint(point);
                tbl_content_list.setRowSelectionInterval(selected_row, selected_row);

                btn_save_review.setEnabled(true);
            }
        });
        cmb_course_list.addActionListener(e -> {
            btn_course_add.setEnabled(true);
        });
        btn_course_add.addActionListener(e -> {
            if (cmb_course_list.getSelectedIndex() != -1) {
                var courseId = ((Item) cmb_course_list.getSelectedItem()).getKey();
                if (Course.addStudentToCourse(u.getId(), courseId)) {
                    loadContentModel();
                    Helper.showMsg("done");
                }
            }
        });

        btn_save_review.addActionListener(e -> {
            if (cmb_content_rating.getSelectedIndex() != -1) {
                var rating = ((Item) cmb_content_rating.getSelectedItem()).getKey();
                var contentId = Integer.parseInt(tbl_content_list.getValueAt(tbl_content_list.getSelectedRow(), 0).toString());
                if (Content.addRating(contentId, student.getId(), rating, fld_content_review.getText())) {
                    loadContentModel();
                    Helper.showMsg("done");
                }
            }
        });
    }


    public static void main(String[] args) {
        Helper.setLayout();
        User st = new User();
        st.setId(16);
        st.setName("Ali Al");
        st.setPass("123");
        st.setType("student");
        st.setUname("ali");
        StudentGUI opGUI = new StudentGUI(st);
    }

    private void loadCourseComboBox(int patika_id) {
        for (Course obj : Course.getListByPatika(patika_id)) {
            cmb_course_list.addItem(new Item(obj.getId(), obj.getName()));
        }
    }

    private void loadRatingComboBox() {
        for (int i = 1; i <= 5; i++) {
            cmb_content_rating.addItem(new Item(i, Integer.toString(i)));
        }
    }

    private void LoadPatikaModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_patika_list.getModel();
        clearModel.setRowCount(0);
        int i = 0;
        for (Patika obj : Patika.getList()) {
            i = 0;
            row_patika_list[i++] = obj.getId();
            row_patika_list[i++] = obj.getName();
            mdl_patika_list.addRow(row_patika_list);
        }
    }

    private void loadContentModel() {
        var courseIds = Course.getStudentCoursesByUser(student.getId());
        DefaultTableModel clearModel = (DefaultTableModel) tbl_content_list.getModel();
        clearModel.setRowCount(0);
        if (!courseIds.isEmpty()) {
            int i = 0;
            for (Content obj : Content.getListByCourses(courseIds)) {
                i = 0;
                row_content_list[i++] = obj.getId();
                row_content_list[i++] = obj.getCourseId();
                row_content_list[i++] = obj.getName();
                row_content_list[i++] = obj.getDescription();
                row_content_list[i++] = obj.getLink();
                row_content_list[i++] = obj.getQuiz();

                mdl_content_list.addRow(row_content_list);
            }
        }

    }
}
