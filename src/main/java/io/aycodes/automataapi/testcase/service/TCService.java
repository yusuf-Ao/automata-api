package io.aycodes.automataapi.testcase.service;


import io.aycodes.automataapi.common.dtos.CustomException;
import io.aycodes.automataapi.common.dtos.PageResponse;
import io.aycodes.automataapi.common.dtos.testcase.TestCaseDto;
import io.aycodes.automataapi.common.enums.TestCasePriority;
import io.aycodes.automataapi.common.enums.TestCaseStatus;
import io.aycodes.automataapi.testcase.model.TestCase;
import io.aycodes.automataapi.users.model.User;

import java.util.Optional;

public interface TCService {

    PageResponse getAllTestCases(User user, int page, int size);

    Optional<TestCase> getUserTestCaseById(Long id, User user);

    TestCase createTestCaseForUser(TestCaseDto testCaseDto, User user) throws CustomException;

    TestCase updateUserTestCase(Long id, TestCaseDto testCaseDto, User user) throws CustomException;

    TestCase updateTestCasePriority(Long id, User user, TestCasePriority priority) throws CustomException;

    TestCase updateTestCaseStatus(Long id, User user, TestCaseStatus status) throws CustomException;

    void deleteUserTestCase(Long id, User user);
}
