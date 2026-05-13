import java.util.HashMap;
import java.util.Map;

public class URLComparator {

    public static void main(String[] args) {
        String bankURL = "https://www.examplebank.com";
        String testURL = "https://www.exampleebak.com";

        // Case sensitivity: convert to lowercase
        String resultTable = calculateSimilarity(bankURL.toLowerCase(), testURL.toLowerCase());
        System.out.println(resultTable);
    }

    /**
     * Reusable method to compare two URLs and return similarity data
     * @param url1 First URL to compare
     * @param url2 Second URL to compare
     * @return Map containing url1, url2, similarity percentage, and matching characters
     */
    public static Map<String, Object> compareUrls(String url1, String url2) {
        Map<String, Object> result = new HashMap<>();
        result.put("url1", url1);
        result.put("url2", url2);

        // Find the index of "www." in the URLs
        int wwwIndex1 = url1.indexOf("www.");
        int wwwIndex2 = url2.indexOf("www.");

        // If "www." is not present, start comparison from the beginning
        int startIndex1 = wwwIndex1 != -1 ? wwwIndex1 : 0;
        int startIndex2 = wwwIndex2 != -1 ? wwwIndex2 : 0;

        char[] url1Chars = url1.substring(startIndex1).toCharArray();
        char[] url2Chars = url2.substring(startIndex2).toCharArray();

        int matchCount = 0;
        StringBuilder matchingCharacters = new StringBuilder();

        for (int i = 0; i < url1Chars.length; i++) {
            boolean foundMatch = false;
            for (int j = i; j < url2Chars.length; j++) {
                if (url1Chars[i] == url2Chars[j]) {
                    matchCount++;
                    matchingCharacters.append(url1Chars[i]);
                    foundMatch = true;
                    break;
                }
            }
            
            if (!foundMatch) {
                matchingCharacters.append(url1Chars[i]);
            }
        }

        int maxLength = Math.max(url1Chars.length, url2Chars.length);
        double similarity = ((double) matchCount / maxLength) * 100;

        result.put("similarity", Math.round(similarity * 100.0) / 100.0);
        result.put("matches", matchingCharacters.toString().trim());
        result.put("matchCount", matchCount);

        return result;
    }

    private static String calculateSimilarity(String url1, String url2) {
        // Find the index of "www." in the URLs
        int wwwIndex1 = url1.indexOf("www.");
        int wwwIndex2 = url2.indexOf("www.");

        // If "www." is not present, start comparison from the beginning
        int startIndex1 = wwwIndex1 != -1 ? wwwIndex1 : 0;
        int startIndex2 = wwwIndex2 != -1 ? wwwIndex2 : 0;

        char[] url1Chars = url1.substring(startIndex1).toCharArray();
        char[] url2Chars = url2.substring(startIndex2).toCharArray();

        int matchCount = 0;

        // Use a StringBuilder to store matching characters
        StringBuilder matchingCharacters = new StringBuilder();

        // Use a for loop to compare characters and count matches
        // First similarity check using [i] iterator
        for (int i = 0; i < url1Chars.length ; i++) {
            boolean foundMatch = false;
            for (int j = i; j < url2Chars.length; j++) {
                // Compare characters at the same position
                if (url1Chars[i] == url2Chars[j]) {
                    // Characters match
                    matchCount++;
                    matchingCharacters.append(url1Chars[i]); // Append the matching character to matchingCharacters
                    foundMatch = true;
                    break; // Break the inner loop as a match is found
                }
            }
            
            // If no match is found, append the non-matching character to matchingCharacters
            if (!foundMatch) {
                matchingCharacters.append(url1Chars[i]);
            }
        }

        // Calculate similarity as a percentage based on the length of the longer substring
        int maxLength = Math.max(url1Chars.length, url2Chars.length);

        double similarity = ((double) matchCount / maxLength) * 100;

        // Build the result table
        StringBuilder resultTable = new StringBuilder();
        resultTable.append(String.format("%-20s | %-40s%n", "URL 1", url1));
        resultTable.append(String.format("%-20s | %-40s%n", "URL 2", url2));
        resultTable.append(String.format("%-20s | %-40s%n", "Matching Characters", matchingCharacters.toString().trim()));

        resultTable.append("-".repeat(60)).append("\n");
        resultTable.append(String.format("%-20s | %.2f%%%n", "Similarity", similarity));

        return resultTable.toString();
    }
}
