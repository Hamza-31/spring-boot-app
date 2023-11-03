package com.springapp.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Repository("list")
public class CustomerListDataAccessService implements CustomerDao{
    private static final List<Customer> customers;
    static {
        customers = new ArrayList<>();
        Customer hamza = new Customer("Hamza", "hamza@gmail.com",30);
        customers.add(hamza);
        Customer nissrine = new Customer("Nissrine", "nissrine@gmail.com",29);
        customers.add(nissrine);
    }
    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Long id) {
        return customers
                .stream()
                .filter(c->c.getId().equals(id))
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        return customers.stream()
                .anyMatch(c->c.getEmail().equals(email));
    }

    @Override
    public boolean existsPersonWithId(Long id) {
        return customers.stream().anyMatch(c->c.getId().equals(id));

    }

    @Override
    public void deleteCustomerById(Long id) {
        customers.stream()
                .filter(c->c.getId().equals(id))
                .findFirst()
                .ifPresent(customers::remove);
    }

    @Override
    public void updateCustomer(Customer customer) {
        customers.add(customer);
    }
}
