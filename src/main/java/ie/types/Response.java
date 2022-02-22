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

    @JsonGetter("data")
    private String getMessage() {
        return message;
    }

    @JsonGetter("success")
    private boolean getStatus() {
        return status;
    }

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        }catch (Exception e) {
            return "";
        }
    }
}
