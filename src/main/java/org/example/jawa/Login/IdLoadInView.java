package org.example.jawa.Login;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.example.jawa.connect.NioClient;

public class IdLoadInView extends VBox {
    public IdLoadInView() {
        setPrefSize(480,240);
        AnchorPane.setTopAnchor(this, 120.0);
        AnchorPane.setLeftAnchor(this, 0.0);

        this.setSpacing(40);
        this.setAlignment(Pos.TOP_CENTER);
        this.setPrefSize(480, 240);

        Tool tool1 = new Tool("用户名/邮箱", "请输入用户名/邮箱");
        Tool tool2 = new Tool("密码", "请输入密码");
        this.getChildren().add(tool1);
        this.getChildren().add(tool2);


        Button LogIn2 = new Button("登录");
        LogIn2.setOnAction(_ ->{
                String str = tool1.getPanelText().getText();
                String password = tool2.getPanelText().getText();
                checkIdLogIn(str, password);});
        LogIn2.setPrefSize(200, 40);
        LogIn2.setStyle("-fx-background-color: #52bdf8; -fx-text-fill: white; -fx-font-size: 18px;");
        this.getChildren().add(LogIn2);
    }

    private void checkIdLogIn(String str, String password) {
        Tool.hideLabel();
        if(Check.isValidEmail(str)){
            if(NioClient.connect()){
                NioClient.sendMessage(NioClient.messageBuilder(0, str, password));
                int result = NioClient.receiveMessage();
                NioClient.connectClose();
                switch (result){
                    case 0://登录成功
                        System.out.println("登录成功");
                        break;
                    case 1://邮箱不存在
                        Tool.showLabel(0,"*邮箱未注册！");
                        break;
                    case 2://密码错误
                        Tool.showLabel(1,"*密码错误！");
                        break;
                }
            }
            return;
        }
        if(Check.isValidUsername(str)){
            if(NioClient.connect()){
                NioClient.sendMessage(NioClient.messageBuilder(1, str, password));
                int result = NioClient.receiveMessage();
                NioClient.connectClose();
                switch (result){
                    case 0://登录成功
                        System.out.println("登录成功");
                        break;
                    case 1://用户名不存在
                        Tool.showLabel(0,"*用户名未注册！");
                        break;
                    case 2://密码错误
                        Tool.showLabel(1,"*密码错误！");
                        break;
                }
            }
        }else{
            Tool.showLabel(0, "*请输入合法的用户名/邮箱！");
        }
    }

}
