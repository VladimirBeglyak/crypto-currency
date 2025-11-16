package com.begliak.cryptocurrency.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "crypto_currency")
public class CryptoCurrency {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column(name = "external_id")
  private Long externalId;

  private String symbol;

  @Column(name = "price_usd")
  private BigDecimal priceUsd;
}
