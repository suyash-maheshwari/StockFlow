package com.Suyash.StockFlow.payload.response;

import com.Suyash.StockFlow.enums.Role;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private Role role;
    private boolean enabled;
}
