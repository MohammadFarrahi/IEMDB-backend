package ie.types;

import ie.exception.CustomException;

import java.util.regex.Pattern;

public class Email {
    private final String addr;
    public Email(String addr) throws CustomException {
        boolean isValid = Pattern.compile("^(.+)@(\\S+)$")
                .matcher(addr)
                .matches();
        if (isValid) {
            this.addr = addr;
        }
        else {
            throw new CustomException("Invalid email");
        }
    }
    @Override
    public String toString() {
        return this.addr;
    }
}
