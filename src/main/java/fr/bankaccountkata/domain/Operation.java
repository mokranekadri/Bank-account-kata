package fr.bankaccountkata.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;


@Entity
@Table(name = "operation")
public class Operation implements Serializable{

        private static final long serialVersionUID = 1L;

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
        @SequenceGenerator(name = "sequenceGenerator")
        private Long id;

        private Instant date;

        @Enumerated(EnumType.STRING)
        private OperationType type;

        private Long amount ;

        @JsonIgnore
        @ManyToOne
        @JoinColumn(name = "account_id")
        private BankAccount account;

        public Operation() {
        }

        public Operation(Instant date, OperationType type, long amount, BankAccount account) {
                this.date = date;
                this.type = type;
                this.amount = amount;
                this.account = account;
        }

        public BankAccount getAccount() {
                return account;
        }

        public void setAccount(BankAccount account) {
                this.account = account;
        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public Instant getDate() {
                return date;
        }

        public void setDate(Instant date) {
                this.date = date;
        }

        public OperationType getType() {
                return type;
        }

        public void setType(OperationType type) {
                this.type = type;
        }

        public Long getAmount() {
                return amount;
        }

        public void setAmount(Long amount) {
                this.amount = amount;
        }
}
