package com.iankoski.api.mafinance.entity;

import java.time.LocalDateTime;

public class User {

    private Long id;
    private String name;
    private String email;
    private String passwordHash;
    private LocalDateTime createdAt;
    private boolean active;

}