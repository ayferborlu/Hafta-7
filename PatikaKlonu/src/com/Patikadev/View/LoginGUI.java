package com.Patikadev.View;

import com.Patikadev.Helper.Config;
import com.Patikadev.Helper.Helper;
import com.Patikadev.Model.Operator;
import com.Patikadev.Model.User;

import javax.swing.*;
import java.awt.*;

public class LoginGUI extends JFrame {
    private JPanel wrapper;
    private JPanel wtop;
    private JPanel wbottom;
    private JLabel lbl_image;
    private JTextField fld_user_uname;
    private JPasswordField fld_user_pass;
    private JButton btn_login;
    private JButton btn_signup;

    public LoginGUI() {
        add(wrapper);
        setSize(400, 400);
        setLocation(Helper.screenCenterPoint("x", getSize()), Helper.screenCenterPoint("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(".\\src\\com\\Patikadev\\View\\patikadev_image.jpg").getImage().getScaledInstance(wtop.getWidth(), 20, Image.SCALE_DEFAULT));
        lbl_image.setIcon(imageIcon);

        btn_login.addActionListener(e -> {
            if (Helper.isFieldEmty(fld_user_uname) || Helper.isFieldEmty(fld_user_pass)) {
                Helper.showMsg("fill");
            } else {

                User u = User.getFetch(fld_user_uname.getText(), fld_user_pass.getText());
                if (u == null) {
                    Helper.showMsg("Kullanıcı Bulunamadı");
                } else {
                    switch ((u.getType())) {
                        case "operator":
                            OperatorGUI opGUI = new OperatorGUI((Operator) u);
                            break;
                        case "educator":
                            EducatorGUI edGUI = new EducatorGUI(u);
                            break;
                        case "student":
                            StudentGUI stGUI = new StudentGUI(u);
                            break;

                    }
                    dispose();
                }
            }
        });
        btn_signup.addActionListener(e -> {
            SignUpGUI s = new SignUpGUI();


        });
    }


    public static void main(String[] args) {
        Helper.setLayout();
        LoginGUI login = new LoginGUI();
    }
}
