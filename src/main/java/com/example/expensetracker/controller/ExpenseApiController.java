package com.example.expensetracker.controller;

import com.example.expensetracker.model.Expense;
import com.example.expensetracker.model.FinancialAccount;
import com.example.expensetracker.model.User;
import com.example.expensetracker.repository.FinancialAccountRepository;
import com.example.expensetracker.repository.UserRepository;
import com.example.expensetracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseApiController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private FinancialAccountRepository financialAccountRepository;

    @Autowired
    private UserRepository userRepository;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @GetMapping
    public ResponseEntity<List<Expense>> list() {
        User currentUser = getCurrentUser();
        return ResponseEntity.ok(expenseService.getExpensesByUser(currentUser));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Expense expense) {
        try {
            User currentUser = getCurrentUser();
            if (expense.getAccount() == null || expense.getAccount().getId() == null) {
                List<FinancialAccount> accounts = financialAccountRepository.findByUser(currentUser);
                FinancialAccount account;
                if (accounts.isEmpty()) {
                    account = new FinancialAccount();
                    account.setName("Default Account");
                    account.setBalance(BigDecimal.ZERO);
                    account.setBudgetLimit(new BigDecimal("10000"));
                    account.setUser(currentUser);
                    financialAccountRepository.save(account);
                } else {
                    account = accounts.get(0);
                }
                expense.setAccount(account);
            }
            expenseService.saveExpense(expense);
            return ResponseEntity.ok(expense);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") long id) {
        User currentUser = getCurrentUser();
        Expense expense = expenseService.getExpenseById(id);
        if (expense == null || !expense.getAccount().getUser().getId().equals(currentUser.getId())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(expense);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody Expense incoming) {
        try {
            User currentUser = getCurrentUser();
            Expense existing = expenseService.getExpenseById(id);
            if (existing == null || !existing.getAccount().getUser().getId().equals(currentUser.getId())) {
                return ResponseEntity.notFound().build();
            }
            incoming.setId(id);
            if (incoming.getAccount() != null && incoming.getAccount().getId() != null) {
                FinancialAccount account = financialAccountRepository.findById(incoming.getAccount().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid account"));
                if (!account.getUser().getId().equals(currentUser.getId())) {
                    throw new IllegalArgumentException("Account does not belong to current user");
                }
            }
            expenseService.updateExpense(incoming);
            return ResponseEntity.ok(Map.of("message", "Expense updated"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        try {
            User currentUser = getCurrentUser();
            Expense expense = expenseService.getExpenseById(id);
            if (expense == null || !expense.getAccount().getUser().getId().equals(currentUser.getId())) {
                return ResponseEntity.notFound().build();
            }
            expenseService.deleteExpenseById(id);
            return ResponseEntity.ok(Map.of("message", "Expense deleted"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}


