package com.itheima.test_01;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;

class MyTomcat extends Thread {

    private Socket s;

    public MyTomcat(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {
        try {
            //1、读取资源： e:/MyWeb/index.html
            FileInputStream fis = new FileInputStream("web/index.html");
            //2、写出资源：给浏览器写出资源  Socket
            OutputStream os = s.getOutputStream();
            /*
            响应头中包含响应的http协议版本HTTP/1.1
            服务器返回的状态码 200
            状态值:OK
         */
            os.write("HTTP/1.1 200 OK\r\n".getBytes());
            //Content-Type:text/html表示响应文本的类型
            os.write("Content-Type:text/html\r\n".getBytes());
            // 必须要写入空行,否则浏览器不解析
            os.write("\r\n".getBytes());
            //边读边写
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = fis.read(buf)) != -1) {
                os.write(buf, 0, len);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public class Test03 {
    public static void main(String[] args) throws Exception {
        //1、启动一个SocketServer，监听客户端的访问
        ServerSocket ss = new ServerSocket(8080);
        System.out.println("服务器启动了：" + new Timestamp(System.currentTimeMillis()));

        //2、接收请求
        while (true) {
            //接收请求
            Socket s = ss.accept();
            //把请求交给MyTomcat处理
            new MyTomcat(s).start();
        }
    }
}
