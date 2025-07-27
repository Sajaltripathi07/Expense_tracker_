package com.example.expensetracker.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
public class FinancialAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private BigDecimal balance;
    private BigDecimal budgetLimit;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Expense> expenses;

    // No-args constructor
    public FinancialAccount() {}

    // All-args constructor
    public FinancialAccount(Long id, String name, java.math.BigDecimal balance, java.math.BigDecimal budgetLimit, User user, java.util.List<Expense> expenses) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.budgetLimit = budgetLimit;
        this.user = user;
        this.expenses = expenses;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public java.math.BigDecimal getBalance() { return balance; }
    public void setBalance(java.math.BigDecimal balance) { this.balance = balance; }
    public java.math.BigDecimal getBudgetLimit() { return budgetLimit; }
    public void setBudgetLimit(java.math.BigDecimal budgetLimit) { this.budgetLimit = budgetLimit; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public java.util.List<Expense> getExpenses() { return expenses; }
    public void setExpenses(java.util.List<Expense> expenses) { this.expenses = expenses; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FinancialAccount that = (FinancialAccount) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(balance, that.balance) &&
                Objects.equals(budgetLimit, that.budgetLimit) &&
                Objects.equals(user, that.user) &&
                Objects.equals(expenses, that.expenses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, balance, budgetLimit, user, expenses);
    }

    @Override
    public String toString() {
        return "FinancialAccount{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                ", budgetLimit=" + budgetLimit +
                ", user=" + user +
                ", expenses=" + expenses +
                '}';
    }
}
