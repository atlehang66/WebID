import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadHeadFile {

    public static void main(String[] args) {
        try {
            FileReader fileReader = new FileReader("TestComp.html");
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

            // Split headContent by whitespace and add non-empty parts to headCode array
            String[] headCode = headContent.toString().split("\\s+");
            
            // Print out the content of headCode array
            System.out.println("Content of headCode array:");
            for (String code : headCode) {
                System.out.println(code);
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
