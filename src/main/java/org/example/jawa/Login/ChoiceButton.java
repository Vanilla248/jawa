package org.example.jawa.Login;

import javafx.scene.control.Button;

public class ChoiceButton extends Button {

    public ChoiceButton(String text, String color) {
        super(text);
        updateStyle(color, 16); // 初始化样式
        setMouseEffects(color);
    }

    // 设置鼠标效果
    private void setMouseEffects(String color) {
        this.setOnMouseEntered(_ -> updateStyle(color, 20)); // 鼠标悬停时增大字体
        this.setOnMouseExited(_ -> updateStyle(color, 16)); // 鼠标移出时恢复字体
    }
    // 方法用于更新按钮样式
    private void updateStyle(String color, int fontSize) {
        setStyle("-fx-background-color: transparent; -fx-text-fill: " + color + "; -fx-font-size: " + fontSize + ";");
    }

    // 更新颜色
    public void setColor(String color) {
        updateStyle(color, 16); // 更新样式
        setMouseEffects(color);
    }
}
