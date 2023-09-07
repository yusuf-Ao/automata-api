package io.aycodes.automataapi.testcase.repository;

import io.aycodes.automataapi.testcase.model.TestCase;
import io.aycodes.automataapi.users.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestCaseRepo extends JpaRepository<TestCase, Long> {

    Page<TestCase> findAllByUser(User user, Pageable pageable);

    Optional<TestCase> findByIdAndUser(Long id, User user);

    void deleteByIdAndUser(Long id, User user);

    boolean existsByTitle(String title);
}
