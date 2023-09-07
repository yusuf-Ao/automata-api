package io.aycodes.automataapi.testcase.service.impl;


import io.aycodes.automataapi.common.dtos.CustomException;
import io.aycodes.automataapi.common.dtos.PageResponse;
import io.aycodes.automataapi.common.dtos.testcase.TestCaseDto;
import io.aycodes.automataapi.common.enums.TestCasePriority;
import io.aycodes.automataapi.common.enums.TestCaseStatus;
import io.aycodes.automataapi.testcase.model.TestCase;
import io.aycodes.automataapi.testcase.repository.TestCaseRepo;
import io.aycodes.automataapi.testcase.service.TCService;
import io.aycodes.automataapi.users.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class TCServiceImpl implements TCService {

    private final TestCaseRepo  testCaseRepo;

    @Override
    public PageResponse getAllTestCases(final User user, final int page, final int size) {
        log.info("Retrieving all user Testcases");
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());
        Page<TestCase> testCasePage = testCaseRepo.findAllByUser(user, pageable);
        List<TestCase> testCaseList = testCasePage.getContent();
        return PageResponse.builder()
                .pageContent(testCaseList)
                .currentPage(testCasePage.getNumber())
                .totalItems(testCasePage.getTotalElements())
                .totalPages(testCasePage.getTotalPages())
                .build();
    }

    @Override
    public Optional<TestCase> getUserTestCaseById(final Long id, final User user) {
        log.info("Retrieving user test case by id");
        return testCaseRepo.findByIdAndUser(id, user);
    }

    @Override
    public TestCase createTestCaseForUser(final TestCaseDto testCaseDto, final User user) throws CustomException {
        log.info("checking for testcase title duplicate");
        if (testCaseRepo.existsByTitle(testCaseDto.getTitle())) {
            final String message = "TestCase with same name already exists";
            log.error(message);
            throw new CustomException(message);
        }
        log.info("Creating new user TestCase");
        TestCase testCase = TestCase.builder()
                .title(testCaseDto.getTitle())
                .description(testCaseDto.getDescription())
                .user(user)
                .build();
        log.info("Persisting new TestCase to db");
        return testCaseRepo.save(testCase);
    }

    @Override
    public TestCase updateUserTestCase(final Long id, final TestCaseDto testCaseDto, final User user) throws CustomException {
        Optional<TestCase> testCase = testCaseRepo.findByIdAndUser(id, user);
        if (testCase.isEmpty()) {
            final String message = "TestCase not found";
            log.error(message);
            throw new CustomException(message);
        }
        testCase.get().setTitle(testCaseDto.getTitle());
        testCase.get().setDescription(testCaseDto.getDescription());
        log.info("Persisting updated TestCase info to db");
        return testCaseRepo.save(testCase.get());
    }

    @Override
    public TestCase updateTestCasePriority(final Long id, final User user, final TestCasePriority priority) throws CustomException {
        Optional<TestCase> testCase = testCaseRepo.findByIdAndUser(id, user);
        if (testCase.isEmpty()) {
            final String message = "TestCase not found";
            log.error(message);
            throw new CustomException(message);
        }
        testCase.get().setPriority(priority);
        log.info("Persisting updated TestCase info to db");
        return testCaseRepo.save(testCase.get());
    }

    @Override
    public TestCase updateTestCaseStatus(final Long id, final User user, final TestCaseStatus status) throws CustomException {
        Optional<TestCase> testCase = testCaseRepo.findByIdAndUser(id, user);
        if (testCase.isEmpty()) {
            final String message = "TestCase not found";
            log.error(message);
            throw new CustomException(message);
        }
        testCase.get().setStatus(status);
        log.info("Persisting updated TestCase info to db");
        return testCaseRepo.save(testCase.get());
    }

    @Override
    public void deleteUserTestCase(final Long id, final User user) {
        log.info("Deleting user testcase");
        testCaseRepo.deleteByIdAndUser(id, user);
    }
}
