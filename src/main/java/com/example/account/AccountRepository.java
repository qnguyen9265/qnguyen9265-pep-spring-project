package com.example.account;

import com.example.account.Account;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer>{

    @Modifying
    @Query(value = "INSERT INTO Account (username, password) VALUES (:usernameVar, :passwordVar)", nativeQuery = true)
    void registerAccount(@Param("usernameVar") String username, @Param("passwordVar") String password);

    @Query("FROM Account WHERE username = :usernameVar AND password = :passwordVar")
    Account login(@Param("usernameVar") String username, @Param("passwordVar") String password);

    Account findAccountByUsername(String username);

    Account findAccountByUsernameAndPassword(String username, String password);
    
    @Query("FROM Account WHERE account_id = :idVar")
    Account findAccountById(@Param("idVar") int id);
}
