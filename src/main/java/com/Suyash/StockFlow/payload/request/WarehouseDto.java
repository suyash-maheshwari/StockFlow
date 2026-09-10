package com.Suyash.StockFlow.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseDto {

    @NotBlank(message = "Warehouse name is required")
    private String warehouseName;

    @NotBlank(message = "Address line is required")
    private String addressLine;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Postal code is required")
    private String postalCode;

    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "Contact number is required")
    private String contactNumber;

}
