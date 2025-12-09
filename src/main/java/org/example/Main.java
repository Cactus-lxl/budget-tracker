package org.example;

import com.budgettracker.config.DatabaseConfig;
import com.budgettracker.models.*;
import com.budgettracker.services.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Users currentUser = null;

        UserService userService = new UserService();
        TransactionService transactionService = new TransactionService();
        BudgetService budgetService = new BudgetService();
        GoalService goalService = new GoalService();
        RecurringTransactionService recurringService = new RecurringTransactionService();
        NotificationService notificationService = new NotificationService();

        System.out.println("BUDGET TRACKER DEMO");
        System.out.println();

        if (DatabaseConfig.testConnection()) {
            System.out.println("Database connected successfully");
            System.out.println();
        } else {
            System.out.println("Database connection failed");
            return;
        }

        boolean running = true;
        while (running) {
            try {
                if (currentUser == null) {
                    System.out.println("=== LOGIN MENU ===");
                    System.out.println("1. Register");
                    System.out.println("2. Login");
                    System.out.println("3. Exit");
                    System.out.print("Choose option: ");

                    int choice = Integer.parseInt(scanner.nextLine());

                    if (choice == 1) {
                        //register
                        System.out.println("\n=== User Registration ===");
                        System.out.print("Username: ");
                        String username = scanner.nextLine();
                        System.out.print("First Name: ");
                        String fname = scanner.nextLine();
                        System.out.print("Last Name: ");
                        String lname = scanner.nextLine();
                        System.out.print("Email: ");
                        String email = scanner.nextLine();
                        System.out.print("Password: ");
                        String password = scanner.nextLine();

                        try {
                            Users user = userService.registerUser(username, fname, lname, email, password);
                            System.out.println("Registration successful! User ID: " + user.getUserId());
                            System.out.println();
                        } catch (Exception e) {
                            System.out.println("Registration failed: " + e.getMessage());
                            System.out.println();
                        }

                    } else if (choice == 2) {
                        //login
                        System.out.println("\n=== User Login ===");
                        System.out.print("Username: ");
                        String username = scanner.nextLine();
                        System.out.print("Password: ");
                        String password = scanner.nextLine();

                        try {
                            currentUser = userService.login(username, password);
                            currentUser = userService.findUserById(currentUser.getUserId());
                            System.out.println("Login successful! Welcome " + currentUser.getFName());
                            System.out.println();
                        } catch (Exception e) {
                            System.out.println("Login failed: " + e.getMessage());
                            System.out.println();
                        }

                    } else if (choice == 3) {
                        running = false;
                    }

                } else {
                    System.out.println("=== MAIN MENU ===");
                    System.out.println("User: " + currentUser.getUname() + " | Balance: $" + currentUser.getBalance());
                    System.out.println();
                    System.out.println("1. Add Transaction");
                    System.out.println("2. View Transactions");
                    System.out.println("3. Update Transaction");
                    System.out.println("4. Delete Transaction");
                    System.out.println("5. Create Budget");
                    System.out.println("6. View Budgets");
                    System.out.println("7. Update Budget");
                    System.out.println("8. Delete Budget");
                    System.out.println("9. Create Goal");
                    System.out.println("10. View Goal");
                    System.out.println("11. Transfer Balance to Goal");
                    System.out.println("12. Update Goal");
                    System.out.println("13. Delete Goal");
                    System.out.println("14. Create Recurring Transaction");
                    System.out.println("15. View Recurring Transactions");
                    System.out.println("16. Update Recurring Transaction");
                    System.out.println("17. Delete Recurring Transaction");
                    System.out.println("18. View Notifications");
                    System.out.println("19. Generate Report");
                    System.out.println("20. Logout");
                    System.out.print("Choose option: ");

                    int choice = Integer.parseInt(scanner.nextLine());

                    if (choice == 1) {
                        //add transaction
                        System.out.println("\n=== Add Transaction ===");
                        System.out.println("1. Income");
                        System.out.println("2. Expense");
                        System.out.print("Choose type: ");
                        int typeChoice = Integer.parseInt(scanner.nextLine());
                        String type = (typeChoice == 1) ? "income" : "expense";

                        System.out.print("Amount: ");
                        BigDecimal amount = new BigDecimal(scanner.nextLine());
                        System.out.print("Description: ");
                        String description = scanner.nextLine();
                        System.out.print("Category ID: ");
                        int categoryId = Integer.parseInt(scanner.nextLine());

                        Transaction transaction = new Transaction();
                        transaction.setType(type);
                        transaction.setAmount(amount);
                        transaction.setDescription(description);
                        transaction.setUid(currentUser.getUserId());
                        transaction.setCid(categoryId);
                        transaction.setTime(new Timestamp(System.currentTimeMillis()));

                        try {
                            transactionService.createTransaction(transaction);
                            currentUser = userService.findUserById(currentUser.getUserId());
                            System.out.println("Transaction added! New balance: $" + currentUser.getBalance());
                            System.out.println();
                        } catch (Exception e) {
                            System.out.println("Failed: " + e.getMessage());
                            System.out.println();
                        }

                    } else if (choice == 2) {
                        //view transaction
                        System.out.println("\n=== View Transactions ===");

                        try {
                            List<Transaction> transactions = transactionService.getTransactionsByUser(currentUser.getUserId());
                            System.out.println("Found " + transactions.size() + " transactions:");
                            for (Transaction t : transactions) {
                                System.out.println("ID: " + t.getTid() + " | " + t.getType() +
                                        " | $" + t.getAmount() + " | " + t.getDescription() +
                                        " | Category: " + t.getCid());
                            }
                            System.out.println();
                        } catch (Exception e) {
                            System.out.println("Failed: " + e.getMessage());
                            System.out.println();
                        }

                    } else if (choice == 3) {
                        //update transaction
                        System.out.println("\n=== Update Transaction ===");
                        System.out.print("Enter Transaction ID: ");
                        int tid = Integer.parseInt(scanner.nextLine());

                        try {
                            Transaction existing = transactionService.getTransactionById(tid);
                            System.out.println("Current: " + existing.getDescription() + " - $" + existing.getAmount());

                            System.out.print("New Amount: ");
                            BigDecimal newAmount = new BigDecimal(scanner.nextLine());
                            System.out.print("New Description: ");
                            String newDesc = scanner.nextLine();

                            Transaction updated = new Transaction();
                            updated.setTid(tid);
                            updated.setType(existing.getType());
                            updated.setAmount(newAmount);
                            updated.setDescription(newDesc);
                            updated.setUid(existing.getUid());
                            updated.setCid(existing.getCid());
                            updated.setTime(existing.getTime());

                            transactionService.updateTransaction(tid, updated);
                            currentUser = userService.findUserById(currentUser.getUserId());
                            System.out.println("Transaction updated! New balance: $" + currentUser.getBalance());
                            System.out.println();
                        } catch (Exception e) {
                            System.out.println("Failed: " + e.getMessage());
                            System.out.println();
                        }

                    } else if (choice == 4) {
                        //delete transaction
                        System.out.println("\n=== Delete Transaction ===");
                        System.out.print("Enter Transaction ID: ");
                        int tid = Integer.parseInt(scanner.nextLine());

                        try {
                            transactionService.deleteTransaction(tid);
                            currentUser = userService.findUserById(currentUser.getUserId());
                            System.out.println("Transaction deleted! New balance: $" + currentUser.getBalance());
                            System.out.println();
                        } catch (Exception e) {
                            System.out.println("Failed: " + e.getMessage());
                            System.out.println();
                        }

                    } else if (choice == 5) {
                        //create budget
                        System.out.println("\n=== Create Budget ===");
                        System.out.print("Category ID: ");
                        int categoryId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Month (1-12): ");
                        int month = Integer.parseInt(scanner.nextLine());
                        System.out.print("Year: ");
                        int year = Integer.parseInt(scanner.nextLine());
                        System.out.print("Budget Limit: ");
                        BigDecimal limit = new BigDecimal(scanner.nextLine());


                        Budget budget = new Budget();
                        budget.setLimit_amount(limit);
                        budget.setMonth(month);
                        budget.setYear(year);
                        budget.setUid(currentUser.getUserId());
                        budget.setCid(categoryId);

                        try {
                            budgetService.createBudget(budget);
                            System.out.println("Budget created!");
                            System.out.println();
                        } catch (Exception e) {
                            System.out.println("Failed: " + e.getMessage());
                            System.out.println();
                        }

                    } else if (choice == 6) {
                        //view budget
                        System.out.println("\n=== View Budgets ===");

                        try {
                            List<Budget> budgets = budgetService.getAllBudgets(currentUser.getUserId());
                            System.out.println("Found " + budgets.size() + " budgets:");
                            for (Budget b : budgets) {
                                System.out.println("ID: " + b.getBid() + " | Category: " + b.getCid() +
                                        " | Month: " + b.getMonth() + "/" + b.getYear() +
                                        " | Limit: $" + b.getLimit_amount());
                            }
                            System.out.println();
                        } catch (Exception e) {
                            System.out.println("Failed: " + e.getMessage());
                            System.out.println();
                        }

                    } else if (choice == 7) {
                        //update budget
                        System.out.println("\n=== Update Budget ===");
                        System.out.print("Enter Budget ID: ");
                        int bid = Integer.parseInt(scanner.nextLine());

                        try {
                            Budget existing = budgetService.getBudget(bid);
                            System.out.println("Current limit: $" + existing.getLimit_amount());

                            System.out.print("New Limit: ");
                            BigDecimal newLimit = new BigDecimal(scanner.nextLine());

                            existing.setLimit_amount(newLimit);
                            budgetService.updateBudget(existing);
                            System.out.println("Budget updated!");
                            System.out.println();
                        } catch (Exception e) {
                            System.out.println("Failed: " + e.getMessage());
                            System.out.println();
                        }

                    } else if (choice == 8) {
                        //delete budget
                        System.out.println("\n=== Delete Budget ===");
                        System.out.print("Enter Budget ID: ");
                        int bid = Integer.parseInt(scanner.nextLine());

                        try {
                            budgetService.deleteBudget(bid);
                            System.out.println("Budget deleted!");
                            System.out.println();
                        } catch (Exception e) {
                            System.out.println("Failed: " + e.getMessage());
                            System.out.println();
                        }

                    } else if (choice == 9) {
                        //create goal
                        System.out.println("\n=== Create Goal ===");
                        System.out.print("Target Amount: ");
                        BigDecimal target = new BigDecimal(scanner.nextLine());
                        System.out.print("End Date (YYYY-MM-DD): ");
                        String dateStr = scanner.nextLine();

                        Goal goal = new Goal();
                        goal.setTarget(target);
                        goal.setCurrent(BigDecimal.ZERO);
                        goal.setCreate_date(java.sql.Date.valueOf(LocalDate.now()));
                        goal.setEnd_date(java.sql.Date.valueOf(dateStr));
                        goal.setUid(currentUser.getUserId());

                        try {
                            goalService.createGoal(goal);
                            System.out.println("Goal created!");
                            System.out.println();
                        } catch (Exception e) {
                            System.out.println("Failed: " + e.getMessage());
                            System.out.println();
                        }

                    } else if (choice == 10) {
                        //view goal
                        System.out.println("\n=== View Goal ===");

                        try {
                            Goal goal = goalService.getGoalOfUser(currentUser.getUserId());


                            System.out.println("Goal ID: " + goal.getGid());
                            System.out.println("Target: $" + goal.getTarget());
                            System.out.println("Current: $" + goal.getCurrent());
                            System.out.println("End Date: " + goal.getEnd_date());
                            System.out.println();
                        } catch (Exception e) {
                            System.out.println("Failed: " + e.getMessage());
                            System.out.println();
                        }

                    } else if (choice == 11) {
                        //transfer balance to goal
                        System.out.println("\n=== Transfer Balance to Goal ===");

                        try {
                            Goal goal = goalService.getGoalOfUser(currentUser.getUserId());
                            System.out.println("Current Balance: $" + currentUser.getBalance());
                            System.out.println("Goal Target: $" + goal.getTarget());
                            System.out.println("Goal Current: $" + goal.getCurrent());
                            System.out.println();
                            System.out.println("This will transfer your ENTIRE balance to the goal.");
                            System.out.print("Continue? (yes/no): ");
                            String confirm = scanner.nextLine();

                            if (confirm.equalsIgnoreCase("yes")) {
                                goalService.transferToGoal(goal.getGid(), currentUser.getUserId());
                                currentUser = userService.findUserById(currentUser.getUserId());
                                Goal updatedGoal = goalService.getGoalOfUser(currentUser.getUserId());
                                System.out.println("Transfer successful!");
                                System.out.println("New Balance: $" + currentUser.getBalance());
                                System.out.println("New Goal Progress: $" + updatedGoal.getCurrent());
                                System.out.println();
                            } else {
                                System.out.println("Transfer cancelled");
                                System.out.println();
                            }
                        } catch (Exception e) {
                            System.out.println("Failed: " + e.getMessage());
                            System.out.println();
                        }

                    } else if (choice == 12) {
                        //update goal
                        System.out.println("\n=== Update Goal ===");

                        try {
                            Goal existing = goalService.getGoalOfUser(currentUser.getUserId());
                            System.out.println("Current target: $" + existing.getTarget());

                            System.out.print("New Target: ");
                            BigDecimal newTarget = new BigDecimal(scanner.nextLine());

                            existing.setTarget(newTarget);
                            goalService.updateGoal(existing);
                            System.out.println("Goal updated!");
                            System.out.println();
                        } catch (Exception e) {
                            System.out.println("Failed: " + e.getMessage());
                            System.out.println();
                        }

                    } else if (choice == 13) {
                        //delete goal
                        System.out.println("\n=== Delete Goal ===");

                        try {
                            Goal existing = goalService.getGoalOfUser(currentUser.getUserId());
                            goalService.deleteGoal(existing.getGid());
                            System.out.println("Goal deleted!");
                            System.out.println();
                        } catch (Exception e) {
                            System.out.println("Failed: " + e.getMessage());
                            System.out.println();
                        }

                    } else if (choice == 14) {
                        //create recurring transaction
                        System.out.println("\n=== Create Recurring Transaction ===");
                        System.out.println("1. Income");
                        System.out.println("2. Expense");
                        System.out.print("Choose type: ");
                        int typeChoice = Integer.parseInt(scanner.nextLine());
                        String type = (typeChoice == 1) ? "income" : "expense";

                        System.out.print("Amount: ");
                        BigDecimal amount = new BigDecimal(scanner.nextLine());
                        System.out.print("Description: ");
                        String description = scanner.nextLine();
                        System.out.print("Category ID: ");
                        int categoryId = Integer.parseInt(scanner.nextLine());
                        System.out.println("Frequency: 1=Weekly, 2=Monthly, 3=Yearly");
                        System.out.print("Choose: ");
                        int freqChoice = Integer.parseInt(scanner.nextLine());
                        String frequency = freqChoice == 1 ? "weekly" : (freqChoice == 2 ? "monthly" : "yearly");
                        System.out.print("Start Date (YYYY-MM-DD): ");
                        String startDate = scanner.nextLine();

                        RecurringTransaction recurring = new RecurringTransaction();
                        recurring.setType(type);
                        recurring.setAmount(amount);
                        recurring.setDescription(description);
                        recurring.setUid(currentUser.getUserId());
                        recurring.setCid(categoryId);
                        recurring.setFrequency(frequency);
                        recurring.setS_date(java.sql.Date.valueOf(startDate));
                        recurring.setIs_active(true);

                        try {
                            recurringService.createRecurringTransaction(recurring);
                            System.out.println("Recurring transaction created!");
                            System.out.println();
                        } catch (Exception e) {
                            System.out.println("Failed: " + e.getMessage());
                            System.out.println();
                        }

                    } else if (choice == 15) {
                        //view recurring transaction
                        System.out.println("\n=== View Recurring Transactions ===");

                        try {
                            List<RecurringTransaction> recurring = recurringService.getTransactionsByUser(currentUser.getUserId());
                            System.out.println("Found " + recurring.size() + " recurring transactions:");
                            for (RecurringTransaction r : recurring) {
                                System.out.println("ID: " + r.getRid() + " | " + r.getType() +
                                        " | $" + r.getAmount() + " | " + r.getDescription() +
                                        " | Frequency: " + r.getFrequency() +
                                        " | Active: " + r.getIs_active());
                            }
                            System.out.println();
                        } catch (Exception e) {
                            System.out.println("Failed: " + e.getMessage());
                            System.out.println();
                        }

                    } else if (choice == 16) {
                        //update recurring transaction
                        System.out.println("\n=== Update Recurring Transaction ===");
                        System.out.print("Enter Recurring Transaction ID: ");
                        int rid = Integer.parseInt(scanner.nextLine());

                        try {
                            RecurringTransaction existing = recurringService.getRecurringTransaction(rid);
                            System.out.println("Current: " + existing.getDescription() + " - $" + existing.getAmount());

                            System.out.print("New Amount: ");
                            BigDecimal newAmount = new BigDecimal(scanner.nextLine());

                            existing.setAmount(newAmount);
                            recurringService.updateRecurringTransaction(rid, existing);
                            System.out.println("Recurring transaction updated!");
                            System.out.println();
                        } catch (Exception e) {
                            System.out.println("Failed: " + e.getMessage());
                            System.out.println();
                        }

                    } else if (choice == 17) {
                        //delete recurring transaction
                        System.out.println("\n=== Delete Recurring Transaction ===");
                        System.out.print("Enter Recurring Transaction ID: ");
                        int rid = Integer.parseInt(scanner.nextLine());

                        try {
                            recurringService.deleteRecurringTransaction(rid);
                            System.out.println("Recurring transaction deleted!");
                            System.out.println();
                        } catch (Exception e) {
                            System.out.println("Failed: " + e.getMessage());
                            System.out.println();
                        }

                    } else if (choice == 18) {
                        //view notification
                        System.out.println("\n=== Notifications ===");

                        try {
                            List<Notification> notifications = notificationService.findAllNotifications(currentUser.getUserId());
                            System.out.println("Found " + notifications.size() + " notifications:");
                            for (Notification n : notifications) {
                                System.out.println("ID: " + n.getNid() + " | " + n.getMessage() + " | " +
                                        (n.isRead() ? "Read" : "Unread"));
                            }
                            System.out.println();
                        } catch (Exception e) {
                            System.out.println("No notifications found");
                            System.out.println();
                        }

                    } else if (choice == 19) {
                        //generate report
                        System.out.println("\n=== Account Report ===");

                        currentUser = userService.findUserById(currentUser.getUserId());
                        System.out.println("User: " + currentUser.getUname());
                        System.out.println("Email: " + currentUser.getEmail());
                        System.out.println("Balance: $" + currentUser.getBalance());
                        System.out.println();

                        try {
                            Goal goal = goalService.getGoalOfUser(currentUser.getUserId());
                            System.out.println("Goal Target: $" + goal.getTarget());
                            System.out.println("Goal Current: $" + goal.getCurrent());
                            System.out.println();
                        } catch (Exception e) {
                            System.out.println("No goal set");
                            System.out.println();
                        }

                        try {
                            List<Budget> budgets = budgetService.getAllBudgets(currentUser.getUserId());
                            System.out.println("Budgets:");
                            for (Budget b : budgets) {
                                System.out.println("  Category " + b.getCid() + " | " + b.getMonth() + "/" +
                                        b.getYear() + " | Limit: $" + b.getLimit_amount());
                            }
                            System.out.println();
                        } catch (Exception e) {
                            System.out.println("No budgets found");
                            System.out.println();
                        }

                        try {
                            List<Notification> unread = notificationService.findUnreadNotifications(currentUser.getUserId());
                            System.out.println("Unread Notifications: " + unread.size());
                            for (Notification n : unread) {
                                System.out.println("  " + n.getMessage());
                            }
                            System.out.println();
                        } catch (Exception e) {
                            System.out.println("No unread notifications");
                            System.out.println();
                        }

                        try {
                            List<Transaction> transactions = transactionService.getTransactionsByUser(currentUser.getUserId());
                            System.out.println("Total Transactions: " + transactions.size());
                            System.out.println();
                        } catch (Exception e) {
                            System.out.println("No transactions found");
                            System.out.println();
                        }

                    } else if (choice == 20) {
                        //logout
                        currentUser = null;
                        System.out.println("Logged out");
                        System.out.println();
                    }
                }

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number");
                System.out.println();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println();
            }
        }

        scanner.close();
        System.out.println("Goodbye!");
    }
}