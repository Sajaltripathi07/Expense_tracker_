package com.example.expensetracker.service;

import com.example.expensetracker.model.Expense;
import com.example.expensetracker.model.FinancialAccount;
import com.example.expensetracker.model.User;
import com.example.expensetracker.repository.ExpenseRepository;
import com.example.expensetracker.repository.FinancialAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private FinancialAccountRepository financialAccountRepository;

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public List<Expense> getExpensesByUser(User user) {
        return expenseRepository.findByAccount_User(user);
    }

    public BigDecimal getTotalAmount() {
        List<Expense> expenses = expenseRepository.findAll();
        BigDecimal total = BigDecimal.ZERO;
        for (Expense expense : expenses) {
            total = total.add(expense.getAmount());
        }
        return total;
    }

    public BigDecimal getTotalAmountByUser(User user) {
        List<Expense> expenses = expenseRepository.findByAccount_User(user);
        BigDecimal total = BigDecimal.ZERO;
        for (Expense expense : expenses) {
            total = total.add(expense.getAmount());
        }
        return total;
    }

    public BigDecimal getHighestExpense() {
        List<Expense> expenses = expenseRepository.findAll();
        BigDecimal highest = BigDecimal.ZERO;
        for (Expense expense : expenses) {
            if (expense.getAmount().compareTo(highest) > 0) {
                highest = expense.getAmount();
            }
        }
        return highest;
    }

    public BigDecimal getHighestExpenseByUser(User user) {
        List<Expense> expenses = expenseRepository.findByAccount_User(user);
        BigDecimal highest = BigDecimal.ZERO;
        for (Expense expense : expenses) {
            if (expense.getAmount().compareTo(highest) > 0) {
                highest = expense.getAmount();
            }
        }
        return highest;
    }

    public long getTotalEntries() {
        return expenseRepository.count();
    }

    public Map<String, BigDecimal> getCategorySummaryByUser(User user) {
        List<Expense> expenses = expenseRepository.findByAccount_User(user);
        Map<String, BigDecimal> categorySummary = new HashMap<>();
        for (Expense expense : expenses) {
            String category = expense.getCategory();
            if (category != null) {
                categorySummary.put(category, categorySummary.getOrDefault(category, BigDecimal.ZERO).add(expense.getAmount()));
            }
        }
        return categorySummary;
    }

    // Save a new expense
    public void saveExpense(Expense expense) {
        if (expense.getCategory() == null || expense.getCategory().isEmpty()) {
            expense.setCategory(categorizeExpense(expense.getDescription()));
        }
        if (expense.getDate() == null) {
            expense.setDate(LocalDate.now());
        }

        // Ensure the account is valid and fetched from DB
        if (expense.getAccount() == null || expense.getAccount().getId() == null) {
            throw new IllegalArgumentException("Expense must be linked to a valid financial account.");
        }

        FinancialAccount account = financialAccountRepository.findById(expense.getAccount().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid financial account ID."));

        // Update balance and save
        account.setBalance(account.getBalance().subtract(expense.getAmount()));
        financialAccountRepository.save(account);

        // Link the full account entity
        expense.setAccount(account);
        expenseRepository.save(expense);
    }

    public Expense getExpenseById(Long id) {
        return expenseRepository.findById(id).orElse(null);
    }

    // Optionally update this too if you handle account changes during update
    public void updateExpense(Expense updatedExpense) {
        if (updatedExpense.getCategory() == null || updatedExpense.getCategory().isEmpty()) {
            updatedExpense.setCategory(categorizeExpense(updatedExpense.getDescription()));
        }
        if (updatedExpense.getDate() == null) {
            updatedExpense.setDate(LocalDate.now());
        }

        if (updatedExpense.getAccount() == null || updatedExpense.getAccount().getId() == null) {
            throw new IllegalArgumentException("Expense must be linked to a valid financial account.");
        }

        FinancialAccount account = financialAccountRepository.findById(updatedExpense.getAccount().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid financial account ID."));

        // For now we assume amount hasn't changed — you can enhance this to recalculate based on diff
        account.setBalance(account.getBalance().subtract(updatedExpense.getAmount()));
        financialAccountRepository.save(account);

        updatedExpense.setAccount(account);
        expenseRepository.save(updatedExpense);
    }

    public void deleteExpenseById(Long id) {
        Expense expense = expenseRepository.findById(id).orElse(null);
        if (expense != null) {
            FinancialAccount account = expense.getAccount();

            if (account != null) {
                account.setBalance(account.getBalance().add(expense.getAmount()));
                financialAccountRepository.save(account);
            }

            expenseRepository.deleteById(id);
        }
    }

    public List<Expense> getExpensesByCategory(String category) {
        return expenseRepository.findByCategoryIgnoreCase(category);
    }

    private String categorizeExpense(String description) {
        description = description.toLowerCase();
        if (description.contains("food") || description.contains("restaurant") || description.contains("grocery")) {
            return "Food";
        } else if (description.contains("bus") || description.contains("uber") || description.contains("train")) {
            return "Travel";
        } else if (description.contains("rent") || description.contains("electricity") || description.contains("wifi")) {
            return "Bills";
        } else if (description.contains("movie") || description.contains("netflix") || description.contains("entertainment")) {
            return "Entertainment";
        } else {
            return "Other";
        }
    }
}
