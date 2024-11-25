package org.example.jawa.Login;

import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class SetPassword extends VBox {
    private final String email;
    private final Tool tool1;
    private final Tool tool2;
    public SetPassword(String email) {
        this.email = email;

        setPrefSize(480,240);
        AnchorPane.setTopAnchor(this, 120.0);
        AnchorPane.setLeftAnchor(this, 0.0);

        this.setSpacing(40);

        this.setAlignment(Pos.TOP_CENTER);
        this.setPrefSize(480, 240);

        tool1 = new Tool("密码", "请输入密码");
        tool2 = new Tool("确认密码", "请再次输入密码");
        this.getChildren().add(tool1);
        this.getChildren().add(tool2);

    }

    public Tool getTool1() {
        return tool1;
    }

    public Tool getTool2() {
        return tool2;
    }

    public String getEmail() {
        return email;
    }
}
