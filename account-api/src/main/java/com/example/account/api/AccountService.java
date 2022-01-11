package com.example.account.api;

import akka.Done;
import akka.NotUsed;
import com.example.account.api.request.*;
import com.example.account.api.response.GetAllAccountsResponse;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.broker.Topic;
import com.lightbend.lagom.javadsl.api.broker.kafka.KafkaProperties;
import com.lightbend.lagom.javadsl.api.transport.Method;
import com.example.account.api.publish.AccountPublishEvent;
import com.example.account.api.response.GetAccountResponse;

import static com.lightbend.lagom.javadsl.api.Service.*;

public interface AccountService extends Service {

    ServiceCall<NotUsed, GetAccountResponse> getAccountDetails(String id);
    ServiceCall<CreateAccountRequest, Done> createAccount();
    ServiceCall<DepositRequest, Done> deposit();
    ServiceCall<WithdrawRequest, Done> withdraw();
    ServiceCall<TransferRequest, Done> transfer();
    ServiceCall<NotUsed, GetAllAccountsResponse> getAllAccounts();
    ServiceCall<EmailUpdateRequest, String> emailUpdate();

    /**
     * This gets published to Kafka.
     */
    Topic<AccountPublishEvent> accountEvents();

    @Override
    default Descriptor descriptor() {
        return named("Account")
                .withCalls(
                        restCall(Method.GET, "/api/account/details/:id", this::getAccountDetails),
                        restCall(Method.POST, "/api/account/create", this::createAccount),
                        restCall(Method.POST, "/api/account/deposit", this::deposit),
                        restCall(Method.POST, "/api/account/withdraw", this::withdraw),
                        restCall(Method.POST, "/api/account/transfer", this::transfer),
                        restCall(Method.GET, "/api/account/all/accounts", this::getAllAccounts),
                        restCall(Method.PUT, "/api/account/email", this::emailUpdate)
                )
                .withTopics(
                        Service.topic("account-events", this::accountEvents)
                                .withProperty(KafkaProperties.partitionKeyStrategy(), AccountPublishEvent::entityId)
                )
                .withAutoAcl(true);
    }
}
