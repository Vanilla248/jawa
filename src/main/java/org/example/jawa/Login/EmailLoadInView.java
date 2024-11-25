package org.example.jawa.Login;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class EmailLoadInView extends VBox {
    private String email;
    private final Button button;
    public EmailLoadInView() {
        setPrefSize(480,240);
        AnchorPane.setTopAnchor(this, 120.0);
        AnchorPane.setLeftAnchor(this, 0.0);

        this.setSpacing(40);
        this.setAlignment(Pos.TOP_CENTER);
        this.setPrefSize(480, 240);

        Tool tool1 = new Tool("邮箱地址", "请输入邮箱地址");
        Tool tool2 = new Tool("验证码", "验证码");
        this.getChildren().add(tool1);
        this.getChildren().add(tool2);

        tool2.getPanelText().setPrefWidth(90);

        button = new Button("发送验证码");
        button.setPrefWidth(110);
        button.setStyle("-fx-background-color: rgb(138,219,237); -fx-text-fill: black; -fx-font-size: 16px;");
        button.setOnAction(_ -> {
            this.email = tool1.getPanelText().getText();
            Check.checkEmail(this.email, button, 0);
        });
        tool2.getChildren().add(button);

        Button LogIn1 = new Button("登录");
        LogIn1.setOnAction(_ ->{
            String checkNumber = tool2.getPanelText().getText();
            if(Check.checkEmailNumber(this.email,checkNumber)){
                //登录成功,测试用
                System.out.println("登录成功");
            }
        });
        LogIn1.setPrefSize(200, 40);
        LogIn1.setStyle("-fx-background-color: #52bdf8; -fx-text-fill: white; -fx-font-size: 18px;");
        this.getChildren().add(LogIn1);
    }


}
