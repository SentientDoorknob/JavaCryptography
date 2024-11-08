package org.application;

import org.application.decoders.*;
import org.application.results.cipher.AffineResult;
import org.crypography_tools.LinearAlgebra;
import org.crypography_tools.Tools;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

import static java.lang.String.format;

public class DialogueBoxHandler {


    private String ciphertext;

    public Font bold_font;
    public Font plain_font;

    public String getCipherText() {
        return ciphertext;
    }

    public ActionListener returnListener = e -> {
        Main.main(null);
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor((JButton) e.getSource());
        parentFrame.dispose();
    };

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
            UIManager.put("ComboBox.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
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


    public void OpenCipherInputDialogue(String[] ciphers) {
        JFrame frame = new JFrame("Cipher Selection");
        frame.setSize(800, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // center on screen

        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Please Select Cipher:");
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 5));

        JComboBox<String> cipherOptionDropdown = new JComboBox<String>(ciphers);
        cipherOptionDropdown.setMaximumSize(new Dimension(800, 60));
        cipherOptionDropdown.setAlignmentX(Component.LEFT_ALIGNMENT);
        cipherOptionDropdown.setFont(plain_font);

        cipherOptionDropdown.setFocusable(false);

        JButton confirm = new JButton("Confirm");
        JButton exit = new JButton("Exit");

        confirm.setFocusable(false);
        exit.setFocusable(false);

        exit.addActionListener(e -> frame.dispose());
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.OnCipherSelection(cipherOptionDropdown.getSelectedItem().toString());
                frame.dispose();
            }
        });

        confirm.setMaximumSize(new Dimension(100, 60));
        exit.setMaximumSize(new Dimension(75, 60));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
        inputPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        inputPanel.add(cipherOptionDropdown);
        inputPanel.add(confirm);
        inputPanel.add(exit);

        frame.add(label);
        frame.add(inputPanel);

        frame.setVisible(true);
    }

    public void OpenCiphertextInputDialogue() {
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

        JScrollPane inputPane = new JScrollPane(inputField);

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
        frame.add(inputPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    public void OpenVignereOutput(String predictedKeyword, String usedKeyword, String plaintext, String ciphertext) {
        // Open Window
        JFrame frame = new JFrame("Vignere Cipher Output");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center on screen

        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        String text = String.format("<html>Permutation Cipher Analysis Results:<br/>" +
                "&nbsp;&nbsp;&nbsp;&nbsp;Keyword Length: <b>%d</b><br/>" +
                "&nbsp;&nbsp;&nbsp;&nbsp;Predicted Keyword: <b>%s</b><html><br/>" +
                "&nbsp;&nbsp;&nbsp;&nbsp;Used Keyword: <b>%s</b><html>", usedKeyword.length(), predictedKeyword, usedKeyword);

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
        JTextField keywordInput = new JTextField(usedKeyword);
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
                                        VignereCipher.DecryptFromDialogue(ciphertext, keywordInput.getText(), predictedKeyword);
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

    public void OpenAffineOutput(AffineResult result) {
        // Open Window
        JFrame frame = new JFrame("Affine Cipher");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center on screen

        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        System.out.println("Here");
        System.out.println(Arrays.toString(result.predictedKeyword));

        String text = String.format("<html>Permutation Cipher Analysis Results:<br/>" +
                "&nbsp;&nbsp;&nbsp;&nbsp; Predicted Keyword: <b>%s</b><br/>" +
                "&nbsp;&nbsp;&nbsp;&nbsp; Used Keyword: <b>%s</b><br/>" +
                "&nbsp;&nbsp;&nbsp;&nbsp; Used ET: <b>%s</b><html>",
                Arrays.toString(result.predictedKeyword), Arrays.toString(result.usedKeyword), Arrays.toString(result.ET));

        JLabel affineInfo = new JLabel(text);
        affineInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        affineInfo.setFont(plain_font);
        affineInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextArea plaintextDisplay = new JTextArea(result.plaintext);
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

        JButton swap = new JButton("Swap");
        JButton retry = new JButton("Retry");
        JButton exit = new JButton("Exit");

        inputPanel.add(keywordInput);
        inputPanel.add(swap);
        inputPanel.add(retry);
        inputPanel.add(exit);

        exit.addActionListener(returnListener);

        retry.addActionListener(e -> {
            String[] inputValue = keywordInput.getText().trim().split(",[ ]*");

            result.ET = Tools.StringToIntArray(inputValue);
            result.ReAnalyse();

            frame.dispose();
        });

        swap.addActionListener(e -> {
            int temp = result.ET[0];
            result.ET[0] = result.ET[1];
            result.ET[1] = temp;

            result.ReAnalyse();
            frame.dispose();
        });

        // Optional: Add an empty border for better spacing
        keywordInput.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Add components to the frame
        frame.add(affineInfo);
        frame.add(scrollPane);
        frame.add(inputPanel);

        frame.setVisible(true);
    }

    public void OpenPermutationOutput(int[] predictedKeyword, int[] usedKeyword, String plaintext, String ciphertext) {
        // Open Window
        JFrame frame = new JFrame("Permutation Cipher Output");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center on screen

        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        String text = String.format("<html>Permutation Cipher Analysis Results:<br/>" +
                "&nbsp;&nbsp;&nbsp;&nbsp;Keyword Length: <b>%d</b><br/>" +
                "&nbsp;&nbsp;&nbsp;&nbsp;Predicted Keyword: <b>%s</b><html><br/>" +
                "&nbsp;&nbsp;&nbsp;&nbsp;Used Keyword: <b>%s</b><html>", predictedKeyword.length, Arrays.toString(predictedKeyword), Arrays.toString(usedKeyword));

        JLabel permutationInfo = new JLabel(text);
        permutationInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        permutationInfo.setFont(plain_font);
        permutationInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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
        JTextField keywordInput = new JTextField(Arrays.toString(usedKeyword));
        keywordInput.setMaximumSize(new Dimension(800, 60)); // Adjusted to allow more width
        keywordInput.setAlignmentX(Component.CENTER_ALIGNMENT);
        keywordInput.setFont(bold_font);

        JButton retry = new JButton("Retry");
        JButton exit = new JButton("Exit");

        inputPanel.add(keywordInput);
        inputPanel.add(retry);
        inputPanel.add(exit);

        exit.addActionListener(e -> frame.dispose());

        retry.addActionListener(e -> {
            PermutationCipher.DecryptFromResultsDialogue(ciphertext, keywordInput.getText().replaceAll("[^\\d|,]", "").split(","), predictedKeyword);
            frame.dispose();
        });

        keywordInput.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Add components to the frame
        frame.add(permutationInfo);
        frame.add(scrollPane);
        frame.add(inputPanel);

        frame.setVisible(true);
    }

    public void OpenBifidOutput(String substitutionText) {
        // Open Window
        JFrame frame = new JFrame("Bifid Output");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // center on screen

        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Bifid Cipher -> Substitution Cipher");
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(new EmptyBorder(5, 0, 5, 0));

        JTextArea substitutionOutput = new JTextArea(substitutionText);
        substitutionOutput.setLineWrap(true);
        substitutionOutput.setAlignmentX(Component.LEFT_ALIGNMENT);
        substitutionOutput.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(substitutionOutput);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(x -> frame.dispose());

        buttonPanel.add(new JLabel(""));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(exitButton);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        frame.add(label);
        frame.add(scrollPane);
        frame.add(buttonPanel);

        frame.setVisible(true);
    }

    public void OpenHillOutput(double[][] predictedMatrix, double[][] usedMatrix, String[] thhe, String plaintext, String ciphertext) {
        JFrame frame = new JFrame("Hill Cipher Output");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center on screen

        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        String text = String.format("<html>Permutation Cipher Analysis Results:<br/>" +
                "&nbsp;&nbsp;&nbsp;&nbsp;Predicted Matrix: <b>%s</b><br/>" +
                "&nbsp;&nbsp;&nbsp;&nbsp;Used Matrix: <b>%s</b><html><br/>" +
                "&nbsp;&nbsp;&nbsp;&nbsp;[th, he]: <b>%s</b><html><br/>" +
                "&nbsp;&nbsp;&nbsp;&nbsp;Index of Coincidence: <b>%.5f</b><html>"
                                                                        , LinearAlgebra.MatrixToString(predictedMatrix, true)
                                                                        , LinearAlgebra.MatrixToString(usedMatrix, true)
                                                                        , Arrays.toString(thhe)
                                                                        , Tools.IndexOfCoincidence(plaintext));

        JLabel hillInfo = new JLabel(text);
        hillInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        hillInfo.setFont(plain_font);
        hillInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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
        JTextField keywordInput = new JTextField(Arrays.toString(thhe));
        keywordInput.setMaximumSize(new Dimension(800, 60)); // Adjusted to allow more width
        keywordInput.setAlignmentX(Component.CENTER_ALIGNMENT);
        keywordInput.setFont(bold_font);

        JButton retry = new JButton("Retry");
        JButton exit = new JButton("Exit");

        inputPanel.add(keywordInput);
        inputPanel.add(retry);
        inputPanel.add(exit);

        exit.addActionListener(e -> frame.dispose());

        retry.addActionListener(e -> {
            HillCipher.DecryptFromResultsDialogue(ciphertext, keywordInput.getText().replaceAll("[^[a-z]|,]", "").split(","), predictedMatrix);
            frame.dispose();
        });

        // Optional: Add an empty border for better spacing
        keywordInput.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        frame.add(hillInfo);
        frame.add(scrollPane);
        frame.add(inputPanel);
        frame.setVisible(true);
    }

    public void OpenNihilistOutput(int[][] predictedKeywords, int[] usedKeyword, String substitutionText, String ciphertext) {
        // Open Window
        JFrame frame = new JFrame("Nihilist Cipher Output");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center on screen

        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        String text = String.format("<html>Nihilist Cipher Analysis Results:<br/>" +
                "&nbsp;&nbsp;&nbsp;&nbsp;Keyword Length: <b>%d</b><br/>" +
                "&nbsp;&nbsp;&nbsp;&nbsp;Predicted Keywords: <br/>", usedKeyword.length);

        for (int[] k : predictedKeywords) {
            text = text.concat(String.format("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>%s</b><br/>", Arrays.toString(k)));
        }

        JLabel permutationInfo = new JLabel(text);
        permutationInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        permutationInfo.setFont(plain_font);
        permutationInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextArea plaintextDisplay = new JTextArea(substitutionText);
        plaintextDisplay.setLineWrap(true);
        plaintextDisplay.setEditable(false);
        plaintextDisplay.setAlignmentX(Component.CENTER_ALIGNMENT);
        JScrollPane scrollPane = new JScrollPane(plaintextDisplay);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setPreferredSize(new Dimension(800, 100));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));

        // Input field for the keyword
        JTextField keywordInput = new JTextField(Arrays.toString(usedKeyword));
        keywordInput.setMaximumSize(new Dimension(800, 60)); // Adjusted to allow more width
        keywordInput.setAlignmentX(Component.CENTER_ALIGNMENT);
        keywordInput.setFont(bold_font);

        JButton retry = new JButton("Retry");
        JButton exit = new JButton("Exit");

        inputPanel.add(keywordInput);
        inputPanel.add(retry);
        inputPanel.add(exit);

        exit.addActionListener(e -> frame.dispose());

        retry.addActionListener(e -> {
            NihilistCipher.ConvertFromResultsDialogue(ciphertext, predictedKeywords, keywordInput.getText().replaceAll("[^\\d|,]", "").split(","));
            frame.dispose();
        });

        keywordInput.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Add components to the frame
        frame.add(permutationInfo);
        frame.add(scrollPane);
        frame.add(inputPanel);

        frame.setVisible(true);
    }
}

