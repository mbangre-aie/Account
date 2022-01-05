package com.example.account.api.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Value;

import java.math.BigDecimal;

@Value
@JsonDeserialize
public class TransactionRequest {
    public final String accountId;
    public final BigDecimal amount;
    public final String transactionType;   //DEPOSIT,WITHDRAW,TRANSFER   // need to convert to ENUM type
    public final String beneficiaryAccountId;   // for amount transfer to another account

    @JsonCreator
    public TransactionRequest(String accountId, BigDecimal amount, String transactionType, String beneficiaryAccountId) {
        this.accountId = accountId;
        this.amount = amount;
        this.transactionType = transactionType;
        this.beneficiaryAccountId = beneficiaryAccountId;
    }
}
