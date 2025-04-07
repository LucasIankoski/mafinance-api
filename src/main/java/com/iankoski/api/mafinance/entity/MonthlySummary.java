package com.iankoski.api.mafinance.entity;

import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;

public class MonthlySummary {

    private Long id;
    private Integer month;
    private Integer year;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal balance;

    @ManyToOne
    private User user;

}
