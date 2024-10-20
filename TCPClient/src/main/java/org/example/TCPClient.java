package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient {
    public static void main(String[] args) throws Exception {
        boolean run = true;
        Scanner scanner = new Scanner(System.in);
        String httpMethod = "";

        System.out.println("-- Para parar a execução do client digite 'stop' --");

        while(run) {
            System.out.println("Digite o nome do arquivo solicitado: ");
            String fileName = scanner.nextLine();

            System.out.println("Digite o método http desejado (GET, POST, PUT, DELETE): ");
            httpMethod = scanner.nextLine().toUpperCase();

            if(httpMethod.equals("stop")) {
                run = false;
                return;
            }

            Socket socket = new Socket("localhost", 8080);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            String request = httpMethod + "/" + fileName + "HTTP/1.1\r\n" +
                    "Host: localhost\r\n" +
                    "Connection close\r\n";

            if(httpMethod.equals("POST") || httpMethod.equals("PUT")) {
                String content = "Conteúdo de exemplo para " + httpMethod;
                request += "Content-Length: " + content.length() + "\r\n" +
                        "Content-Type: text/plain\r\n\r\n" +
                        content;
            } else {
                request += "\r\n";
            }
        }
    }
}