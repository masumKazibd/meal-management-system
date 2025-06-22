Run this script in MySQL
```
-- Drop the database if it already exists to start fresh
DROP DATABASE IF EXISTS Meal_Management_System;

-- Create the new database
CREATE DATABASE Meal_Management_System;

-- Switch to the newly created database
USE Meal_Management_System;

--
-- Table structure for table users
-- The foreign key to messes is added later with ALTER TABLE to resolve the circular dependency.
--
CREATE TABLE users (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  full_name VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL UNIQUE,
  role ENUM('admin', 'member') NOT NULL,
  mess_id INT
);

--
-- Table structure for table messes
--
CREATE TABLE messes (
  mess_id INT AUTO_INCREMENT PRIMARY KEY,
  mess_name VARCHAR(100) NOT NULL UNIQUE,
  admin_id INT,
   FOREIGN KEY (admin_id) REFERENCES users(user_id) ON DELETE SET NULL
);

--
-- Add the foreign key constraint to the users table now that messes exists.
--
ALTER TABLE users
ADD CONSTRAINT fk_user_mess
FOREIGN KEY (mess_id) REFERENCES messes(mess_id) ON DELETE SET NULL;


--
-- Table structure for table meals
--
CREATE TABLE meals (
  meal_id INT AUTO_INCREMENT PRIMARY KEY,
  mess_id INT NOT NULL,
  meal_date DATE NOT NULL,
  meal_type ENUM('breakfast', 'lunch', 'dinner') NOT NULL,
  cost_per_meal DECIMAL(10, 2) NOT NULL,
  UNIQUE (mess_id, meal_date, meal_type),
  FOREIGN KEY (mess_id) REFERENCES messes(mess_id) ON DELETE CASCADE
);

--
-- Table structure for table meal_attendance
--
CREATE TABLE meal_attendance (
  attendance_id INT AUTO_INCREMENT PRIMARY KEY,
  meal_id INT NOT NULL,
  user_id INT NOT NULL,
  is_present BOOLEAN NOT NULL DEFAULT FALSE,
  UNIQUE (meal_id, user_id),
  FOREIGN KEY (meal_id) REFERENCES meals(meal_id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

--
-- Table structure for table expenses
--
CREATE TABLE expenses (
  expense_id INT AUTO_INCREMENT PRIMARY KEY,
  mess_id INT NOT NULL,
  expense_type VARCHAR(50) NOT NULL,
  amount DECIMAL(10, 2) NOT NULL,
  expense_date DATE NOT NULL,
  description TEXT,
  added_by INT NOT NULL,
  FOREIGN KEY (mess_id) REFERENCES messes(mess_id) ON DELETE CASCADE,
  FOREIGN KEY (added_by) REFERENCES users(user_id)
);

--
-- Table structure for table payments
--
CREATE TABLE payments (
  payment_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  mess_id INT NOT NULL,
  amount DECIMAL(10, 2) NOT NULL,
  payment_date DATE NOT NULL,
  payment_for_month VARCHAR(7) NOT NULL, -- Format: 'YYYY-MM'
  is_paid BOOLEAN NOT NULL DEFAULT FALSE,
  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
  FOREIGN KEY (mess_id) REFERENCES messes(mess_id) ON DELETE CASCADE
);


-- --------------------------------------------------------
--
-- POPULATING TABLES WITH SAMPLE DATA
--
-- --------------------------------------------------------

-- Insert Users (Admins first, without mess_id)
-- Note: In a real app, passwords should be hashed.
INSERT INTO users (user_id, username, password, full_name, email, role, mess_id) VALUES
(1, 'lamisha', 'admin', 'Lamisha Rahman', 'lamisha@example.com', 'admin', NULL),
(2, 'charlie', 'pass123', 'Charlie Brown', 'charlie@example.com', 'admin', NULL);

-- Insert Messes
INSERT INTO messes (mess_id, mess_name, admin_id) VALUES
(1, 'The Foodies', 1),
(2, 'Brogrammers Hub', 2);

-- Update Admins with their mess_id
UPDATE users SET mess_id = 1 WHERE user_id = 1;
UPDATE users SET mess_id = 2 WHERE user_id = 2;

-- Insert Members
INSERT INTO users (user_id, username, password, full_name, email, role, mess_id) VALUES
(3, 'bob', 'pass123', 'Bob Builder', 'bob@example.com', 'member', 1),
(4, 'diana', 'pass123', 'Diana Prince', 'diana@example.com', 'member', 1),
(5, 'david', 'pass123', 'David Copperfield', 'david@example.com', 'member', 2);

-- Insert Meals for Mess 1 for the current month (June 2025)
INSERT INTO meals (mess_id, meal_date, meal_type, cost_per_meal) VALUES
(1, '2025-06-16', 'breakfast', 30.00),
(1, '2025-06-16', 'lunch', 60.00),
(1, '2025-06-16', 'dinner', 55.00),
(1, '2025-06-17', 'breakfast', 35.00),
(1, '2025-06-17', 'lunch', 65.00),
(1, '2025-06-17', 'dinner', 60.00),
(1, '2025-06-22', 'breakfast', 35.00),
(1, '2025-06-22', 'lunch', 65.00),
(1, '2025-06-23', 'lunch', 70.00),
(1, '2025-06-23', 'dinner', 60.00),
(1, '2025-06-25', 'breakfast', 40.00),
(1, '2025-06-25', 'dinner', 75.00);

-- Insert Meal Attendance for the current month
INSERT INTO meal_attendance (meal_id, user_id, is_present) VALUES
(1, 1, TRUE), (1, 3, TRUE), -- Breakfast on June 16
(2, 3, TRUE), (2, 4, FALSE), -- Lunch on June 16
(3, 1, TRUE), (3, 4, TRUE), -- Dinner on June 16
(8, 3, TRUE), (8, 1, TRUE); -- Lunch on June 22

-- Insert Expenses for the current month (June 2025)
INSERT INTO expenses (mess_id, expense_type, amount, expense_date, description, added_by) VALUES
(1, 'Grocery', 4500.00, '2025-06-13', 'Weekly groceries', 1),
(1, 'Electricity', 1200.00, '2025-06-15', 'Monthly electricity bill', 1),
(1, 'Internet', 1000.00, '2025-06-20', 'Internet subscription', 1),
(2, 'Rent', 15000.00, '2025-06-08', 'Monthly rent payment', 2);

-- Insert Payments for the current month (June 2025)
INSERT INTO payments (user_id, mess_id, amount, payment_date, payment_for_month, is_paid) VALUES
(3, 1, 2000.00, '2025-06-20', '2025-06', TRUE), -- Bob paid
(4, 1, 1500.00, '2025-06-21', '2025-06', TRUE); -- Diana paid

-- --- End of Script ---

```

