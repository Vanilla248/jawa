package org.example.jawa.Login;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;


public class Register extends VBox {
    private String email;
    private final Tool tool1;
    private final Tool tool5;
    public Register() {
        setAlignment(Pos.TOP_CENTER);
        setPrefSize(480,320);
        AnchorPane.setTopAnchor(this, 30.0);
        AnchorPane.setLeftAnchor(this, 0.0);
        setSpacing(30);

        Label label = new Label("账号注册");
        label.setStyle("-fx-font-size: 20px; -fx-font-color:black;");
        label.setAlignment(Pos.CENTER);
        getChildren().add(label);

        tool1 = new Tool("用户名", "请输入用户名");
        Tool tool2 = new Tool("邮箱", "请输入您的邮箱地址");
        tool5 = new Tool("验证码", "验证码");
        getChildren().add(tool1);
        getChildren().add(tool2);
        getChildren().add(tool5);

        tool5.getPanelText().setPrefWidth(90);
        Button button = new Button("发送验证码");
        button.setOnAction(_ -> {
            this.email = tool2.getPanelText().getText();
            Check.checkEmail(this.email,button,3);
        });
        button.setPrefWidth(110);
        button.setStyle("-fx-background-color: rgb(138,219,237); -fx-text-fill: black; -fx-font-size: 16px;");
        tool5.getChildren().add(button);

    }
    public Tool getTool1() {
        return this.tool1;
    }
    public Tool getTool5() {
        return this.tool5;
    }
    public String getEmail() {
        return this.email;
    }
}
