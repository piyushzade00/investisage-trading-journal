package com.tradingjournal.backend.entity;

import com.tradingjournal.backend.enums.TradeType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trades")
public class TradeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private AccountEntity account;

    @Column(nullable = false)
    private String instrument;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TradeType type;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal entryPrice;

    @Column(precision = 10, scale = 4)
    private BigDecimal exitPrice;

    @Column(nullable = false)
    private LocalDateTime entryDateTime;

    private LocalDateTime exitDateTime;

    @Column(precision = 10, scale = 2)
    private BigDecimal stopLoss;

    @Column(precision = 10, scale = 2)
    private BigDecimal targetPrice;

    @Column(precision = 10, scale = 2)
    private BigDecimal profitLoss;

    @Column(length = 1000)
    private String notes;
}
