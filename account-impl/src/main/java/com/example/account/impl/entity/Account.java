/*******************************************************
 * Copyright (C) 2020 RETISIO Inc. All Rights Reserved
 *
 * This file is part of ARC project.
 * Unauthorised copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by ngollapothu
 *******************************************************/
package com.example.account.impl.entity;

import com.example.account.impl.entity.vo.Address;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.serialization.CompressedJsonable;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;

@Value
@JsonDeserialize
public class Account implements CompressedJsonable {
    public final String accountId;
    public final String name;
    public final String email;
    public final BigDecimal balance;
    public final Address address;
    public final List<Transaction> transactions;

    @JsonCreator
    public Account(String accountId, String name, String email, BigDecimal balance, Address address, List<Transaction> transactions) {
        this.accountId = accountId;
        this.name = name;
        this.email = email;
        this.balance = balance;
        this.address = address;
        this.transactions = transactions;

    }
}