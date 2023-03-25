package com.lin.bili.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimeHistoryVo implements Serializable {
    /** 动漫id */
    private String id;
    /** 动漫名称 */
    private String name;
    /** 动漫封面 */
    private String cover;
    /** 浏览时间 */
    private Date date;
}
