package com.example.account.api.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Value;

import java.math.BigDecimal;

@Value
@JsonDeserialize
public final class DepositRequest {

    public final String accountId;
    public final BigDecimal amount;

    @JsonCreator
    public DepositRequest(String accountId, BigDecimal amount) {
        this.accountId = accountId;
        this.amount = amount;
    }
}
