package com.lin.bili.anime.mapper;

import com.lin.bili.anime.AnimeApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = AnimeApplication.class)
@RunWith(SpringRunner.class)
public class AnimeMapperTest {
    @Autowired
    AnimeMapper animeMapper;

    @Test
    public void getAnimeDetailById() {
    }
}