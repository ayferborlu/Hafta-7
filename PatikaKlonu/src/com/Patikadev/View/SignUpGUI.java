package com.Patikadev.View;

import com.Patikadev.Helper.Config;
import com.Patikadev.Helper.Helper;
import com.Patikadev.Model.Student;

import javax.swing.*;

public class SignUpGUI extends JFrame {
    private JPanel wrapper;
    private JTextField fld_username;
    private JTextField fld_uname;
    private JTextField fld_pass;
    private JButton btn_save;
    private JButton btn_back_to_login;


    public SignUpGUI() {
        add(wrapper);
        setSize(200, 350);
        setLocation(Helper.screenCenterPoint("x", getSize()), Helper.screenCenterPoint("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);

        btn_save.addActionListener(e -> {
            if (Student.add(fld_username.getText(), fld_uname.getText(), fld_pass.getText(), "student")) {
                Helper.showMsg("done");
            } else {
                Helper.showMsg("error");
            }
        });
        btn_back_to_login.addActionListener(e -> {
            this.dispose();


        });
    }

}
