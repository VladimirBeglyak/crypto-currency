package com.begliak.cryptocurrency.repository;

import com.begliak.cryptocurrency.entity.Client;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

  Optional<Client> findByUsernameAndSymbol(String username, String symbol);

  List<Client> findAllBySymbol(String symbol);
}
