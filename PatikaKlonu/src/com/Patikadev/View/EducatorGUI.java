package com.Patikadev.View;

import com.Patikadev.Helper.Config;
import com.Patikadev.Helper.Helper;
import com.Patikadev.Helper.Item;
import com.Patikadev.Model.Content;
import com.Patikadev.Model.Course;
import com.Patikadev.Model.Educator;
import com.Patikadev.Model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class EducatorGUI extends JFrame {
    private JPanel wrapper;
    private JTabbedPane pnl_course_list;
    private JTable tbl_course_list;
    private JScrollPane scrl_course_list;
    private JPanel pnl_content_list_left;
    private JPanel pnl_content_list_right;
    private JTable tbl_content_list;
    private JPanel pnl_content_search;
    private JComboBox cmb_course_filter;
    private JButton btn_content_search;
    private JTextField fld_content_search;
    private JTextField fld_content_name;
    private JTextField fld_content_description;
    private JTextField fld_content_link;
    private JTextField fld_content_quiz;
    private JComboBox cmb_content_course_add;
    private JButton btn_content_add;
    private JButton btn_content_edit;
    private JButton btn_content_delete;
    private JButton btn_content_pre_edit;

    private DefaultTableModel mdl_course_list;
    private DefaultTableModel mdl_content_list;

    private Object[] row_course_list;
    private Object[] row_content_list;
    private User educator;
    private List<Integer> filteredCourseIds;


    public EducatorGUI(User _educator) {
        add(wrapper);
        setSize(900, 700);
        setLocation(Helper.screenCenterPoint("x", getSize()), Helper.screenCenterPoint("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);
        educator = _educator;
        filteredCourseIds = new ArrayList<>();


        mdl_course_list = new DefaultTableModel();
        Object[] col_courseList = {"ID", "Ders Adı", "Programlama Dili", "Patika", "Eğitmen"};
        mdl_course_list.setColumnIdentifiers(col_courseList);
        row_course_list = new Object[col_courseList.length];
        loadCourseModel();
        tbl_course_list.setModel(mdl_course_list);
        tbl_course_list.getColumnModel().getColumn(0).setMaxWidth(75);
        tbl_course_list.getTableHeader().setReorderingAllowed(false);

        mdl_content_list = new DefaultTableModel();
        Object[] col_contentList = {"ID", "Ders ID", "Adı", "Açıklama", "Link", "Quiz"};
        mdl_content_list.setColumnIdentifiers(col_contentList);
        row_content_list = new Object[col_contentList.length];
        loadContentModel();
        tbl_content_list.setModel(mdl_content_list);
        tbl_content_list.getColumnModel().getColumn(0).setMaxWidth(75);
        tbl_content_list.getTableHeader().setReorderingAllowed(false);

        loadCourseComboBoxes(educator);

        btn_content_add.addActionListener(e -> {
            if (Helper.isFieldEmty(fld_content_name) || Helper.isFieldEmty(fld_content_description)
                    || Helper.isFieldEmty(fld_content_link) || Helper.isFieldEmty(fld_content_quiz)) {
                Helper.showMsg("fill");
            } else {
                var courseId = ((Item) cmb_content_course_add.getSelectedItem()).getKey();
                if (Content.add(courseId, fld_content_name.getText(), fld_content_description.getText(), fld_content_link.getText(), fld_content_quiz.getText())) {
                    Helper.showMsg("done");
                    loadContentModel();
                    fld_content_name.setText(null);
                    fld_content_description.setText(null);
                    fld_content_link.setText(null);
                    fld_content_quiz.setText(null);

                } else {
                    Helper.showMsg("error");
                }
            }

        });

        btn_content_delete.addActionListener(e -> {
            if (Content.delete(Integer.parseInt(tbl_content_list.getValueAt(tbl_content_list.getSelectedRow(), 0).toString()))) {
                Helper.showMsg("done");
                loadContentModel();
            } else {
                Helper.showMsg("error");
            }

        });


        btn_content_pre_edit.addActionListener(e -> {
            var contentId = Integer.parseInt(tbl_content_list.getValueAt(tbl_content_list.getSelectedRow(), 0).toString());
            var content = Content.getContentById(contentId);
            if (content != null) {
                for (int i = 0; i < cmb_content_course_add.getItemCount(); i++) {
                    if (((Item) cmb_content_course_add.getItemAt(i)).getKey() == content.getCourseId()) {
                        cmb_content_course_add.setSelectedIndex(i);
                    }
                }
                fld_content_name.setText(content.getName());
                fld_content_description.setText(content.getDescription());
                fld_content_link.setText(content.getLink());
                fld_content_quiz.setText(content.getQuiz());

                btn_content_edit.setEnabled(true);
            } else {
                Helper.showMsg("error");
            }

        });

        btn_content_edit.addActionListener(e -> {
            var contentId = Integer.parseInt(tbl_content_list.getValueAt(tbl_content_list.getSelectedRow(), 0).toString());
            var courseId = ((Item) cmb_content_course_add.getSelectedItem()).getKey();
            if (Content.update(contentId, courseId, fld_content_name.getText(), fld_content_description.getText(), fld_content_link.getText(), fld_content_quiz.getText())) {
                Helper.showMsg("done");
                loadContentModel();
                fld_content_name.setText(null);
                fld_content_description.setText(null);
                fld_content_link.setText(null);
                fld_content_quiz.setText(null);
            } else {
                Helper.showMsg("error");
            }
        });

        cmb_course_filter.addActionListener(e -> {
            var courseId = ((Item) cmb_course_filter.getSelectedItem()).getKey();
            if (courseId != -1) {
                filteredCourseIds = new ArrayList<>();

                filteredCourseIds.add(courseId);
            } else {
                filteredCourseIds = new ArrayList<>();
            }

            loadContentModel();
        });
    }

    public static void main(String[] args) {
        Helper.setLayout();
        Educator op = new Educator();
        op.setId(16);
        op.setName("Yalçın Borlu");
        op.setPass("123");
        op.setType("educator");
        op.setUname("yalcinborlu");
        EducatorGUI opGUI = new EducatorGUI(op);
    }

    private void loadCourseModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_course_list.getModel();
        clearModel.setRowCount(0);
        int i = 0;
        for (Course obj : Course.getListByUser(educator.getId())) {
            i = 0;
            row_course_list[i++] = obj.getId();
            row_course_list[i++] = obj.getName();
            row_course_list[i++] = obj.getLang();
            row_course_list[i++] = obj.getPatika().getName();
            row_course_list[i++] = obj.getEducator().getName();
            mdl_course_list.addRow(row_course_list);
        }
    }

    private void loadContentModel() {
        if (filteredCourseIds.isEmpty()) {
            var courses = Course.getListByUser(educator.getId());
            filteredCourseIds = new ArrayList<>();
            int i = 0;
            for (Course obj : courses) {
                filteredCourseIds.add(obj.getId());
                i++;
            }
        }

        DefaultTableModel clearModel = (DefaultTableModel) tbl_content_list.getModel();
        clearModel.setRowCount(0);
        int i = 0;
        for (Content obj : Content.getListByCourses(filteredCourseIds)) {
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

    private void loadCourseComboBoxes(User educator) {
        cmb_course_filter.addItem((new Item(-1, "Tümü")));

        for (Course obj : Course.getListByUser(educator.getId())) {
            cmb_course_filter.addItem(new Item(obj.getId(), obj.getName()));
            cmb_content_course_add.addItem(new Item(obj.getId(), obj.getName()));
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
