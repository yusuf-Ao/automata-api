package io.aycodes.automataapi.products.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.aycodes.automataapi.users.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "product")
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class Product {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long            id;

    @NotBlank
    @Column(name = "name", nullable = false, unique = true)
    private String          name;

    @NotNull
    @DecimalMin("0.01")
    private Double          price;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User            user;

    @CreationTimestamp
    private LocalDateTime   createdOn;

    @UpdateTimestamp
    private LocalDateTime   updatedOn;

}
