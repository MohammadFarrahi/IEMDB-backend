package ie.generic.view;

import java.util.List;

public abstract class View {
    public static String getCSVFromList(String list) {
        return list.substring(1, list.length()-1).trim().replace("\"", "");
    }
}
