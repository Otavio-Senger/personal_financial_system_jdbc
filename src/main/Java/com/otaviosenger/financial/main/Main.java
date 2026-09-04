package com.otaviosenger.financial.main;

import com.otaviosenger.financial.dao.CategoryDAO;
import com.otaviosenger.financial.dao.TransactionDAO;
import com.otaviosenger.financial.model.Category;
import com.otaviosenger.financial.model.Transaction;
import com.otaviosenger.financial.model.Type;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        TransactionDAO transactionDAO = new TransactionDAO();
        CategoryDAO categoryDAO = new CategoryDAO();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        boolean running = true;
        menuLoop:
        while (running) {

            System.out.println("==== SISTEMA FINANCEIRO PESSOAL ====");
            System.out.println("1 - Register Transaction \n" +
                    "2 - List Transactions \n" +
                    "3 - Register Category \n" +
                    "4 - List Categories \n" +
                    "5 - Delete Transaction \n" +
                    "6 - Delete Category \n" +
                    "7 - Filter Transactions \n" +
                    "8 - Inflow and Outflow Report \n" +
                    "9 - Edit Transaction \n" +
                    "0 - Exit");

            System.out.println("Enter choice: ");
            int choice;
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid option !!!");
                scanner.next();
                continue;
            }
            scanner.nextLine(); // clear the buffer

            switch (choice) {
                case 1: // ADD new transaction

                    System.out.println("=== Register New Transaction ====");
                    System.out.println("(type -1 in numbers or 'return' in text to cancel and go back)");

                    // enter amount
                    BigDecimal amount = null;
                    while (amount == null) {
                        System.out.println("Enter amount: ");
                        try {
                            amount = scanner.nextBigDecimal();
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid amount");
                            scanner.next();
                            continue;
                        }
                        if (amount.compareTo(BigDecimal.valueOf(-1)) == 0) {
                            scanner.nextLine();
                            System.out.println("Cancelled. Returning to menu...");
                            continue menuLoop;
                        }
                    }
                    scanner.nextLine(); // clean the buffer

// ====================================================================

                    // enter description
                    System.out.println("Enter descriptions: ");
                    String description = scanner.nextLine();
                    if (description.equalsIgnoreCase("return")) {
                        System.out.println("Cancelled. Returning to menu...");
                        continue menuLoop;
                    }

// ====================================================================

                    // enter date
                    java.sql.Date sqlDate = null;
                    while (sqlDate == null) {
                        System.out.println("Enter date(yyyy-MM-dd): ");
                        String inputDate = scanner.nextLine();
                        if (inputDate.equalsIgnoreCase("return")) {
                            System.out.println("Cancelled. Returning to menu...");
                            continue menuLoop;
                        }
                        try {
                            java.util.Date dateUtil = formatter.parse(inputDate);
                            sqlDate = new java.sql.Date(dateUtil.getTime());
                        } catch (ParseException e) {
                            System.out.println("Invalid date");
                        }
                    }

// ====================================================================

                    // enter category
                    List<Category> categories = categoryDAO.listAllCategories();

                    System.out.println("Enter Category: ");
                    for (Category c : categories) {
                        System.out.println(c.getId() + "-" + c.getName() + "(" + c.getType() + ")");
                    }

                    Category selectedCategory = null;
                    while (selectedCategory == null) {
                        System.out.println("Select Category ID: ");
                        int categoryID;
                        try {
                            categoryID = scanner.nextInt();
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid option !!!");
                            scanner.next();
                            continue;
                        }
                        scanner.nextLine(); // clean the buffer

                        if (categoryID == -1) {
                            System.out.println("Cancelled. Returning to menu...");
                            continue menuLoop;
                        }

                        for (Category c : categories) {
                            if (c.getId() == categoryID) {
                                selectedCategory = c;
                                break;
                            }
                        }
                        if (selectedCategory == null) {
                            System.out.println("Invalid Category ID !!!");
                        }
                    }

// ====================================================================

                    // ADD new transaction
                    Transaction newTransaction = new Transaction(description, amount, sqlDate, selectedCategory);
                    transactionDAO.insertNewTransaction(newTransaction);
                    System.out.println("Transaction succefully added !!!");
                    break;

// ====================================================================
// ====================================================================

                case 2: // list all transactions
                    List<Transaction> transactionList = transactionDAO.listAllTransactions();

                    System.out.println("==== Your Transactions ====");
                    for (Transaction t : transactionList) {
                        System.out.println("ID: " + t.getId() + "\nAmount: " + t.getAmount() +
                                "\nDescription: " + t.getDescription() + "\nDate: " + t.getDate() +
                                "\nCategory: " + t.getCategory());

                        System.out.println("============================================================");
                    }
                    if (transactionList.isEmpty()) {
                        System.out.println("No transactions added !!!");
                    }
                    break;

// ====================================================================
// ====================================================================

                case 3: // Register new category

                    System.out.println("==== Register Category ====");
                    System.out.println("(type 'return' to cancel and go back)");

                    // enter name
                    System.out.println("Enter Name: ");
                    String nameCategory = scanner.nextLine();
                    if (nameCategory.equalsIgnoreCase("return")) {
                        System.out.println("Cancelled. Returning to menu...");
                        continue menuLoop;
                    }

// ====================================================================

                    // enter type
                    Type selectedType = null;
                    while (selectedType == null) {
                        System.out.println("Enter Type: (EXPENSES, REVENUE) ");
                        String inputType = scanner.nextLine();
                        if (inputType.equalsIgnoreCase("return")) {
                            System.out.println("Cancelled. Returning to menu...");
                            continue menuLoop;
                        }
                        try {
                            selectedType = Type.valueOf(inputType.toUpperCase());
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid Type");
                        }
                    }

// ====================================================================

                    // add new category
                    Category newCategory = new Category(nameCategory, selectedType);
                    categoryDAO.insertNewCategory(newCategory);

                    System.out.println("Category succefully added !!!");
                    break;

// ====================================================================
// ====================================================================

                case 4: // list categories
                    List<Category> categoriesList = categoryDAO.listAllCategories();

                    System.out.println("==== Categories ====");
                    for (Category c : categoriesList) {
                        System.out.println(c.getId() + "-" + c.getName() + "(" + c.getType() + ")");
                    }

                    break;

// ====================================================================
// ====================================================================

                case 5: // delete transaction
                    List<Transaction> transactionListToDelete = transactionDAO.listAllTransactions();

                    System.out.println("==== Delete Transaction ====");
                    System.out.println("(type -1 to cancel and go back)");
                    System.out.println("----------------------------------------");
                    System.out.println("==== Your Transactions ====");
                    for (Transaction t : transactionListToDelete) {
                        System.out.println("ID: " + t.getId() + "\nAmount: " + t.getAmount() +
                                "\nDescription: " + t.getDescription() + "\nDate: " + t.getDate() +
                                "\nCategory: " + t.getCategory());

                        System.out.println("============================================================");
                    }

                    Transaction transactionToDelete = null;
                    while (transactionToDelete == null) {
                        System.out.println("Enter transaction ID: ");
                        int transactionID;
                        try {
                            transactionID = scanner.nextInt();
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid option !!!");
                            scanner.next();
                            continue;
                        }
                        scanner.nextLine(); // clear the buffer

                        if (transactionID == -1) {
                            System.out.println("Cancelled. Returning to menu...");
                            continue menuLoop;
                        }

                        for (Transaction t : transactionListToDelete) {
                            if (t.getId() == transactionID) {
                                transactionToDelete = t;
                                break;
                            }
                        }
                        if (transactionToDelete == null) {
                            System.out.println("Invalid transaction ID !!!");
                        }
                    }
                    transactionDAO.deleteTransaction(transactionToDelete);
                    System.out.println("Transaction successfully deleted !!!");
                    break;

// ====================================================================
// ====================================================================

                case 6: // delete category
                    List<Category> categoriesListToDelete = categoryDAO.listAllCategories();

                    System.out.println("==== Delete Category ====");
                    System.out.println("(type -1 to cancel and go back)");
                    System.out.println("----------------------------------------");
                    System.out.println("==== Categories ====");
                    for (Category c : categoriesListToDelete) {
                        System.out.println(c.getId() + "-" + c.getName() + "(" + c.getType() + ")");
                    }

                    Category categoryToDelete = null;
                    while (categoryToDelete == null) {
                        System.out.println("Enter Category ID: ");
                        int categoryID;
                        try {
                            categoryID = scanner.nextInt();
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid option !!!");
                            scanner.next();
                            continue;
                        }
                        scanner.nextLine(); // clear the buffer

                        if (categoryID == -1) {
                            System.out.println("Cancelled. Returning to menu...");
                            continue menuLoop;
                        }

                        for (Category c : categoriesListToDelete) {
                            if (c.getId() == categoryID) {
                                categoryToDelete = c;
                                break;
                            }
                        }
                        if (categoryToDelete == null) {
                            System.out.println("Invalid Category ID !!!");
                        }
                    }
                    categoryDAO.deleteCategory(categoryToDelete);
                    System.out.println("Category successfully deleted !!!");
                    break;

// ====================================================================
// ====================================================================

                case 7: // filter transactions
                    filterLoop:
                    while (true) {
                        System.out.println("==== Filter Transactions ====");
                        System.out.println("Select the desired filter: \n" +
                                "1 - Type \n" +
                                "2 - Category \n" +
                                "3 - Date \n" +
                                "0 - Return");
                        int choiceFilter;
                        try {
                            choiceFilter = scanner.nextInt();
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid option !!!");
                            scanner.next();
                            continue;
                        }
                        scanner.nextLine(); // clear the buffer

                        switch (choiceFilter) {
                            case 1: // type filter
                                Type filterType = null;
                                while (filterType == null) {
                                    System.out.println("Enter Filter Type: (EXPENSES, REVENUE) ");
                                    String inputType = scanner.nextLine();
                                    if (inputType.equalsIgnoreCase("return")) {
                                        System.out.println("Cancelled. Returning to menu...");
                                        continue menuLoop;
                                    }
                                    try {
                                        filterType = Type.valueOf(inputType.toUpperCase());
                                    } catch (IllegalArgumentException e) {
                                        System.out.println("Invalid Type");
                                    }
                                }
                                List<Transaction> filterByType = transactionDAO.filterTransactionsByType(filterType);

                                System.out.println("==== Filtered Transactions (Type: " + filterType + ") ====");
                                for (Transaction t : filterByType) {
                                    System.out.println("ID: " + t.getId() + "\nAmount: " + t.getAmount() +
                                            "\nDescription: " + t.getDescription() + "\nDate: " + t.getDate() +
                                            "\nCategory: " + t.getCategory());
                                    System.out.println("============================================================");
                                }
                                if (filterByType.isEmpty()) {
                                    System.out.println("No transactions found for this type !!!");
                                }
                                break;

                            case 2: // category filter
                                List<Category> categoriesForFilter = categoryDAO.listAllCategories();

                                System.out.println("Enter Category: ");
                                for (Category c : categoriesForFilter) {
                                    System.out.println(c.getId() + "-" + c.getName() + "(" + c.getType() + ")");
                                }
                                Category selectedCategoryFilter = null;
                                while (selectedCategoryFilter == null) {
                                    System.out.println("Select Category ID: ");
                                    int categoryID;
                                    try {
                                        categoryID = scanner.nextInt();
                                    } catch (InputMismatchException e) {
                                        System.out.println("Invalid option !!!");
                                        scanner.next();
                                        continue;
                                    }
                                    scanner.nextLine(); // clean the buffer

                                    if (categoryID == -1) {
                                        System.out.println("Cancelled. Returning to menu...");
                                        continue menuLoop;
                                    }
                                    for (Category c : categoriesForFilter) {
                                        if (c.getId() == categoryID) {
                                            selectedCategoryFilter = c;
                                            break;
                                        }
                                    }
                                    if (selectedCategoryFilter == null) {
                                        System.out.println("Invalid Category ID !!!");
                                    }
                                }
                                List<Transaction> filterByCategory = transactionDAO.filterTransactionsByCategory(selectedCategoryFilter);

                                System.out.println("==== Filtered Transactions (Category: " + selectedCategoryFilter.getName() + ") ====");
                                for (Transaction t : filterByCategory) {
                                    System.out.println("ID: " + t.getId() + "\nAmount: " + t.getAmount() +
                                            "\nDescription: " + t.getDescription() + "\nDate: " + t.getDate() +
                                            "\nCategory: " + t.getCategory());
                                    System.out.println("============================================================");
                                }

                                if (filterByCategory.isEmpty()) {
                                    System.out.println("No transactions found for this Category !!!");
                                }
                                break;

                            case 3: // date filter

                                java.sql.Date sqlStartDate = null;
                                while (sqlStartDate == null) {
                                    System.out.println("Enter date(yyyy-MM-dd): ");
                                    String inputDate = scanner.nextLine();
                                    if (inputDate.equalsIgnoreCase("return")) {
                                        System.out.println("Cancelled. Returning to menu...");
                                        continue menuLoop;
                                    }
                                    try {
                                        java.util.Date dateUtil = formatter.parse(inputDate);
                                        sqlStartDate = new java.sql.Date(dateUtil.getTime());
                                    } catch (ParseException e) {
                                        System.out.println("Invalid date");
                                    }
                                }

                                java.sql.Date sqlEndDate = null;
                                while (sqlEndDate == null) {
                                    System.out.println("Enter date(yyyy-MM-dd): ");
                                    String inputDate = scanner.nextLine();
                                    if (inputDate.equalsIgnoreCase("return")) {
                                        System.out.println("Cancelled. Returning to menu...");
                                        continue menuLoop;
                                    }
                                    try {
                                        java.util.Date dateUtil = formatter.parse(inputDate);
                                        sqlEndDate = new java.sql.Date(dateUtil.getTime());
                                    } catch (ParseException e) {
                                        System.out.println("Invalid date");
                                    }
                                }
                                List<Transaction> filterByDate = transactionDAO.filterTransactionsByDate(sqlStartDate, sqlEndDate);
                                System.out.println("==== Filtered Transactions (" + sqlStartDate + " to " + sqlEndDate + ") ====");
                                for (Transaction t : filterByDate) {
                                    System.out.println("ID: " + t.getId() + "\nAmount: " + t.getAmount() +
                                            "\nDescription: " + t.getDescription() + "\nDate: " + t.getDate() +
                                            "\nCategory: " + t.getCategory());
                                    System.out.println("============================================================");
                                }
                                if (filterByDate.isEmpty()) {
                                    System.out.println("No transactions found for this date range !!!");
                                }
                                break;

                            case 0:
                                continue menuLoop;
                            default:
                                System.out.println("Invalid option !!!");
                                continue filterLoop;
                        }

                    }
// ====================================================================
// ====================================================================

                case 8: // inflow and outflow report
                    Map<String, BigDecimal> report = transactionDAO.inflowAndOutflowReport();

                    BigDecimal totalRevenue = report.getOrDefault("REVENUE", BigDecimal.ZERO);
                    BigDecimal totalExpenses = report.getOrDefault("EXPENSES", BigDecimal.ZERO);

                    System.out.println("==== Inflow and Outflow Report ====");
                    System.out.println("Total Revenue: " + totalRevenue);
                    System.out.println("Total Expenses: " + totalExpenses);
                    break;

// ====================================================================
// ====================================================================

                case 9: // edit transaction
                    System.out.println("==== EDIT TRANSACTION ====");

                    List<Transaction> transactionListToEdit = transactionDAO.listAllTransactions();

                    System.out.println("==== Your Transactions ====");
                    for (Transaction t : transactionListToEdit) {
                        System.out.println("ID: " + t.getId() + "\nAmount: " + t.getAmount() +
                                "\nDescription: " + t.getDescription() + "\nDate: " + t.getDate() +
                                "\nCategory: " + t.getCategory());

                        System.out.println("============================================================");
                    }
                    if (transactionListToEdit.isEmpty()) {
                        System.out.println("No transactions added !!!");
                    }

                    Transaction transactionToEdit = null;
                    while (transactionToEdit == null) {
                        System.out.println("Enter transaction ID: ");
                        int transactionID;
                        try {
                            transactionID = scanner.nextInt();
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid option !!!");
                            scanner.next();
                            continue;
                        }
                        scanner.nextLine(); // clean the buffer

                        if (transactionID == -1) {
                            System.out.println("Cancelled. Returning to menu...");
                            continue menuLoop;
                        }

                        for (Transaction t : transactionListToEdit) {
                            if (t.getId() == transactionID) {
                                transactionToEdit = t;
                                break;
                            }
                        }
                        if (transactionToEdit == null) {
                            System.out.println("Invalid transaction ID !!!");
                        }
                    }
// ====================================================================
                    System.out.println("Current descrition: " + transactionToEdit.getDescription());
                    System.out.println("New descrition (press Enter to keep current): ");
                    String newDescription = scanner.nextLine();

                    if (!newDescription.isBlank()) {
                        transactionToEdit.setDescription(newDescription);
                    }
// ====================================================================
                    System.out.println("Current amount: " + transactionToEdit.getAmount());
                    System.out.println("New amount (press Enter to keep current): ");
                    String newAmountInput = scanner.nextLine();

                    if (!newAmountInput.isBlank()) {
                        try {
                            BigDecimal newAmount = new BigDecimal(newAmountInput);
                            transactionToEdit.setAmount(newAmount);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid amount, keeping current value.");
                        }
                    }

// ====================================================================
                    System.out.println("Current date: " + transactionToEdit.getDate());
                    System.out.println("New date yyyy-MM-dd (press Enter to keep current): ");

                    java.sql.Date newDate = null;
                    boolean validDateEntered = false;

                    while (!validDateEntered) {
                        String inputDate = scanner.nextLine();
                        if (inputDate.isBlank()) {
                            validDateEntered = true;
                        } else {
                            try {
                                java.util.Date parsedDate = formatter.parse(inputDate);
                                newDate = new java.sql.Date(parsedDate.getTime());
                                transactionToEdit.setDate(newDate);
                                validDateEntered = true;
                            } catch (ParseException e) {
                                System.out.println("Invalid date, try again (or press Enter to keep current): ");
                            }
                        }
                    }
// ====================================================================
                    Category currentCategory = transactionToEdit.getCategory();
                    List<Category> categoriesToEdit = categoryDAO.listAllCategories();

                    System.out.println("Enter Category: ");
                    for (Category c : categoriesToEdit) {
                        System.out.println(c.getId() + "-" + c.getName() + "(" + c.getType() + ")");
                    }
                    System.out.println("Current category: " + currentCategory.getId() + "-" + currentCategory.getName() + "(" + currentCategory.getType() + ")");
                    System.out.println("New category id (press Enter to keep current): ");
                    String idCategory = scanner.nextLine();

                    if (!idCategory.isBlank()) {
                        try {
                            int categoryId = Integer.parseInt(idCategory);

                            Category foundCategory = null;
                            for (Category c : categoriesToEdit) {
                                if (c.getId() == categoryId) {
                                    foundCategory = c;
                                    break;
                                }
                            }
                            if (foundCategory != null) {
                                transactionToEdit.setCategory(foundCategory);
                            } else {
                                System.out.println("Invalid Category ID, keeping current category.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input, keeping current category.");
                        }
                    }
// ====================================================================

                    transactionDAO.updateTransaction(transactionToEdit);
                    System.out.println("Transaction successfully updated !!!");
                    break;

// ====================================================================
// ====================================================================

                case 0:
                    // exit
                    System.out.println("Leaving...");
                    running = false;
                    break;

// ====================================================================
// ====================================================================

                default:
                    System.out.println("Invalid option !!!");
            }
        }
        scanner.close();
    }
}