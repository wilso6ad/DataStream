import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataStreamProcessingGUI extends JFrame {
    private JTextArea originalTextArea;
    private JTextArea filteredTextArea;
    private JTextField searchTextField;
    private JButton loadButton;
    private JButton searchButton;
    private JButton quitButton;
    private JFileChooser fileChooser;

    public DataStreamProcessingGUI() {
        createUI();
    }

    private void createUI() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 700);
        setLocationRelativeTo(null);
        setTitle("Data Stream Processing Interface");

        originalTextArea = new JTextArea();
        originalTextArea.setEditable(false);
        JScrollPane originalScrollPane = new JScrollPane(originalTextArea);

        filteredTextArea = new JTextArea();
        filteredTextArea.setEditable(false);
        JScrollPane filteredScrollPane = new JScrollPane(filteredTextArea);

        searchTextField = new JTextField(20);

        loadButton = new JButton("Load File");
        searchButton = new JButton("Search");
        quitButton = new JButton("Quit");

        fileChooser = new JFileChooser();


        JPanel controlPanel = new JPanel(new BorderLayout(5, 0));
        controlPanel.add(loadButton, BorderLayout.WEST);
        controlPanel.add(searchTextField, BorderLayout.CENTER);
        controlPanel.add(searchButton, BorderLayout.EAST);


        JPanel quitPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        quitPanel.add(quitButton);


        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(controlPanel, BorderLayout.CENTER);
        bottomPanel.add(quitPanel, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                originalScrollPane, filteredScrollPane);
        splitPane.setResizeWeight(0.5);

        getContentPane().add(splitPane, BorderLayout.CENTER);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        loadButton.addActionListener(this::loadFileAction);
        searchButton.addActionListener(this::searchFileAction);
        quitButton.addActionListener(e -> System.exit(0));
    }

    private void loadFileAction(ActionEvent event) {
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                Path filePath = fileChooser.getSelectedFile().toPath();
                String content = Files.lines(filePath).collect(Collectors.joining("\n"));
                originalTextArea.setText(content);
                filteredTextArea.setText("");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error reading this file: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchFileAction(ActionEvent event) {
        String searchStr = searchTextField.getText();
        if (searchStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search string.",
                    "No Search String", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (fileChooser.getSelectedFile() == null) {
            JOptionPane.showMessageDialog(this, "Please load a file first.",
                    "No File Loaded", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Path filePath = fileChooser.getSelectedFile().toPath();
            try (Stream<String> lines = Files.lines(filePath)) {
                String filteredContent = lines
                        .filter(line -> line.contains(searchStr))
                        .collect(Collectors.joining("\n"));
                filteredTextArea.setText(filteredContent);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error processing this file: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DataStreamProcessingGUI gui = new DataStreamProcessingGUI();
            gui.setVisible(true);
        });
    }
}