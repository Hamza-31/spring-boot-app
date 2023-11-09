package com.springapp;

import com.springapp.customer.Customer;
import com.springapp.customer.CustomerRepository;
import net.datafaker.Faker;
import net.datafaker.providers.base.Name;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Random;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository){
        return args -> {
            var faker = new Faker();
            
            Random random = new Random();
            Name name = faker.name();
            String firstName = name.firstName();
            String lastname = name.lastName();

            Customer customer = new Customer(
                firstName+" "+lastname,
                firstName.toLowerCase() +"."+lastname.toLowerCase()+"@example.com",
                random.nextInt(16,99));
                customerRepository.save(customer);
        };
    }
}
