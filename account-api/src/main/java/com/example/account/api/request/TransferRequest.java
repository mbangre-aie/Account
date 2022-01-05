package com.example.account.api.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Value;

import java.math.BigDecimal;

@Value
@JsonDeserialize
public final class TransferRequest {

    public final String ownAccountId;
    public final String beneficiaryAccountId;
    public final BigDecimal amount;

    @JsonCreator
    public TransferRequest(String ownAccountId, String beneficiaryAccountId, BigDecimal amount) {
        this.ownAccountId = ownAccountId;
        this.beneficiaryAccountId = beneficiaryAccountId;
        this.amount = amount;
    }
}
