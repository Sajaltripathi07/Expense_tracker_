package com.example.expensetracker.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private BigDecimal amount;

    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private String category;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonBackReference
    private FinancialAccount account;

    // No-args constructor
    public Expense() {}

    // All-args constructor
    public Expense(Long id, String description, java.math.BigDecimal amount, java.time.LocalDate date, String category, FinancialAccount account) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.account = account;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public java.math.BigDecimal getAmount() { return amount; }
    public void setAmount(java.math.BigDecimal amount) { this.amount = amount; }
    public java.time.LocalDate getDate() { return date; }
    public void setDate(java.time.LocalDate date) { this.date = date; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public FinancialAccount getAccount() { return account; }
    public void setAccount(FinancialAccount account) { this.account = account; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expense expense = (Expense) o;
        return Objects.equals(id, expense.id) &&
                Objects.equals(description, expense.description) &&
                Objects.equals(amount, expense.amount) &&
                Objects.equals(date, expense.date) &&
                Objects.equals(category, expense.category) &&
                Objects.equals(account, expense.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, amount, date, category, account);
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", category='" + category + '\'' +
                ", account=" + account +
                '}';
    }
}

