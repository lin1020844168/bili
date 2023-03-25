package com.lin.bili.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendInfoDto {
    private String userId;
    private String name;
    private String img;
}
