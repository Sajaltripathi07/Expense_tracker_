package com.example.expensetracker.controller;

import com.example.expensetracker.model.Expense;
import com.example.expensetracker.model.FinancialAccount;
import com.example.expensetracker.model.User;
import com.example.expensetracker.repository.FinancialAccountRepository;
import com.example.expensetracker.repository.UserRepository;
import com.example.expensetracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

/**
 * Controller for handling expense-related web requests
 * Provides both web UI endpoints and REST API endpoints
 */
@Controller
public class ExpenseController {

    private static final Logger logger = LoggerFactory.getLogger(ExpenseController.class);

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private FinancialAccountRepository financialAccountRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Get the currently authenticated user
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * Root path - redirects to dashboard if authenticated, otherwise to login
     */
    @GetMapping("/")
    public String rootPath() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            !authentication.getName().equals("anonymousUser")) {
            return "redirect:/dashboard";
        } else {
            return "redirect:/auth";
        }
    }

    /**
     * Display the main dashboard with user's expenses and summary
     */
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        User currentUser = getCurrentUser();
        logger.debug("Loading dashboard for user: {}", currentUser.getUsername());
        List<Expense> expenses = expenseService.getExpensesByUser(currentUser);
        
        BigDecimal totalAmount = expenseService.getTotalAmountByUser(currentUser);
        BigDecimal highestExpense = expenseService.getHighestExpenseByUser(currentUser);
        int totalEntries = expenses.size();
        
        // Category summary for current user
        Map<String, BigDecimal> categorySummary = expenseService.getCategorySummaryByUser(currentUser);
        
        model.addAttribute("expenses", expenses);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("highestExpense", highestExpense);
        model.addAttribute("totalEntries", totalEntries);
        model.addAttribute("categorySummary", categorySummary);
        model.addAttribute("currentUser", currentUser);
        return "index";
    }

    @GetMapping("/addExpense")
    public String showAddExpenseForm(Model model) {
        User currentUser = getCurrentUser();
        model.addAttribute("expense", new Expense());
        model.addAttribute("currentUser", currentUser);
        return "add-expense";
    }

    @PostMapping("/saveExpense")
    public String saveExpense(@ModelAttribute Expense expense, RedirectAttributes redirectAttributes) {
        try {
            User currentUser = getCurrentUser();
            logger.info("Saving expense for user: {}", currentUser.getUsername());
            
            // Get or create a default account for the user
            List<FinancialAccount> accounts = financialAccountRepository.findByUser(currentUser);
            FinancialAccount defaultAccount;
            
            if (accounts.isEmpty()) {
                // Create a default account if none exist
                defaultAccount = new FinancialAccount();
                defaultAccount.setName("Default Account");
                defaultAccount.setBalance(BigDecimal.ZERO);
                defaultAccount.setBudgetLimit(new BigDecimal("10000"));
                defaultAccount.setUser(currentUser);
                financialAccountRepository.save(defaultAccount);
            } else {
                // Use the first available account
                defaultAccount = accounts.get(0);
            }
            
            // Assign the expense to the default account
            expense.setAccount(defaultAccount);
            
            expenseService.saveExpense(expense);
            redirectAttributes.addFlashAttribute("message", "Expense added successfully!");
            return "redirect:/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding expense: " + e.getMessage());
            return "redirect:/addExpense";
        }
    }

    @GetMapping("/editExpense/{id}")
    public String showEditExpenseForm(@PathVariable("id") long id, Model model) {
        User currentUser = getCurrentUser();
        Expense expense = expenseService.getExpenseById(id);
        
        if (expense == null || !expense.getAccount().getUser().getId().equals(currentUser.getId())) {
            return "redirect:/dashboard";
        }
        
        List<FinancialAccount> accounts = financialAccountRepository.findByUser(currentUser);
        model.addAttribute("expense", expense);
        model.addAttribute("accounts", accounts);
        model.addAttribute("currentUser", currentUser);
        return "update-expense";
    }

    @PostMapping("/updateExpense")
    public String updateExpense(@ModelAttribute Expense expense, RedirectAttributes redirectAttributes) {
        try {
            User currentUser = getCurrentUser();
            Expense existingExpense = expenseService.getExpenseById(expense.getId());
            
            if (existingExpense == null || !existingExpense.getAccount().getUser().getId().equals(currentUser.getId())) {
                redirectAttributes.addFlashAttribute("error", "Expense not found or access denied");
                return "redirect:/dashboard";
            }
            
            // Verify the new account belongs to the current user
            if (expense.getAccount() != null && expense.getAccount().getId() != null) {
                FinancialAccount account = financialAccountRepository.findById(expense.getAccount().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid account"));
                if (!account.getUser().getId().equals(currentUser.getId())) {
                    throw new IllegalArgumentException("Account does not belong to current user");
                }
            }
            
            expenseService.updateExpense(expense);
            redirectAttributes.addFlashAttribute("message", "Expense updated successfully!");
            return "redirect:/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating expense: " + e.getMessage());
            return "redirect:/editExpense/" + expense.getId();
        }
    }

    @GetMapping("/deleteExpense/{id}")
    public String deleteExpense(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        try {
            User currentUser = getCurrentUser();
            Expense expense = expenseService.getExpenseById(id);
            
            if (expense == null || !expense.getAccount().getUser().getId().equals(currentUser.getId())) {
                redirectAttributes.addFlashAttribute("error", "Expense not found or access denied");
                return "redirect:/dashboard";
            }
            
            expenseService.deleteExpenseById(id);
            redirectAttributes.addFlashAttribute("message", "Expense deleted successfully!");
            return "redirect:/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting expense: " + e.getMessage());
            return "redirect:/dashboard";
        }
    }

    // API endpoints for AJAX calls
    @PostMapping("/api/addExpense")
    public ResponseEntity<?> addExpense(@RequestBody Expense expense) {
        try {
            User currentUser = getCurrentUser();
            if (expense.getAccount() == null || expense.getAccount().getId() == null) {
                List<FinancialAccount> accounts = financialAccountRepository.findByUser(currentUser);
                if (!accounts.isEmpty()) {
                    expense.setAccount(accounts.get(0));
                }
            }
            expenseService.saveExpense(expense);
            return ResponseEntity.ok(Map.of("message", "Expense added successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Dashboard API endpoints
    @GetMapping("/api/dashboard/summary")
    public ResponseEntity<?> dashboardSummary() {
        User currentUser = getCurrentUser();
        List<Expense> expenses = expenseService.getExpensesByUser(currentUser);
        java.math.BigDecimal totalAmount = expenseService.getTotalAmountByUser(currentUser);
        java.math.BigDecimal highestExpense = expenseService.getHighestExpenseByUser(currentUser);
        int totalEntries = expenses.size();
        Map<String, java.math.BigDecimal> categorySummary = expenseService.getCategorySummaryByUser(currentUser);
        Map<String, Object> body = new HashMap<>();
        body.put("totalAmount", totalAmount);
        body.put("highestExpense", highestExpense);
        body.put("totalEntries", totalEntries);
        body.put("categorySummary", categorySummary);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/api/expense/{id}")
    public ResponseEntity<?> getExpense(@PathVariable("id") long id) {
        User currentUser = getCurrentUser();
        Expense expense = expenseService.getExpenseById(id);
        if (expense == null || !expense.getAccount().getUser().getId().equals(currentUser.getId())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(expense);
    }

    @PutMapping("/api/expense/{id}")
    public ResponseEntity<?> updateExpense(@PathVariable("id") long id, @RequestBody Expense expense) {
        try {
            User currentUser = getCurrentUser();
            Expense existingExpense = expenseService.getExpenseById(id);
            if (existingExpense == null || !existingExpense.getAccount().getUser().getId().equals(currentUser.getId())) {
                return ResponseEntity.notFound().build();
            }
            expenseService.updateExpense(expense);
            return ResponseEntity.ok(Map.of("message", "Expense updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/api/expense/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable("id") long id) {
        try {
            User currentUser = getCurrentUser();
            Expense expense = expenseService.getExpenseById(id);
            if (expense == null || !expense.getAccount().getUser().getId().equals(currentUser.getId())) {
                return ResponseEntity.notFound().build();
            }
            expenseService.deleteExpenseById(id);
            return ResponseEntity.ok(Map.of("message", "Expense deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
