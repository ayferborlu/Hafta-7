package com.Patikadev.Model;

import com.Patikadev.Helper.DBconnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Course {
    private int id;
    private int user_id;
    private int patika_id;
    private String name;
    private String lang;

    private Patika patika;
    private User educator;

    public Course(int id, int user_id, int patika_id, String name, String lang) {
        this.id = id;
        this.user_id = user_id;
        this.patika_id = patika_id;
        this.name = name;
        this.lang = lang;
        this.patika = Patika.getFetch(patika_id);
        this.educator = User.getFetch(user_id);
    }

    public static ArrayList<Course> getList() {
        ArrayList<Course> courseList = new ArrayList<>();
        Course obj;
        try {
            Statement st = DBconnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM course");
            while (rs.next()) {
                int id = rs.getInt("id");
                int user_id = rs.getInt("user_id");
                int patika_id = rs.getInt("patika_id");
                String name = rs.getString("name");
                String lang = rs.getString("lang");
                obj = new Course(id, user_id, patika_id, name, lang);
                courseList.add(obj);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return courseList;
    }

    public static boolean add(int user_id, int patika_id, String name, String lang) {
        String query = "INSERT INTO course(user_id,patika_id,name,lang) VALUES (?,?,?,?)";
        try {
            PreparedStatement pr = DBconnector.getInstance().prepareStatement(query);
            pr.setInt(1, user_id);
            pr.setInt(2, patika_id);
            pr.setString(3, name);
            pr.setString(4, lang);
            return pr.executeUpdate() != -1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

    public static ArrayList<Course> getListByUser(int user_id) {
        ArrayList<Course> courseList = new ArrayList<>();
        Course obj;
        try {
            Statement st = DBconnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM course WHERE user_id = " + user_id);
            while (rs.next()) {
                int id = rs.getInt("id");
                int userID = rs.getInt("user_id");
                int patika_id = rs.getInt("patika_id");
                String name = rs.getString("name");
                String lang = rs.getString("lang");
                obj = new Course(id, userID, patika_id, name, lang);
                courseList.add(obj);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return courseList;
    }

    public static List<Integer> getStudentCoursesByUser(int user_id) {
        List<Integer> courseList = new ArrayList<>();

        try {
            Statement st = DBconnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT course_id FROM student_course WHERE user_id = " + user_id);
            while (rs.next()) {
                int courseId = rs.getInt("course_id");
                courseList.add(courseId);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return courseList;
    }

    public static ArrayList<Course> getListByPatika(int patikaId) {
        ArrayList<Course> courseList = new ArrayList<>();
        Course obj;
        try {
            Statement st = DBconnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM course WHERE patika_id = " + patikaId);
            while (rs.next()) {
                int id = rs.getInt("id");
                int userID = rs.getInt("user_id");
                int patika_id = rs.getInt("patika_id");
                String name = rs.getString("name");
                String lang = rs.getString("lang");
                obj = new Course(id, userID, patika_id, name, lang);
                courseList.add(obj);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return courseList;
    }

    public static Course getCourseById(int courseId) {
        Course obj = null;
        try {
            Statement st = DBconnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM course WHERE id = " + courseId);
            if (rs.next()) {
                int id = rs.getInt("id");
                int userID = rs.getInt("user_id");
                int patika_id = rs.getInt("patika_id");
                String name = rs.getString("name");
                String lang = rs.getString("lang");
                obj = new Course(id, userID, patika_id, name, lang);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }

    public static boolean delete(int id) {
        String query = "DELETE FROM course WHERE id = ?";
        try {
            PreparedStatement pr = DBconnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
            return pr.executeUpdate() != -1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            // throw new RuntimeException(e);
        }
        return true;
    }

    public static boolean update(int id, int userId, int patikaId, String name, String lang) {
        String query = "UPDATE course SET user_id=?, patika_id=?, name=?, lang=? WHERE id = ?";
        try {
            PreparedStatement pr = DBconnector.getInstance().prepareStatement(query);

            pr.setInt(1, userId);
            pr.setInt(2, patikaId);
            pr.setString(3, name);
            pr.setString(4, lang);
            pr.setInt(5, id);

            return pr.executeUpdate() != -1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            // throw new RuntimeException(e);
        }
        return true;
    }

    public static boolean addStudentToCourse(int user_id, int course_id) {
        String query = "INSERT INTO student_course(user_id,course_id) VALUES (?,?)";
        try {
            PreparedStatement pr = DBconnector.getInstance().prepareStatement(query);
            pr.setInt(1, user_id);
            pr.setInt(2, course_id);
            return pr.executeUpdate() != -1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPatika_id() {
        return patika_id;
    }

    public void setPatika_id(int patika_id) {
        this.patika_id = patika_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Patika getPatika() {
        return patika;
    }

    public void setPatika(Patika patika) {
        this.patika = patika;
    }

    public User getEducator() {
        return educator;
    }

    public void setEducator(User educator) {
        this.educator = educator;
    }
}
