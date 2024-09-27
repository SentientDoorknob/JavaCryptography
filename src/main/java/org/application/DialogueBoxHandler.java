package org.application;

import org.application.decoders.AffineCipher;
import org.application.decoders.VignereCipher;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

import static java.lang.String.format;

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


    private String ciphertext;

    public Font bold_font;
    public Font plain_font;

    public String getCipherText() {
        return ciphertext;
    }

    public void setCipherText(String text) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        ciphertext = text;
        Main.OnCipherTextInput(ciphertext);
    }


    public DialogueBoxHandler() {
        bold_font = new Font("Consolas", Font.BOLD, 15);
        plain_font = new Font("Consolas", Font.PLAIN, 15);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("Label.font", plain_font);
            UIManager.put("Button.font", bold_font);
            UIManager.put("TextArea.font", plain_font);
        }
        catch (UnsupportedLookAndFeelException e) {
        }
        catch (ClassNotFoundException e) {
        }
        catch (InstantiationException e) {
        }
        catch (IllegalAccessException e) {
        }
    }


    public void OpenCipherDialogue() {
        OpenInputDialogue("Ciphertext Input", "Please input ciphertext: ");
    }

    public void OpenInputDialogue(String title, String prompt) {

        // Open Window
        JFrame frame = new JFrame(title);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // center on screen


        // Text saying "Please Enter Ciphertext" Above text input, with spacing
        JLabel label = new JLabel(prompt);
        label.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        label.setFont(bold_font);

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
                    try {
                        setCipherText(userInput);
                    } catch (UnsupportedLookAndFeelException ex) {
                        throw new RuntimeException(ex);
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    } catch (InstantiationException ex) {
                        throw new RuntimeException(ex);
                    } catch (IllegalAccessException ex) {
                        throw new RuntimeException(ex);
                    }
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


    public void OpenVignereOutput(String keyword, String plaintext, String ciphertext) {
        // Open Window
        JFrame frame = new JFrame("Vignere Cipher Output");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center on screen

        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        String text = String.format("<html>Vignere Cipher Analysis Results:<br/>" +
                "&nbsp;&nbsp;&nbsp;&nbsp;Keyword Length: <b>%d</b><br/>" +
                "&nbsp;&nbsp;&nbsp;&nbsp;Predicted Keyword: <b>%s</b><html>", keyword.length(), keyword);

        JLabel vignereInfo = new JLabel(text);
        vignereInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        vignereInfo.setFont(plain_font);
        vignereInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextArea plaintextDisplay = new JTextArea(plaintext);
        plaintextDisplay.setLineWrap(true);
        plaintextDisplay.setEditable(false);
        plaintextDisplay.setAlignmentX(Component.CENTER_ALIGNMENT);
        JScrollPane scrollPane = new JScrollPane(plaintextDisplay);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setPreferredSize(new Dimension(800, 100));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));

        // Input field for the keyword
        JTextField keywordInput = new JTextField(keyword);
        keywordInput.setMaximumSize(new Dimension(800, 60)); // Adjusted to allow more width
        keywordInput.setAlignmentX(Component.CENTER_ALIGNMENT);
        keywordInput.setFont(bold_font);

        JButton retry = new JButton("Retry");
        JButton exit = new JButton("Exit");

        inputPanel.add(keywordInput);
        inputPanel.add(retry);
        inputPanel.add(exit);

        exit.addActionListener(e -> frame.dispose());

        retry.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        VignereCipher.DecryptFromDialogue(ciphertext, keywordInput.getText());
                                        frame.dispose();
                                    }
        });

        // Optional: Add an empty border for better spacing
        keywordInput.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Add components to the frame
        frame.add(vignereInfo);
        frame.add(scrollPane);
        frame.add(inputPanel);

        frame.setVisible(true);

    }

    public void OpenAffineOutput(int[] key, String plaintext, String ciphertext) {
        // Open Window
        JFrame frame = new JFrame("Affine");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center on screen

        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        String text = String.format("<html>Vignere Cipher Analysis Results:<br/>" +
                "&nbsp;&nbsp;&nbsp;&nbsp;Predicted Key: <b>%s</b><html>", Arrays.toString(key));

        JLabel vignereInfo = new JLabel(text);
        vignereInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        vignereInfo.setFont(plain_font);
        vignereInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextArea plaintextDisplay = new JTextArea(plaintext);
        plaintextDisplay.setLineWrap(true);
        plaintextDisplay.setEditable(false);
        plaintextDisplay.setAlignmentX(Component.CENTER_ALIGNMENT);
        JScrollPane scrollPane = new JScrollPane(plaintextDisplay);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setPreferredSize(new Dimension(800, 100));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));

        // Input field for the keyword
        JTextField keywordInput = new JTextField("[e], [t]");
        keywordInput.setMaximumSize(new Dimension(800, 60)); // Adjusted to allow more width
        keywordInput.setAlignmentX(Component.CENTER_ALIGNMENT);
        keywordInput.setFont(bold_font);

        JButton retry = new JButton("Retry");
        JButton exit = new JButton("Exit");

        inputPanel.add(keywordInput);
        inputPanel.add(retry);
        inputPanel.add(exit);

        exit.addActionListener(e -> frame.dispose());

        retry.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AffineCipher.DecryptFromDialogue(ciphertext, keywordInput.getText().trim().split(",[ ]*"));
                frame.dispose();
            }
        });

        // Optional: Add an empty border for better spacing
        keywordInput.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Add components to the frame
        frame.add(vignereInfo);
        frame.add(scrollPane);
        frame.add(inputPanel);

        frame.setVisible(true);
    }
}

