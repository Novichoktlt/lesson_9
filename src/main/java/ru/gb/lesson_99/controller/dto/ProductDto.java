package ru.gb.lesson_99.controller.dto;

import com.sun.istack.NotNull;
import lombok.*;
import ru.gb.lesson_99.entity.enums.Status;

import javax.persistence.Column;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {

    private Long id;
    @NotBlank
    private String title;

    @NotNull
    private int amt;
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 6, fraction = 2)
    private BigDecimal cost;
    @PastOrPresent
    private LocalDate manufactureDate;
    @NotNull
    private Status status;
}
