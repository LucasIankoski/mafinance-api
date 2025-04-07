package com.iankoski.api.mafinance.entity;

import com.iankoski.api.mafinance.enums.TransactionType;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {

    private Long id;
    private String description;
    private BigDecimal amount;
    private LocalDate date;
    private TransactionType type;
    private boolean recurring;
    private boolean installment;
    private Integer totalInstallments;
    private Integer installmentNumber;
    private BigDecimal totalAmount;
    private boolean paid;

    @ManyToOne
    private Category category;

    @ManyToOne
    private User user;

}
