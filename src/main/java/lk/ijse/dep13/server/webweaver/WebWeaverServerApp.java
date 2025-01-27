package lk.ijse.dep13.server.webweaver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class WebWeaverServerApp {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(80)) {
            System.out.println("Server started on port 80");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected from " + clientSocket.getRemoteSocketAddress());

                new Thread(() -> {
                    try {
                        InputStream is = clientSocket.getInputStream();
                        OutputStream os = clientSocket.getOutputStream();
                        InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(isr);

                        /* Reading the Command Line In the Request */
                        String commandLine = br.readLine();
                        if (commandLine == null || commandLine.isBlank()) {
                            String response = """
                                    HTTP/1.1 400 Bad Request
                                    Server: web-weaver/0.1.0
                                    Date: %s
                                    Content-Type: text/html
                                    
                                    <!DOCTYPE html>
                                    <html>
                                    <head>
                                    <title>Web Weaver | 400 Bad Request</title>
                                    </head>
                                    <body>
                                    <h1>Web Weaver | 400 Bad Request</h1>
                                    </body>
                                    </html>
                                    """.formatted(LocalDateTime.now());
                            os.write(response.getBytes());
                            os.flush();
                            return;
                        }

                        String[] cmdArray = commandLine.split(" ");
                        if (cmdArray.length < 2) {
                            String response = """
                                    HTTP/1.1 400 Bad Request
                                    Server: web-weaver/0.1.0
                                    Date: %s
                                    Content-Type: text/html
                                    
                                    <!DOCTYPE html>
                                    <html>
                                    <head>
                                    <title>Web Weaver | 400 Bad Request</title>
                                    </head>
                                    <body>
                                    <h1>Web Weaver | 400 Bad Request</h1>
                                    </body>
                                    </html>
                                    """.formatted(LocalDateTime.now());
                            os.write(response.getBytes());
                            os.flush();
                            return;
                        }

                        String cmd = cmdArray[0];
                        String resourcePath = cmdArray[1];

                        /* Validating the HTTP */
                        if (!cmd.equalsIgnoreCase("GET")) {
                            String response = """
                                    HTTP/1.1 405 Method Not Allowed
                                    Server: web-weaver/0.1.0
                                    Date: %s
                                    Content-Type: text/html
                                    
                                    <!DOCTYPE html>
                                    <html>
                                    <head>
                                    <title>Web Weaver | 405 Method Not Allowed</title>
                                    </head>
                                    <body>
                                    <h1>Web Weaver | 405 Method Not Allowed</h1>
                                    </body>
                                    </html>
                                    """.formatted(LocalDateTime.now());
                            os.write(response.getBytes());
                            os.flush();
                            return;
                        }

                        /* Reading the Headers */
                        String host = null;
                        String line;
                        while ((line = br.readLine()) != null && !line.isBlank()) {
                            if (line.toLowerCase().startsWith("host:")) {
                                host = line.substring(5).trim();
                            }
                        }

                        if (host == null) {
                            String response = """
                                    HTTP/1.1 400 Bad Request : Missing host Header
                                    Server: web-weaver/0.1.0
                                    Date: %s
                                    Content-Type: text/html
                                    
                                    <!DOCTYPE html>
                                    <html>
                                    <head>
                                    <title>Web Weaver | 400 Bad Request : Missing host Header</title>
                                    </head>
                                    <body>
                                    <h1>Web Weaver | 400 Bad Request : Missing host Header</h1>
                                    </body>
                                    </html>
                                    """.formatted(LocalDateTime.now());
                            os.write(response.getBytes());
                            os.flush();
                            return;
                        }

                        /* File Path */
                        Path filePath;
                        if (resourcePath.equals("/")) {
                            filePath = Path.of("http" , host , "index.html");
                        } else {
                            filePath = Path.of("http", host , resourcePath.substring(1));
                        }

                        if (!Files.exists(filePath)) {
                            String response = """
                                    HTTP/1.1 404 Not Found
                                    Server: web-weaver/0.1.0
                                    Date: %s
                                    Content-Type: text/html
                                    
                                    <!DOCTYPE html>
                                    <html>
                                    <head>
                                    <title>Web Weaver | 404 Not Found</title>
                                    </head>
                                    <body>
                                    <h1>Web Weaver | 404 Not Found</h1>
                                    </body>
                                    </html>
                                    """.formatted(LocalDateTime.now());
                            os.write(response.getBytes());
                            os.flush();
                            return;
                        }

                        /* Serving File */
                        String contentType = Files.probeContentType(filePath);
                        String headerResponse = """
                                HTTP/1.1 200 OK
                                Server: web-weaver/0.1.0
                                Date: %s
                                Content-Type: %s
                                Content-Length: %d
                                
                                """.formatted(LocalDateTime.now(), contentType, Files.size(filePath));
                        os.write(headerResponse.getBytes());

                        try {
                            FileChannel fileChannel = FileChannel.open(filePath);
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            while (fileChannel.read(buffer) != -1) {
                                buffer.flip();
                                os.write(buffer.array(), 0, buffer.limit());
                                buffer.clear();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                clientSocket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        os.flush();

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
