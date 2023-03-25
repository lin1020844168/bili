package com.lin.bili.search.po;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.lin.bili.search.SearchApplication;
import com.lin.bili.search.feign.AnimeFeign;
import com.lin.bili.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@SpringBootTest(classes = SearchApplication.class)
@RunWith(SpringRunner.class)
public class EsCompletionPoTest {
    @Autowired
    AnimeFeign animeFeign;

    @Autowired
    ElasticsearchClient esClient;

    @Autowired
    SearchService searchService;

    @Test
    public void test() throws IOException {
    }
}