# WebID - Cybersecurity Analysis Dashboard

## Overview

WebID is a modern, dashboard-driven cybersecurity analysis toolkit that transforms the original Java-based web security analyzers into an interactive web dashboard. The system analyzes web components (HTML structure, CSS selectors, URL similarity, and metadata) to identify potential security risks and phishing attempts.

## Architecture

### System Components

```
WebID/
├── Frontend (Web Dashboard)
│   ├── index.html          - Dashboard UI
│   ├── style.css           - Dark cybersecurity theme
│   └── app.js              - Dashboard logic & API integration
│
├── Backend (Java HTTP Server)
│   ├── WebIDServer.java    - HTTP server & API endpoints
│   ├── URLComparator.java  - URL similarity analysis
│   ├── divCounter.java     - HTML structure analysis
│   ├── ReadCss.java        - CSS selector extraction
│   └── ReadHeadFile.java   - HTML metadata extraction
│
└── Test Files
    ├── TestComp.html       - Sample HTML for analysis
    └── admin.css           - Sample CSS for analysis
```

## Quick Start

### Prerequisites
- Java 11+ (includes `com.sun.net.httpserver`)
- Modern web browser
- No external dependencies required (lightweight setup)

### Installation & Execution

1. **Compile all Java files:**
```bash
cd /workspaces/WebID
javac *.java
```

2. **Start the server:**
```bash
java WebIDServer
```

3. **Access the dashboard:**
   - Open browser and navigate to: `http://localhost:8080`
   - The dashboard will automatically run initial analysis

### API Endpoints

All endpoints are REST-based and return JSON responses.

#### 1. Analyze HTML Structure
**GET** `/run/divcounter`

**Response:**
```json
{
  "divCount": 42,
  "status": "success"
}
```

#### 2. Extract CSS Selectors
**GET** `/run/readcss`

**Response:**
```json
{
  "selectors": ["custom-button", "main-container", "brand-color"],
  "selectorCount": 3,
  "status": "success"
}
```

#### 3. Extract Head Metadata
**GET** `/run/headfile`

**Response:**
```json
{
  "headContent": ["<title>", "Example", "Page", "</title>", ...],
  "contentCount": 24,
  "status": "success"
}
```

#### 4. Compare URLs
**GET** `/run/urlcompare?url1=<URL1>&url2=<URL2>`

**Parameters:**
- `url1` - First URL to compare
- `url2` - Second URL to compare

**Response:**
```json
{
  "url1": "https://www.examplebank.com",
  "url2": "https://www.exampleebak.com",
  "similarity": 94.74,
  "matches": "www.examplebank.com",
  "matchCount": 18,
  "status": "success"
}
```

## Dashboard Features

### Control Panel
- **Analyze HTML** - Scan for closing div tags and HTML structure analysis
- **Analyze CSS** - Extract non-standard CSS selectors
- **Extract Head Data** - Parse HTML metadata and scripts
- **Compare URLs** - Assess URL similarity for phishing detection

### Risk Assessment Meter
Dynamic risk scoring system that evaluates multiple security factors:
- **URL Risk** (0-40 points) - URL similarity to known legitimate sites
- **HTML Risk** (0-20 points) - Structure anomalies and excessive nesting
- **CSS Risk** (0-25 points) - Suspicious CSS patterns and obfuscation
- **Metadata Risk** (0-15 points) - Suspicious scripts or metadata

#### Risk Status Levels
- 🟢 **0-20: SAFE** - No security concerns detected
- 🟡 **21-40: LOW RISK** - Minor anomalies detected
- 🟠 **41-60: MEDIUM RISK** - Multiple risk factors identified
- 🔴 **61-80: HIGH RISK** - Significant security concerns
- 🚨 **81-100: PHISHING SUSPECTED** - Critical threat indicators

### Result Cards
Real-time metrics displayed for:
- **HTML Structure** - Count of closing div tags
- **URL Comparison** - Similarity percentage
- **CSS Selectors** - Non-standard selector count
- **Head Metadata** - Metadata token count

### Visualizations
- **Bar Chart** - Comparative analysis metrics (HTML, CSS, Metadata, URL Risk)
- **Doughnut Chart** - Risk distribution and safety assessment

### Detailed Analysis Section
- URL comparison details with matching character display
- Complete list of CSS selectors found
- Full head metadata token listing

## Technology Stack

### Frontend
- **HTML5** - Semantic markup
- **CSS3** - Dark cybersecurity theme with glassmorphism
- **Vanilla JavaScript** - No framework dependencies
- **Chart.js** - Data visualization

### Backend
- **Java 11+** - Core language
- **com.sun.net.httpserver** - Built-in HTTP server (no external libs)
- **JSON** - Manual string building for responses

