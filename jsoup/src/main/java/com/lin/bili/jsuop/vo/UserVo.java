package com.lin.bili.jsuop.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo implements Serializable {
    private String avatar;
    private String name;
    private String date;
}
