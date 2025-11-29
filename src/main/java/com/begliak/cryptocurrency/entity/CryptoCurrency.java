package com.begliak.cryptocurrency.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
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
