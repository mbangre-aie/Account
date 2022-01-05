package com.example.account.api.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.serialization.CompressedJsonable;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;

@Value
@JsonDeserialize
public class GetAccountResponse {
    public Account account;

    @JsonCreator
    public GetAccountResponse(Account account) {
        this.account = account;
    }

    @Value
    @JsonDeserialize
    public static class Account {
        public String accountId;
        public String name;
        public String email;
        public BigDecimal balance;
        public Address address;
        public List<Transaction> transactions;


        @JsonCreator
        public Account(String accountId, String name, String email, BigDecimal balance, Address address,List<Transaction> transactions) {
            this.accountId = accountId;
            this.name = name;
            this.email = email;
            this.balance = balance;
            this.address = address;
            this.transactions=transactions;
        }

        @Value
        public static class Address {
            public String houseNo;
            public String street;
            public String city;
            public Integer pincode;
        }
    }

    @Value
    @JsonDeserialize
    public static class Transaction implements CompressedJsonable {
        public String accountId;
        public String transactionId;
        public String transactionType;
        public String beneficiaryAccountId;
        public BigDecimal transactionAmount;
    }
}
