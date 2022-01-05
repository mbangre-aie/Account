package com.example.account.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import com.example.account.api.AccountService;

public class AccountModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindService(AccountService.class, AccountServiceImpl.class);
    }
}
