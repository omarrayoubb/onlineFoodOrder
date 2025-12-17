package com.example.demo.factory;

import com.example.demo.entity.User;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Restaurant;
import com.example.demo.entity.Delivery;
import com.example.demo.enums.UserRole;

public class UserFactory {

    /**
     * Creates a User instance based on the specified role
     * @param role The user role (CUSTOMER, RESTAURANT, or DELIVERY)
     * @param email User's email
     * @param passwordHash Hashed password
     * @param name User's name
     * @return Appropriate User subclass instance
     * @throws IllegalArgumentException if role is invalid
     */
    public static User createUser(UserRole role, String email, String passwordHash, String name) {
        if (role == null) {
            throw new IllegalArgumentException("User role cannot be null");
        }

        return switch (role) {
            case CUSTOMER -> {
                Customer customer = new Customer(email, passwordHash, name);
                customer.setUserRole(UserRole.CUSTOMER);
                yield customer;
            }
            case RESTAURANT -> {
                Restaurant restaurant = new Restaurant();
                restaurant.setEmail(email);
                restaurant.setPasswordHash(passwordHash);
                restaurant.setName(name);
                restaurant.setUserRole(UserRole.RESTAURANT);
                yield restaurant;
            }
            case DELIVERY -> {
                Delivery delivery = new Delivery(email, passwordHash, name);
                delivery.setUserRole(UserRole.DELIVERY);
                yield delivery;
            }
        };
    }
}

