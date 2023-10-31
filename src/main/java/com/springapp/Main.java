package com.springapp;

import com.springapp.customer.Customer;
import com.springapp.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository){
        return args -> {
        Customer hamza = new Customer("Hamza", "hamza@gmail.com",30);
        Customer nissrine = new Customer("Nissrine", "nissrine@gmail.com",29);
        List<Customer> customers = List.of(hamza, nissrine);
        // customerRepository.saveAll(customers);


        };
    }
}
