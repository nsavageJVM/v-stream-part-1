package fish.beans.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.StringJoiner;

@Component
@Qualifier("utubeapiservice")
public class UTubeApi {

    @Value("${app.youtube.api}")
    private String YOUTUBE_API_KEY;

    private static final String url0 = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=1&order=rating&q=";
    private static final String urlVidBase = "https://www.youtube.com/watch?v=%s";


    public String runApiRequest(String mtchTxt) {
        String url1 = String.format("&key=%s", YOUTUBE_API_KEY);

        mtchTxt = mtchTxt.replace(" ", "+");

        String result = null;

        StringJoiner urlBuilder = new StringJoiner("");
        urlBuilder.add(url0).add(mtchTxt).add(url1);
        String url = urlBuilder.toString();
        Document doc = null;
        try {
            doc = Jsoup.connect(url).timeout(10 * 1000).ignoreContentType(true).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String getJson = doc.text();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jObj = null;
        try {
            jObj = mapper.readTree(getJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Iterator<Map.Entry<String, JsonNode>> itr = jObj.fields();

        while (itr.hasNext()) {

            Map.Entry<String, JsonNode> e = itr.next();
            if (e.getKey().equals("items")) {
                JsonNode items = e.getValue();

                result = String.format(urlVidBase, items.get(0).get("id").get("videoId").asText());

            }

        }
        return result;

    }


}
