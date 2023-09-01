package io.aycodes.automataapi.products.controller;

import io.aycodes.automataapi.common.config.SecurityConfig;
import io.aycodes.automataapi.common.dtos.CustomException;
import io.aycodes.automataapi.common.dtos.CustomResponse;
import io.aycodes.automataapi.common.dtos.PageResponse;
import io.aycodes.automataapi.common.dtos.product.ProductDto;
import io.aycodes.automataapi.products.model.Product;
import io.aycodes.automataapi.products.service.ProductService;
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
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {


    private final ProductService                productService;


    @PostMapping("/new-product")
    @Operation(summary = "Create Product", description = "Create new product for logged-in user",
            security = { @SecurityRequirement(name = "Bearer Token") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "417", description = "Unable to create product")
    })
    public ResponseEntity<CustomResponse> createProduct(@Valid @RequestBody final ProductDto productDto,
                                                        SecurityContextHolder securityContextHolder) {
        try {
            log.info("Attempt product creation");
            final String message = "successful";
            final User user = SecurityConfig.extractUserDetailsFromSecurityContext(securityContextHolder);
            final Product product = productService.createProductForUser(productDto, user);
            final CustomResponse response = CustomResponse.builder().timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.CREATED.value()).status(HttpStatus.CREATED)
                    .message(message).success(true)
                    .data(product)
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
            final String message = "Unable to create product";
            log.error(message, e);
            CustomResponse response = CustomResponse.builder().timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.EXPECTATION_FAILED.value()).status(HttpStatus.EXPECTATION_FAILED)
                    .message(message).success(false)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PutMapping("/update/{productId}")
    @Operation(summary = "Update Product", description = "Update existing product for logged-in user",
            security = { @SecurityRequirement(name = "Bearer Token") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "417", description = "Unable to update product")
    })
    public ResponseEntity<CustomResponse> updateProduct(@PathVariable("productId") Long productId,
                                                        @Valid @RequestBody ProductDto productDto,
                                                        SecurityContextHolder securityContextHolder) {
        try {
            log.info("Attempt product updating");
            final String message = "successful";
            final User user = SecurityConfig.extractUserDetailsFromSecurityContext(securityContextHolder);
            final Product product = productService.updateUserProduct(productId, productDto, user);
            final CustomResponse response = CustomResponse.builder().timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.OK.value()).status(HttpStatus.OK)
                    .message(message).success(true)
                    .data(product)
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
            final String message = "Unable to update product";
            log.error(message, e);
            CustomResponse response = CustomResponse.builder().timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.EXPECTATION_FAILED.value()).status(HttpStatus.EXPECTATION_FAILED)
                    .message(message).success(false)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping
    @Operation(summary = "All products", description = "Retrieve all user products",
            security = { @SecurityRequirement(name = "Bearer Token") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User products fetched successfully"),
            @ApiResponse(responseCode = "417", description = "Unable to fetch user products")
    })
    public ResponseEntity<CustomResponse> getAllProducts(SecurityContextHolder securityContextHolder,
                                                         @RequestParam("page") int page,
                                                         @RequestParam("size") int size) {
        try {
            log.info("Attempt to fetch all user products");
            final String message = "User products fetched successfully";
            final User user = SecurityConfig.extractUserDetailsFromSecurityContext(securityContextHolder);
            final PageResponse pageResponse = productService.getAllUserProducts(user, page, size);
            final CustomResponse response = CustomResponse.builder().timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.OK.value()).status(HttpStatus.OK)
                    .message(message).success(true)
                    .data(pageResponse)
                    .build();
            log.info(message);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (final Exception e) {
            final String message = "Unable to fetch user products";
            log.error(message, e);
            CustomResponse response = CustomResponse.builder().timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.EXPECTATION_FAILED.value()).status(HttpStatus.EXPECTATION_FAILED)
                    .message(message).success(false)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Get product", description = "Get product by id",
            security = { @SecurityRequirement(name = "Bearer Token") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User product fetched successfully"),
            @ApiResponse(responseCode = "417", description = "Unable to fetch user product")
    })
    public ResponseEntity<CustomResponse> getProductById(SecurityContextHolder securityContextHolder,
                                                         @PathVariable("productId") Long productId) {
        try {
            log.info("Attempt to fetch user product");
            final String message = "User product fetched successfully";
            final User user = SecurityConfig.extractUserDetailsFromSecurityContext(securityContextHolder);
            final Optional<Product> product = productService.getUserProductById(productId, user);
            final CustomResponse response = CustomResponse.builder().timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.OK.value()).status(HttpStatus.OK)
                    .message(message).success(true)
                    .data(product)
                    .build();
            log.info(message);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (final Exception e) {
            final String message = "Unable to fetch user product";
            log.error(message, e);
            CustomResponse response = CustomResponse.builder().timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.EXPECTATION_FAILED.value()).status(HttpStatus.EXPECTATION_FAILED)
                    .message(message).success(false)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "Delete product", description = "Delete product by id",
            security = { @SecurityRequirement(name = "Bearer Token") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User product deleted successfully"),
            @ApiResponse(responseCode = "417", description = "Unable to delete user product")
    })
    public ResponseEntity<CustomResponse> deleteProductById(SecurityContextHolder securityContextHolder,
                                                         @PathVariable("productId") Long productId) {
        try {
            log.info("Attempt to delete user product");
            final String message = "User product deleted successfully";
            final User user = SecurityConfig.extractUserDetailsFromSecurityContext(securityContextHolder);
            productService.deleteUserProduct(productId, user);
            final CustomResponse response = CustomResponse.builder().timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.OK.value()).status(HttpStatus.OK)
                    .message(message).success(true)
                    .build();
            log.info(message);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (final Exception e) {
            final String message = "Unable to delete user product";
            log.error(message, e);
            CustomResponse response = CustomResponse.builder().timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.EXPECTATION_FAILED.value()).status(HttpStatus.EXPECTATION_FAILED)
                    .message(message).success(false)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
    }
}
