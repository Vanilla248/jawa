package org.example.jawa;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.example.jawa.Login.*;
import org.jetbrains.annotations.NotNull;
import org.example.jawa.chat.*;
import org.example.jawa.chat.element.group_bar_friend.ElementFriendLuckUser;


import java.io.IOException;
import java.util.Date;
import java.util.Random;

public class Application extends javafx.application.Application {
    public static final StackPane loginRoot = new StackPane();
    private final AnchorPane anchorPane = new AnchorPane();
    public static final AnchorPane errorAnchorPane = new AnchorPane();
    private final HBox titleBar = new HBox();
    public static final Register register = new Register();
    private static final EmailCheck emailCheck = new EmailCheck();
    private final EmailLoadInView emailLoadInView = new EmailLoadInView();
    private final IdLoadInView idLoadInView = new IdLoadInView();
    private final ChoiceButton idButton = new ChoiceButton("账号登录","blue");
    private final ChoiceButton emailButton = new ChoiceButton("邮箱登录","black");
    private VBox view = idLoadInView;
    private double xOffset = 0;
    private double yOffset = 0;
    // 雪花数量
    private static final int SNOWFLAKES_COUNT = 100;
//---------------------------------------------------------------------------------------------------------------


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("hello-view.fxml"));
        VBox content = fxmlLoader.load();

        // Custom title bar
        titleBar.setStyle("-fx-background-color: transparent;");
        titleBar.setAlignment(Pos.TOP_RIGHT);

        // Minimize button
        Button minButton = new Button("—");
        minButton.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-font-size: 16;");
        minButton.setOnAction(_ -> stage.setIconified(true));

        // Close button
        Button closeButton = getCloseButton(stage);

        titleBar.getChildren().addAll( minButton, closeButton);
        AnchorPane.setTopAnchor(titleBar, 0.0); // 与顶部距离
        AnchorPane.setRightAnchor(titleBar, 0.0); // 与右侧距离

        // StackPane to ensure full window coverage
        loginRoot.getChildren().add(content);

        // Create a snowflake layer
        createSnowflakes();


        loginRoot.getChildren().add(errorAnchorPane);

        // Ensure VBox fills its parent
        VBox.setVgrow(content, Priority.ALWAYS);
        loginRoot.setPrefSize(480, 320);

        // Make the stage draggable
        loginRoot.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        loginRoot.setOnMouseDragged((MouseEvent event) -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        //初始化
        Init();

        // 登录
        logIn();

        loginRoot.getChildren().add(anchorPane);
        // Set the scene
        Scene scene = new Scene(loginRoot);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.show();

        // Set styles
        content.setStyle("-fx-background-color: linear-gradient(to bottom, #b0e0f7, #c3e1f6);");
//------------------------------------------------------------------------------------------------------------------------------

        IChatMethod chat = new ChatController(new IChatEvent() {
            @Override
            public void doQuit() {
                System.out.println("退出操作！");
            }

            @Override
            public void doSendMsg(String userId, String talkId, Integer talkType, String msg, Integer msgType, Date msgDate) {
                System.out.println("发送消息");
                System.out.println("userId：" + userId);
                System.out.println("talkType[0好友/1群组]：" + talkType);
                System.out.println("talkId：" + talkId);
                System.out.println("msg：" + msg);
                System.out.println("msgType[0文字消息/1固定表情]：" + msgType);
            }

            @Override
            public void doEventAddTalkUser(String userId, String userFriendId) {
                System.out.println("填充到聊天窗口[好友] userFriendId：" + userFriendId);
            }

            @Override
            public void doEventAddTalkGroup(String userId, String groupId) {
                System.out.println("填充到聊天窗口[群组] groupId：" + groupId);
            }

            @Override
            public void doEventDelTalkUser(String userId, String talkId) {
                System.out.println("删除对话框：" + talkId);
            }

            @Override
            public void addFriendLuck(String userId, ListView<Pane> listView) {
                System.out.println("新的朋友");
                // 添加朋友
                listView.getItems().add(new ElementFriendLuckUser("1000005", "比丘卡", "05_50", 0).pane());
                listView.getItems().add(new ElementFriendLuckUser("1000006", "兰兰", "06_50", 1).pane());
                listView.getItems().add(new ElementFriendLuckUser("1000007", "Alexa", "07_50", 2).pane());
            }

            @Override
            public void doFriendLuckSearch(String userId, String text) {
                System.out.println("搜索好友：" + text);
            }

            @Override
            public void doEventAddLuckUser(String userId, String friendId) {
                System.out.println("添加好友：" + friendId);
            }
        });
        chat.doShow();
        chat.setUserInfo("1000001", "拎包冲", "02_50");
        // 模拟测试
        chat.addTalkBox(-1, 0, "1000004", "哈尼克兔", "04_50", null, null, false);
        chat.addTalkMsgUserLeft("1000004", "沉淀、分享、成长，让自己和他人都有所收获！", 0, new Date(), true, false, true);
        chat.addTalkMsgUserLeft("1000004", "f_23", 1, new Date(), true, false, true);

        chat.addTalkMsgRight("1000004", "今年过年是放假时间最长的了！", 0, new Date(), true, true, false);

        chat.addTalkBox(-1, 0, "1000002", "铁锤", "03_50", "秋风扫过树叶落，哪有棋盘哪有我", new Date(), false);
        chat.addTalkMsgUserLeft("1000002", "秋风扫过树叶落，哪有棋盘哪有我", 0, new Date(), true, false, true);
        chat.addTalkMsgRight("1000002", "我Q，传说中的老头杀？", 0, new Date(), true, true, false);

        // 群组
        chat.addFriendGroup("5307397", "虫洞技术栈(1区)", "group_1");
        chat.addFriendGroup("5307392", "CSDN 社区专家", "group_2");
        chat.addFriendGroup("5307399", "洗脚城VIP", "group_3");

        // 群组 - 对话框
        chat.addTalkBox(0, 1, "5307397", "虫洞技术栈(1区)", "group_1", "", new Date(), true);
        chat.addTalkMsgRight("5307397", "你炸了我的山", 0, new Date(), true, true, false);
        chat.addTalkMsgRight("5307397", "f_14", 1, new Date(), true, true, false);
        chat.addTalkMsgGroupLeft("5307397", "1000002", "拎包冲", "01_50", "推我过忘川", 0, new Date(), true, false, true);
        chat.addTalkMsgGroupLeft("5307397", "1000003", "铁锤", "03_50", "奈河桥边的姑娘", 0, new Date(), true, false, true);
        chat.addTalkMsgGroupLeft("5307397", "1000004", "哈尼克兔", "04_50", "等我回头看", 0, new Date(), true, false, true);
        chat.addTalkMsgGroupLeft("5307397", "1000004", "哈尼克兔", "04_50", "f_25", 1, new Date(), true, false, true);

        // 好友
        chat.addFriendUser(false, "1000004", "哈尼克兔", "04_50");
        chat.addFriendUser(false, "1000001", "拎包冲", "02_50");
        chat.addFriendUser(false, "1000002", "铁锤", "03_50");
        chat.addFriendUser(true, "1000003", "小傅哥 | bugstack.cn", "01_50");

    }


