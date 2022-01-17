package com.example.account.impl;

import akka.Done;
import akka.NotUsed;
import akka.cluster.sharding.typed.javadsl.ClusterSharding;
import akka.cluster.sharding.typed.javadsl.Entity;
import akka.cluster.sharding.typed.javadsl.EntityRef;
import akka.japi.Pair;
import com.example.account.api.request.*;
import com.example.account.api.response.GetAllAccountsResponse;
import com.example.account.impl.entity.Transaction;
import com.example.account.impl.event.AccountEvent;
import com.example.account.impl.repository.AccountRepository;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.broker.Topic;
import com.lightbend.lagom.javadsl.broker.TopicProducer;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.example.account.api.AccountService;
import com.example.account.api.publish.AccountPublishEvent;
import com.example.account.api.response.GetAccountResponse;
import com.example.account.impl.aggregate.AccountAggregate;
import com.example.account.impl.command.AccountCommand;
import com.example.account.impl.util.AccountPublishEventConverter;
import com.example.account.impl.util.AccountServiceUtil;
import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
public class AccountServiceImpl implements AccountService {

    private final PersistentEntityRegistry registry;
    private final Duration askTimeout = Duration.ofSeconds(5);
    private ClusterSharding clusterSharding;
    private final Config config;

    @Inject
    private AccountServiceUtil accountServiceUtil;

    @Inject
    private AccountRepository accountRepository;

    @Inject
    private AccountPublishEventConverter accountPublishEventConverter;

    @Inject
     public AccountServiceImpl(PersistentEntityRegistry registry,
                               ClusterSharding clusterSharding,
                               Config config) {
        this.clusterSharding = clusterSharding;
        // The persistent entity registry is only required to build an event stream for the TopicProducer
        this.registry = registry;
        this.config = config;

        // register the Aggregate as a sharded entity
        registerAllEntity(registry, clusterSharding, config);
    }

    private void registerAllEntity(PersistentEntityRegistry registry, ClusterSharding clusterSharding, Config config) {

        clusterSharding.init(
                Entity.of(
                        AccountAggregate.ENTITY_TYPE_KEY,
                        context -> AccountAggregate.registerEntity(
                                context,
                                config.getInt("account.retentionCriteria.numberOfEvents"),
                                config.getInt("account.retentionCriteria.keepNumberOfSnapshots")
                        )
                )
        );
    }

    public EntityRef<AccountCommand> accountAggregateRef(String entityId) {
        return clusterSharding.entityRefFor(AccountAggregate.ENTITY_TYPE_KEY, entityId);
    }

    public List<GetAccountResponse.Transaction> convert(List<Transaction> transactions) {
        return transactions.stream().map(transaction ->
                new GetAccountResponse.Transaction(transaction.getAccountId(),
                        transaction.getTransactionId(),
                        transaction.getTransactionType(),
                        transaction.getBeneficiaryAccountId(),
                        transaction.getTransactionAmount())
        ).collect(Collectors.toList());
    }


    @Override
    public ServiceCall<NotUsed, GetAccountResponse> getAccountDetails(String id) {
        return notUsed -> {
            return accountServiceUtil.getAccount(accountAggregateRef(id))
                    .thenApply(optionalAccount -> {
                        return optionalAccount.map(account -> {
                            return new GetAccountResponse(new GetAccountResponse.Account(account.accountId, account.name, account.email, account.balance, new GetAccountResponse.Account.Address(account.address.houseNo, account.address.street, account.address.city, account.address.pincode),convert(account.getTransactions())));
                        }).orElseGet(()-> new GetAccountResponse(null));
                    });
        };
    }

    @Override
    public ServiceCall<NotUsed, GetAllAccountsResponse> getAllAccounts(){
        return notUsed -> {
            return accountRepository.getAllAccounts().thenApply(list -> {
                return list.stream()
                        .map(a -> new GetAllAccountsResponse.Account(a.accountId, a.name, a.email, a.balance))
                        .collect(Collectors.toList());
            }).thenApply(accounts -> new GetAllAccountsResponse(accounts));
        };
    }


   /* @Override
    public ServiceCall<EmailUpdateRequest,String> emailUpdate() {
        return request -> {
            //     return accountServiceUtil.emailUpdate(request, accountAggregateRef(request.getAccountId()));
            return CompletableFuture.supplyAsync(()->"adhandland");
        };
    }*/

    @Override
    public ServiceCall<CreateAccountRequest, Done> createAccount() {
        return request -> {
            return accountServiceUtil.createAccount(request, accountAggregateRef(request.getAccountId()));
        };
    }
    @Override
    public ServiceCall<EmailUpdateRequest, Done> emailUpdate() {
        return request -> {
            return accountServiceUtil.emailUpdate(request, accountAggregateRef(request.getAccountId()));
        };
    }

    @Override
    public ServiceCall<DepositRequest, Done> deposit() {
        return request -> {
            return accountServiceUtil.deposit(request, accountAggregateRef(request.getAccountId()));
        };
    }

    @Override
    public ServiceCall<WithdrawRequest, Done> withdraw() {
        return request -> {
            return accountServiceUtil.withdraw(request, accountAggregateRef(request.getAccountId()));
        };
    }

    @Override
    public ServiceCall<TransferRequest, Done> transfer() {
        return request -> {
            return accountServiceUtil.transfer(request, accountAggregateRef(request.getOwnAccountId()))
                    .thenCompose(done -> accountServiceUtil.deposit(new DepositRequest(request.beneficiaryAccountId, request.amount), accountAggregateRef(request.beneficiaryAccountId)));
        };
    }

    @Override
    public Topic<AccountPublishEvent> accountEvents() {
        return TopicProducer.taggedStreamWithOffset(
                AccountEvent.TAG.allTags(),
                (tag, offset) -> registry.eventStream(tag, offset)
                        .filter(pair -> {
                                    log.info("{} event is fired. entityId::{}", pair.first(), pair.first().id());
                                    return (pair.first() instanceof AccountEvent.AccountCreated)
                                            || (pair.first() instanceof AccountEvent.Deposited)
                                            || (pair.first() instanceof AccountEvent.Withdrawn)
                                            || (pair.first() instanceof AccountEvent.Transfered)
                                            || (pair.first() instanceof AccountEvent.EmailUpdated);
                                }
                        )
                        .mapAsync(
                                1,
                                eventAndOffset -> accountServiceUtil.getAccount(accountAggregateRef(eventAndOffset.first().id()))
                                        .thenApply(
                                                account -> Pair.create(
                                                        accountPublishEventConverter.convertToAccountPublishEvent(eventAndOffset.first(), account.get()),
                                                        eventAndOffset.second()
                                                )
                                        )
                        )
        );
    }
}
