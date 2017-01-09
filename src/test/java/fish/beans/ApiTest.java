package fish.beans;

import com.fasterxml.jackson.databind.JsonNode;
import fish.beans.service.UTubeApi;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testfx.api.FxToolkit;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by ubu on 22.12.16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {



    @Autowired
    @Qualifier("utubeapiservice")
    UTubeApi utubeApi;


    @BeforeClass
    public static void baseSetupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();

    }


    @Test
    public void genericQueryTest() {
        String result=  utubeApi.runApiRequest("pop");
        System.out.println(result);








    }





}
