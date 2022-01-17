/*******************************************************
 * Copyright (C) 2020 RETISIO Inc. All Rights Reserved
 *
 * This file is part of ARC project.
 * Unauthorised copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by madhan
 *******************************************************/
package com.example.account.impl.util;

import com.example.account.api.publish.AccountPublishEvent;
import com.example.account.impl.entity.Account;
import com.example.account.impl.event.AccountEvent;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;

@Slf4j
@Singleton
public class AccountPublishEventConverter {

    public AccountPublishEvent convertToAccountPublishEvent(AccountEvent event, Account account) {
        if(event instanceof AccountEvent.AccountCreated){
            log.info("AccountCreated event published, id::{}", event.id());
            return new AccountPublishEvent.AccountCreated(convertToAccountDetails(account));
        } else if(event instanceof AccountEvent.Deposited){
            log.info("Deposited event published, id::{}", event.id());
            return new AccountPublishEvent.Deposited(convertToAccountDetails(account));
        } else if(event instanceof AccountEvent.Withdrawn){
            log.info("Withdrawn event published, id::{}", event.id());
            return new AccountPublishEvent.Withdrawn(convertToAccountDetails(account));
        } else if(event instanceof AccountEvent.Transfered){
            log.info("Transfered event published, id::{}", event.id());
            return new AccountPublishEvent.Transfered(convertToAccountDetails(account));
        } else if(event instanceof AccountEvent.EmailUpdated) {
            log.info("emailupdated event published, id::{}", event.id());
            return new AccountPublishEvent.EmailUpdated(convertToAccountDetails(account));
        }else {
            log.error("Try to convert non publish AccountEvent: {}", event);
            throw new IllegalArgumentException("non publish AccountEvent");
        }
    }
    private AccountPublishEvent.Account convertToAccountDetails(Account account) {
        return new AccountPublishEvent.Account(account.accountId, account.name, account.email, account.balance);
    }
}