//-----------------------------------------------------------------------------------------------------------------------------


    @NotNull
    private static Button getCloseButton (Stage stage) {
        Button closeButton = new Button("X");
        closeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-font-size: 16;");
        // Mouse hover effect
        closeButton.setOnMouseEntered(_ -> closeButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 16;"));
        closeButton.setOnMouseExited(_ -> closeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-font-size: 16;"));
        closeButton.setOnAction(_ -> stage.close());
        return closeButton;
    }


    private void createSnowflakes() {
        for (int i = 0; i < SNOWFLAKES_COUNT; i++) {
            Circle snowflake = createSnowflake();
            Application.loginRoot.getChildren().add(snowflake);
            animateSnowflake(snowflake);
        }
    }
    private Circle createSnowflake() {
        Random rand = new Random();
        double radius = rand.nextDouble() * 2 + 1; // 雪花大小范围
        Circle snowflake = new Circle(radius, Color.WHITE);
        snowflake.setTranslateX(getRandomBetweenMinusOneAndOne() * 240); // 随机位置
        snowflake.setTranslateY(getRandomBetweenMinusOneAndOne() * 160); // 起始位置在窗口上方
        return snowflake;
    }
    private void animateSnowflake(Circle snowflake) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.04), _ -> {
            snowflake.setTranslateY(snowflake.getTranslateY() + 2); // 下落速度
            // 当雪花落到底部时，重置到顶部
            if (snowflake.getTranslateY() > 160) {
                snowflake.setRadius(new Random().nextDouble() * 2 + 1);
                snowflake.setTranslateY(-160); // 重新设置为窗口顶部
                snowflake.setTranslateX(getRandomBetweenMinusOneAndOne() * 240); // 随机水平位置
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private double getRandomBetweenMinusOneAndOne() {
        return new Random().nextDouble() * 2 - 1; // 生成 -1 到 1 之间的随机数
    }

    public void logIn(){

        Button regesterButton = new Button("去注册");
        regesterButton.setStyle("-fx-background-color: #7ec8f3; -fx-text-fill: white; -fx-font-size: 14;");
        AnchorPane.setBottomAnchor(regesterButton, 20.0);
        AnchorPane.setRightAnchor(regesterButton, 10.0);
        regesterButton.setOnAction(_ -> {
            Tool.hideLabel();
            Register();
        });

        Button forgetPasswordButton = new Button("忘记密码?");
        forgetPasswordButton.setStyle("-fx-background-color: transparent ; -fx-text-fill: black ; -fx-font-size: 12;");
        AnchorPane.setBottomAnchor(forgetPasswordButton, 110.0);
        AnchorPane.setRightAnchor(forgetPasswordButton, 83.0);
        forgetPasswordButton.setOnAction(_ -> {
            Tool.hideLabel();
            emailCheck();
        });

        // 创建按钮
        HBox buttonBox = new HBox(50);
        buttonBox.setPrefSize(480,50);
        buttonBox.setAlignment(Pos.TOP_CENTER);
        buttonBox.getChildren().addAll(idButton, emailButton);
        AnchorPane.setTopAnchor(buttonBox, 50.0);

        idButton.setOnAction(_ -> {
            Tool.hideLabel();
            loginRoot.getChildren().remove(anchorPane);
            view = idLoadInView;
            anchorPane.getChildren().clear();
            anchorPane.getChildren().addAll(view,titleBar, buttonBox,regesterButton,forgetPasswordButton);
            loginRoot.getChildren().add(anchorPane);
            emailButton.setColor("black");
            idButton.setColor("blue");
        });

        emailButton.setOnAction(_ -> {
            Tool.hideLabel();
            loginRoot.getChildren().remove(anchorPane);
            view = emailLoadInView;
            anchorPane.getChildren().clear();
            anchorPane.getChildren().addAll(view,titleBar, buttonBox,regesterButton);
            loginRoot.getChildren().add(anchorPane);
            emailButton.setColor("blue");
            idButton.setColor("black");
        });

        anchorPane.getChildren().clear();
        if(view == idLoadInView) {
            anchorPane.getChildren().addAll(view, titleBar, buttonBox, regesterButton, forgetPasswordButton);
        }else if(view == emailLoadInView){
            anchorPane.getChildren().addAll(view, titleBar, buttonBox, regesterButton);
        }
    }

    public void Register(){

        anchorPane.getChildren().clear();
        anchorPane.getChildren().addAll(register,titleBar,getLogInButton());

    }

    private Button getLogInButton() {
        Button logInButton = new Button("去登录");
        logInButton.setStyle("-fx-background-color: #7ec8f3; -fx-text-fill: white; -fx-font-size: 14;");
        AnchorPane.setBottomAnchor(logInButton, 20.0);
        AnchorPane.setLeftAnchor(logInButton, 10.0);
        logInButton.setOnAction(_ -> {
            Tool.hideLabel();
            logIn();
        });
        return logInButton;
    }

    @NotNull
    private Button getButton() {
        Button LogIn = new Button("注册");
        LogIn.setOnAction(_ -> {
            String userName= Application.register.getTool1().getPanelText().getText();
            String checkNumber= Application.register.getTool5().getPanelText().getText();
            if(Check.checkRegister(userName, register.getEmail(), checkNumber)){
            setPassword("设置密码", Application.register.getEmail());
            }
        });
        LogIn.setPrefSize(200, 40);
        LogIn.setStyle("-fx-background-color: #52bdf8; -fx-text-fill: white; -fx-font-size: 18px;");
        return LogIn;
    }

    private void emailCheck(){
        Label label = new Label("身份验证");
        label.setStyle("-fx-font-size: 20px; -fx-font-color:black;");
        label.setAlignment(Pos.CENTER);
        AnchorPane.setTopAnchor(label, 50.0);
        AnchorPane.setLeftAnchor(label, 200.0);

        Button backButton = new Button("<-");
        backButton.setStyle("-fx-background-color: #7ec8f3; -fx-text-fill: white; -fx-font-size: 18;");
        AnchorPane.setTopAnchor(backButton, 0.0);
        AnchorPane.setLeftAnchor(backButton, 0.0);
        backButton.setOnAction(_ -> {
            Tool.hideLabel();
            logIn();
        });

        anchorPane.getChildren().clear();
        anchorPane.getChildren().addAll(label,backButton,emailCheck,titleBar);
    }

    private void setPassword(String text,String email) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 20px; -fx-font-color:black;");
        label.setAlignment(Pos.CENTER);
        AnchorPane.setTopAnchor(label, 50.0);
        AnchorPane.setLeftAnchor(label, 200.0);

        SetPassword setPassword = new SetPassword(email);
        Button LogIn = getButton(text, setPassword);
        setPassword.getChildren().add(LogIn);

        anchorPane.getChildren().clear();
        anchorPane.getChildren().addAll(label,setPassword,titleBar);
    }

    @NotNull
    private Button getButton(String text, SetPassword setPassword) {
        Button LogIn = new Button(text);
        LogIn.setOnAction(_ -> {
            String password = setPassword.getTool1().getPanelText().getText();
            String password2 = setPassword.getTool2().getPanelText().getText();
            Tool.hideLabel();
            if(Check.isValidPassword(password)){
                if(password.equals(password2)){
                    if(Check.successSetPassword(setPassword.getEmail(),password)) {
                        logIn();
                    }
                }else{
                    Tool.showLabel(1, "两次密码输入不一致");
                }
            }else{
                Tool.showLabel(0, "密码格式错误，请输入6-16位字母、数字或符号的组合");
            }
        });
        LogIn.setPrefSize(200, 40);
        LogIn.setStyle("-fx-background-color: #52bdf8; -fx-text-fill: white; -fx-font-size: 18px;");
        return LogIn;
    }

    private void Init() {
        Button LogIn = getButton();
        register.getChildren().add(LogIn);

        Button LogIn2 = new Button("验证");
        LogIn2.setOnAction(_ -> {
            String checkNumber = emailCheck.getTool2().getPanelText().getText();
            if(Check.checkEmailNumber(emailCheck.getEmail(),checkNumber)){
                setPassword("重置密码",emailCheck.getEmail());
            }
        });
        LogIn2.setPrefSize(200, 40);
        LogIn2.setStyle("-fx-background-color: #52bdf8; -fx-text-fill: white; -fx-font-size: 18px;");
        emailCheck.getChildren().add(LogIn2);
    }

    public static void main(String[] args) {
        launch();
    }
}
