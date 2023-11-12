package com.springapp.journey;

import com.springapp.customer.Customer;
import com.springapp.customer.CustomerRegistrationRequest;
import com.springapp.customer.CustomerUpdateRequest;
import com.springapp.customer.Gender;
import net.datafaker.Faker;
import net.datafaker.providers.base.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;

@SpringBootTest(
        webEnvironment = RANDOM_PORT
)
public class CustomerIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;
    private static final Random random = new Random();
    @Test
    void canRegisterCustomer() {
        // create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email =
                fakerName.lastName().toLowerCase()+"-"+ UUID.randomUUID()+"@foobar.co";
        int age = random.nextInt(1,99);
        Gender gender = age%2 ==0 ? Gender.MALE: Gender.FEMALE;
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name,
                email,
                age,
                gender
        );
        // send a post request
        String customerUri = "/api/v1/customers";
        webTestClient.post()
                .uri(customerUri)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request),CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(customerUri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();
        // make sure that consumer is present
        Customer expectedCustomer = new Customer(
                name,
                email,
                age,
                gender);
        assertThat(allCustomers).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);
        long id = allCustomers.stream()
                .filter(c->c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        expectedCustomer.setId(id);
        // get customer by id
        webTestClient.get()
                .uri(customerUri+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                })
                .isEqualTo(expectedCustomer);
    }

    @Test
    void canDeleteCustomer() {
        // create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email =
                fakerName.lastName().toLowerCase()+"-"+ UUID.randomUUID()+"@foobar.co";
        int age = random.nextInt(1,99);
        Gender gender = age%2 ==0 ? Gender.MALE: Gender.FEMALE;
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name,
                email,
                age,
                gender
        );
        // send a post request
        String customerUri = "/api/v1/customers";
        webTestClient.post()
                .uri(customerUri)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request),CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(customerUri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();
        // make sure that consumer is present

        long id = allCustomers.stream()
                .filter(c->c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // delete customer
        webTestClient.delete()
                        .uri(customerUri+"/{id}",id)
                        .accept(MediaType.APPLICATION_JSON)
                        .exchange()
                        .expectStatus()
                        .isOk();
        // get customer by id not found
        webTestClient.get()
                .uri(customerUri+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateCustomer() {
        // create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email =
                fakerName.lastName().toLowerCase()+"-"+ UUID.randomUUID()+"@foobar.co";
        int age = random.nextInt(1,99);
        Gender gender = age%2 ==0 ? Gender.MALE: Gender.FEMALE;
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name,
                email,
                age,
                gender
        );
        // send a post request
        String customerUri = "/api/v1/customers";
        webTestClient.post()
                .uri(customerUri)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request),CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(customerUri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();
        // make sure that consumer is present

        long id = allCustomers.stream()
                .filter(c->c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // update customer
        String updateName = "Ali";
        CustomerUpdateRequest updateCustomer = new CustomerUpdateRequest(
                updateName,
                null,
                null,
                gender
        );
        webTestClient.put()
                .uri(customerUri+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateCustomer),CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
        // get customer by id
        Customer updatedCustomer = webTestClient.get()
                .uri(customerUri + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Customer.class)
                .returnResult()
                .getResponseBody();
        Customer expectedCustomer = new Customer(
                id,updateName,email,age,gender);
        assertThat(updatedCustomer).isEqualTo(expectedCustomer);
    }
}
