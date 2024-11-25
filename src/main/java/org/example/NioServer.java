package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.logging.log4j.LogManager;

public class NioServer {
    // 端口号
    private static final int PORT = 6666;
    // 控制服务器是否运行的标志
    private static volatile boolean running = true;
    // 用于存储客户端通道及其远程地址
    private static final HashMap<SocketChannel, String> clientAddresses = new HashMap<>();

    public static void main(String[] args) {
        //连接数据库
        MySQL mySQL = new MySQL();

        // 启动一个线程来监听控制台输入
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                String input;
                while ((input = reader.readLine()) != null) {
                    if ("-1".equals(input.trim())) {
                        running = false; // 设置标志以停止服务器
                        break;
                    }
                }
            } catch (IOException e) {
                Logger.getLogger(NioServer.class.getName()).log(Level.SEVERE, "发生 IOException", e);
            }
        }).start();

        // 启动服务器
        try{
            // 创建一个Selector
            Selector selector = Selector.open();

            // 创建ServerSocketChannel
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(PORT));
            serverSocketChannel.configureBlocking(false); // 设置为非阻塞模式

            // 将ServerSocketChannel注册到Selector
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("NIO服务器已启动，监听端口：" + PORT);

            // 轮询处理事件
            while (running) {

                // 等待通道准备好
                selector.select(500);

                if(!running) {
                    break;
                }

                // 获取选择器中注册的事件
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();

                    // 处理新接入的客户端
                    if (key.isAcceptable()) {
                        SocketChannel clientChannel = serverSocketChannel.accept();
                        clientChannel.configureBlocking(false);
                        clientChannel.register(selector, SelectionKey.OP_READ);
                        // 保存远程地址
                        String remoteAddress = clientChannel.getRemoteAddress().toString();
                        clientAddresses.put(clientChannel, remoteAddress);
                        System.out.println("新客户端连接：" + clientChannel.getRemoteAddress());
                    }

                    // 处理客户端发送的消息
                    if (key.isReadable()) {
                        SocketChannel clientChannel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        int bytesRead = clientChannel.read(buffer);
                        if (bytesRead == -1) {
                            // 获取远程地址并从客户端列表中移除
                            clientAddresses.remove(clientChannel);
                            System.out.println("客户端断开连接：" + clientChannel.getRemoteAddress());
                            clientChannel.close();
                        } else {
                            //只取有效字节
                            String message = new String(buffer.array(),0,bytesRead).trim();
                            // 获取远程地址
                            String remoteAddress = clientAddresses.get(clientChannel);
                            System.out.println("收到来自" + remoteAddress + "的消息：" + message);
                            // 处理消息
                            int result = MessageCheck.checkMessage(message, mySQL);
                            buffer.clear();
                            // 回送消息到客户端
                            buffer.put(String.valueOf(result).getBytes(StandardCharsets.UTF_8));
                            buffer.flip();
                            System.out.println("回送消息到客户端：" + result);
                            clientChannel.write(buffer);
                        }
                    }
                }
            }
            // 清理工作：关闭所有连接的客户端
            for (SocketChannel client : clientAddresses.keySet()) {
                try {
                    // 关闭客户端通道
                    System.out.println("关闭客户端通道：" + client.getRemoteAddress());
                    client.close();
                } catch (IOException e) {
                    System.err.println("关闭客户端通道时发生错误: " + e.getMessage());
                }
            }
            // 清空客户端地址列表
            clientAddresses.clear();
            // 清理工作：关闭通道和选择器
            serverSocketChannel.close();
            selector.close();
            System.out.println("NIO服务器已终止。");

        } catch (IOException e) {
            LogManager.getLogger(NioServer.class).error("发生 IOException", e);
        }
    }
}
