package com.Patikadev.View;

import com.Patikadev.Helper.Config;
import com.Patikadev.Helper.Helper;
import com.Patikadev.Helper.Item;
import com.Patikadev.Model.*;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class OperatorGUI extends JFrame {


    private final Operator operator;
    private JPanel wrapper;
    private JTabbedPane pnl_course_list;
    private JLabel lbl_welcome;
    private JPanel pnl_top;
    private JButton btn_logout;
    private JPanel pnl_user_list;
    private JScrollPane scrl_user_list;
    private JTable tbl_user_list;
    private JPanel pnl_user_form;
    private JTextField fld_user_name;
    private JTextField fld_user_uname;
    private JTextField fld_user_pass;
    private JComboBox cmb_user_type;
    private JButton btn_user_add;
    private JTextField fld_sh_user_name;
    private JTextField fld_sh_user_uname;
    private JComboBox cmb_sh_user_type;
    private JButton btn_user_sh;
    private JPanel pnl_patika_list;
    private JScrollPane scrl_patika_list;
    private JTable tbl_patika_list;
    private JPanel pnl_patika_add;
    private JTextField fld_patika_name;
    private JButton btn_Patika_add;
    private JPanel pnl_course_form;
    private JScrollPane scrl_course_list;
    private JTable tbl_course_list;
    private JPanel pnl_course_add;
    private JTextField fld_course_name;

    private JTextField fld_course_lang;
    private JComboBox cmb_course_patika;
    private JComboBox cmb_course_user;
    private JButton btn_course_add;
    private JPanel pnl_content_list_left;
    private JTable tbl_content_list;
    private JPanel pnl_content_list_right;
    private JTextField fld_content_name;
    private JTextField fld_content_description;
    private JTextField fld_content_link;
    private JTextField fld_content_quiz;
    private JComboBox cmb_content_course_add;
    private JButton btn_content_add;
    private JButton btn_content_edit;
    private JButton btn_content_delete;
    private JButton btn_content_pre_edit;
    private JButton btn_delete_user;
    private JButton btn_pre_edit_user;
    private JButton btn_delete_patika;
    private JButton btn_pre_edit_patika;
    private JButton btn_delete_course;
    private JButton btn_pre_edit_course;
    private JButton btn_course_edit;
    private JButton btn_patika_edit;
    private JButton btn_user_edit;
    private DefaultTableModel mdl_user_list;
    private Object[] row_user_list;
    private DefaultTableModel mdl_patika_list;
    private Object[] row_patika_list;
    private JPopupMenu patikaMenu;
    private DefaultTableModel mdl_course_list;
    private Object[] row_course_list;
    private Object[] row_content_list;
    private List<Integer> filteredCourseIds;
    private DefaultTableModel mdl_content_list;


    public OperatorGUI(Operator operator) {
        this.operator = operator;

        add(wrapper);
        setSize(900, 700);
        setLocation(Helper.screenCenterPoint("x", getSize()), Helper.screenCenterPoint("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);

        lbl_welcome.setText("Hoşgeldin: " + operator.getName());

        //ModelUserList
        mdl_user_list = new DefaultTableModel();
                /*if(column==0)
                    return false;
                return super.isCellEditable(row, column);
            }
        };*/
        Object[] col_user_list = {"ID", "Ad Soyad", "Kullanıcı Adı", "Şifre", "Üyelik Tipi"};
        mdl_user_list.setColumnIdentifiers(col_user_list);
        row_user_list = new Object[col_user_list.length];
        loadUserModel();

        tbl_user_list.setModel(mdl_user_list);
        tbl_user_list.getTableHeader().setReorderingAllowed(false);

        tbl_user_list.getSelectionModel().addListSelectionListener(e -> {
            try {
                String select_user_id = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 0).toString();
            } catch (Exception exception) {

            }

        });

        tbl_user_list.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int user_id = Integer.parseInt(tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 0).toString());
                String user_name = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 1).toString();
                String user_uname = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 2).toString();
                String user_pass = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 3).toString();
                String user_type = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 4).toString();

                if (User.update(user_id, user_name, user_uname, user_pass, user_type)) {
                    Helper.showMsg("done");
                    loadUserModel();

                }

                loadUserModel();
                loadEducatorCombo();
                loadCouseModel();
            }
        });
        //patikalist

        patikaMenu = new JPopupMenu();
        JMenuItem updatemenu = new JMenuItem("Güncelle");
        JMenuItem deleteMenu = new JMenuItem("Sil");
        patikaMenu.add(updatemenu);
        patikaMenu.add(deleteMenu);

        updatemenu.addActionListener(e -> {
            int select_id = Integer.parseInt(tbl_patika_list.getValueAt(tbl_patika_list.getSelectedRow(), 0).toString());
            UpdatePatikaGUI updateGUI = new UpdatePatikaGUI(Patika.getFetch(select_id));
            updateGUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    LoadPatikaModel();
                    loadPatikaCombo();
                    loadCouseModel();
                }
            });

        });
        deleteMenu.addActionListener(e -> {
            if (Helper.confirm("sure")) {
                int select_id = Integer.parseInt(tbl_patika_list.getValueAt(tbl_patika_list.getSelectedRow(), 0).toString());
                if (Patika.delete(select_id)) {
                    Helper.showMsg("done");
                    LoadPatikaModel();
                    loadPatikaCombo();
                    loadCouseModel();
                } else {
                    Helper.showMsg("error");
                }

            }
        });

        mdl_patika_list = new DefaultTableModel();
        Object[] col_patika_list = {"ID", "Patika Adı"};
        mdl_patika_list.setColumnIdentifiers(col_patika_list);
        row_patika_list = new Object[col_patika_list.length];
        LoadPatikaModel();


        tbl_patika_list.setModel(mdl_patika_list);  // amaç patikanın içine değerler yerleşssin
        //tbl_patika_list.setComponentPopupMenu(patikaMenu);
        tbl_patika_list.getTableHeader().setReorderingAllowed(false); // patikanın içindeki id ve name ksıımlarının yerlerinin manuel değişmesini önler
        tbl_patika_list.getColumnModel().getColumn(0).setMaxWidth(75); // id kısmı çok uzundu onu kısalttık

        tbl_patika_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                int selected_row = tbl_patika_list.rowAtPoint(point);
                tbl_patika_list.setRowSelectionInterval(selected_row, selected_row);
            }
        });

        //patikalist bitti
        //CourseList

        mdl_course_list = new DefaultTableModel();
        Object[] col_courseList = {"ID", "Ders Adı", "Programlama Dili", "Patika", "Eğitmen"};
        mdl_course_list.setColumnIdentifiers(col_courseList);
        row_course_list = new Object[col_courseList.length];
        loadCouseModel();
        tbl_course_list.setModel(mdl_course_list);
        tbl_course_list.getColumnModel().getColumn(0).setMaxWidth(75);
        tbl_course_list.getTableHeader().setReorderingAllowed(false);
        loadPatikaCombo();
        loadEducatorCombo();

        mdl_content_list = new DefaultTableModel();
        Object[] col_contentList = {"ID", "Ders ID", "Adı", "Açıklama", "Link", "Quiz"};
        mdl_content_list.setColumnIdentifiers(col_contentList);
        row_content_list = new Object[col_contentList.length];
        loadContentModel();
        tbl_content_list.setModel(mdl_content_list);
        tbl_content_list.getColumnModel().getColumn(0).setMaxWidth(75);
        tbl_content_list.getTableHeader().setReorderingAllowed(false);
        loadContentCourseCombo();

