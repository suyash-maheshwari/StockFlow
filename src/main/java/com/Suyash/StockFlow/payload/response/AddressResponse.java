package com.Suyash.StockFlow.payload.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {

    private Long addressId;
    private Long userId;
    private String addressLine;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private boolean isDefault;
}
