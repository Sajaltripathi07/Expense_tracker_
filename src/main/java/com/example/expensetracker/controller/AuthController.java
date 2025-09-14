package com.example.expensetracker.controller;

import com.example.expensetracker.model.Role;
import com.example.expensetracker.model.User;
import com.example.expensetracker.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.expensetracker.config.JwtUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/auth")
    public String showAuthPage(@RequestParam(value = "error", required = false) String error,
                               @RequestParam(value = "logout", required = false) String logout,
                               @RequestParam(value = "success", required = false) String success,
                               Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        if (logout != null) {
            model.addAttribute("message", "Logged out successfully");
        }
        if (success != null) {
            model.addAttribute("message", "Signup successful! Please login.");
        }
        return "auth";
    }

    @PostMapping("/signup")
    public String signupForm(@RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String confirmPassword,
                           RedirectAttributes redirectAttributes) {
        if (username == null || email == null || password == null || confirmPassword == null) {
            redirectAttributes.addFlashAttribute("error", "All fields are required");
            return "redirect:/auth";
        }
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match");
            return "redirect:/auth";
        }
        if (userRepository.existsByUsername(username)) {
            redirectAttributes.addFlashAttribute("error", "Username already exists");
            return "redirect:/auth";
        }
        if (userRepository.existsByEmail(email)) {
            redirectAttributes.addFlashAttribute("error", "Email already exists");
            return "redirect:/auth";
        }
        
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.USER);
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("message", "Signup successful! Please login.");
        return "redirect:/auth?success";
    }

    @PostMapping("/api/auth/signup")
    public ResponseEntity<?> signupJson(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String email = payload.get("email");
        String password = payload.get("password");
        String confirmPassword = payload.get("confirmPassword");
        if (username == null || email == null || password == null || confirmPassword == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "All fields are required"));
        }
        if (!password.equals(confirmPassword)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Passwords do not match"));
        }
        if (userRepository.existsByUsername(username)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username already exists"));
        }
        if (userRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already exists"));
        }
        
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.USER);
        userRepository.save(user);
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        Map<String, Object> body = new HashMap<>();
        body.put("token", token);
        body.put("username", user.getUsername());
        body.put("role", user.getRole());
        return ResponseEntity.ok(body);
    }

    @GetMapping("/api/auth/me")
    public ResponseEntity<?> me(@RequestParam(value = "username", required = false) String usernameFromParam) {
        // For simplicity, accept username param as a fallback when called without security context
        String username;
        try {
            org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            username = (auth != null) ? auth.getName() : usernameFromParam;
        } catch (Exception e) {
            username = usernameFromParam;
        }
        if (username == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }
        return userRepository.findByUsername(username)
                .map(u -> ResponseEntity.ok(Map.of("username", u.getUsername(), "email", u.getEmail(), "role", u.getRole())))
                .orElse(ResponseEntity.status(404).body(Map.of("error", "User not found")));
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<?> loginJson(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String password = payload.get("password");
        if (username == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username and password are required"));
        }
        try {
            User user = userRepository.findByUsername(username)
                    .orElse(null);
            if (user == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
            }
            
            if (passwordEncoder.matches(password, user.getPassword())) {
                String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
                Map<String, Object> body = new HashMap<>();
                body.put("token", token);
                body.put("username", user.getUsername());
                body.put("role", user.getRole());
                return ResponseEntity.ok(body);
            } else {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid password"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid username or password"));
        }
    }
}
