package com.example.account.impl.aggregate;

import akka.Done;
import akka.cluster.sharding.typed.javadsl.EntityContext;
import akka.cluster.sharding.typed.javadsl.EntityTypeKey;
import akka.persistence.typed.PersistenceId;
import akka.persistence.typed.javadsl.*;
import com.lightbend.lagom.javadsl.persistence.AkkaTaggerAdapter;
import com.example.account.impl.command.AccountCommand;
import com.example.account.impl.event.AccountEvent;
import com.example.account.impl.state.AccountState;

import java.util.Set;

public class AccountAggregate extends EventSourcedBehaviorWithEnforcedReplies<AccountCommand, AccountEvent, AccountState> {

    public static EntityTypeKey<AccountCommand> ENTITY_TYPE_KEY =
        EntityTypeKey
            .create(AccountCommand.class, "AccountAggregate");


    final private EntityContext<AccountCommand> entityContext;
    final private int numberOfEvents;
    final private int keepNSnapshots;

    AccountAggregate(EntityContext<AccountCommand> entityContext, int numberOfEvents, int keepNSnapshots) {
        super(
            PersistenceId.of(
                entityContext.getEntityTypeKey().name(),
                entityContext.getEntityId()
            )
        );
        this.entityContext = entityContext;
        this.numberOfEvents = numberOfEvents;
        this.keepNSnapshots = keepNSnapshots;
    }

    public static AccountAggregate registerEntity(EntityContext<AccountCommand> entityContext, int numberOfEvents, int keepNSnapshots) {
        return new AccountAggregate(entityContext,numberOfEvents,keepNSnapshots);
    }

    @Override
    public AccountState emptyState() {
        return AccountState.EMPTY;
    }


    @Override
    public CommandHandlerWithReply<AccountCommand, AccountEvent, AccountState> commandHandler() {

        CommandHandlerWithReplyBuilder<AccountCommand, AccountEvent, AccountState> builder = newCommandHandlerWithReplyBuilder();

        builder.forAnyState()
            .onCommand(AccountCommand.CreateAccount.class, (state, cmd) ->
                Effect()
                    .persist(new AccountEvent.AccountCreated(cmd.request))
                    .thenReply(cmd.replyTo, __ -> Done.getInstance())
            )
            .onCommand(AccountCommand.Deposit.class, (state, cmd) ->
                Effect()
                    .persist(new AccountEvent.Deposited(cmd.request))
                    .thenReply(cmd.replyTo, __ -> Done.getInstance())
            )
            .onCommand(AccountCommand.Withdraw.class, (state, cmd) ->
                Effect()
                    .persist(new AccountEvent.Withdrawn(cmd.request))
                    .thenReply(cmd.replyTo, __ -> Done.getInstance())
            )
            .onCommand(AccountCommand.Transfer.class, (state, cmd) ->
                Effect()
                    .persist(new AccountEvent.Transfered(cmd.request))
                    .thenReply(cmd.replyTo, __ -> Done.getInstance())
            )
            .onCommand(AccountCommand.GetAccount.class, (state, cmd) ->
                Effect().none()
                    .thenReply(cmd.replyTo, __ -> state.account)
            )
            .onCommand(AccountCommand.Transaction.class, (state, cmd) ->
                Effect()
                     .persist(new AccountEvent.TransactionMade(cmd.request))
                     .thenReply(cmd.replyTo, __ -> Done.getInstance())
            )
            .onCommand(AccountCommand.EmailUpdate.class, (state, cmd) ->
                Effect()
                      .persist(new AccountEvent.EmailUpdated(cmd.request))
                      .thenReply(cmd.replyTo, __ -> Done.getInstance())
                );
        return builder.build();
    }

    @Override
    public RetentionCriteria retentionCriteria() {
        return RetentionCriteria.snapshotEvery(numberOfEvents, keepNSnapshots).withDeleteEventsOnSnapshot();
    }

    @Override
    public EventHandler<AccountState, AccountEvent> eventHandler() {
        EventHandlerBuilder<AccountState, AccountEvent> builder = newEventHandlerBuilder();

        builder
                .forAnyState()
                .onEvent(AccountEvent.AccountCreated.class, (state, evt) ->

                        state.createAccount(evt.request)
                )
                .onEvent(AccountEvent.Deposited.class, (state, evt) ->
                        state.deposit(evt.request)
                )
                .onEvent(AccountEvent.Withdrawn.class, (state, evt) ->
                        state.withdraw(evt.request)
                )
                .onEvent(AccountEvent.Transfered.class, (state, evt) ->
                        state.transfer(evt.request)
                )
                .onEvent(AccountEvent.TransactionMade.class, (state, evt) ->
                        state.performTransaction(evt.request)
                )
                .onEvent(AccountEvent.EmailUpdated.class, (state, evt) ->
                        state.emailUpdate(evt.request));
        return builder.build();
    }

    @Override
    public Set<String> tagsFor(AccountEvent event) {
        return AkkaTaggerAdapter.fromLagom(entityContext, AccountEvent.TAG).apply(event);
    }
}
