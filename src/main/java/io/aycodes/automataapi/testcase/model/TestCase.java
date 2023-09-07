package io.aycodes.automataapi.testcase.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.aycodes.automataapi.common.enums.TestCasePriority;
import io.aycodes.automataapi.common.enums.TestCaseStatus;
import io.aycodes.automataapi.users.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

import static io.aycodes.automataapi.common.enums.TestCasePriority.MEDIUM;
import static io.aycodes.automataapi.common.enums.TestCaseStatus.NOT_RUN;


@Entity
@Table(name = "test_case")
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class TestCase {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long                id;

    @Column(name = "title", unique = true, nullable = false)
    private String              title;

    @NotBlank
    private String              description;

    private TestCaseStatus      status = NOT_RUN;

    private TestCasePriority    priority = MEDIUM;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User                user;

    @CreationTimestamp
    private LocalDateTime       createdOn;

    @UpdateTimestamp
    private LocalDateTime       updatedOn;

}
