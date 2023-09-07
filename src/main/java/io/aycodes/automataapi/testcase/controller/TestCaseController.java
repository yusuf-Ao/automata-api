package io.aycodes.automataapi.testcase.controller;

import io.aycodes.automataapi.common.config.SecurityConfig;
import io.aycodes.automataapi.common.dtos.CustomException;
import io.aycodes.automataapi.common.dtos.CustomResponse;
import io.aycodes.automataapi.common.dtos.PageResponse;
import io.aycodes.automataapi.common.dtos.testcase.TestCaseDto;
import io.aycodes.automataapi.common.enums.TestCasePriority;
import io.aycodes.automataapi.common.enums.TestCaseStatus;
import io.aycodes.automataapi.testcase.model.TestCase;
import io.aycodes.automataapi.testcase.service.TCService;
import io.aycodes.automataapi.users.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/api/v1/test-case")
@RequiredArgsConstructor
public class TestCaseController {


    private final TCService TCService;


    @PostMapping("/new")
    @Operation(summary = "Add TestCase", description = "Add new TestCase for logged-in user",
            security = { @SecurityRequirement(name = "Bearer Token") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Testcase created successfully"),
            @ApiResponse(responseCode = "417", description = "Unable to create testcase")
    })
    public ResponseEntity<CustomResponse> createTestCase(@Valid @RequestBody final TestCaseDto testCaseDto,
                                                        SecurityContextHolder securityContextHolder) {
        try {
            log.info("Attempt TestCase creation");
            final String message = "successful";
            final User user = SecurityConfig.extractUserDetailsFromSecurityContext(securityContextHolder);
            final TestCase testCase = TCService.createTestCaseForUser(testCaseDto, user);
            final CustomResponse response = CustomResponse.builder().timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.CREATED.value()).status(HttpStatus.CREATED)
                    .message(message).success(true)
                    .data(testCase)
                    .build();
            log.info(message);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (final CustomException cs) {
            final String message = cs.getMessage();
            log.error(message, cs);
            CustomResponse response = CustomResponse.builder().timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.EXPECTATION_FAILED.value()).status(HttpStatus.EXPECTATION_FAILED)
                    .message(message).success(false)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        } catch (final Exception e) {
            final String message = "Unable to create TestCase";
            log.error(message, e);
            CustomResponse response = CustomResponse.builder().timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.EXPECTATION_FAILED.value()).status(HttpStatus.EXPECTATION_FAILED)
                    .message(message).success(false)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PutMapping("/update/{testCaseId}")
    @Operation(summary = "Update TestCase", description = "Update existing testcase for logged-in user",
            security = { @SecurityRequirement(name = "Bearer Token") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "TestCase updated successfully"),
            @ApiResponse(responseCode = "417", description = "Unable to update testcase")
    })
    public ResponseEntity<CustomResponse> updateTestCase(@PathVariable("testCaseId") Long testCaseId,
                                                        @Valid @RequestBody TestCaseDto testCaseDto,
                                                        SecurityContextHolder securityContextHolder) {
        try {
            log.info("Attempt TestCase updating");
            final String message = "successful";
            final User user = SecurityConfig.extractUserDetailsFromSecurityContext(securityContextHolder);
            final TestCase testCase = TCService.updateUserTestCase(testCaseId, testCaseDto, user);
            final CustomResponse response = CustomResponse.builder().timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.OK.value()).status(HttpStatus.OK)
                    .message(message).success(true)
                    .data(testCase)
                    .build();
            log.info(message);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (final CustomException cs) {
            final String message = cs.getMessage();
            log.error(message, cs);
            CustomResponse response = CustomResponse.builder().timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.EXPECTATION_FAILED.value()).status(HttpStatus.EXPECTATION_FAILED)
                    .message(message).success(false)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        } catch (final Exception e) {
            final String message = "Unable to update TestCase";
            log.error(message, e);
            CustomResponse response = CustomResponse.builder().timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.EXPECTATION_FAILED.value()).status(HttpStatus.EXPECTATION_FAILED)
                    .message(message).success(false)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PutMapping("/update-status/{testCaseId}")
    @Operation(summary = "Update TestCase Status", description = "Update existing testcase status for logged-in user",
            security = { @SecurityRequirement(name = "Bearer Token") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "TestCase status updated successfully"),
            @ApiResponse(responseCode = "417", description = "Unable to update testcase status")
    })
    public ResponseEntity<CustomResponse> updateTestCaseStatus(@PathVariable("testCaseId") Long testCaseId,
                                                               @RequestParam("status")TestCaseStatus status,
                                                               SecurityContextHolder securityContextHolder) {
        try {
            log.info("Attempt TestCase status updating");
            final String message = "successful";
            final User user = SecurityConfig.extractUserDetailsFromSecurityContext(securityContextHolder);
            final TestCase testCase = TCService.updateTestCaseStatus(testCaseId, user, status);
            final CustomResponse response = CustomResponse.builder().timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.OK.value()).status(HttpStatus.OK)
                    .message(message).success(true)
                    .data(testCase)
                    .build();
            log.info(message);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (final CustomException cs) {
            final String message = cs.getMessage();
            log.error(message, cs);
            CustomResponse response = CustomResponse.builder().timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.EXPECTATION_FAILED.value()).status(HttpStatus.EXPECTATION_FAILED)
                    .message(message).success(false)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        } catch (final Exception e) {
            final String message = "Unable to update TestCase status";
            log.error(message, e);
            CustomResponse response = CustomResponse.builder().timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.EXPECTATION_FAILED.value()).status(HttpStatus.EXPECTATION_FAILED)
                    .message(message).success(false)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PutMapping("/update-priority/{testCaseId}")
    @Operation(summary = "Update TestCase Priority", description = "Update existing testcase priority for logged-in user",
            security = { @SecurityRequirement(name = "Bearer Token") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "TestCase status updated successfully"),
            @ApiResponse(responseCode = "417", description = "Unable to update testcase status")
    })
    public ResponseEntity<CustomResponse> updateTestCasePriority(@PathVariable("testCaseId") Long testCaseId,
                                                               @RequestParam("priority") TestCasePriority priority,
                                                               SecurityContextHolder securityContextHolder) {
        try {
            log.info("Attempt TestCase priority updating");
            final String message = "successful";
            final User user = SecurityConfig.extractUserDetailsFromSecurityContext(securityContextHolder);
            final TestCase testCase = TCService.updateTestCasePriority(testCaseId, user, priority);
            final CustomResponse response = CustomResponse.builder().timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.OK.value()).status(HttpStatus.OK)
                    .message(message).success(true)
                    .data(testCase)
                    .build();
            log.info(message);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (final CustomException cs) {
            final String message = cs.getMessage();
            log.error(message, cs);
            CustomResponse response = CustomResponse.builder().timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.EXPECTATION_FAILED.value()).status(HttpStatus.EXPECTATION_FAILED)
                    .message(message).success(false)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        } catch (final Exception e) {
            final String message = "Unable to update TestCase priority";
            log.error(message, e);
            CustomResponse response = CustomResponse.builder().timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.EXPECTATION_FAILED.value()).status(HttpStatus.EXPECTATION_FAILED)
                    .message(message).success(false)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping
    @Operation(summary = "All TestCase", description = "Retrieve all user TestCases",
            security = { @SecurityRequirement(name = "Bearer Token") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Testcases fetched successfully"),
            @ApiResponse(responseCode = "417", description = "Unable to fetch user testcases")
    })
    public ResponseEntity<CustomResponse> getAllTestCases(SecurityContextHolder securityContextHolder,
                                                         @RequestParam("page") int page,
                                                         @RequestParam("size") int size) {
        try {
            log.info("Attempt to fetch all user TestCases");
            final String message = "User testcases fetched successfully";
            final User user = SecurityConfig.extractUserDetailsFromSecurityContext(securityContextHolder);
            final PageResponse pageResponse = TCService.getAllTestCases(user, page, size);
            final CustomResponse response = CustomResponse.builder().timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.OK.value()).status(HttpStatus.OK)
                    .message(message).success(true)
                    .data(pageResponse)
                    .build();
            log.info(message);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (final Exception e) {
            final String message = "Unable to fetch user testcase";
            log.error(message, e);
            CustomResponse response = CustomResponse.builder().timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.EXPECTATION_FAILED.value()).status(HttpStatus.EXPECTATION_FAILED)
                    .message(message).success(false)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/{testCaseId}")
    @Operation(summary = "Get TestCase", description = "Get TestCase by id",
            security = { @SecurityRequirement(name = "Bearer Token") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User testcase fetched successfully"),
            @ApiResponse(responseCode = "417", description = "Unable to fetch user testcase")
    })
    public ResponseEntity<CustomResponse> getTestCaseById(SecurityContextHolder securityContextHolder,
                                                         @PathVariable("testCaseId") Long testCaseId) {
        try {
            log.info("Attempt to fetch user testcase");
            final String message = "User testcase fetched successfully";
            final User user = SecurityConfig.extractUserDetailsFromSecurityContext(securityContextHolder);
            final Optional<TestCase> testCase = TCService.getUserTestCaseById(testCaseId, user);
            final CustomResponse response = CustomResponse.builder().timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.OK.value()).status(HttpStatus.OK)
                    .message(message).success(true)
                    .data(testCase)
                    .build();
            log.info(message);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (final Exception e) {
            final String message = "Unable to fetch user testcase";
            log.error(message, e);
            CustomResponse response = CustomResponse.builder().timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.EXPECTATION_FAILED.value()).status(HttpStatus.EXPECTATION_FAILED)
                    .message(message).success(false)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @DeleteMapping("/{testCaseId}")
    @Operation(summary = "Delete TestCase", description = "Delete testcase by id",
            security = { @SecurityRequirement(name = "Bearer Token") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User testcase deleted successfully"),
            @ApiResponse(responseCode = "417", description = "Unable to delete user testcase")
    })
    public ResponseEntity<CustomResponse> deleteTestCaseById(SecurityContextHolder securityContextHolder,
                                                         @PathVariable("testCaseId") Long testCaseId) {
        try {
            log.info("Attempt to delete user testcase");
            final String message = "User testcase deleted successfully";
            final User user = SecurityConfig.extractUserDetailsFromSecurityContext(securityContextHolder);
            TCService.deleteUserTestCase(testCaseId, user);
            final CustomResponse response = CustomResponse.builder().timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.OK.value()).status(HttpStatus.OK)
                    .message(message).success(true)
                    .build();
            log.info(message);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (final Exception e) {
            final String message = "Unable to delete user TestCase";
            log.error(message, e);
            CustomResponse response = CustomResponse.builder().timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.EXPECTATION_FAILED.value()).status(HttpStatus.EXPECTATION_FAILED)
                    .message(message).success(false)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
    }
}
