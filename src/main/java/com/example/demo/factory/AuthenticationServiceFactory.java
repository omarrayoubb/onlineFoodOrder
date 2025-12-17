package com.example.demo.factory;

import com.example.demo.enums.UserRole;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.CustomerService;
import com.example.demo.service.RestaurantService;
import com.example.demo.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationServiceFactory {

    private final CustomerService customerService;
    private final RestaurantService restaurantService;
    private final DeliveryService deliveryService;

    @Autowired
    public AuthenticationServiceFactory(
            CustomerService customerService,
            RestaurantService restaurantService,
            DeliveryService deliveryService) {
        this.customerService = customerService;
        this.restaurantService = restaurantService;
        this.deliveryService = deliveryService;
    }

    /**
     * Returns the appropriate authentication service based on user role
     * @param role The user role (CUSTOMER, RESTAURANT, or DELIVERY)
     * @return The corresponding AuthenticationService implementation
     * @throws IllegalArgumentException if role is invalid
     */
    public AuthenticationService getService(UserRole role) {
        if (role == null) {
            throw new IllegalArgumentException("User role cannot be null");
        }

        return switch (role) {
            case CUSTOMER -> customerService;
            case RESTAURANT -> restaurantService;
            case DELIVERY -> deliveryService;
        };
    }
}

