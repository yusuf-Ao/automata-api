package io.aycodes.automataapi.products.service;


import io.aycodes.automataapi.common.dtos.CustomException;
import io.aycodes.automataapi.common.dtos.PageResponse;
import io.aycodes.automataapi.common.dtos.product.ProductDto;
import io.aycodes.automataapi.products.model.Product;
import io.aycodes.automataapi.users.model.User;

import java.util.Optional;

public interface ProductService {

    PageResponse getAllUserProducts(User user, int page, int size);

    Optional<Product> getUserProductById(Long id, User user);

    Product createProductForUser(ProductDto productDto, User user) throws CustomException;

    Product updateUserProduct(Long id, ProductDto productDto, User user) throws CustomException;

    void deleteUserProduct(Long id, User user);
}
