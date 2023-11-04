package com.springapp.customer;

import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class CustomerJPADataAccessServiceTest {
    private CustomerJPADataAccessService underTest;
    @Mock
    private CustomerRepository customerRepository;
    private AutoCloseable autoCloseable ;
    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        // When
        underTest.selectAllCustomers();
        // Then
        verify(customerRepository).findAll();
    }

    @Test
    void selectCustomerById() {
        // Given
        long id = 1;

        // When
        underTest.selectCustomerById(id);
        // Then
        verify(customerRepository).findById(id);
    }

    @Test
    void insertCustomer() {
        // Given
        Customer customer = new Customer(
                "Chaymae",
                "chaymae@gmail.com",
                25
        );
        // When
        underTest.insertCustomer(customer);
        // Then
        verify(customerRepository).save(customer);
    }

    @Test
    void existsPersonWithEmail() {
        // Given
        String email = "chaymae@example.com";
        // When
        underTest.existsPersonWithEmail(email);
        // Then
        verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void existsPersonWithId() {
        // Given
        long id = 1;
        // When
        underTest.existsPersonWithId(id);
        // Then
        verify(customerRepository).existsCustomerById(id);
    }

    @Test
    void deleteCustomerById() {
        // Given
        long id = 1;
        // When
        underTest.deleteCustomerById(id);
        // Then
        verify(customerRepository).deleteById(id);
    }

    @Test
    void updateCustomer() {
        // Given
        Customer update = new Customer(
                "Aya",
                "aya@gmail.com",
                20
        );
        // When
        underTest.updateCustomer(update);
        // Then
        verify(customerRepository).save(update);
    }
}