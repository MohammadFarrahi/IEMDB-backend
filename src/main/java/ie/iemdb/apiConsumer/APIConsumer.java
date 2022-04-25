package ie.iemdb.apiConsumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.iemdb.util.types.Constant;
import org.jsoup.Jsoup;

import java.io.IOException;

public abstract class APIConsumer {
    protected String apiUrl;

    public void importData() throws IOException {
        var jsonData = getJsonData();
        var nodeArray = getJsonNode(jsonData);
        loadRepo(nodeArray);
    }

    private String getJsonData() throws IOException {
        return Jsoup.connect(apiUrl).ignoreContentType(true).execute().body();
    }

    private JsonNode getJsonNode(String jsonData) throws JsonProcessingException {
        return new ObjectMapper().convertValue(jsonData, JsonNode.class);
    }

    protected abstract void loadRepo(JsonNode nodeArray);

}
