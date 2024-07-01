import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadCss {
    public static void main(String[] args) {
        String cssFile = "admin.css"; // Path to your CSS file
        String[] commonHTMLTags = {"h1", "h2", "h3", "h4", "h5", "h6", "p", "a", "div", "span", "img", "ul", "ol",
                                    "li", "table", "tr", "td", "th", "form", "input", "textarea", "button", "select",
                                    "button", "label", "fieldset", "legend", "header", "footer", "nav", "main", 
                                    "section", "article", "blockquote", "cite", "blockquote", "iframe", "audio", 
                                    "video", "abbr", "acronym", "address", "b", "em", "strong", "i", "cite", "code",
                                    "pre", "q", "s", "small", "sub", "sup", "u"};
        
        List<String> nonStandardSelectors = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(cssFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Regular expression to match CSS selectors
                Pattern pattern = Pattern.compile("([\\w-]+)\\s*\\{");
                Matcher matcher = pattern.matcher(line);
                
                while (matcher.find()) {
                    String selector = matcher.group(1);
                    if (!isCommonHTMLTag(selector, commonHTMLTags)) {
                        nonStandardSelectors.add(selector);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Non-standard CSS selectors targeting HTML elements:");
        for (String selector : nonStandardSelectors) {
            System.out.println(selector);
        }
    }

    private static boolean isCommonHTMLTag(String selector, String[] commonHTMLTags) {
        for (String tag : commonHTMLTags) {
            if (selector.equalsIgnoreCase(tag)) {
                return true;
            }
        }
        return false;
    }
}
