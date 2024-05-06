package com.klimovich.qafordevsreactive.repository;

import com.klimovich.qafordevsreactive.entity.DeveloperEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DeveloperRepository extends R2dbcRepository<DeveloperEntity, Integer> {
  Mono<DeveloperEntity> findByEmail(String email);

  @Query("SELECT d FROM developers d WHERE d.status = 'ACTIVE' AND d.specialty = ?1")
  Flux<DeveloperEntity> findAllActiveBySpecialty(String specialty);
}
