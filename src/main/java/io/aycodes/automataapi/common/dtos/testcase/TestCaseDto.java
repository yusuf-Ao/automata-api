package io.aycodes.automataapi.common.dtos.testcase;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class TestCaseDto {

    @NotBlank(message = "Testcase must have a valid title")
    private String              title;

    @NotBlank(message = "Testcase must have a valid description")
    private String              description;
}
