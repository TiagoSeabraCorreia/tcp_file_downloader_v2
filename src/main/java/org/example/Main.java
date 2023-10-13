package org.example;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.FileAlreadyExistsException;

public class Main {
    public static void main(String[] args) throws IOException {
        String server = "localhost";
        String response = null;
        byte[] data;
        Integer chunkSize = 2000;
        int read = 0;
        try(Socket s = new Socket(server, 2000)){
            try (OutputStream fos = s.getOutputStream();
                 InputStream fis = s.getInputStream()){
                //We send the name of the file we want to download
                String fileName = "ponei.jpeg";
                data = fileName.getBytes();
                fos.write(data);
                fos.flush();
                //We wait for the response of the server
                data = new byte[256];
                 read = fis.read(data);
                System.out.println("[Server response]:" + new String(data).trim());

                //We send the chunk size we want to the server
                data = String.valueOf(chunkSize).getBytes();
                fos.write(data);
                fos.flush();

                //We wait for the number of chunks that will be sent
                data = new byte[256];
                read = fis.read(data);
                Integer numberOfChunks = Integer.valueOf(new String(data).trim());
                System.out.println(numberOfChunks + " chunks will be received!");


                File f = new File("./files/" + fileName);

                try {
                    f.createNewFile();
                }catch (FileAlreadyExistsException e){
                    System.out.println(e);
                }
                FileOutputStream newFile = new FileOutputStream(f);
                //We receive the file from the server
                for (Integer i = 1; i < numberOfChunks; i++) {
                    byte[] x = new byte[chunkSize];
                    fis.read(x);
                    newFile.write(x);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }catch(SocketException e){

        }
    }
}