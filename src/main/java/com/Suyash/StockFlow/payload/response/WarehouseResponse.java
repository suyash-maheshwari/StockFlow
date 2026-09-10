package com.Suyash.StockFlow.payload.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WarehouseResponse {

    private Long warehouseId;
    private String warehouseName;
    private String addressLine;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String contactNumber;
    private boolean active;
}