# Meal Management System

A desktop application built with JavaFX and MySQL to help shared messes, hostels, or groups of students manage their daily meals and shared monthly expenses in a simple and organized way.

![Dashboard Screenshot](https://i.imgur.com/rLz9w3S.png) <!-- Replace with a URL to your actual screenshot -->

---

## 🌟 Features

This system provides a complete solution for mess management with two distinct user roles: **Admin** and **Member**.

* **User Management:**
    * Secure user registration and login for both admins and members.
    * Option for users to create a new mess (becoming its admin) or join an existing one.
    * Ability for users to view their profile and change their password.
* **Meal Planning & Tracking:**
    * Admins can add, edit, or remove daily meals (breakfast, lunch, dinner) along with their costs.
    * All members can view the weekly meal plan in a modern, card-based interface.
    * Members can mark their attendance for each meal on any given day.
* **Expense & Financial Management:**
    * **Admin-only:** Add shared monthly expenses like Rent, Groceries, Electricity, etc.
    * View a detailed list of all monthly expenses.
    * Automatically calculate and display a complete payment summary for all members.
* **Dynamic Dashboard:**
    * A central dashboard that shows key statistics at a glance, such as total members and total expenses for the current month.

---

## 🛠️ Technologies Used

* **Frontend:** JavaFX
* **Backend:** Java
* **Database:** MySQL
* **Build Tool:** Maven (or Gradle)
* **IDE:** IntelliJ IDEA (or any other Java IDE)

---

## ⚙️ Setup and Installation

Follow these steps to get the project up and running on your local machine.

### 1. Prerequisites

* **Java Development Kit (JDK):** Version 11 or higher.
* **JavaFX SDK:** Ensure it's configured with your IDE.
* **MySQL Server:** The database used to store all data. You can use tools like XAMPP, WAMP, or install MySQL directly.
* **MySQL JDBC Driver:** The connector that allows the Java application to communicate with the database.
* **An IDE:** IntelliJ IDEA or Eclipse is recommended.

### 2. Database Setup

1.  **Start MySQL:** Ensure your MySQL server is running.
2.  **Run the SQL Script:** Open a MySQL client (like MySQL Workbench, HeidiSQL, or phpMyAdmin) and run the complete SQL script provided in the project (`database_script.sql` or similar). This script will:
    * Create the `Meal_Management_System` database.
    * Create all the required tables (`users`, `messes`, `meals`, etc.).
    * Populate the tables with sample data, including an admin user.
3.  **Verify Database Credentials:** Open the `DatabaseConnection.java` file in the project (`src/main/java/bd/edu/seu/mealmanagementsystem/DB/`). Make sure the `URL`, `USER`, and `PASSWORD` constants match your local MySQL setup.

    ```java
    // Default Configuration
    private static final String URL = "jdbc:mysql://localhost:3306/Meal_Management_System";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Use your MySQL root password if you have one
    ```

### 3. IDE Setup (IntelliJ IDEA Example)

1.  **Clone or Download:** Get the project code onto your local machine.
2.  **Open in IDE:** Open the project folder in IntelliJ IDEA.
3.  **Configure JavaFX:**
    * Go to `File` > `Project Structure` > `Libraries`.
    * Add the path to your JavaFX SDK's `lib` folder.
4.  **Add VM Options:**
    * Go to `Run` > `Edit Configurations...`.
    * In the "VM options" field, add the following line, replacing `PATH_TO_YOUR_JAVAFX_SDK` with the actual path on your computer:
        ```
        --module-path "PATH_TO_YOUR_JAVAFX_SDK/lib" --add-modules javafx.controls,javafx.fxml
        ```
5.  **Build and Run:** Build the project and run the `Main.java` class.

---

## 🚀 Usage

Once the application is running, you can use the following sample credentials to test the features:

* **Admin User:**
    * **Username:** `lamisha`
    * **Password:** `admin`
* **Member User:**
    * **Username:** `bob`
    * **Password:** `pass123`

Log in as the admin to access features like adding expenses and meals. Log in as a member to see the restricted view.

---

## 📂 Project Structure

The project follows a standard Model-View-Controller (MVC) architecture to keep the code organized and maintainable.
```
src/main/
├── java/bd/edu/seu/mealmanagementsystem/
│   ├── controller/   # Contains all JavaFX controller classes
│   ├── DAO/          # Data Access Objects for database interaction
│   ├── DB/           # Database connection utility
│   ├── model/        # POJO (Plain Old Java Object) classes
│   └── Main.java     # Main application entry point
│
└── resources/bd/edu/seu/mealmanagementsystem/
├── css/          # Stylesheets (if any)
├── fxml/         # All FXML layout files (This is where your .fxml files are)
└── images/       # Application images and icons
```
