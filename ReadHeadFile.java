import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadHeadFile {

    public static void main(String[] args) {
        List<String> headContent = extractHeadContent("TestComp.html");
        System.out.println("Content of headCode array:");
        for (String code : headContent) {
            System.out.println(code);
        }
    }

    /**
     * Reusable method to extract content from the <head> section of an HTML file
     * @param htmlFile Path to the HTML file
     * @return List of tokens from the head section
     */
    public static List<String> extractHeadContent(String htmlFile) {
        List<String> headTokens = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader(htmlFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            StringBuilder headContent = new StringBuilder();
            boolean insideHead = false;

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("<head>")) {
                    insideHead = true;
                    continue; // Skip the <head> tag itself
                }
                if (insideHead) {
                    headContent.append(line).append("\n");
                    if (line.contains("</head>")) {
                        break; // Exit loop when </head> tag is found
                    }
                }
            }

            // Split headContent by whitespace and add non-empty parts to list
            String[] headCode = headContent.toString().split("\\s+");
            
            for (String code : headCode) {
                if (!code.isEmpty()) {
                    headTokens.add(code);
                }
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return headTokens;
    }
}
