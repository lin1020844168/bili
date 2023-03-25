package com.lin.bili.test.vo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

@Data
public class EsCompletionVo {
    /**
     *
     */
    @JsonProperty("keyword")
    private String name;
}
