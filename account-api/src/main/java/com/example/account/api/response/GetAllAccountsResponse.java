/*******************************************************
 * Copyright (C) 2020 RETISIO Inc. All Rights Reserved
 *
 * This file is part of ARC project.
 * Unauthorised copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by ngollapothu
 *******************************************************/
package com.example.account.api.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;

public class GetAllAccountsResponse {

    public final List<Account> accounts;

    @JsonCreator
    public GetAllAccountsResponse(List<Account> accounts) {
        this.accounts = accounts;
    }

    @Value
    @JsonDeserialize
    public static class Account {
        public final String accountId;
        public final String name;
        public final String email;
        public final BigDecimal balance;

        @JsonCreator
        public Account(String accountId, String name, String email, BigDecimal balance) {
            this.accountId = accountId;
            this.name = name;
            this.email = email;
            this.balance = balance;
        }
    }
}
