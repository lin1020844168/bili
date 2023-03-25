package com.lin.bili.anime.po;

import com.lin.bili.anime.AnimeApplication;
import com.lin.bili.anime.service.RegionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;

@SpringBootTest(classes = AnimeApplication.class)
@RunWith(SpringRunner.class)
public class RegionTest {
    @Autowired
    RegionService regionService;

    @Test
    public void test() throws IOException {
        FileReader fileReader = new FileReader("E:\\java\\gitdb\\bili\\anime\\src\\test\\a.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String s = "";
        while ((s = bufferedReader.readLine()) != null) {
            String[] split = s.split("：");
            regionService.addRegion(split[1]);
        }
    }
}