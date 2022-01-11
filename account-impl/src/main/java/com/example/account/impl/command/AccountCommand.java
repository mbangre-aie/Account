package com.example.account.impl.command;

import akka.Done;
import akka.actor.typed.ActorRef;
import com.example.account.api.request.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.Jsonable;
import com.example.account.impl.entity.Account;
import lombok.Value;

import java.util.Optional;


public interface AccountCommand extends Jsonable {

    @SuppressWarnings("serial")
    @Value
    @JsonDeserialize
    final class CreateAccount implements AccountCommand {
        public final ActorRef<Done> replyTo;
        public final CreateAccountRequest request;

        @JsonCreator
        public CreateAccount(CreateAccountRequest request, ActorRef<Done> replyTo) {
            this.request = request;
            this.replyTo = replyTo;
        }
    }

    @SuppressWarnings("serial")
    @Value
    @JsonDeserialize
    final class EmailUpdate implements AccountCommand {
        public final ActorRef<Done> replyTo;
        public final EmailUpdateRequest request;

        @JsonCreator
        public EmailUpdate(EmailUpdateRequest request, ActorRef<Done> replyTo) {
            this.request = request;
            this.replyTo = replyTo;
        }
    }

    @SuppressWarnings("serial")
    @Value
    @JsonDeserialize
    final class Deposit implements AccountCommand {
        public final ActorRef<Done> replyTo;
        public final DepositRequest request;

        @JsonCreator
        public Deposit(DepositRequest request, ActorRef<Done> replyTo) {
            this.request = request;
            this.replyTo = replyTo;
        }
    }

    @SuppressWarnings("serial")
    @Value
    @JsonDeserialize
    final class Withdraw implements AccountCommand {
        public final ActorRef<Done> replyTo;
        public final WithdrawRequest request;

        @JsonCreator
        public Withdraw(WithdrawRequest request, ActorRef<Done> replyTo) {
            this.request = request;
            this.replyTo = replyTo;
        }
    }

    @SuppressWarnings("serial")
    @Value
    @JsonDeserialize
    final class Transfer implements AccountCommand {
        public final ActorRef<Done> replyTo;
        public final TransferRequest request;

        @JsonCreator
        public Transfer(TransferRequest request, ActorRef<Done> replyTo) {
            this.request = request;
            this.replyTo = replyTo;
        }
    }

    @SuppressWarnings("serial")
    @Value
    @JsonDeserialize
    final class Transaction implements AccountCommand {
        public final ActorRef<Done> replyTo;
        public final TransactionRequest request;

        @JsonCreator
        public Transaction(TransactionRequest request, ActorRef<Done> replyTo) {
            this.request = request;
            this.replyTo = replyTo;
        }
    }

    @Value
    @JsonDeserialize
    final class GetAccount implements AccountCommand, PersistentEntity.ReplyType<Optional<Account>> {
        public final ActorRef<Optional<Account>> replyTo;

        @JsonCreator
        public GetAccount(ActorRef<Optional<Account>> replyTo) {
            this.replyTo = replyTo;
        }

    }

}
