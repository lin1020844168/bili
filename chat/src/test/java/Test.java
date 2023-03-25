import com.lin.bili.chat.ChatApplication;
import com.lin.bili.chat.mapper.NotificationMessageMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = ChatApplication.class)
@RunWith(SpringRunner.class)
public class Test {
    @Autowired
    NotificationMessageMapper notificationMessageMapper;

    @org.junit.Test
    public void test () {

    }

}
