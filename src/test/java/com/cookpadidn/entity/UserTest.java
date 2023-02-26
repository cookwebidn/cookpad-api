package com.cookpadidn.entity;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class UserTest {

    @Test
    void userTestSuccess() {

        UUID uuid = UUID.randomUUID();
        String name = "John Doe";
        String email = "johndoe@gmail.com";
        String password = "password";

        User user = new User("John Doe", "johndoe@gmail.com", "password");
        user.setId(uuid);

        assertEquals(uuid, user.getId());
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
    }

    @Test
    void userTestFail() {

        UUID uuid = UUID.randomUUID();
        String name = "John Doe";
        String email = "johndoe@gmail.com";
        String password = "password";

        User user = new User();

        assertNotEquals(uuid, user.getId());
        assertNotEquals(name, user.getName());
        assertNotEquals(email, user.getEmail());
        assertNotEquals(password, user.getPassword());
    }
}
