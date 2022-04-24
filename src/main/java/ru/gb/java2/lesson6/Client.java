package ru.gb.java2.lesson6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }

    private void start() {
        try {
            openConnection();
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String msg = scanner.nextLine();
                out.writeUTF(msg);
                if ("/end".equalsIgnoreCase(msg)) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() throws IOException {
        socket.close();
        in.close();
        out.close();
    }

    private void openConnection() throws IOException {
        socket = new Socket("localhost", 50000);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        String message = in.readUTF();
                        System.out.println("Сервер " + message);
                        if ("/end".equalsIgnoreCase(message)) {
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        closeConnection();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();

    }
}
