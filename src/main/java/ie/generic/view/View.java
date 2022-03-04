package ie.generic.view;

import ie.util.types.Constant;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class View {
    public static String getCSVFromList(String list) {
        return list.substring(1, list.length()-1).trim().replace("\"", "");
    }
    public String getSuccessHtmlResponse() throws IOException {
        return Jsoup.parse(new File(Constant.Template.SUCCESS_200), "UTF-8").html();
    }
}
