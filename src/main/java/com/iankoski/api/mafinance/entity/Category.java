package com.iankoski.api.mafinance.entity;

import com.iankoski.api.mafinance.enums.TransactionType;
import jakarta.persistence.ManyToOne;


public class Category {

    private Long id;
    private String name;
    private TransactionType type;
    private boolean systemDefault;

    @ManyToOne
    private User user;

}


