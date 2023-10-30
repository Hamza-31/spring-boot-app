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
        Customer hamza = new Customer(1,"Hamza", "hamza@gmail.com",30);
        customers.add(hamza);
        Customer nissrine = new Customer(2,"Nissrine", "nissrine@gmail.com",29);
        customers.add(nissrine);
    }
    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        return customers
                .stream()
                .filter(c->c.getId().equals(id))
                .findFirst();
    }
}
