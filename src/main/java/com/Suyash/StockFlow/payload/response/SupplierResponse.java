package com.Suyash.StockFlow.payload.response;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierResponse {

    private Long supplierId;
    private String supplierName;
    private String contactPerson;
    private String contactNumber;
    private String email;
    private String addressLine;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private boolean active;
}
