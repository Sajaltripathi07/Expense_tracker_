package com.example.expensetracker.repository;

import com.example.expensetracker.model.FinancialAccount;
import com.example.expensetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FinancialAccountRepository extends JpaRepository<FinancialAccount, Long> {
    List<FinancialAccount> findByUser(User user);
}
