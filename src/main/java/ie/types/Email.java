package ie.types;

import java.util.regex.Pattern;

public class Email {
    private final String addr;
    public Email(String addr) throws Exception{
        boolean isValid = Pattern.compile("^(.+)@(\\S+)$")
                .matcher(addr)
                .matches();
        if (isValid) {
            this.addr = addr;
        }
        else {
            throw new Exception("Invalid email");
        }
    }
    @Override
    public String toString() {
        return this.addr;
    }
}
