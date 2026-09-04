package com.otaviosenger.financial.dao;

import com.otaviosenger.financial.connection.ConnectionDatabase;
import com.otaviosenger.financial.model.Transaction;
import com.otaviosenger.financial.model.Category;
import com.otaviosenger.financial.model.Type;


import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionDAO {

    //insert new transaction
    public void insertNewTransaction(Transaction transaction) {

        String sql = "INSERT INTO TRANSACTIONS (descriptions, amount, dates, category_id) VALUES (?, ?, ?, ?)";

        try (Connection connection = ConnectionDatabase.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, transaction.getDescription());
            stmt.setBigDecimal(2, transaction.getAmount());
            stmt.setDate(3, transaction.getDate());
            stmt.setInt(4, transaction.getCategory().getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //list all transactions
    public List<Transaction> listAllTransactions() {
        String sql = "SELECT t.id AS transaction_id, t.descriptions, t.amount, t.dates, " +
                "c.id AS category_id, c.name AS category_name, c.type AS category_type " +
                "FROM TRANSACTIONS t " +
                "JOIN CATEGORY c ON c.id = t.category_id";
        List<Transaction> transactions = new ArrayList<>();

        try (Connection connection = ConnectionDatabase.connect();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Category category = new Category(
                        rs.getInt("category_id"),
                        rs.getString("category_name"),
                        Type.valueOf(rs.getString("category_type"))
                );

                Transaction transaction = new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getString("descriptions"),
                        rs.getBigDecimal("amount"),
                        rs.getDate("dates"),
                        category
                );
                transactions.add(transaction);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }

    //delete transaction
    public void deleteTransaction(Transaction transaction) {
        String sql = "DELETE FROM TRANSACTIONS WHERE id = ?";

        try (Connection connection = ConnectionDatabase.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, transaction.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //filter transactions by type
    public List<Transaction> filterTransactionsByType(Type type) {
        String sql = "SELECT t.id AS transaction_id, t.descriptions, t.amount, t.dates, " +
                "c.id AS category_id, c.name AS category_name, c.type AS category_type " +
                "FROM TRANSACTIONS t " +
                "JOIN CATEGORY c ON c.id = t.category_id " +
                "WHERE c.type = ?";
        List<Transaction> transactions = new ArrayList<>();

        try (Connection connection = ConnectionDatabase.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, type.name());
            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    Category category = new Category(
                            rs.getInt("category_id"),
                            rs.getString("category_name"),
                            Type.valueOf(rs.getString("category_type"))
                    );
                    Transaction transaction = new Transaction(
                            rs.getInt("transaction_id"),
                            rs.getString("descriptions"),
                            rs.getBigDecimal("amount"),
                            rs.getDate("dates"),
                            category
                    );
                    transactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    //filter transaction by categories
    public List<Transaction> filterTransactionsByCategory(Category category) {
        String sql = "SELECT t.id AS transaction_id, t.descriptions, t.amount, t.dates, " +
                "c.id AS category_id, c.name AS category_name, c.type AS category_type " +
                "FROM TRANSACTIONS t " +
                "JOIN CATEGORY c ON c.id = t.category_id " +
                "WHERE c.id = ?";

        List<Transaction> transactions = new ArrayList<>();

        try (Connection connection = ConnectionDatabase.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, category.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {

                    Category foundCategory = new Category(
                            rs.getInt("category_id"),
                            rs.getString("category_name"),
                            Type.valueOf(rs.getString("category_type"))
                    );
                    Transaction transaction = new Transaction(
                            rs.getInt("transaction_id"),
                            rs.getString("descriptions"),
                            rs.getBigDecimal("amount"),
                            rs.getDate("dates"),
                            foundCategory
                    );
                    transactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    //filter transactions by dates
    public List<Transaction> filterTransactionsByDate(Date startDate, Date endDate) {
        String sql = "SELECT t.id AS transaction_id, t.descriptions, t.amount, t.dates, " +
                "c.id AS category_id, c.name AS category_name, c.type AS category_type " +
                "FROM TRANSACTIONS t " +
                "JOIN CATEGORY c ON c.id = t.category_id " +
                "WHERE t.dates BETWEEN ? AND ?";
        List<Transaction> transactions = new ArrayList<>();

        try (Connection connection = ConnectionDatabase.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setDate(1, startDate);
            stmt.setDate(2, endDate);


            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {

                    Category category = new Category(
                            rs.getInt("category_id"),
                            rs.getString("category_name"),
                            Type.valueOf(rs.getString("category_type"))
                    );
                    Transaction transaction = new Transaction(
                            rs.getInt("transaction_id"),
                            rs.getString("descriptions"),
                            rs.getBigDecimal("amount"),
                            rs.getDate("dates"),
                            category
                    );
                    transactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    //Inflow and outflow report
    public Map<String, BigDecimal> inflowAndOutflowReport() {
        Map<String, BigDecimal> report = new HashMap<>();

        String sql = "SELECT c.type AS type, SUM(t.amount) AS total_amount " +
                "FROM TRANSACTIONS t " +
                "JOIN CATEGORY c ON c.id = t.category_id " +
                "GROUP BY c.type";

        try (Connection connection = ConnectionDatabase.connect();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                report.put(rs.getString("type"), rs.getBigDecimal("total_amount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return report;
    }

    //edit transaction
    public void updateTransaction(Transaction transaction) {

        String sql = "UPDATE TRANSACTIONS SET descriptions = ?, amount = ?, dates = ?, category_id = ? WHERE id = ?";

        try (Connection connection = ConnectionDatabase.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, transaction.getDescription());
            stmt.setBigDecimal(2, transaction.getAmount());
            stmt.setDate(3, transaction.getDate());
            stmt.setInt(4, transaction.getCategory().getId());
            stmt.setInt(5, transaction.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}

