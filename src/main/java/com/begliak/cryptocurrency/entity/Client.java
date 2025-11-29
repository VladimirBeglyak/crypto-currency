package com.begliak.cryptocurrency.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "notify_client")
public class Client {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  private String username;

  private String symbol;

  private BigDecimal price;
}
