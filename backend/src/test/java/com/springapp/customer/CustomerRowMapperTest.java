package com.springapp.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        // Given
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getLong("id")).thenReturn((long)1);
        when(resultSet.getString("name")).thenReturn("Jamila");
        when(resultSet.getString("email")).thenReturn("jamila@gmail.com");
        when(resultSet.getInt("age")).thenReturn(19);

        // When
        Customer actual = customerRowMapper.mapRow(resultSet, 1);
        // Then
        Customer expected = new Customer(
                (long)1,"Jamila","jamila@gmail.com",19
        );
        assertThat(actual).isEqualTo(expected);
    }
}