package org.example.jawa.Login;

import javafx.scene.control.Button;
import org.example.jawa.connect.NioClient;

import java.util.regex.Pattern;

public class Check {
    // 邮箱正则表达式
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+.]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    // 用户名正则表达式，用户名只能包含字母、数字、下划线，且长度在3到15个字符之间
    private static final String USERNAME_REGEX = "^[a-zA-Z0-9_]{3,15}$";
    // 编译正则表达式
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final Pattern USERNAME_PATTERN = Pattern.compile(USERNAME_REGEX);

    // 检查邮箱是否合法
    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false; // null值不合法
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
    // 检查用户名是否合法
    public static boolean isValidUsername(String username) {
        if (username == null) {
            return false; // null值不合法
        }
        return USERNAME_PATTERN.matcher(username).matches();
    }

    // 测试代码
    public static void main(String[] args) {
        String email = "test@test.com";
        String username = "test123";
        System.out.println(isValidEmail(email)); // true
        System.out.println(isValidUsername(username)); // true
        System.out.println(isValidEmail(null)); // false
        System.out.println(isValidUsername(null)); // false
    }

    public static void checkEmail(String email, Button button,int i) {
        Tool.hideLabel();
        if(Check.isValidEmail(email)) {
            if (NioClient.connect()) {
                NioClient.sendMessage(NioClient.messageBuilder(2, email, String.valueOf(i)));
                int result = NioClient.receiveMessage();
                NioClient.connectClose();
                switch (result) {
                    case 0://验证码发送成功
                        Tool.CountDown(button);
                        break;
                    case 1://邮箱不存在
                        if(i==0) {
                            Tool.showLabel(i, "*邮箱未注册！");
                        }else{
                            Tool.showLabel(i,"邮箱已被注册！");
                        }
                        break;
                }
            }
        }else {
            Tool.showLabel(i, "*请输入合法的邮箱！");
        }
    }

    public static boolean checkEmailNumber(String email,String checkNumber) {
        Tool.hideLabel();
        if(checkNumber.matches("[A-Za-z0-9]{6}")){
            if (NioClient.connect()){
                NioClient.sendMessage(NioClient.messageBuilder(3, email,checkNumber));
                int result = NioClient.receiveMessage();
                NioClient.connectClose();
                switch (result) {
                    case 0://验证成功
                        return true;
                    case 1://验证码错误
                        Tool.showLabel(1, "*验证码错误！");
                        return false;
                }
            }
        }
        Tool.showLabel(1, "*验证码不合法！");
        return false;
    }

    // 检查是否只包含字母、数字和特殊字符
    // 这里的特殊字符可以根据需求调整
    //至少包含一个小写字母。
    //至少包含一个大写字母。
    //至少包含一个数字。
    //至少包含一个特殊字符（在此情况下是 $@$!%*?&）。
    //密码长度在 6 到 16 个字符之间。
    public static boolean isValidPassword(String password) {
        // 检查长度是否在6到16个字符之间
        if (password.length() < 6 || password.length() > 16) {
            return false;
        }
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@!%*?&])[A-Za-z\\d$@!%*?&]{6,16}$");// 如果所有条件都满足，返回合法
    }

    public static boolean successSetPassword(String email,String password) {
        if(NioClient.connect()){
            NioClient.sendMessage(NioClient.messageBuilder(4, email,password));
            int result = NioClient.receiveMessage();
            NioClient.connectClose();
            return result == 0;
        }
        return false;
    }

    public static boolean checkRegister(String username, String email, String checkNumber) {
        Tool.hideLabel();
        if(isValidUsername(username)) {
            if(checkNumber.matches("[A-Za-z0-9]{6}")){
                if(NioClient.connect()){
                    NioClient.sendMessage(NioClient.messageBuilder(5, username,email,checkNumber));
                    int result = NioClient.receiveMessage();
                    NioClient.connectClose();
                    switch (result) {
                        case 0://注册成功
                            return true;
                        case 2:
                            Tool.showLabel(2,"用户名已注册");
                            break;
                        case 1:
                            Tool.showLabel(4,"验证码错误！");
                            break;
                    }
                }
            }else{
                Tool.showLabel(4,"验证码不合法");
            }
        }else {
            Tool.showLabel(2,"用户名不合法！");
        }
        return false;
    }
}