// butona veri eklemek için btn_user_add
        btn_user_add.addActionListener(e -> {
            // kodu kısaltmak için önce helper sınıfında metod yazdık public static boolean isFieldEmty olan
            if (Helper.isFieldEmty(fld_user_name) || Helper.isFieldEmty(fld_user_uname) || Helper.isFieldEmty(fld_user_pass)) {
                Helper.showMsg("fill");

            } else {
                // veri ekleme işlemine geçiyoruz 2:
                String name = fld_user_name.getText();
                String uname = fld_user_uname.getText();
                String pass = fld_user_pass.getText();
                String type = cmb_user_type.getSelectedItem().toString();
                if (User.add(name, uname, pass, type)) {
                    Helper.showMsg("done");
                    loadUserModel();
                    loadEducatorCombo();
                } else {
                    Helper.showMsg("error");

                    loadUserModel();
                    loadEducatorCombo();
                    fld_user_name.setText(null);
                    fld_user_uname.setText(null);
                    fld_user_pass.setText(null);
                }
            }
        });
        btn_user_sh.addActionListener(e -> {
            String name = fld_sh_user_name.getText();
            String uname = fld_sh_user_uname.getText();
            String type = cmb_sh_user_type.getSelectedItem().toString();
            String query = User.searchQuery(name, uname, type);

            loadUserModel(User.serchUserList(query));
        });
        btn_logout.addActionListener(e -> {
            dispose();
            LoginGUI login = new LoginGUI();
        });
        btn_Patika_add.addActionListener(e -> {
            if (Helper.isFieldEmty(fld_patika_name)) {
                Helper.showMsg("fill");
            } else {
                if (Patika.add(fld_patika_name.getText())) {
                    Helper.showMsg("done");
                    LoadPatikaModel();
                    loadPatikaCombo();
                    loadEducatorCombo();

                    fld_patika_name.setText(null);
                } else {
                    Helper.showMsg("error");
                }
            }
        });
        btn_course_add.addActionListener(e -> {
            Item patikaItem = (Item) cmb_course_patika.getSelectedItem();
            Item userItem = (Item) cmb_course_user.getSelectedItem();
            if (Helper.isFieldEmty(fld_course_name) || Helper.isFieldEmty(fld_course_lang)) {
                Helper.showMsg("fill");
            } else {
                if (Course.add(userItem.getKey(), patikaItem.getKey(), fld_course_name.getText(), fld_course_lang.getText())) {
                    Helper.showMsg("done");
                    loadCouseModel();
                    fld_course_lang.setText(null);
                    fld_course_name.setText(null);
                } else {
                    Helper.showMsg("error");
                }
            }

        });


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

        btn_delete_course.addActionListener(e -> {
            var courseId = Integer.parseInt(tbl_course_list.getValueAt(tbl_course_list.getSelectedRow(), 0).toString());
            if (Course.delete(courseId)) {
                Helper.showMsg("done");
                loadCouseModel();
            } else {
                Helper.showMsg("error");
            }
        });

        btn_pre_edit_course.addActionListener(e -> {
            var courseId = Integer.parseInt(tbl_course_list.getValueAt(tbl_course_list.getSelectedRow(), 0).toString());
            var course = Course.getCourseById(courseId);
            if (course != null) {
                fld_course_name.setText(course.getName());
                fld_course_lang.setText(course.getLang());

                for (int i = 0; i < cmb_course_patika.getItemCount(); i++) {
                    if (((Item) cmb_course_patika.getItemAt(i)).getKey() == course.getPatika_id()) {
                        cmb_course_patika.setSelectedIndex(i);
                    }
                }

                for (int i = 0; i < cmb_course_user.getItemCount(); i++) {
                    if (((Item) cmb_course_user.getItemAt(i)).getKey() == course.getUser_id()) {
                        cmb_course_user.setSelectedIndex(i);
                    }
                }

                btn_course_edit.setEnabled(true);
            } else {
                Helper.showMsg("error");
            }

        });

        btn_pre_edit_patika.addActionListener(e -> {
            var patikaId = Integer.parseInt(tbl_patika_list.getValueAt(tbl_patika_list.getSelectedRow(), 0).toString());
            var patika = Patika.getFetch(patikaId);
            if (patika != null) {
                fld_patika_name.setText(patika.getName());

                btn_patika_edit.setEnabled(true);
            } else {
                Helper.showMsg("error");
            }
        });

        btn_course_edit.addActionListener(e -> {
            var courseId = Integer.parseInt(tbl_course_list.getValueAt(tbl_course_list.getSelectedRow(), 0).toString());
            var userId = ((Item) cmb_course_user.getSelectedItem()).getKey();
            var patikaId = ((Item) cmb_course_patika.getSelectedItem()).getKey();
            if (Course.update(courseId, userId, patikaId, fld_course_name.getText(), fld_course_lang.getText())) {
                Helper.showMsg("done");
                loadContentModel();
                fld_course_name.setText(null);
                fld_course_lang.setText(null);
            } else {
                Helper.showMsg("error");
            }
        });

        btn_patika_edit.addActionListener(e -> {
            var patikaId = Integer.parseInt(tbl_patika_list.getValueAt(tbl_patika_list.getSelectedRow(), 0).toString());
            if (Patika.update(patikaId, fld_patika_name.getText())) {
                Helper.showMsg("done");
                loadPatikaModel();
                fld_patika_name.setText(null);
            } else {
                Helper.showMsg("error");
            }
        });

        btn_delete_patika.addActionListener(e -> {
            var patikaId = Integer.parseInt(tbl_patika_list.getValueAt(tbl_patika_list.getSelectedRow(), 0).toString());
            if (Patika.delete(patikaId)) {
                Helper.showMsg("done");
                loadPatikaModel();
            } else {
                Helper.showMsg("error");
            }
        });

        btn_pre_edit_user.addActionListener(e -> {
            var userId = Integer.parseInt(tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 0).toString());
            var user = User.getFetch(userId);
            if (user != null) {
                fld_user_name.setText(user.getName());
                fld_user_uname.setText(user.getUname());
                fld_user_pass.setText(user.getPass());
            } else {
                Helper.showMsg("error");
            }
        });

        btn_user_edit.addActionListener(e -> {
            var userId = Integer.parseInt(tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 0).toString());
            if (User.update(userId, fld_user_name.getText(), fld_user_uname.getText(), fld_user_pass.getText(), cmb_user_type.getSelectedItem().toString())) {
                Helper.showMsg("done");
                loadUserModel();
                fld_user_pass.setText(null);
                fld_user_uname.setText(null);
                fld_user_name.setText(null);

            } else {
                Helper.showMsg("error");
            }
        });
        btn_delete_user.addActionListener(e -> {
            var userId = Integer.parseInt(tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 0).toString());
            if (User.delete(userId)) {
                loadUserModel();
                loadEducatorCombo();
                loadCouseModel();
            }
        });
    }

    public static void main(String[] args) {
        Helper.setLayout();
        Operator op = new Operator();
        op.setId(1);
        op.setName("Operatör Operatöroğlu");
        op.setPass("123");
        op.setType("operator");
        op.setUname("operatör");
        OperatorGUI opGUI = new OperatorGUI(op);
    }

    private void loadContentModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_content_list.getModel();
        clearModel.setRowCount(0);
        int i = 0;
        for (Content obj : Content.getList()) {
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

    private void loadPatikaModel() {
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


    private void loadCouseModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_course_list.getModel();
        clearModel.setRowCount(0);
        int i = 0;
        for (Course obj : Course.getList()) {
            i = 0;
            row_course_list[i++] = obj.getId();
            row_course_list[i++] = obj.getName();
            row_course_list[i++] = obj.getLang();
            row_course_list[i++] = obj.getPatika().getName();
            row_course_list[i++] = obj.getEducator().getName();
            mdl_course_list.addRow(row_course_list);
        }
    }


    public void loadUserModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_user_list.getModel();
        clearModel.setRowCount(0);


        for (User obj : User.getList()) {
            int i = 0;

            row_user_list[i++] = obj.getId();
            row_user_list[i++] = obj.getName();
            row_user_list[i++] = obj.getUname();
            row_user_list[i++] = obj.getPass();
            row_user_list[i++] = obj.getType();
            mdl_user_list.addRow(row_user_list);
        }
    }

    public void loadPatikaCombo() {
        cmb_course_patika.removeAllItems();
        for (Patika obj : Patika.getList()) {
            cmb_course_patika.addItem(new Item(obj.getId(), obj.getName()));
        }
    }

    public void loadEducatorCombo() {
        cmb_course_user.removeAllItems();
        for (User obj : User.getList()) {
            if (obj.getType().equals("educator")) {
                cmb_course_user.addItem(new Item(obj.getId(), obj.getName()));
            }
        }

    }

    public void loadContentCourseCombo() {
        cmb_content_course_add.removeAllItems();
        for (Course obj : Course.getList()) {
            cmb_content_course_add.addItem(new Item(obj.getId(), obj.getName()));

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

    public void loadUserModel(ArrayList<User> list) {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_user_list.getModel();
        clearModel.setRowCount(0);

        for (User obj : list) {
            int i = 0;

            row_user_list[i++] = obj.getId();
            row_user_list[i++] = obj.getName();
            row_user_list[i++] = obj.getUname();
            row_user_list[i++] = obj.getPass();
            row_user_list[i++] = obj.getType();
            mdl_user_list.addRow(row_user_list);
        }

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
