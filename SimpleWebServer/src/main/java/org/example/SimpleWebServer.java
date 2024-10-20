package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SimpleWebServer {
    public static void main(String[] args) throws IOException {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println("Iniciando servidor na porta 8080...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Conexão estabelecida...");

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                OutputStream out = clientSocket.getOutputStream();
                String requestLine = "";

                if(clientSocket.getInputStream() != null) {
                    requestLine = in.readLine();
                }

                System.out.println("Requisição: " + requestLine);

                if (requestLine != null && !requestLine.isEmpty()) {
                    //requestLine = requestLine.replaceAll(" ", "");
                    String[] requestParts = requestLine.split(" ");
                    String fileName = requestParts[1];
                    // Caso seja acessado o localhost:8080 o fileName estará com o valor 'HTTP', sendo assim, considera que está tentando acessar o html
                    String httpMethod = requestParts[0];
                    String filesPath = Paths.get("").toAbsolutePath().toString() + "/files/";

                    if (httpMethod.equals("GET")) {
                        if (fileName.isEmpty() || fileName.equals("/")) {
                            fileName = "index.html";
                        }

                        File file = new File(filesPath + fileName);

                        if (file.exists()) {
                            String responseReader = montaResponse("200 OK", httpMethod, file);

                            out.write(responseReader.getBytes());
                            Files.copy(file.toPath(), out);
                        } else {
                            String error404 = montaResponse("404 NOT FOUND", httpMethod, null);
                            error404 += "<html><body><h1>404 Not Found</h1></body></html>";
                            out.write(error404.getBytes());
                        }
                    } else if (httpMethod.equals("POST")) {
                        String responseHeader = "";

                        // Pega o conteúdo do header
                        String headerLine;
                        int contentLength = 0;
                        while (!(headerLine = in.readLine()).isEmpty()) {
                            if (headerLine.startsWith("Content-Length:")) {
                                contentLength = Integer.parseInt(headerLine.split(" ")[1]);
                            }
                        }

                        // Armazena o conteúdo no content
                        char[] body = new char[contentLength];
                        in.read(body, 0, contentLength);
                        String content = new String(body);

                        try {
                            File newFile = new File(filesPath + fileName);

                            try {
                                newFile.createNewFile();
                                FileWriter myWriter = new FileWriter(newFile.getAbsolutePath());
                                myWriter.write(content);
                                myWriter.close();
                                responseHeader = montaResponse("200 OK", httpMethod, newFile);
                            } catch (IOException e) {
                                responseHeader = montaResponse("500 INTERNAL SERVER ERROR", httpMethod, null);
                                responseHeader += e.getMessage();
                                out.write(responseHeader.getBytes());
                            }

                            out.write(responseHeader.getBytes());
                            Files.copy(newFile.toPath(), out);
                        } catch (IOException e) {
                            responseHeader = montaResponse("500 INTERNAL SERVER ERROR",httpMethod, null);
                            responseHeader += e.getMessage();
                            out.write(responseHeader.getBytes());
                        }
                    } else if (httpMethod.equals("PUT")) {
                        String responseHeader = "";

                        // Pega o conteúdo do header
                        String headerLine;
                        int contentLength = 0;
                        while (!(headerLine = in.readLine()).isEmpty()) {
                            if (headerLine.startsWith("Content-Length:")) {
                                contentLength = Integer.parseInt(headerLine.split(" ")[1]);
                            }
                        }

                        // Armazena o conteúdo no content
                        char[] body = new char[contentLength];
                        in.read(body, 0, contentLength);
                        String content = new String(body);

                        File file = new File(filesPath + fileName);

                        if (file.exists()) {
                            FileWriter myWriter = new FileWriter(file.getAbsolutePath());
                            myWriter.write(content);
                            myWriter.close();
                            responseHeader = montaResponse("200 OK", httpMethod, file);

                            out.write(responseHeader.getBytes());
                            Files.copy(file.toPath(), out);
                        } else {
                            responseHeader = montaResponse("404 NOT FOUND", httpMethod, null);
                            responseHeader += "<html><body><h1>404 Not Found</h1></body></html>";
                            out.write(responseHeader.getBytes());
                        }

                    } else if (httpMethod.equals("DELETE")) {
                        String responseHeader = "";

                        File fileToDelete = new File(filesPath + fileName);

                        if (fileToDelete.exists()) {
                            try {
                                fileToDelete.delete();
                                responseHeader = montaResponse("200 OK", httpMethod, null);
                                responseHeader += "<html><body><h1>Deletado com sucesso</h1></body></html>";
                                out.write(responseHeader.getBytes());
                            } catch (IOException e) {
                                responseHeader = montaResponse("500 INTERNAL SERVER ERROR", httpMethod, null);
                                out.write(responseHeader.getBytes());
                            }
                        } else {
                            responseHeader = montaResponse("404 NOT FOUND", httpMethod, null);
                            responseHeader += "<html><body><h1>404 Not Found</h1></body></html>";
                            out.write(responseHeader.getBytes());
                        }
                    } else {
                        String responseHeader = montaResponse("501 NOT IMPLEMENTED", httpMethod, null);
                        responseHeader += "Error: Requisição inválida\r\n";
                        out.write(responseHeader.getBytes());
                    }

                    out.flush();
                    clientSocket.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String montaResponse(String status, String method, File file) {
        String responseHeader = "";

        responseHeader += "HTTP/1.1 " + status + "\r\n";
        responseHeader += "Content-Type: text/html\r\n";

        if(file != null) {
            responseHeader += "Content-Length: " + file.length() + "\r\n";
        }

        responseHeader += "\r\n";

        return responseHeader;
    }
}