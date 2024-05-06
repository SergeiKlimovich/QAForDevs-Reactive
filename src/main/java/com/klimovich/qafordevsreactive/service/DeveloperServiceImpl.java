package com.klimovich.qafordevsreactive.service;

import com.klimovich.qafordevsreactive.entity.DeveloperEntity;
import com.klimovich.qafordevsreactive.entity.Status;
import com.klimovich.qafordevsreactive.exception.DeveloperNotFoundException;
import com.klimovich.qafordevsreactive.exception.DeveloperWithEmailAlreadyExistsException;
import com.klimovich.qafordevsreactive.repository.DeveloperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DeveloperServiceImpl implements DeveloperService {

    private final DeveloperRepository developerRepository;

    private Mono<Void> checkIfExistsByEmail(String email) {
        return developerRepository.findByEmail(email)
                .flatMap(developer -> {
                    if (Objects.nonNull(developer)) {
                        return Mono.error(new DeveloperWithEmailAlreadyExistsException("Developer with defined email already exists", "DEVELOPER_DUPLICATE_EMAIL"));
                    }
                    return Mono.empty();
                });
    }

    @Override
    public Mono<DeveloperEntity> createDeveloper(DeveloperEntity developer) {
        return checkIfExistsByEmail(developer.getEmail())
                .then(Mono.defer(() -> {
                    developer.setStatus(Status.ACTIVE);
                    return developerRepository.save(developer);
                }));
    }

    @Override
    public Mono<DeveloperEntity> updateDeveloper(DeveloperEntity developer) {
        return developerRepository.findById(developer.getId())
                .switchIfEmpty(Mono.error(new DeveloperNotFoundException("Developer not found", "DEVELOPER_NOT_FOUND")))
                .flatMap(dev -> developerRepository.save(developer));
    }

    @Override
    public Flux<DeveloperEntity> getAll() {
        return developerRepository.findAll();
    }

    @Override
    public Flux<DeveloperEntity> findAllActiveBySpeciality(String speciality) {
        return developerRepository.findAllActiveBySpecialty(speciality);
    }

    @Override
    public Mono<DeveloperEntity> getById(Integer id) {
        return developerRepository.findById(id);
    }

    @Override
    public Mono<Void> softDeleteById(Integer id) {
        return developerRepository.findById(id)
                .switchIfEmpty(Mono.error(new DeveloperNotFoundException("Developer not found", "DEVELOPER_NOT_FOUND")))
                .flatMap(developer -> {
                    developer.setStatus(Status.DELETED);
                    return developerRepository.save(developer).then();
                });
    }

    @Override
    public Mono<Void> hardDeleteById(Integer id) {
        return developerRepository.findById(id)
                .switchIfEmpty(Mono.error(new DeveloperNotFoundException("Developer not found", "DEVELOPER_NOT_FOUND")))
                .flatMap(developer -> developerRepository.deleteById(id).then());
    }
}
