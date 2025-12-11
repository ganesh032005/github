import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.zip.CRC32;


public class LoveCalculator extends JFrame {
    private final JTextField nameField1 = new JTextField();
    private final JTextField nameField2 = new JTextField();
    private final JButton calcButton = new JButton("Calculate Love %");
    private final JProgressBar progressBar = new JProgressBar(0, 100);
    private final JLabel resultLabel = new JLabel("Enter two names and click the button.");

    public LoveCalculator() {
        super("Simple Love Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 260);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Top panel for input
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 12));
        inputPanel.add(new JLabel("Name 1:"));
        inputPanel.add(nameField1);
        inputPanel.add(new JLabel("Name 2:"));
        inputPanel.add(nameField2);
        add(inputPanel, BorderLayout.NORTH);

        // Center panel for button and progress
        JPanel center = new JPanel(new BorderLayout(8, 8));
        center.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
        calcButton.setFont(calcButton.getFont().deriveFont(Font.BOLD));
        center.add(calcButton, BorderLayout.NORTH);

        progressBar.setStringPainted(true);
        progressBar.setValue(0);
        center.add(progressBar, BorderLayout.CENTER);

        add(center, BorderLayout.CENTER);

        // Bottom panel for result text
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 12, 12, 12));
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bottom.add(resultLabel, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        // Button action
        calcButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateAndAnimate();
            }
        });
    }

    private void calculateAndAnimate() {
        String a = nameField1.getText().trim();
        String b = nameField2.getText().trim();

        if (a.isEmpty() || b.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both names.", "Input required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int percent = deterministicLovePercentage(a, b);

        // Disable button while animating
        calcButton.setEnabled(false);
        resultLabel.setText("Calculating...");

        // Animation: increment progress until reaches percent
        Timer timer = new Timer(12, null);
        timer.addActionListener(new ActionListener() {
            private int value = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (value >= percent) {
                    timer.stop();
                    progressBar.setValue(percent);
                    progressBar.setString(percent + "%");
                    resultLabel.setText(verdictText(percent, a, b));
                    calcButton.setEnabled(true);
                } else {
                    value++;
                    progressBar.setValue(value);
                    progressBar.setString(value + "%");
                }
            }
        });
        progressBar.setValue(0);
        progressBar.setString("0%");
        timer.start();
    }

    /**
     * Deterministic love percentage based on CRC32 hash of the combined names.
     * Uses a simple algorithm so the same two names always produce the same result.
     */
    private int deterministicLovePercentage(String name1, String name2) {
        String combo = (name1 + "&" + name2).toLowerCase();
        CRC32 crc = new CRC32();
        crc.update(combo.getBytes());
        long hash = crc.getValue();
        // Map hash to 0..100
        int percent = (int) (hash % 101);
        return percent;
    }

    private String verdictText(int percent, String a, String b) {
        String base = String.format("%s ❤ %s : %d%% — ", a, b, percent);
        String verdict;
        if (percent >= 90) verdict = "A match made in the movies!";
        else if (percent >= 75) verdict = "Great chemistry.";
        else if (percent >= 50) verdict = "Good potential — give it time.";
        else if (percent >= 30) verdict = "Some sparks, but work needed.";
        else verdict = "Hmm... friendship first?";
        return base + verdict;
    }

    public static void main(String[] args) {
        // Use system look and feel for nicer appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoveCalculator app = new LoveCalculator();
                app.setVisible(true);
            }
        });
    }
}
