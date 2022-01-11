/*******************************************************
 * Copyright (C) 2020 RETISIO Inc. All Rights Reserved
 *
 * This file is part of ARC project.
 * Unauthorised copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by ngollapothu
 *******************************************************/
package com.example.account.impl.util;

import akka.Done;
import akka.cluster.sharding.typed.javadsl.EntityRef;
import com.example.account.api.request.*;
import com.example.account.impl.command.AccountCommand;
import com.example.account.impl.entity.Account;

import javax.inject.Singleton;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

@Singleton
public class AccountServiceUtil {

    private final Duration askTimeout = Duration.ofSeconds(10);

    public CompletionStage<Optional<Account>> getAccount(EntityRef<AccountCommand> ref) {
        return ref.ask(AccountCommand.GetAccount::new, askTimeout);
    }

    public CompletionStage<Done> createAccount(CreateAccountRequest request, EntityRef<AccountCommand> ref) {
        return ref.<Done>ask(replyTo -> new AccountCommand.CreateAccount(request,replyTo), askTimeout);
    }
    public CompletionStage<Done> deposit(DepositRequest request, EntityRef<AccountCommand> ref) {
        return ref.<Done>ask(replyTo -> new AccountCommand.Deposit(request,replyTo), askTimeout);
    }
    public CompletionStage<Done> withdraw(WithdrawRequest request, EntityRef<AccountCommand> ref) {
        return ref.<Done>ask(replyTo -> new AccountCommand.Withdraw(request,replyTo), askTimeout);
    }
    public CompletionStage<Done> transfer(TransferRequest request, EntityRef<AccountCommand> ref) {
        return ref.<Done>ask(replyTo -> new AccountCommand.Transfer(request,replyTo), askTimeout);
    }

    public CompletionStage<Done> emailUpdate(EmailUpdateRequest request, EntityRef<AccountCommand> ref) {
        return ref.<Done>ask(replyTo -> new AccountCommand.EmailUpdate(request,replyTo), askTimeout);
    }
}
