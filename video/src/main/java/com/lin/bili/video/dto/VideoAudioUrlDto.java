package com.lin.bili.video.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 视频音频地址
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoAudioUrlDto implements Serializable {
    /**
     * 视频地址
     */
    private String videoUrl;
    /**
     * 音频地址
     */
    private String audioUrl;
}
