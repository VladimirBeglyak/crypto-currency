package com.begliak.cryptocurrency.dto;

import lombok.Value;

@Value
public class ClientNotifyRequest {

  String username;
  String symbol;
}
