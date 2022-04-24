package ie.iemdb.util.types;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Response {
    private final boolean status;
    private final String message;
    private final ObjectMapper mapper;

    public Response (boolean status, String message) {
        this.message = message;
        this.status = status;
        this.mapper = new ObjectMapper();
    }

    @JsonGetter(Constant.Response.DATA)
    private String getMessage() {
        return message;
    }

    @JsonGetter(Constant.Response.STATUS)
    private boolean getStatus() {
        return status;
    }

    public String stringify() throws Exception {
        ObjectNode node = mapper.createObjectNode();
        node.put(Constant.Response.STATUS, status);
        try {
            var messageNode = mapper.readTree(message);
            node.set(Constant.Response.DATA, messageNode);

        }catch (Exception e) {
            node.put(Constant.Response.DATA, message);
        }
        return mapper.writeValueAsString(node);
    }
}
