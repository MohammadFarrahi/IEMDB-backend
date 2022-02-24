package ie.types;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Response {
    private final boolean status;
    private final String message;

    public Response (boolean status, String message) {
        this.message = message;
        this.status = status;
    }

    @JsonGetter(Constant.Response.DATA)
    private String getMessage() {
        return message;
    }

    @JsonGetter(Constant.Response.STATUS)
    private boolean getStatus() {
        return status;
    }

    @Override
    public String toString() {
        // TODO: resolve this issue : if message is a string of json, printing response will be not well-formatted
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        }catch (Exception e) {
            return "";
        }
    }
}
