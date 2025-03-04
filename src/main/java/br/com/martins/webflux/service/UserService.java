package br.com.martins.webflux.service;

import br.com.martins.webflux.entity.User;
import br.com.martins.webflux.exception.ObjectNotFoundException;
import br.com.martins.webflux.mapper.UserMapper;
import br.com.martins.webflux.model.request.UserRequest;
import br.com.martins.webflux.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public Mono<User> save(UserRequest request){
        return repository.save(mapper.toEntity(request));
    }

    public Mono<User> findById(final String id){
        return handleNotFound(repository.findById(id), id);
    }

    public Flux<User> findAll(){
        return repository.findAll();
    }

    public Mono<User> update(final String id, final UserRequest request){
        return findById(id)
                .map(entity -> mapper.toEntity(request, entity))
                .flatMap(repository::save);
    }

    public Mono<User> delete(final String id){
        return handleNotFound(repository.findAndRemove(id), id);
    }

    private <T> Mono<T> handleNotFound(Mono<T> mono, String id){
        return mono.switchIfEmpty(Mono.error(
                new ObjectNotFoundException(String.format("Objeto não encontrado. Id: %s, Type: %s", id, User.class.getSimpleName()))
        ));
    }
}
