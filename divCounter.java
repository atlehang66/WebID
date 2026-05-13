import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class divCounter {
    public static void main(String[] args) {
        int count = countDivs("TestComp.html");
        System.out.println("Occurrences of \"</div>\" in TestComp.html: " + count);
    }

    /**
     * Reusable method to count occurrences of </div> tags in an HTML file
     * @param filePath Path to the HTML file
     * @return Count of </div> tags found
     */
    public static int countDivs(String filePath) {
        String wordToCount = "</div>";
        int count = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
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

        return count;
    }
}
