package org.example.jawa.connect;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.example.jawa.Application;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class NioClient {
    // 服务器地址和端口
    private static final String HOST = "2001:250:206:842f:a7bc:7fcf:2477:3e28"; // 可以修改为服务器的 IP 地址
    private static final int PORT = 6666;
    // 连接到服务器
    private static SocketChannel socketChannel;
    // 缓冲区
    private static final ByteBuffer buffer = ByteBuffer.allocate(1024);

    public static boolean connect() {
        try {
            socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
            socketChannel.configureBlocking(false); // 设置为非阻塞模式
        } catch (IOException e) {
            connectError();
            return false;
        }
        //测试用
        System.out.println("已连接到 NIO 服务器：" + HOST + ":" + PORT);
        return true;
    }

    public static void connectError() {
        //测试用
        System.out.println("无法连接到 NIO 服务器：" + HOST + ":" + PORT);
        Label label = new Label("无法连接到服务器，请检查网络连接。");
        StackPane.setAlignment(label, Pos.TOP_CENTER);
        label.setStyle("-fx-font-size: 16px;-fx-background-color: transparent ;-fx-text-fill: red;");
        // 在UI线程中添加标签
        Platform.runLater(() -> {
            Application.loginRoot.getChildren().add(label);
            StackPane.setAlignment(label, javafx.geometry.Pos.TOP_CENTER); // 设置对齐方式为顶部居中

            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.schedule(() -> {
                // 在UI线程中移除标签
                Platform.runLater(() -> Application.loginRoot.getChildren().remove(label));
                scheduler.shutdown(); // 关闭调度器
            }, 3, TimeUnit.SECONDS);
        });
    }

    public static void connectClose() {
        try {
            socketChannel.close();
        } catch (IOException e) {
            connectError();
        }
        //测试用
        System.out.println("已断开与服务器的连接。");
    }

    //0:邮箱+密码
    //1:用户名+密码
    //2:邮箱+type(0:邮箱不存在返回1；1：邮箱存在返回1 )
    //3:邮箱+验证码
    //4:设置密码
    //5注册：用户名+邮箱+验证码
    public static String messageBuilder(int type, String... args) {
        return type + " " + String.join(" ", args);
    }

    public static void sendMessage(String message) {
        try {
            buffer.clear();
            buffer.put(message.getBytes());
            buffer.flip();
            socketChannel.write(buffer);
            buffer.clear();
        } catch (IOException e) {
            connectError();
        }
    }

    public static int receiveMessage() {
        while (true) {
            int bytesRead;
            try {
                bytesRead = socketChannel.read(buffer);
            } catch (IOException e) {
                connectError();
                return -1;
            }
            if (bytesRead > 0) {
                String response = new String(buffer.array(),0,bytesRead, StandardCharsets.UTF_8);
                //测试用
                System.out.println("收到响应：" + response+"字节数："+bytesRead);
                return Integer.parseInt(response);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                LogManager.getLogger(NioClient.class).error("发生 InterruptedException", e);
            }
        }
    }

}