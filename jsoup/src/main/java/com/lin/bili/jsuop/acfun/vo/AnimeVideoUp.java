package com.lin.bili.jsuop.acfun.vo;

import com.lin.bili.jsuop.acfun.po.Anime;
import com.lin.bili.jsuop.acfun.po.AnimeVideo;
import com.lin.bili.jsuop.acfun.po.Video;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimeVideoUp {
    private Anime anime;
    private AnimeVideo animeVideo;
    private Video video;
}
