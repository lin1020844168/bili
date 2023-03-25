package com.lin.bili.search.service.impl;

import com.lin.bili.search.SearchApplication;
import com.lin.bili.search.dto.ToEsAnimeDto;
import com.lin.bili.search.feign.AnimeFeign;
import com.lin.bili.search.po.EsAnimePo;
import com.lin.bili.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(classes = SearchApplication.class)
@RunWith(SpringRunner.class)
public class SearchServiceImplTest {
    @Autowired
    private SearchService searchService;

    @Autowired
    private AnimeFeign animeFeign;


    @Test
    public void test2() throws IOException, ParseException {
        List<ToEsAnimeDto> data = animeFeign.AllToEsAnime().getData();
        List<EsAnimePo> esAnimePoList = data.stream().map(e -> {
            EsAnimePo esAnimePo = new EsAnimePo();
            esAnimePo.setId(e.getId());
            esAnimePo.setSeasonMonth(calcMonth(e.getPubTime()));
            return esAnimePo;
        }).collect(Collectors.toList());
        int size = esAnimePoList.size();
        System.out.println(size);
        int bucketCnt = 5;
        int remainder = size%bucketCnt;
        int bucketSize = size/bucketCnt+1;
        for (int i=0; i<size; i+=bucketSize) {
            if (remainder--==0) {
                bucketSize--;
            }
            List<EsAnimePo> subList = esAnimePoList.subList(i, i + bucketSize);
            searchService.bulkUpdateAnime(subList);
        }
    }

    @Test
    public void test() throws IOException {
        List<ToEsAnimeDto> data = animeFeign.AllToEsAnime().getData();
        List<EsAnimePo> esAnimePoList = data.stream().map(e -> new EsAnimePo(e.getCover(), e.getId(),
                e.getSeason(), e.getTitle(), e.getPartitionId(),
                e.getFavCount(), e.getRank() / 10.0, e.getPlayCount(),
                e.getPubTime(), e.getSeasonVersionId(), e.getSpokenLanguageTypeId(),
                e.getRegionId(), e.getIsFinish() == 1, e.getStyleIdList(), calcMonth(e.getPubTime())))
                .collect(Collectors.toList());
        int size = esAnimePoList.size();
        System.out.println(size);
        int bucketCnt = 5;
        int remainder = size%bucketCnt;
        int bucketSize = size/bucketCnt+1;
        for (int i=0; i<size; i+=bucketSize) {
            if (remainder--==0) {
                bucketSize--;
            }
            List<EsAnimePo> subList = esAnimePoList.subList(i, i + bucketSize);
            searchService.bulkAddAnime(subList);
        }

    }

    private Integer calcMonth(Date pubTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(pubTime);
        int month = calendar.get(Calendar.MONTH);
        return month/3*3+1;
    }
}