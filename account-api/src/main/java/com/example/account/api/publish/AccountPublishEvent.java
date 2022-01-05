package com.example.account.api.publish;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Value;

import java.math.BigDecimal;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = Void.class)
@JsonSubTypes({
        @JsonSubTypes.Type(AccountPublishEvent.AccountCreated.class),
        @JsonSubTypes.Type(AccountPublishEvent.Deposited.class),
        @JsonSubTypes.Type(AccountPublishEvent.Withdrawn.class),
        @JsonSubTypes.Type(AccountPublishEvent.Transfered.class)
})
public interface AccountPublishEvent {
    String entityId();

    @JsonTypeName(value = "account-created")
    @Value
    final class AccountCreated implements AccountPublishEvent {
        public final Account account;

        public AccountCreated(Account account) {
            this.account = account;
        }

        public String entityId(){
            return this.account.accountId;
        }
    }

    @JsonTypeName(value = "deposited")
    @Value
    final class Deposited implements AccountPublishEvent {
        public final Account account;

        public Deposited(Account account) {
            this.account = account;
        }

        public String entityId(){
            return this.account.accountId;
        }
    }

    @JsonTypeName(value = "withdrawn")
    @Value
    final class Withdrawn implements AccountPublishEvent {
        public final Account account;

        public Withdrawn(Account account) {
            this.account = account;
        }

        public String entityId(){
            return this.account.accountId;
        }
    }

    @JsonTypeName(value = "transfered")
    @Value
    final class Transfered implements AccountPublishEvent {
        public final Account account;

        public Transfered(Account account) {
            this.account = account;
        }

        public String entityId(){
            return this.account.accountId;
        }
    }

    @Value
    public class Account {
        public final String accountId;
        public final String name;
        public final String email;
        public final BigDecimal balance;

        public Account(String accountId, String name, String email, BigDecimal balance) {
            this.accountId = accountId;
            this.name = name;
            this.email = email;
            this.balance = balance;
        }
    }
}
