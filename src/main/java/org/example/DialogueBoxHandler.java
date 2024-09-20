package org.example;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class RoundedBorder extends AbstractBorder {
    private final int radius;

    public RoundedBorder(int radius) {
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.GRAY);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        g2.dispose();
    }
}

public class DialogueBoxHandler {

    public volatile String dialogue_text = "cheese";

    public String return_str;

    public DialogueBoxHandler() {

    }

    public String GetCipherText() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException {
        OpenDialogue("Ciphertext Input", "Please input ciphertext: ");

        while (dialogue_text == "cheese") {
        }

        return_str = dialogue_text;
        dialogue_text = "";

        return return_str;
    }

    public void OpenDialogue(String title, String prompt) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {


        Font bold_font = new Font("Consolas", Font.BOLD, 15);
        Font plain_font = new Font("Consolas", Font.PLAIN, 15);

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        UIManager.put("Label.font", bold_font);
        UIManager.put("Button.font", bold_font);
        UIManager.put("TextArea.font", plain_font);

        // Open Window
        JFrame frame = new JFrame(title);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // center on screen


        // Text saying "Please Enter Ciphertext" Above text input, with spacing
        JLabel label = new JLabel(prompt);
        label.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // Text input
        JTextArea inputField = new JTextArea();
        inputField.setLineWrap(true);

        // Add Key Processor
        inputField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                // If Key is Enter, clear text area and print it out.
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    String userInput = inputField.getText();
                    dialogue_text = userInput;
                    frame.setVisible(false);
                    frame.dispose();
                }
            }
        }

        );

        frame.add(label, BorderLayout.NORTH);
        frame.add(inputField, BorderLayout.CENTER);

        frame.setVisible(true);
    }
}

