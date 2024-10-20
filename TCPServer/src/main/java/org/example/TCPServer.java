package org.example;

import jdk.jshell.spi.ExecutionControlProvider;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class TCPServer {
    public static void main(String[] args) throws Exception {
        final int port = 6789;
        ServerSocket welcomeSocket = new ServerSocket(port);

        while (true) {
            System.out.println("Ouvindo na porta: " + port + '\n');
            Socket connectionSocket = welcomeSocket.accept();

            new Thread(() -> {
                try {
                    BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                    DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

                    String clientSentence;
                    String capitalizedSentence;

                    while ((clientSentence = inFromClient.readLine()) != null) {
                        capitalizedSentence = clientSentence.toUpperCase() + '\n';
                        outToClient.writeBytes(capitalizedSentence);
                    }

                    connectionSocket.close();
                } catch (SocketException e) {
                    System.out.println("Cliente desconectado.");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        connectionSocket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}