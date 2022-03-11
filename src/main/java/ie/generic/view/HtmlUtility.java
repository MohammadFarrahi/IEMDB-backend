package ie.generic.view;

import java.util.Map;

public class HtmlUtility {
    public static String getHtmlList(Map<String, String> dataMap) {
        var html = "";
        if (dataMap == null)
            return null;
        html += "<div><ul>";
        for(var data : dataMap.entrySet()) {
            html += "<li>" + data.getKey() + ": " + data.getValue() + "</li";
        }
        html +="</ul><div>";
        return html;
    }
}
