package br.com.martins.webflux.controller;

import br.com.martins.webflux.entity.User;
import br.com.martins.webflux.mapper.UserMapper;
import br.com.martins.webflux.model.request.UserRequest;
import br.com.martins.webflux.model.response.UserResponse;
import br.com.martins.webflux.service.UserService;
import com.mongodb.reactivestreams.client.MongoClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
class UserControllerImplTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService service;

    @MockBean
    private UserMapper mapper;

    @MockBean
    private MongoClient mongoClient;

    @Test
    @DisplayName("Teste endpoint save com sucesso")
    void testSaveWithSuccess() {
        UserRequest request = new UserRequest("teste", "teste@mail.com", "123");

        when(service.save(any(UserRequest.class))).thenReturn(Mono.just(User.builder().build()));

        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isCreated();

        Mockito.verify(service, times(1)).save(any(UserRequest.class));
    }

    @Test
    @DisplayName("Teste endpoint save sem sucesso")
    void testSaveWithoutSuccess() {
        UserRequest request = new UserRequest(" teste", "teste@mail.com", "123");

        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.path").isEqualTo("/users")
                .jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
                .jsonPath("$.error").isEqualTo("Erro de validação")
                .jsonPath("$.message").isEqualTo("Erro ao validar atributos")
                .jsonPath("$.errors[0].fieldName").isEqualTo("name")
                .jsonPath("$.errors[0].message").isEqualTo("Os campos não podem conter espaços em branco no início ou no final");
    }

    @Test
    @DisplayName("Teste endpoint findById com sucesso")
    void testFindByIdWithSuccess() {
        UserResponse userResponse = new UserResponse("12345", "teste", "teste@mail.com", "123");

        when(service.findById(anyString())).thenReturn(Mono.just(User.builder().build()));
        when(mapper.toResponse(any(User.class))).thenReturn(userResponse);

        webTestClient.get().uri("/users/" + "12345")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("12345")
                .jsonPath("$.name").isEqualTo("teste")
                .jsonPath("$.email").isEqualTo("teste@mail.com")
                .jsonPath("$.password").isEqualTo("123");
    }

    @Test
    @DisplayName("Teste endpoint findAll com sucesso")
    void testFindAllWithSuccess() {
        UserResponse userResponse = new UserResponse("12345", "teste", "teste@mail.com", "123");

        when(service.findAll()).thenReturn(Flux.just(User.builder().build()));
        when(mapper.toResponse(any(User.class))).thenReturn(userResponse);

        webTestClient.get().uri("/users")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.[0].id").isEqualTo("12345")
                .jsonPath("$.[0].name").isEqualTo("teste")
                .jsonPath("$.[0].email").isEqualTo("teste@mail.com")
                .jsonPath("$.[0].password").isEqualTo("123");
    }

    @Test
    void testUpdate() {
    }

    @Test
    void testDelete() {
    }
}