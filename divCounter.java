import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class divCounter {
    public static void main(String[] args) {
        String fileName = "TestComp.html";
        String wordToCount = "</div>";
        int count = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split the line into words
                String[] words = line.split("\\s+");
                // Count occurrences of the word
                for (String word : words) {
                    if (word.equals(wordToCount)) {
                        count++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Occurrences of \"" + wordToCount + "\" in " + fileName + ": " + count);
    }
}
