package io.aycodes.automataapi.products.service.impl;


import io.aycodes.automataapi.common.dtos.CustomException;
import io.aycodes.automataapi.common.dtos.PageResponse;
import io.aycodes.automataapi.common.dtos.product.ProductDto;
import io.aycodes.automataapi.products.model.Product;
import io.aycodes.automataapi.products.repository.ProductRepo;
import io.aycodes.automataapi.products.service.ProductService;
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
public class ProductServiceImpl implements ProductService {

    private final ProductRepo           productRepo;

    @Override
    public PageResponse getAllUserProducts(final User user, final int page, final int size) {
        log.info("Retrieving all user products");
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());
        Page<Product> productPage = productRepo.findAllByUser(user, pageable);
        List<Product> productList = productPage.getContent();
        return PageResponse.builder()
                .pageContent(productList)
                .currentPage(productPage.getNumber())
                .totalItems(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .build();
    }

    @Override
    public Optional<Product> getUserProductById(final Long id, final User user) {
        log.info("Retrieving user product by id");
        return productRepo.findByIdAndUser(id, user);
    }

    @Override
    public Product createProductForUser(final ProductDto productDto, final User user) throws CustomException {
        log.info("checking for product name duplicate");
        if (productRepo.existsByName(productDto.getName())) {
            final String message = "Product with same name already exists";
            log.error(message);
            throw new CustomException(message);
        }
        log.info("Creating new user product");
        Product product = Product.builder()
                .name(productDto.getName())
                .price(productDto.getPrice())
                .user(user)
                .build();
        log.info("Persisting new product to db");
        return productRepo.save(product);
    }

    @Override
    public Product updateUserProduct(final Long id, final ProductDto productDto, final User user) throws CustomException {
        Optional<Product> product = productRepo.findByIdAndUser(id, user);
        if (product.isEmpty()) {
            final String message = "Product not found";
            log.error(message);
            throw new CustomException(message);
        }
        product.get().setName(productDto.getName());
        product.get().setPrice(productDto.getPrice());
        log.info("Persisting updated product info to db");
        return productRepo.save(product.get());
    }

    @Override
    public void deleteUserProduct(final Long id, final User user) {
        log.info("Deleting user product");
        productRepo.deleteByIdAndUser(id, user);
    }
}
