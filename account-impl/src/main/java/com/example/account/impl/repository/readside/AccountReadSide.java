/*******************************************************
 * Copyright (C) 2020 RETISIO Inc. All Rights Reserved
 *
 * This file is part of ARC project.
 * Unauthorised copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by ngollapothu
 *******************************************************/
package com.example.account.impl.repository.readside;

import com.example.account.api.request.CreateAccountRequest;
import com.example.account.api.request.DepositRequest;
import com.example.account.api.request.TransferRequest;
import com.example.account.api.request.WithdrawRequest;
import com.example.account.impl.event.AccountEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.ReadSideProcessor;
import com.lightbend.lagom.javadsl.persistence.jdbc.JdbcReadSide;
import lombok.extern.slf4j.Slf4j;
import org.pcollections.PSequence;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class AccountReadSide extends ReadSideProcessor<AccountEvent> {

    private final JdbcReadSide jdbcReadSide;

    @Inject
    public AccountReadSide(JdbcReadSide jdbcReadSide) {
        this.jdbcReadSide = jdbcReadSide;
    }

    @Override
    public PSequence<AggregateEventTag<AccountEvent>> aggregateTags() {
        return AccountEvent.TAG.allTags();
    }

    @Override
    public ReadSideHandler<AccountEvent> buildHandler() {
        log.info(
                "Creating Account buildHandler");
        return jdbcReadSide.<AccountEvent>builder("accountEventOffset")
                .setEventHandler(AccountEvent.AccountCreated.class, (con ,evt) -> create(con, evt.id(), evt.getRequest()))
                .setEventHandler(AccountEvent.Deposited.class, (con ,evt) -> deposit(con, evt.id(), evt.getRequest()))
                .setEventHandler(AccountEvent.Withdrawn.class, (con ,evt) -> withdraw(con, evt.id(), evt.getRequest()))
                .setEventHandler(AccountEvent.Transfered.class, (con ,evt) -> transfer(con, evt.id(), evt.getRequest()))
                .build();
    }

    private static final String SAVE_ACCOUNT_QUERY = "INSERT INTO ACCOUNT(" +
            "ACCOUNT_ID, " +
            "NAME, " +
            "EMAIL, " +
            "BALANCE, " +
            "LAST_MODIFIED_TMST) " +
            "VALUES (?, ?, ?, ?, ?) " +
            "on conflict (ACCOUNT_ID) DO UPDATE " +
            "set NAME=excluded.NAME, " +
            "EMAIL=excluded.EMAIL, " +
            "BALANCE=excluded.BALANCE, " +
            "LAST_MODIFIED_TMST=now()";

    private void create(Connection connection, String entityId, CreateAccountRequest request) {
        log.info("Creating Account at read side, ID : {}", entityId);
        AtomicInteger index = new AtomicInteger();
        try (PreparedStatement ps = connection.prepareStatement(SAVE_ACCOUNT_QUERY)) {
            ps.setString(index.incrementAndGet(), entityId);
            ps.setString(index.incrementAndGet(), request.getName());
            ps.setString(index.incrementAndGet(), request.getEmail());
            ps.setBigDecimal(index.incrementAndGet(), request.getBalance());
            ps.setTimestamp(index.incrementAndGet(), Timestamp.valueOf(LocalDateTime.now()));
            ps.execute();
        } catch (Exception e) {
            log.error("Exception Occurred While Creating Account for Id : {}", entityId, e);
        }
    }

    private static final String DEPOSIT_AMOUNT_QUERY = "UPDATE ACCOUNT " +
            "SET BALANCE = BALANCE + ?, " +
            "LAST_MODIFIED_TMST = now() " +
            "WHERE ACCOUNT_ID = ?";

    private void deposit(Connection connection, String entityId, DepositRequest request) {
        log.info("deposit at read side, ID : {}", entityId);
        AtomicInteger index = new AtomicInteger();
        try (PreparedStatement ps = connection.prepareStatement(DEPOSIT_AMOUNT_QUERY)) {
            ps.setBigDecimal(index.incrementAndGet(), request.getAmount());
            ps.setString(index.incrementAndGet(), entityId);
            ps.execute();
        } catch (Exception e) {
            log.error("Exception Occurred While deposit amount for Id : {}", entityId, e);
        }
    }

    private static final String WITHDRAW_AMOUNT_QUERY = "UPDATE ACCOUNT " +
            "SET BALANCE = BALANCE - ?, " +
            "LAST_MODIFIED_TMST = now() " +
            "WHERE ACCOUNT_ID = ?";

    private void withdraw(Connection connection, String entityId, WithdrawRequest request) {
        log.info("withdraw at read side, ID : {}", entityId);
        AtomicInteger index = new AtomicInteger();
        try (PreparedStatement ps = connection.prepareStatement(WITHDRAW_AMOUNT_QUERY)) {
            ps.setBigDecimal(index.incrementAndGet(), request.getAmount());
            ps.setString(index.incrementAndGet(), entityId);
            ps.execute();
        } catch (Exception e) {
            log.error("Exception Occurred While withdraw amount for Id : {}", entityId, e);
        }
    }

    private void transfer(Connection connection, String entityId, TransferRequest request) {
        log.info("transfer at read side, ID : {}", entityId);
        AtomicInteger index = new AtomicInteger();
        try (PreparedStatement ps = connection.prepareStatement(WITHDRAW_AMOUNT_QUERY)) {
            ps.setBigDecimal(index.incrementAndGet(), request.getAmount());
            ps.setString(index.incrementAndGet(), entityId);
            ps.execute();
        } catch (Exception e) {
            log.error("Exception Occurred While transfer amount for Id : {}", entityId, e);
        }
    }
}
