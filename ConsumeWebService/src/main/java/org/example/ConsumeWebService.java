package org.example;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class ConsumeWebService {
    public static void main(String[] args) throws Exception {
        boolean run = true;
        Scanner scanner = new Scanner(System.in);
        String httpMethod = "";

        System.out.println("-- Para parar a execução do client digite 'stop' --");

        while (run) {
            try {
                System.out.println("Digite o método http desejado (GET, POST, PUT, DELETE): ");
                httpMethod = scanner.nextLine().toUpperCase();

                System.out.println("Digite o nome do arquivo (junto com a extensão): ");
                String fileName = scanner.nextLine();

                if (httpMethod.equals("stop")) {
                    run = false;
                    return;
                }

                Socket socket = new Socket("localhost", 8080);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                String request = httpMethod + " " + fileName + " HTTP 1.1\r\n" +
                        "Host: localhost\r\n" +
                        "Connection close\r\n";

                if (httpMethod.equals("POST")) {
                    Scanner scannerToFile = new Scanner(System.in);
                    String newFileName = fileName;
                    System.out.println("Digite o conteúdo do arquivo\n");
                    String content = scannerToFile.nextLine();

                    request += "Content-Length: " + content.length() + "\r\n" +
                            "Content-Type: text/plain\r\n" +
                            "\r\n";

                    out.print(request);
                    out.flush();

                    out.print(content);
                    out.flush();
                } else if(httpMethod.equals("GET") || httpMethod.equals("DELETE")) {
                    request += "\r\n";
                    out.print(request);
                    out.flush();
                } else if(httpMethod.equals("PUT")) {
                    Scanner scannerToFile = new Scanner(System.in);
                    String fileToUpdate = fileName;
                    System.out.println("Digite o conteúdo do arquivo\n");
                    String content = scannerToFile.nextLine();

                    request += "Content-Length: " + content.length() + "\r\n" +
                            "Content-Type: text/plain\r\n" +
                            "\r\n";

                    out.print(request);
                    out.flush();

                    out.print(content);
                    out.flush();
                } else {
                    out.write(request);
                    out.flush();
                }

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String responseLine;
                while ((responseLine = in.readLine()) != null) {
                    System.out.println(responseLine);
                }

                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}