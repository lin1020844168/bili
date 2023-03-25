package com.lin.bili.chat.po;

import com.lin.bili.common.utils.JJsonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.lin.bili.chat.constant.MessageType.RETURN;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnMessage {
    private static final long serialVersionUID = 1L;

    /**
     * 方便前端对接
     */
    private String token;

    private Integer type;

    private Integer srcType;

    private boolean isSuccess;

    public final static String success(String token, Integer srcType) {
        ReturnMessage returnMessage = new ReturnMessage(token, RETURN, srcType, true);
        return JJsonUtils.parse(returnMessage);
    }

    public final static String fail(String token, Integer srcType) {
        ReturnMessage returnMessage = new ReturnMessage(token, RETURN, srcType, false);
        return JJsonUtils.parse(returnMessage);
    }
}
