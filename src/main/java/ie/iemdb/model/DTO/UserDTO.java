package ie.iemdb.model.DTO;

public class UserDTO {
    private String email;
    private String password;
    private String nickname;
    private String name;
    private String birthDate;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public boolean checkNullability() {
        if(
                this.email==null
                ||this.password==null
                ||this.name==null
                ||this.nickname==null
                ||this.birthDate==null
        ) {
            return false;
        }
        return true;
    }
}
