package com.otaviosenger.financial.dao;

import com.otaviosenger.financial.connection.ConnectionDatabase;
import com.otaviosenger.financial.model.Category;
import com.otaviosenger.financial.model.Type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {


    //insert new category
    public void insertNewCategory(Category category) {
        String sql = "insert into CATEGORY (name, type) values (?, ?)";

        try (Connection connection = ConnectionDatabase.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, category.getName());
            stmt.setString(2, category.getType().name());

            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //list all categories
    public List<Category> listAllCategories() {
        String sql = "select * from CATEGORY";
        List<Category> categories = new ArrayList<>();

        try (Connection connection = ConnectionDatabase.connect();
             PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){

            while (rs.next()) {
                Category category = new Category(
                        rs.getInt("id"),
                        rs.getString("name"),
                        Type.valueOf(rs.getString("type"))
                );
                categories.add(category);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return categories;
    }

    //delete category
    public void deleteCategory(Category category) {
        String sql = "delete from CATEGORY where id = ?";

        try (Connection connection = ConnectionDatabase.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, category.getId());
            stmt.executeUpdate();

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("This category has linked transactions; it cannot be deleted !!!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
