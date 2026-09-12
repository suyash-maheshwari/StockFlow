package com.Suyash.StockFlow.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierDto {

    @NotBlank(message = "Supplier name is required")
    private String supplierName;

    private String contactPerson;

    @NotBlank(message = "Contact Number is required")
    @Pattern(regexp = "^\\+?[0-9]{7,15}$",
            message = "Contact number must be 7 to 15 digits, optionally starting with '+'")
    private String contactNumber;

    @Email(message = "Email must be valid")
    private String email;

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
