package com.example.account.api.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Value;

@Value
@JsonDeserialize
public class EmailUpdateRequest {

    public final String accountId;
    public final String email;

    @JsonCreator
    public EmailUpdateRequest(String accountId, String email) {
        this.accountId = accountId;
        this.email = email;
    }

}
