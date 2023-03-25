import com.lin.bili.video.VideoApplication;
import com.lin.bili.video.service.VideoRequestService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = VideoApplication.class)
@RunWith(SpringRunner.class)
public class Test {
    @Autowired
    private VideoRequestService videoRequestService;

    @org.junit.Test
    public void getVideoTest() throws Exception {
        videoRequestService.getVideoRequestUrl(508404+"", 16+"");
    }
}
