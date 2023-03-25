import cn.hutool.core.util.IdUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lin.bili.test.TestApplication;
import com.lin.bili.test.constant.EsConstant;
import com.lin.bili.test.mapper.AnimeMapper;
import com.lin.bili.test.po.Anime;
import com.lin.bili.test.vo.EsAnimeVo;
import com.lin.bili.test.vo.EsCompletionVo;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@SpringBootTest(classes = TestApplication.class)
@RunWith(SpringRunner.class)
public class Test {
    @Autowired
    private AnimeMapper animeMapper;

    @Autowired
    private ElasticsearchClient esClient;

    @org.junit.Test
    public void test() throws IOException {
        List<Anime> animeList = animeMapper.selectList(new QueryWrapper<>());
        BulkRequest.Builder animeBuilder = new BulkRequest.Builder().index(EsConstant.ES_INDEX_ANIME);
        BulkRequest.Builder completionBuilder = new BulkRequest.Builder().index(EsConstant.ES_INDEX_COMPLETION);

        animeList.stream().forEach(e -> {
            EsAnimeVo esAnimeVo = new EsAnimeVo();
            BeanUtils.copyProperties(e, esAnimeVo);
            String id = IdUtil.getSnowflakeNextIdStr();
            animeBuilder.operations(o->o.create(o1->o1.id(id).document(esAnimeVo)));
            EsCompletionVo esCompletionVo = new EsCompletionVo();
            BeanUtils.copyProperties(e, esCompletionVo);
            completionBuilder.operations(o->o.create(o1->o1.id(id).document(esCompletionVo)));
        });
        esClient.bulk(animeBuilder.build());
        esClient.bulk(completionBuilder.build());
    }
}
