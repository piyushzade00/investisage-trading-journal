package com.tradingjournal.backend.entity;

import com.tradingjournal.backend.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String broker;

    private boolean isPrimary;

    @ManyToOne(fetch = FetchType.LAZY) // Many accounts can belong to one user
    @JoinColumn(name = "user_id", nullable = false) // Foreign key column
    private UserEntity user;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TradeEntity> trades;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionEntity> transactions;

    @Transient
    public BigDecimal getAccountBalance() {
        // If there are no transactions, the balance is 0
        if (transactions == null || transactions.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return transactions.stream()
                .filter(tx -> tx.getAmount() != null && tx.getType() != null)
                .map(tx -> tx.getType() == TransactionType.DEPOSIT
                        ? tx.getAmount()
                        : tx.getAmount().negate())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
