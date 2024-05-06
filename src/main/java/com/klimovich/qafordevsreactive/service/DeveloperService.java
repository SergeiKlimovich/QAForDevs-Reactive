package com.klimovich.qafordevsreactive.service;

import com.klimovich.qafordevsreactive.entity.DeveloperEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DeveloperService {
  Mono<DeveloperEntity> createDeveloper(DeveloperEntity developer);

  Mono<DeveloperEntity> updateDeveloper(DeveloperEntity developer);

  Flux<DeveloperEntity> getAll();

  Flux<DeveloperEntity> findAllActiveBySpeciality(String speciality);

  Mono<DeveloperEntity> getById(Integer id);


  Mono<Void> softDeleteById(Integer id);

  Mono<Void> hardDeleteById(Integer id);
}
