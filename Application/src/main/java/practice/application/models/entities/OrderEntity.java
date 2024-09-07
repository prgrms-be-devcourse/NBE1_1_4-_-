package practice.application.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class OrderEntity {
    @Id
    @Column(name = "order_id", nullable = false, length = 16)
    private UUID orderId;

    @ColumnDefault("(now())")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "order_status", nullable = false, length = 50)
    private String orderStatus;

    @Column(name = "address", nullable = false, length = 200)
    private String address;

    @Column(name = "postcode", nullable = false, length = 200)
    private String postcode;

}