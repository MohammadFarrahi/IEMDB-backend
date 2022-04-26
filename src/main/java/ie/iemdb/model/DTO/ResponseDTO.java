package ie.iemdb.model.DTO;

public class ResponseDTO {
    public boolean status;
    public String statusMessage;

    public ResponseDTO(boolean status, String statusMessage) {
        this.status = status;
        this.statusMessage = statusMessage;
    }

    public boolean getStatusCode() {
        return status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusCode(boolean status) {
        this.status = status;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
