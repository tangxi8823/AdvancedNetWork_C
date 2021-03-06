package com.trans;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

//客户端向服务器端传东西
public class clientFindServer implements Runnable{
    private Socket socket;
    private String fileName;

    public clientFindServer(Socket socket,String fileName) {
        this.socket = socket;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        //io流封装文件名
        try {
            InputStream is = new ByteArrayInputStream(this.fileName.getBytes(StandardCharsets.UTF_8));
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            //输出流
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String line;
            while ((line = br.readLine())!=null){
                bw.write(line);
                bw.newLine();
                bw.flush();
            }

            //终止输出，这样输出流才能给服务器端
            socket.shutdownOutput();

            //客户端接收反馈
            BufferedReader brClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String data = brClient.readLine();
            System.out.println("2.real port:"+data);
            //给客户端设定下次连接的服务地地址
            Client.setPort(Integer.parseInt(data));

            //释放资源
            socket.close();
            br.close();
            System.out.println("3.clientFindServer  thread died");


        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
