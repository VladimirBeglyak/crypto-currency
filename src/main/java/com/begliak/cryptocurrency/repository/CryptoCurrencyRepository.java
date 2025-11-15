package com.begliak.cryptocurrency.repository;

import com.begliak.cryptocurrency.entity.CryptoCurrency;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoCurrencyRepository extends JpaRepository<CryptoCurrency, Long> {

  Optional<CryptoCurrency> findBySymbol(String symbol);
}
