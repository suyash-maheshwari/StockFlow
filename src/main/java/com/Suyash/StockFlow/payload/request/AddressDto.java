package com.Suyash.StockFlow.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

    @NotBlank(message = "Address Line is required")
    private String addressLine;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Postal Code is required")
    private String postalCode;

    @NotBlank(message = "Country is required")
    private String country;
}
