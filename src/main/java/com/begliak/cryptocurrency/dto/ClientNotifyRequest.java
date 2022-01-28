package com.begliak.cryptocurrency.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.persistence.Access;

@Value
public class ClientNotifyRequest {

    String username;
    String symbol;

}
