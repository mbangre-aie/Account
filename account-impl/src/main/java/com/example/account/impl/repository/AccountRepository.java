/*******************************************************
 * Copyright (C) 2020 RETISIO Inc. All Rights Reserved
 *
 * This file is part of ARC project.
 * Unauthorised copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by madhan
 *******************************************************/
package com.example.account.impl.repository;

import com.example.account.impl.entity.Account;
import com.example.account.impl.repository.readside.AccountReadSide;
import com.lightbend.lagom.javadsl.persistence.ReadSide;
import com.lightbend.lagom.javadsl.persistence.jdbc.JdbcSession;
import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Singleton
@Slf4j
public class AccountRepository {

    private static final Logger logger = LoggerFactory.getLogger(AccountRepository.class);

    private final JdbcSession jdbcSession;

    private final Config config;

    @Inject
    public AccountRepository(JdbcSession jdbcSession, ReadSide readSide, Config config) {
        this.jdbcSession = jdbcSession;
        readSide.register(AccountReadSide.class);
        this.config = config;
    }

    public CompletionStage<List<Account>> getAllAccounts() {

        return jdbcSession.withConnection(connection -> {
            String queryStatement = "SELECT ACCOUNT_ID, NAME, EMAIL, BALANCE FROM account";
            PreparedStatement pStmt = connection.prepareStatement(queryStatement);
            ResultSet resultSet = pStmt.executeQuery();
            List<Account> list = new ArrayList<>();
            while (resultSet.next()) {
                Account account = new Account(
                        resultSet.getString("ACCOUNT_ID"),
                        resultSet.getString("NAME"),
                        resultSet.getString("EMAIL"),
                        resultSet.getBigDecimal("BALANCE"),
                        null,
                        null);
                list.add(account);
            }
            return list;
        }).whenComplete((input, exception) -> {
            if (exception != null) {
                log.error("Data not present in table ",exception);
            } else {
                log.info("Data  present in table for account");
            }
        }).exceptionally(throwable ->
        {
            log.error("account db exception ", throwable);
            return new ArrayList<>();
        });
    }
}
