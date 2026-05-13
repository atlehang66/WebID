# WebID Java Scripts Documentation

## Project Overview
This project contains Java utilities for analyzing and comparing web identifiers, CSS configurations, and HTML structure. The scripts are designed to identify phishing risks and analyze website components.

---

## Table of Contents
1. [URLComparator.java](#urlcomparatorjava)
2. [divCounter.java](#divcounterjava)
3. [ReadCss.java](#readcssjava)
4. [ReadHeadFile.java](#readheadfilejava)

---

## URLComparator.java

### Purpose
Compares the similarity between two URLs (typically to detect phishing attempts or domain spoofing) by calculating character matches and generating a similarity percentage.

### Key Features
- **Case-insensitive comparison**: Converts URLs to lowercase before analysis
- **WWW-aware comparison**: Starts comparison from the "www." portion to focus on domain names
- **Character-by-character matching**: Compares characters and tracks matching positions
- **Formatted output**: Displays results in a structured table format

### Main Method
```java
public static void main(String[] args)
```
- Compares two URLs: `https://www.examplebank.com` vs `https://www.exampleebak.com`
- Outputs a formatted table with URLs, matching characters, and similarity percentage

### Private Method

#### `calculateSimilarity(String url1, String url2)`
Performs the core comparison logic:
- Extracts substring starting from "www." (or beginning if "www." not found)
- Converts URLs to character arrays
- Iterates through url1 characters and searches for matches in url2
- Counts matches and tracks matching characters
- Calculates similarity as: `(matchCount / maxLength) * 100`
- Returns formatted table with results

### Output Example
```
URL 1                | https://www.examplebank.com
URL 2                | https://www.exampleebak.com
Matching Characters  | www.example.com
------------------------------------------------------------
Similarity           | XX.XX%
```

### Use Cases
- Phishing detection by comparing suspicious URLs against known legitimate URLs
- Domain typosquatting analysis
- Brand security monitoring

---

## divCounter.java

### Purpose
Counts the total occurrences of `</div>` closing tags in an HTML file to analyze page structure and complexity.

### Key Features
- **File reading**: Uses `BufferedReader` for efficient file processing
- **Word-based counting**: Splits each line into words and counts exact matches
- **Error handling**: Catches and prints `IOException` for file access issues
- **Simple output**: Displays the count of closing div tags

### Main Method
```java
public static void main(String[] args)
```
- Sets target file: `TestComp.html`
- Sets search term: `</div>`
- Reads file line by line and counts occurrences

### Algorithm
1. Opens `TestComp.html` with `BufferedReader`
2. Reads each line sequentially
3. Splits each line by whitespace using regex `\\s+`
4. Compares each word against `</div>` tag
5. Increments counter on exact match
6. Prints final count

### Output Example
```
Occurrences of "</div>" in TestComp.html: 42
```

### Use Cases
- HTML structure analysis
- Page complexity assessment
- Identifying poorly structured or deeply nested HTML

---

## ReadCss.java

### Purpose
Analyzes CSS files to identify non-standard CSS selectors that target custom HTML elements or classes (not standard HTML tags).

### Key Features
- **Comprehensive tag database**: Maintains array of 50+ common HTML tags
- **Regex pattern matching**: Uses regex to extract CSS selectors from file
- **Filter mechanism**: Identifies selectors that don't match standard HTML tags
- **List collection**: Uses `ArrayList` to store found non-standard selectors

### Main Method
```java
public static void main(String[] args)
```
- Targets CSS file: `admin.css`
- Defines array of standard HTML tags (h1-h6, p, div, span, form, etc.)
- Identifies and prints all non-standard selectors

### Standard HTML Tags Recognized
Headers: h1-h6
Structure: div, span, header, footer, nav, main, section, article
Text: p, a, b, em, strong, i, code, pre, blockquote, cite, q, small, sub, sup, u
Forms: form, input, textarea, button, select, label, fieldset, legend
Tables: table, tr, td, th
Lists: ul, ol, li
Media: img, iframe, audio, video
Other: abbr, acronym, address, s

### Private Method

#### `isCommonHTMLTag(String selector, String[] commonHTMLTags)`
- **Parameters**: selector (CSS selector to check), commonHTMLTags (array of valid tags)
- **Returns**: `true` if selector matches any standard HTML tag (case-insensitive), `false` otherwise
- **Logic**: Case-insensitive comparison against all known tags

### Algorithm
1. Opens `admin.css` file
2. Reads file line by line
3. Applies regex pattern `(\\w-]+)\\s*\\{` to extract selectors
4. For each matched selector, checks if it's a standard HTML tag
5. Non-matching selectors added to `ArrayList`
6. Prints all non-standard selectors

### Output Example
```
Non-standard CSS selectors targeting HTML elements:
.custom-button
#main-container
.brand-color
```

### Use Cases
- CSS code analysis and audit
- Identifying custom styling classes
- Understanding page styling patterns
- CSS framework analysis

---

## ReadHeadFile.java

### Purpose
Extracts and displays all content from the `<head>` section of an HTML file, useful for analyzing meta tags, stylesheets, scripts, and other header information.

### Key Features
- **Head section extraction**: Locates `<head>` and `</head>` tags
- **Line-by-line processing**: Uses `BufferedReader` for efficient reading
- **Content collection**: Accumulates head content in `StringBuilder`
- **Word tokenization**: Splits content into individual tokens for display

### Main Method
```java
public static void main(String[] args)
```
- Targets file: `TestComp.html`
- Extracts everything between `<head>` and `</head>` tags
- Prints content as tokenized words

### Algorithm
1. Creates `FileReader` to open `TestComp.html`
2. Wraps with `BufferedReader` for efficient line reading
3. Sets `insideHead` flag to `false`
4. Reads file line by line:
   - When `<head>` found: Sets flag to `true` and skips tag line
   - While inside head section: Appends lines to `StringBuilder`
   - When `</head>` found: Breaks loop
5. Splits accumulated content by whitespace using regex `\\s+`
6. Prints each token on separate line
7. Closes reader and handles `IOException`

### Output Example
```
Content of headCode array:
<title>
Example
Page
</title>
<meta
charset="utf-8">
<link
rel="stylesheet"
href="style.css">
```

### Use Cases
- HTML metadata analysis
- External stylesheet and script identification
- SEO meta tag extraction
- Document structure validation
- Security analysis (detecting inline scripts, etc.)

---

## Integration & Dependencies

### Files Required
- `TestComp.html` - HTML test file (required by divCounter and ReadHeadFile)
- `admin.css` - CSS file (required by ReadCss)

### Common Import Statements
All scripts use Java standard library I/O utilities:
- `java.io.BufferedReader` - Efficient file reading
- `java.io.FileReader` - File access
- `java.io.IOException` - Exception handling
- `java.util.ArrayList` - List storage (ReadCss only)
- `java.util.regex.Pattern` & `Matcher` - Pattern matching (ReadCss only)

---

## Compilation & Execution

### Compile All Scripts
```bash
javac divCounter.java ReadCss.java ReadHeadFile.java URLComparator.java
```

### Run Individual Scripts
```bash
java URLComparator      # Compare URLs
java divCounter         # Count </div> tags
java ReadCss            # Analyze CSS selectors
java ReadHeadFile       # Extract HTML head content
```

---

## Error Handling

All scripts implement basic error handling:
- **IOException catching**: For file access errors
- **Stack trace printing**: `e.printStackTrace()` displays error details
- **Graceful failure**: Scripts continue execution even if errors occur

---

## Performance Considerations

| Script | Time Complexity | Space Complexity | Notes |
|--------|-----------------|------------------|-------|
| URLComparator | O(n*m) | O(n+m) | n,m = URL lengths; character-by-character comparison |
| divCounter | O(n) | O(1) | n = file size; line-by-line processing |
| ReadCss | O(n*t) | O(k) | n = file lines; t = tags; k = non-standard selectors |
| ReadHeadFile | O(n) | O(n) | n = head section size; stores entire section |

---

## Security & Phishing Detection

These scripts were designed to support **phishing detection** analysis:
- **URLComparator**: Identifies domain spoofing attempts
- **divCounter**: Analyzes page complexity (phishing pages often have unusual structures)
- **ReadCss**: Detects suspicious styling patterns
- **ReadHeadFile**: Extracts metadata that may indicate malicious intent

---

## Future Enhancements

- Add command-line arguments for flexible file input
- Implement advanced similarity algorithms (Levenshtein distance)
- Add support for batch processing multiple files
- Create database of known phishing patterns
- Generate detailed security reports
- Add Unicode and international character support
