package com.Patikadev.Model;

import com.Patikadev.Helper.DBconnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Content {
    private final String link;
    private final String quiz;
    private final String description;
    private int id;
    private int courseId;
    private String name;


    public Content(int id, int course_id, String course_name, String description, String link, String quiz) {

        this.id = id;
        courseId = course_id;
        this.name = course_name;
        this.description = description;
        this.link = link;
        this.quiz = quiz;
    }

    public static boolean add(int courseId, String name, String description, String link, String quiz) {
        String query = "INSERT INTO content(course_id,name,description,link,quiz) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement pr = DBconnector.getInstance().prepareStatement(query);
            pr.setInt(1, courseId);
            pr.setString(2, name);
            pr.setString(3, description);
            pr.setString(4, link);
            pr.setString(5, quiz);

            return pr.executeUpdate() != -1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

    public static boolean addRating(int contentId, int userId, int rating, String review) {
        String query = "INSERT INTO content_rating(user_id,content_id,rating,review) VALUES (?,?,?,?)";
        try {
            PreparedStatement pr = DBconnector.getInstance().prepareStatement(query);
            pr.setInt(1, contentId);
            pr.setInt(2, userId);
            pr.setInt(3, rating);
            pr.setString(4, review);

            return pr.executeUpdate() != -1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

    // select *from content WHERE course_id IN(6,7,8)
    public static ArrayList<Content> getListByCourses(List<Integer> course_ids) {
        ArrayList<Content> courseList = new ArrayList<>();
        Content obj;
        try {
            var query = "SELECT * FROM content WHERE course_id IN(";

            for (int i = 0; i < course_ids.size(); i++) {
                query += "?,";
            }
            query = query.substring(0, query.length() - 1);

            query += ")";
            PreparedStatement pr = DBconnector.getInstance().prepareStatement(query);

            for (int i = 1; i <= course_ids.size(); i++) {
                pr.setInt(i, course_ids.get(i - 1));
            }
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int course_id = rs.getInt("course_id");
                String course_name = rs.getString("name");
                String description = rs.getString("description");
                String link = rs.getString("link");
                String quiz = rs.getString("quiz");

                obj = new Content(id, course_id, course_name, description, link, quiz);
                courseList.add(obj);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return courseList;
    }

    public static ArrayList<Content> getList() {
        ArrayList<Content> courseList = new ArrayList<>();
        Content obj;
        try {
            var query = "SELECT * FROM content";
            Statement pr = DBconnector.getInstance().createStatement();

            ResultSet rs = pr.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt("id");
                int course_id = rs.getInt("course_id");
                String course_name = rs.getString("name");
                String description = rs.getString("description");
                String link = rs.getString("link");
                String quiz = rs.getString("quiz");

                obj = new Content(id, course_id, course_name, description, link, quiz);
                courseList.add(obj);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return courseList;
    }

    public static Content getContentById(int contentId) {
        Content obj = null;
        try {
            var query = "SELECT * FROM content WHERE id = ?";
            PreparedStatement pr = DBconnector.getInstance().prepareStatement(query);

            pr.setInt(1, contentId);
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                int course_id = rs.getInt("course_id");
                String course_name = rs.getString("name");
                String description = rs.getString("description");
                String link = rs.getString("link");
                String quiz = rs.getString("quiz");

                obj = new Content(id, course_id, course_name, description, link, quiz);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }

    public static boolean delete(int id) {
        String query = "DELETE FROM content WHERE id = ?";
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

    public static boolean update(int id, int courseId, String name, String description, String link, String quiz) {
        String query = "UPDATE content SET course_id = ?, name = ?, description = ?, link = ?, quiz = ? WHERE id = ?";
        try {
            PreparedStatement pr = DBconnector.getInstance().prepareStatement(query);

            pr.setInt(1, courseId);
            pr.setString(2, name);
            pr.setString(3, description);
            pr.setString(4, link);
            pr.setString(5, quiz);
            pr.setInt(6, id);

            return pr.executeUpdate() != -1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            // throw new RuntimeException(e);
        }
        return true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public String getQuiz() {
        return quiz;
    }
}
