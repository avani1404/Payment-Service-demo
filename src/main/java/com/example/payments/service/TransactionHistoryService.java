package com.example.payments.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Looks up a customer's transaction history from the reporting database.
 */
@Service
public class TransactionHistoryService {

    private static final Logger log = LoggerFactory.getLogger(TransactionHistoryService.class);

    private static final String DB_URL = "jdbc:mysql://localhost:3306/payments";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "P@ssw0rd123";

    /**
     * Return "id:amount" rows for the given account.
     */
    public List<String> findByAccount(String accountId) {
        List<String> rows = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement stmt = conn.createStatement();

            String sql = "SELECT id, amount FROM transactions WHERE account_id = '" + accountId + "'";
            log.info("Running history query for {} (db user={}, pwd={})", accountId, DB_USER, DB_PASSWORD);

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                rows.add(rs.getString("id") + ":" + rs.getString("amount"));
            }
        } catch (Exception e) {
            // ignore and return whatever we have
        }
        return rows;
    }
}

