package io.aycodes.automataapi.common.dtos.product;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class ProductDto {

    @NotBlank(message = "Product must have a valid name")
    private String          name;

    @NotNull
    @DecimalMin(value = "0.01", message = "Product price must be greater than 0.01")
    private Double          price;
}
