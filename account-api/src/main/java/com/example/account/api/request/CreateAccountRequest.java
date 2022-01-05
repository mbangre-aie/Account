package com.example.account.api.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.Value;

import java.math.BigDecimal;

@Value
@JsonDeserialize
public final class CreateAccountRequest {

    public final String accountId;
    public final String name;
    public final String email;
    public final BigDecimal balance;
    public final Address address;

    @JsonCreator
    public CreateAccountRequest(String accountId, String name, String email, BigDecimal balance, Address address) {
        this.accountId = accountId;
        this.name = name;
        this.email = email;
        this.balance = balance;
        this.address = address;
    }

    @Value
    public static class Address {
        public final String houseNo;
        public final String street;
        public final String city;
        public final Integer pincode;
    }
}
