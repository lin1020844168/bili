package com.lin.bili.jsuop.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IllustVo implements Serializable {
    private String[] imgs;
    private String authorId;
    private String authorAvatar;
    private String authorName;
    private String likeTotal;
    private String favTotal;
    private String date;
    private String[] tags;
}
