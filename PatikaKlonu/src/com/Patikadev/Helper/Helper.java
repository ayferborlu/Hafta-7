package com.Patikadev.Helper;

import javax.swing.*;
import java.awt.*;

public class Helper {
    public static void setLayout() {
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {

                try {
                    UIManager.setLookAndFeel(info.getClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                         UnsupportedLookAndFeelException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }
    }

    public static int screenCenterPoint(String eksen, Dimension size) {
        int point;
        switch (eksen) {
            case "x":
                point = (Toolkit.getDefaultToolkit().getScreenSize().width - size.width) / 2;
                break;
            case "y":
                point = (Toolkit.getDefaultToolkit().getScreenSize().height - size.height) / 2;
                break;
            default:
                point = 0;
        }
        return point;


    }

    public static boolean isFieldEmty(JTextField field) {
        return field.getText().trim().isEmpty();
    }

    //showMsg bizim otomatik hazır mesajlarımız.
    public static void showMsg(String str) {
        optionPageTR();
        String msg;
        String title;
        switch (str) {
            case "fill":
                msg = "Lütfen tüm alanları doldurunuz";
                title = "Hata";
                break;
            case "done":
                msg = "İşlem Başarılı";
                title = "Sonuç";
                break;
            case "error":
                msg = "Bir hata oluştu";
                title = "Hata";
                break;

            default:
                msg = str;
                title = "Mesaj";
        }
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }


    public static boolean confirm(String str) {
        optionPageTR();
        String msg;
        switch (str) {
            case "sure":
                msg = "Bu işlemi gerçeklerştirmek istediğinizden emin misiniz ";
                break;
            default:
                msg = str;
        }
        return JOptionPane.showConfirmDialog(null, msg, "Son kararın mı", JOptionPane.YES_NO_OPTION) == 0;

    }


    //optionPageTR ile ekrana çıkan Lütfen tüm alanları doldurunuz uyarısına tamam butonu eklendi
    public static void optionPageTR() {
        UIManager.put("OptionPane.okButtonText", "Tamam");
        UIManager.put("OptionPane.YesButtonText", "Evet");
        UIManager.put("OptionPane.NoButtonText", "Hayır");

    }
}
