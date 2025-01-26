package lk.ijse.dep13.server.webweaver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
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
