package org.music.Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class HintTextField extends JTextField {
    private final String hint;
    private boolean showingHint;

    public HintTextField(final String hint) {
        super(hint);
        this.hint = hint;
        this.showingHint = true;

        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Xóa viền mặc định
        setForeground(Color.GRAY);  // Màu chữ của hint
        setOpaque(false);  // Đặt nền trong suốt
        setCaretColor(Color.WHITE); // Đặt màu con trỏ nhấp nháy thành trắng

        // Xóa hint khi có focus và thêm lại nếu rỗng khi mất focus
        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (showingHint) {
                    setText("");  // Xóa text khi có focus
                    setForeground(Color.WHITE);  // Màu chữ khi nhập là trắng
                    showingHint = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setText(hint);  // Hiển thị hint nếu không có text
                    setForeground(Color.GRAY);  // Màu chữ của hint
                    showingHint = true;
                }
            }
        });
    }

    @Override
    public String getText() {
        return showingHint ? "" : super.getText();  // Trả về chuỗi rỗng nếu đang hiển thị hint
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (showingHint) {
            g.setColor(Color.GRAY);
            g.drawString(hint, getInsets().left, getHeight() / 2 + g.getFontMetrics().getAscent() / 2 - 2);
        }
    }
}