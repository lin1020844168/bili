import com.lin.bili.jsuop.JsoupApplication;
import com.lin.bili.jsuop.service.BiliBIliService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JsoupApplication.class)
public class Test {
    @Autowired
    private BiliBIliService biliBIliService;

    @org.junit.Test
    public void test() throws IOException {
        biliBIliService.getCategoryConfig();
    }
}
