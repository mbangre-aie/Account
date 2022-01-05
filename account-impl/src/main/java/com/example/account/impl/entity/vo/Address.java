/*******************************************************
 * Copyright (C) 2020 RETISIO Inc. All Rights Reserved
 *
 * This file is part of ARC project.
 * Unauthorised copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by ngollapothu
 *******************************************************/
package com.example.account.impl.entity.vo;


import lombok.Value;

@Value
public class Address {
    public final String houseNo;
    public final String street;
    public final String city;
    public final Integer pincode;
}
