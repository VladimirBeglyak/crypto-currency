package com.begliak.cryptocurrency.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
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
