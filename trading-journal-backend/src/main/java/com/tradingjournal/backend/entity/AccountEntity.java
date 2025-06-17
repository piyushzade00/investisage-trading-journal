package com.tradingjournal.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @ManyToOne(fetch = FetchType.LAZY) // Many accounts can belong to one user
    @JoinColumn(name = "user_id", nullable = false) // Foreign key column
    private UserEntity user;

    // In the future, we'll add @OneToMany for Trades and Transactions here
    // @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<Trade> trades;

    // @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<Transaction> transactions;
}
