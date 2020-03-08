package ua.stepess.microservices.pcdemo.domain.money;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "money", schema = "money")
public class Account {
    @Column(name = "client_name")
    private String clientName;
}
