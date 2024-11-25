package org.example;

import com.sun.mail.util.MailSSLSocketFactory;
import org.apache.logging.log4j.LogManager;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Properties;
import java.util.Random;

public class SendEmail {
    // 发件人电子邮箱
    private static final String from = "ma0050514@qq.com";

    // 发件人邮箱密码授权码
    private static final String password = "pttebgknuavscbhh";

    // 指定发送邮件的主机为 smtp.qq.com
    private static final String host = "smtp.qq.com";

    // 邮件主题
    private static final String subject = "Jawa验证码";

    // 邮件内容
    private static final String content = "您的验证码为：";

    //to:收件人邮箱
    //subject:邮件主题
    //content:邮件内容
    public static void sendEmail(String to, MySQL mysql) {
        // 获取系统属性
        Properties properties = getProperties();

        Session session = Session.getDefaultInstance(properties,new Authenticator(){
            public PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(from, password); //发件人邮件用户名、授权码
            }
        });

        try{
            // 创建默认的 MimeMessage 对象
            MimeMessage message = new MimeMessage(session);

            // Set From: 头部头字段
            message.setFrom(new InternetAddress(from));

            // Set To: 头部头字段
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));

            // Set Subject: 头部头字段
            message.setSubject(subject);

            // 设置消息体
            String number = generateRandomAlphaNumericString(6);
            mysql.insertEmailNumber(to, number);
            message.setText(content+number);

            // 发送消息
            Transport.send(message);
            System.out.println("Sent message successfully!");
        }catch (MessagingException mex) {
            LogManager.getLogger(SendEmail.class).error("发生 Messaging Exception", mex);
        }
    }

    private static Properties getProperties() {
        Properties properties = System.getProperties();

        // 设置邮件服务器
        properties.setProperty("mail.smtp.host", host);

        properties.put("mail.smtp.auth", "true");

        //设置ssl加密
        MailSSLSocketFactory sf;
        try {
            sf = new MailSSLSocketFactory();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);
        return properties;
    }

    public static String generateRandomAlphaNumericString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }
}
