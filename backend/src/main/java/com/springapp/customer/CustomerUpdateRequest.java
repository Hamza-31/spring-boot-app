package com.springapp.customer;

public record CustomerUpdateRequest (
        String name,
        String email,
        Integer age
){}
