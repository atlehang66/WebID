import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;
import java.util.*;

public class WebIDServer {
    private static final int PORT = 8080;
    private static final String LOCALHOST = "localhost";

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(LOCALHOST, PORT), 0);

        // Routes
        server.createContext("/", new StaticFileHandler());
        server.createContext("/run/divcounter", new DivCounterHandler());
        server.createContext("/run/readcss", new ReadCssHandler());
        server.createContext("/run/headfile", new HeadFileHandler());
        server.createContext("/run/urlcompare", new URLCompareHandler());

        server.setExecutor(null);
        server.start();

        System.out.println("WebID Server running at http://" + LOCALHOST + ":" + PORT);
        System.out.println("Dashboard available at http://" + LOCALHOST + ":" + PORT);
    }

    /**
     * Serves static files (HTML, CSS, JS)
     */
    static class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            
            if (path.equals("/")) {
                path = "/index.html";
            }

            File file = new File(path.substring(1)); // Remove leading slash

            if (file.exists() && !file.isDirectory()) {
                String contentType = getContentType(file.getName());
                exchange.getResponseHeaders().set("Content-Type", contentType);
                exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                
                byte[] fileContent = readFileBytes(file);
                exchange.sendResponseHeaders(200, fileContent.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(fileContent);
                }
            } else {
                String response = "404 - File not found";
                exchange.sendResponseHeaders(404, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        }
    }

    /**
     * Endpoint: /run/divcounter
     */
    static class DivCounterHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCORSHeaders(exchange);

            if ("GET".equals(exchange.getRequestMethod())) {
                int divCount = divCounter.countDivs("TestComp.html");

                String response = "{" +
                    "\"divCount\":" + divCount + "," +
                    "\"status\":\"success\"" +
                    "}";

                sendJsonResponse(exchange, response, 200);
            } else {
                sendErrorResponse(exchange, "Method not allowed", 405);
            }
        }
    }

    /**
     * Endpoint: /run/readcss
     */
    static class ReadCssHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCORSHeaders(exchange);

            if ("GET".equals(exchange.getRequestMethod())) {
                List<String> selectors = ReadCss.getNonStandardSelectors("admin.css");

                StringBuilder selectorsList = new StringBuilder();
                for (int i = 0; i < selectors.size(); i++) {
                    if (i > 0) selectorsList.append(",");
                    selectorsList.append("\"").append(escapeJson(selectors.get(i))).append("\"");
                }

                String response = "{" +
                    "\"selectors\":[" + selectorsList.toString() + "]," +
                    "\"selectorCount\":" + selectors.size() + "," +
                    "\"status\":\"success\"" +
                    "}";

                sendJsonResponse(exchange, response, 200);
            } else {
                sendErrorResponse(exchange, "Method not allowed", 405);
            }
        }
    }

    /**
     * Endpoint: /run/headfile
     */
    static class HeadFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCORSHeaders(exchange);

            if ("GET".equals(exchange.getRequestMethod())) {
                List<String> headContent = ReadHeadFile.extractHeadContent("TestComp.html");

                StringBuilder contentList = new StringBuilder();
                for (int i = 0; i < headContent.size(); i++) {
                    if (i > 0) contentList.append(",");
                    contentList.append("\"").append(escapeJson(headContent.get(i))).append("\"");
                }

                String response = "{" +
                    "\"headContent\":[" + contentList.toString() + "]," +
                    "\"contentCount\":" + headContent.size() + "," +
                    "\"status\":\"success\"" +
                    "}";

                sendJsonResponse(exchange, response, 200);
            } else {
                sendErrorResponse(exchange, "Method not allowed", 405);
            }
        }
    }

    /**
     * Endpoint: /run/urlcompare?url1=...&url2=...
     */
    static class URLCompareHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCORSHeaders(exchange);

            if ("GET".equals(exchange.getRequestMethod())) {
                Map<String, String> params = parseQueryString(exchange.getRequestURI().getQuery());

                String url1 = params.getOrDefault("url1", "https://www.example.com");
                String url2 = params.getOrDefault("url2", "https://www.examp1e.com");

                // Convert to lowercase for comparison
                Map<String, Object> comparison = URLComparator.compareUrls(
                    url1.toLowerCase(), 
                    url2.toLowerCase()
                );

                String response = "{" +
                    "\"url1\":\"" + escapeJson((String)comparison.get("url1")) + "\"," +
                    "\"url2\":\"" + escapeJson((String)comparison.get("url2")) + "\"," +
                    "\"similarity\":" + comparison.get("similarity") + "," +
                    "\"matches\":\"" + escapeJson((String)comparison.get("matches")) + "\"," +
                    "\"matchCount\":" + comparison.get("matchCount") + "," +
                    "\"status\":\"success\"" +
                    "}";

                sendJsonResponse(exchange, response, 200);
            } else {
                sendErrorResponse(exchange, "Method not allowed", 405);
            }
        }
    }

    // Utility Methods

    /**
     * Add CORS headers to allow cross-origin requests
     */
    static void addCORSHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
    }

    /**
     * Send JSON response
     */
    static void sendJsonResponse(HttpExchange exchange, String jsonResponse, int statusCode) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        byte[] responseBytes = jsonResponse.getBytes();
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }

    /**
     * Send error response
     */
    static void sendErrorResponse(HttpExchange exchange, String message, int statusCode) throws IOException {
        String response = "{" +
            "\"error\":\"" + escapeJson(message) + "\"," +
            "\"status\":\"error\"" +
            "}";
        sendJsonResponse(exchange, response, statusCode);
    }

    /**
     * Escape special characters for JSON
     */
    static String escapeJson(String s) {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\b': sb.append("\\b"); break;
                case '\f': sb.append("\\f"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (c < 32) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        return sb.toString();
    }

    /**
     * Parse query string parameters
     */
    static Map<String, String> parseQueryString(String query) {
        Map<String, String> params = new HashMap<>();
        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    try {
                        params.put(keyValue[0], java.net.URLDecoder.decode(keyValue[1], "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        params.put(keyValue[0], keyValue[1]);
                    }
                }
            }
        }
        return params;
    }

    /**
     * Determine content type based on file extension
     */
    static String getContentType(String fileName) {
        if (fileName.endsWith(".html")) return "text/html";
        if (fileName.endsWith(".css")) return "text/css";
        if (fileName.endsWith(".js")) return "application/javascript";
        if (fileName.endsWith(".json")) return "application/json";
        if (fileName.endsWith(".png")) return "image/png";
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) return "image/jpeg";
        if (fileName.endsWith(".gif")) return "image/gif";
        return "text/plain";
    }

    /**
     * Read file as bytes
     */
    static byte[] readFileBytes(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            return baos.toByteArray();
        }
    }
}
