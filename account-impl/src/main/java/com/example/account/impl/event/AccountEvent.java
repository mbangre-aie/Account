package com.example.account.impl.event;

import com.example.account.api.request.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventShards;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.Value;

public interface AccountEvent extends Jsonable, AggregateEvent<AccountEvent> {

    AggregateEventShards<AccountEvent> TAG = AggregateEventTag.sharded(AccountEvent.class, 4);

    String id();

    @Value
    @JsonDeserialize
    final class AccountCreated implements AccountEvent {

        public final CreateAccountRequest request;

        @JsonCreator
        public AccountCreated(CreateAccountRequest request) {
            this.request = request;
        }

        @Override
        public String id() {
            return request.getAccountId();
        }

    }

    @Value
    @JsonDeserialize
    final class Deposited implements AccountEvent {

        public final DepositRequest request;

        @JsonCreator
        public Deposited(DepositRequest request) {
            this.request = request;
        }

        @Override
        public String id() {
            return request.getAccountId();
        }

    }

    @Value
    @JsonDeserialize
    final class Withdrawn implements AccountEvent {

        public final WithdrawRequest request;

        @JsonCreator
        public Withdrawn(WithdrawRequest request) {
            this.request = request;
        }

        @Override
        public String id() {
            return request.getAccountId();
        }

    }

    @Value
    @JsonDeserialize
    final class Transfered implements AccountEvent {

        public final TransferRequest request;

        @JsonCreator
        public Transfered(TransferRequest request) {
            this.request = request;
        }

        @Override
        public String id() {
            return request.getOwnAccountId();
        }

    }

    @Value
    @JsonDeserialize
    final class TransactionMade implements AccountEvent {

        public final TransactionRequest request;

        @JsonCreator
        public TransactionMade(TransactionRequest request) {
            this.request = request;
        }

        @Override
        public String id() {
            return request.getAccountId();
        }
    }

    @Override
    default AggregateEventTagger<AccountEvent> aggregateTag() {
        return TAG;
    }
}