### Design Elements
- Dark mode theme inspired by cybersecurity terminals
- Neon green accents (`#00ff88`) and cyan secondary colors
- Glassmorphism effects with backdrop blur
- Smooth animations and transitions
- Responsive grid layout

## Refactored Analyzer Methods

Each original Java analyzer has been refactored to expose reusable static methods:

### URLComparator.java
```java
public static Map<String, Object> compareUrls(String url1, String url2)
```
Returns a map containing:
- `url1` - First URL (lowercase)
- `url2` - Second URL (lowercase)
- `similarity` - Similarity percentage (0-100)
- `matches` - Matching character sequence
- `matchCount` - Number of matching characters

### divCounter.java
```java
public static int countDivs(String filePath)
```
Counts all `</div>` closing tags in an HTML file.

### ReadCss.java
```java
public static List<String> getNonStandardSelectors(String cssFile)
```
Returns list of CSS selectors that don't match standard HTML tags.

### ReadHeadFile.java
```java
public static List<String> extractHeadContent(String htmlFile)
```
Returns list of tokenized content from the `<head>` section.

## Original main() Methods Preserved

All original `main()` methods remain for backward compatibility and command-line testing:

```bash
java URLComparator      # Compare default URLs
java divCounter         # Count div tags in TestComp.html
java ReadCss            # Extract CSS selectors from admin.css
java ReadHeadFile       # Extract head content from TestComp.html
```

## Security Features

### Phishing Detection System
The risk scoring system identifies common phishing patterns:

1. **URL Typosquatting** - Detects similar-looking URLs
2. **Excessive Nesting** - Identifies suspicious HTML structures
3. **Custom Obfuscation** - Finds unusual CSS patterns
4. **Metadata Analysis** - Detects suspicious scripts and content

### CORS Support
API supports cross-origin requests for local development and testing.

## Performance Characteristics

| Component | Time Complexity | Notes |
|-----------|-----------------|-------|
| URL Comparison | O(n×m) | n,m = URL lengths |
| Div Counting | O(n) | n = file size (line-by-line) |
| CSS Analysis | O(n×t) | n = CSS lines, t = tag count |
| Head Extraction | O(n) | n = head section size |

## Configuration

### Server Settings
- **Host**: `localhost` (127.0.0.1)
- **Port**: `8080`
- **Thread Model**: Default thread pool

### File References
- HTML Test File: `TestComp.html`
- CSS Test File: `admin.css`

## Error Handling

- **404 Errors** - File not found
- **405 Errors** - Method not allowed
- **400 Errors** - Invalid parameters
- **500 Errors** - Server-side exceptions

All errors return JSON format with status and error message.

## Extensibility

### Adding New Analyzers
1. Create analyzer class with reusable static method
2. Add handler class in WebIDServer.java
3. Create new endpoint
4. Update dashboard UI with new control button

### Customizing Risk Scoring
Modify the `calculateUrlRisk()`, `calculateHtmlRisk()`, `calculateCssRisk()`, and `calculateMetaRisk()` functions in `app.js` to adjust risk weights.

### Dashboard Theming
All colors and animations use CSS variables in `style.css`:
```css
:root {
    --primary-color: #00ff88;
    --secondary-color: #00d4ff;
    --accent-color: #ff0080;
    /* ... more variables ... */
}
```

## Browser Compatibility

- Chrome/Edge 90+
- Firefox 88+
- Safari 14+
- Opera 76+

Requires ES6 JavaScript support and CSS Grid/Flexbox.

## Troubleshooting

### Port Already in Use
```bash
# Find and kill process on port 8080
lsof -i :8080
kill -9 <PID>
```

### Files Not Found
Ensure `TestComp.html` and `admin.css` exist in the working directory.

### JavaScript Console Errors
Check browser DevTools (F12) for detailed error messages and network requests.

## Future Enhancements

1. **Advanced Algorithms**
   - Levenshtein distance for URL comparison
   - Machine learning-based phishing detection
   - SSL certificate validation

2. **Features**
   - File upload for custom HTML/CSS analysis
   - Historical analysis tracking
   - Export reports (PDF/JSON)
   - Real-time URL monitoring

3. **Performance**
   - Caching system for repeated analyses
   - Database backend for storage
   - WebSocket support for real-time updates

4. **Security**
   - User authentication
   - Rate limiting
   - HTTPS support
   - Input validation enhancements

## License

This project is part of the WebID cybersecurity analysis toolkit suite.

## Support

For issues or enhancements, refer to the inline code documentation and comments in each analyzer class.

---

**WebID v1.0** - Transform Java Security Analyzers into Modern Dashboard 🎯
