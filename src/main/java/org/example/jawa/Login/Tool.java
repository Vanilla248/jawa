package org.example.jawa.Login;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import org.example.jawa.Application;

public class Tool extends HBox {
    private final TextField panelText = new TextField();
    private final static Label[] labels = new Label[5];
    static {
        labels[0]= new Label("登录1/找回密码11/找回密码21/注册21");
        labels[1]= new Label("登录2/找回密码12/找回密码22/注册22");
        labels[2]= new Label("注册11");
        labels[3]= new Label("注册12");
        labels[4]= new Label("注册13");

        for (Label label : labels) {
            label.setAlignment(javafx.geometry.Pos.CENTER);
            label.setStyle("-fx-background-color: transparent;");
            label.setStyle("-fx-font-size: 12px;");
            AnchorPane.setLeftAnchor(label, 190.0);
        }

        AnchorPane.setTopAnchor(labels[0], 160.0);
        AnchorPane.setTopAnchor(labels[1], 230.0);
        AnchorPane.setTopAnchor(labels[2], 125.0);
        AnchorPane.setTopAnchor(labels[3], 187.0);
        AnchorPane.setTopAnchor(labels[4], 250.0);

    }
    public Tool(String panelName, String textName){
        setAlignment(javafx.geometry.Pos.CENTER);
        Label panelLabel = new Label(panelName);
        panelLabel.setAlignment(javafx.geometry.Pos.CENTER);
        panelLabel.setPrefWidth(100);
        panelLabel.setStyle("-fx-text-fill: black;");
        panelLabel.setStyle("-fx-font-size: 16px;");
        panelText.setStyle("-fx-font-size: 16px;");
        panelText.setMaxWidth(200);
        panelText.setPromptText(textName);
        getChildren().addAll(panelLabel, panelText);
    }
    public TextField getPanelText(){
        return panelText;
    }
    public static void CountDown(Button button){
        button.setDisable(true);
        var ref = new Object() {
            int count = 59;
        };
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), _ -> {
            if (ref.count > 0) {
                button.setText(ref.count + "秒后重发");
                ref.count--;
            } else {
                button.setDisable(false);
                button.setText("发送验证码");
            }
        }));
        timeline.setCycleCount(60); // 循环60次
        timeline.play(); // 开始倒计时
    }
    public static void showLabel(int i, String text){
        labels[i].setText(text);
        labels[i].setStyle("-fx-text-fill: red;");
        Application.errorAnchorPane.getChildren().remove(labels[i]);
        Application.errorAnchorPane.getChildren().add(labels[i]);
    }
    public static void hideLabel() {
        for (Label label : labels) {
            Application.errorAnchorPane.getChildren().remove(label);
        }
    }
}
