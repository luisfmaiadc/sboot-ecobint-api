package com.portfolio.luisfmdc.sboot_ecobint_api.repository;

import com.portfolio.luisfmdc.sboot_ecobint_api.domain.Bin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface BinRepository extends MongoRepository<Bin, String> {

    @Query("{ '_id' : ?0 }")
    @Update("{ '$set' : { 'nivelEnchimento' : ?1, 'ultimaAtualizacao' : ?2 } }")
    long updateStatus(String binId, Double nivelEnchimento, LocalDateTime ultimaAtualizacao);
}