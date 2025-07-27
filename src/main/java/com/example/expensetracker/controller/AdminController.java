package com.example.expensetracker.controller;

import com.example.expensetracker.model.Expense;
import com.example.expensetracker.model.User;
import com.example.expensetracker.repository.UserRepository;
import com.example.expensetracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private UserRepository userRepository;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        User currentUser = getCurrentUser();
        
        // Check if user is admin
        if (currentUser.getRole() != com.example.expensetracker.model.Role.ADMIN) {
            return "redirect:/dashboard";
        }

        // Get all users
        List<User> users = userRepository.findAll();
        
        // Get all expenses for admin overview
        List<Expense> allExpenses = expenseService.getAllExpenses();
        BigDecimal totalAmount = expenseService.getTotalAmount();
        BigDecimal highestExpense = expenseService.getHighestExpense();
        int totalEntries = allExpenses.size();

        model.addAttribute("users", users);
        model.addAttribute("expenses", allExpenses);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("highestExpense", highestExpense);
        model.addAttribute("totalEntries", totalEntries);
        model.addAttribute("currentUser", currentUser);
        
        return "admin-dashboard";
    }

    @GetMapping("/users")
    public String manageUsers(Model model) {
        User currentUser = getCurrentUser();
        
        if (currentUser.getRole() != com.example.expensetracker.model.Role.ADMIN) {
            return "redirect:/dashboard";
        }

        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        model.addAttribute("currentUser", currentUser);
        
        return "admin-users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable("id") Long userId, RedirectAttributes redirectAttributes) {
        try {
            User currentUser = getCurrentUser();
            
            if (currentUser.getRole() != com.example.expensetracker.model.Role.ADMIN) {
                redirectAttributes.addFlashAttribute("error", "Access denied");
                return "redirect:/dashboard";
            }

            User userToDelete = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (userToDelete.getId().equals(currentUser.getId())) {
                redirectAttributes.addFlashAttribute("error", "Cannot delete yourself");
                return "redirect:/admin/users";
            }

            userRepository.delete(userToDelete);
            redirectAttributes.addFlashAttribute("message", "User deleted successfully");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting user: " + e.getMessage());
        }
        
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/toggle-role")
    public String toggleUserRole(@PathVariable("id") Long userId, RedirectAttributes redirectAttributes) {
        try {
            User currentUser = getCurrentUser();
            
            if (currentUser.getRole() != com.example.expensetracker.model.Role.ADMIN) {
                redirectAttributes.addFlashAttribute("error", "Access denied");
                return "redirect:/dashboard";
            }

            User userToUpdate = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (userToUpdate.getId().equals(currentUser.getId())) {
                redirectAttributes.addFlashAttribute("error", "Cannot change your own role");
                return "redirect:/admin/users";
            }

            // Toggle between USER and ADMIN roles
            if (userToUpdate.getRole() == com.example.expensetracker.model.Role.USER) {
                userToUpdate.setRole(com.example.expensetracker.model.Role.ADMIN);
            } else {
                userToUpdate.setRole(com.example.expensetracker.model.Role.USER);
            }

            userRepository.save(userToUpdate);
            redirectAttributes.addFlashAttribute("message", "User role updated successfully");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating user role: " + e.getMessage());
        }
        
        return "redirect:/admin/users";
    }
} 