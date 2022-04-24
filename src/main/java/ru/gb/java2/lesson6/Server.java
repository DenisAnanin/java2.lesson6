package ru.gb.java2.lesson6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(50000)) {
            System.out.println("Сервер запущен...");
            Socket socket = serverSocket.accept();
            System.out.println("Соединение установлено");
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Scanner scanner = new Scanner(System.in);
                    while (true) {
                        String msg = scanner.nextLine();

                        try {
                            outputStream.writeUTF(msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            thread.setDaemon(true);
            thread.start();

            while (true) {
                String message = inputStream.readUTF();
                System.out.println("Клиент: " + message);
                if ("/end".equalsIgnoreCase(message)) {
                    outputStream.writeUTF("/end");
                    break;
                }
                outputStream.writeUTF("echo: " + message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
