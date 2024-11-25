package org.example.jawa.Login;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class EmailCheck extends VBox {
    private String email;
    private final Tool tool2;
    public EmailCheck() {
        setPrefSize(480,240);
        AnchorPane.setTopAnchor(this, 120.0);
        AnchorPane.setLeftAnchor(this, 0.0);

        this.setSpacing(40);
        this.setAlignment(Pos.TOP_CENTER);
        this.setPrefSize(480, 240);

        Tool tool1 = new Tool("邮箱地址", "请输入邮箱地址");
        this.tool2 = new Tool("验证码", "验证码");
        this.getChildren().add(tool1);
        this.getChildren().add(tool2);

        tool2.getPanelText().setPrefWidth(90);

        Button button = new Button("发送验证码");
        button.setOnAction(_ -> {
            this.email = tool1.getPanelText().getText();
            Check.checkEmail(this.email,button,0);
        });
        button.setPrefWidth(110);
        button.setStyle("-fx-background-color: rgb(138,219,237); -fx-text-fill: black; -fx-font-size: 16px;");
        tool2.getChildren().add(button);

    }
    public Tool getTool2() {
        return tool2;
    }

    public String getEmail() {
        return email;
    }
}
