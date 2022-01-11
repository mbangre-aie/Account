package com.example.account.impl.state;

import com.example.account.api.request.*;
import com.example.account.impl.entity.Transaction;
import com.example.account.impl.entity.vo.Address;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.example.account.impl.entity.Account;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("serial")
@Value
@JsonDeserialize
public final class AccountState implements CompressedJsonable {

    public static final AccountState EMPTY = new AccountState(Optional.empty());

    public final Optional<Account> account;

    @JsonCreator
    public AccountState(Optional<Account> account) {
        this.account = account;
    }

    public AccountState createAccount(CreateAccountRequest request) {

        return new AccountState(
                Optional.of(
                        new Account(
                                request.accountId,
                                request.name,
                                request.email,
                                request.balance,
                                new Address(request.address.houseNo, request.address.street, request.address.city, request.address.pincode),
                                new ArrayList<>()
                        )
                ));
    }

    public AccountState emailUpdate(EmailUpdateRequest request) {

        Transaction transaction = new Transaction(request.getAccountId(), UUID.randomUUID().toString(),
                null, null,null);
        List<Transaction> transactions = account.get().getTransactions();
        transactions.add(transaction);

        AccountState accountState = new AccountState(
                Optional.of(
                        new Account(
                                account.get().accountId,
                                account.get().name,
                                account.get().email.replace(account.get().email,request.getEmail()),
                                account.get().balance,
                                account.get().address,
                                transactions
                        )
                ));
        return accountState;
    }
    public AccountState deposit(DepositRequest request) {
        Transaction transaction = new Transaction(request.getAccountId(), UUID.randomUUID().toString(),
                "DEPOSIT", null, request.getAmount());
        List<Transaction> transactions = account.get().getTransactions();
        transactions.add(transaction);

        return new AccountState(
                Optional.of(
                        new Account(
                                account.get().accountId,
                                account.get().name,
                                account.get().email,
                                account.get().balance.add(request.amount),
                                account.get().address,
                                transactions
                        )
                ));
    }
    public AccountState withdraw(WithdrawRequest request) {

        Transaction transaction = new Transaction(request.getAccountId(), UUID.randomUUID().toString(),
                "WITHDRAW", null, request.getAmount());
        List<Transaction> transactions = account.get().getTransactions();
        transactions.add(transaction);

        return new AccountState(
                Optional.of(
                        new Account(
                                account.get().accountId,
                                account.get().name,
                                account.get().email,
                                account.get().balance.subtract(request.amount),
                                account.get().address,
                                transactions
                        )
                ));
    }
    public AccountState transfer(TransferRequest request) {

        Transaction transaction = new Transaction(request.getOwnAccountId(), UUID.randomUUID().toString(),
                "TRANSFER", request.getBeneficiaryAccountId(), request.getAmount());
        List<Transaction> transactions = account.get().getTransactions();
        transactions.add(transaction);


        return new AccountState(
                Optional.of(
                        new Account(
                                account.get().accountId,
                                account.get().name,
                                account.get().email,
                                account.get().balance.subtract(request.amount),
                                account.get().address,
                                transactions
                        )
                ));
    }

    public AccountState performTransaction(TransactionRequest request) {
        Transaction transaction = new Transaction(request.getAccountId(), UUID.randomUUID().toString(),
                request.getTransactionType(), request.getBeneficiaryAccountId(), request.getAmount());
        List<Transaction> transactions = account.get().getTransactions();
        transactions.add(transaction);

        if ("TRANSFER".equals(request.getTransactionType())) {
            return new AccountState(
                    Optional.of(
                            new Account(
                                    account.get().accountId,
                                    account.get().name,
                                    account.get().email,
                                    account.get().balance.subtract(request.amount),
                                    account.get().address,
                                    transactions)
                    )
            );

        } else if ("DEPOSIT".equals(request.getTransactionType())) {

            return new AccountState(
                    Optional.of(
                            new Account(
                                    account.get().accountId,
                                    account.get().name,
                                    account.get().email,
                                    account.get().balance.add(request.amount),
                                    account.get().address,
                                    transactions)
                    ));

        }
        return new AccountState(
                Optional.of(
                        new Account(
                                account.get().accountId,
                                account.get().name,
                                account.get().email,
                                account.get().balance.subtract(request.amount),
                                account.get().address,
                                transactions)
                )
        );
    }
}
