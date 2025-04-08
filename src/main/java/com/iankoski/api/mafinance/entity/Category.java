package com.iankoski.api.mafinance.entity;

import com.iankoski.api.mafinance.enums.TransactionType;
import jakarta.persistence.*;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "transactiontype")
    private TransactionType type;

    @Column(name = "systemdefault")
    private boolean systemDefault;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public boolean isSystemDefault() {
        return systemDefault;
    }

    public void setSystemDefault(boolean systemDefault) {
        this.systemDefault = systemDefault;
    }

    @ManyToOne
    private Agent agent;

}


