package com.springapp.customer;

import com.springapp.exception.DuplicateResourceException;
import com.springapp.exception.RequestValidationException;
import com.springapp.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    private CustomerService underTest;
    @Mock
    private CustomerDao customerDao;
    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }
    @Test
    void getAllCustomers() {
        // When
        underTest.getAllCustomers();
        // Then
        verify(customerDao).selectAllCustomers();
    }

    @Test
    void canGetCustomerById() {
        // Given
        long id = 10;
        Customer customer = new Customer(
                id,
                "Alex",
                "alex@gmail.com",
                20
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        // When
        Customer actual = underTest.getCustomerById(id);
        // Then
        assertThat(actual).isEqualTo(customer);
    }
    @Test
    void willThrowWhenGetCustomerReturnEmptyOptional() {
        // Given
        long id = 10;
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());
        // When
        // Then
        assertThatThrownBy(()->underTest.getCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id [%s] not found".formatted(id));
    }
    @Test
    void addCustomer() {
        // Given
        String email = "alex@gmail.com";
        when(customerDao.existsCustomerWithEmail(email)).thenReturn(false);
        CustomerRegistrationRequest customer = new CustomerRegistrationRequest(
                "alex",
                email,
                60
        );
        // When
        underTest.addCustomer(customer);
        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(customer.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.age());
    }
    @Test
    void wilThrowWhenEmailExistsWhileAddingCustomer() {
        // Given
        String email = "alex@gmail.com";
        when(customerDao.existsCustomerWithEmail(email)).thenReturn(true);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "alex",
                email,
                60
        );
        // When
        assertThatThrownBy(()-> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                        .hasMessage("email already taken");
        // Then
        verify(customerDao, never()).insertCustomer(any());
    }

    @Test
    void deleteCustomerById() {
        // Given
        long id = 1;
        when(customerDao.existsCustomerById(id)).thenReturn(true);
        // When
        underTest.deleteCustomerById(id);
        // Then
        verify(customerDao).deleteCustomerById(id);
    }
    @Test
    void willThrowDeleteCustomerByIdNotExists() {
        // Given
        long id = -1;
        when(customerDao.existsCustomerById(id)).thenReturn(false);
        // When
        assertThatThrownBy(()-> underTest.deleteCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("customer with id [%s] not found".formatted(id));
        // Then
        verify(customerDao, never()).deleteCustomerById(id);
    }

    @Test
    void canUpdateAllCustomerProperties() {
        // Given
        long id = 1;
        Customer customer = new Customer(
                id,
                "Alex",
                "alex@gmail.com",
                60
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        // When
        String newEmail = "alexandro@gmail.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                "Alexandro",
                newEmail,
                25
        );
        // when(customerDao.existsCustomerWithEmail(newEmail)).thenReturn(false);
        underTest.updateCustomer(
                id,
                updateRequest
                );
        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
    }
    @Test
    void canUpdateOnlyCustomerName() {
        // Given
        long id = 1;
        Customer customer = new Customer(
                id,
                "Alex",
                "alex@gmail.com",
                60
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        // When
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                "Alexandro",
                null,
                null
        );
        underTest.updateCustomer(
                id,
                updateRequest
        );
        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }
    @Test
    void canUpdateOnlyCustomerEmail() {
        // Given
        long id = 1;
        Customer customer = new Customer(
                id,
                "Alex",
                "alex@gmail.com",
                60
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        // When
        String email = "alexandro@gmail.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null,
                email,
                null
        );
        underTest.updateCustomer(
                id,
                updateRequest
        );
        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }
    @Test
    void canUpdateOnlyCustomerAge() {
        // Given
        long id = 1;
        Customer customer = new Customer(
                id,
                "Alex",
                "alex@gmail.com",
                60
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        // When
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null,
                null,
                30
        );
        underTest.updateCustomer(
                id,
                updateRequest
        );
        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
    }
    @Test
    void willThrowWhenTryingToUpdateEmailWhenAlreadyTaken() {
        // Given
        long id = 1;
        Customer customer = new Customer(
                id,
                "Alex",
                "alex@gmail.com",
                60
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        String newEmail = "alexandro@gmail.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null,
                newEmail,
                null
        );
        when(customerDao.existsCustomerWithEmail(newEmail)).thenReturn(true);
        // When
        assertThatThrownBy(()->underTest.updateCustomer(id,updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                        .hasMessage("email already taken");
        // Then

        verify(customerDao,never()).updateCustomer(any());
    }
    @Test
    void willThrowWhenCustomerUpdateHasNoChanges() {
        // Given
        long id = 1;
        Customer customer = new Customer(
                id,
                "Alex",
                "alex@gmail.com",
                60
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                customer.getName(),
                customer.getEmail(),
                customer.getAge()
        );
        // When
        assertThatThrownBy(()->underTest.updateCustomer(id,updateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes found");
        // Then

        verify(customerDao,never()).updateCustomer(any());
    }
}