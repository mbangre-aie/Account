package com.example.account.impl.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.serialization.CompressedJsonable;
import lombok.Value;

import java.math.BigDecimal;


@Value
@JsonDeserialize
public class Transaction implements CompressedJsonable {
    public final String accountId;
    public final String transactionId;
    public final String transactionType;
    public final String beneficiaryAccountId;
    public final BigDecimal transactionAmount;

    @JsonCreator
    public Transaction(String accountId, String transactionId, String transactionType, String beneficiaryAccountId, BigDecimal transactionAmount) {
        this.accountId = accountId;
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.beneficiaryAccountId = beneficiaryAccountId;
        this.transactionAmount = transactionAmount;
    }
}
