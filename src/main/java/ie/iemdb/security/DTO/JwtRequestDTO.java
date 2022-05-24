package ie.iemdb.security.DTO;

public class JwtRequestDTO {
    private String userEmail;
    private String password;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean checkNullability() {
        return userEmail != null && password != null;
    }
}
