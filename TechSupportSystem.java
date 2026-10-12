
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TechSupportSystem extends JFrame {

    private final HashMap<String, ArrayList<String>> knowledgeBase =
            new HashMap<>();

    private final ArrayList<String> conversationHistory =
            new ArrayList<>();

    private final Random random = new Random();

    private JPanel chatPanel;
    private JTextField inputField;
    private JScrollPane scrollPane;
    private JLabel statusLabel;
    private AnimatedBackground background;

    private int messageCount = 0;

    private static final Color CYAN = new Color(0, 225, 255);
    private static final Color WHITE = new Color(235, 245, 255);
    private static final Color PANEL = new Color(15, 25, 48);

    public TechSupportSystem() {
        setTitle("TechSupport AI - Help Desk");
        setSize(1100, 750);
        setMinimumSize(new Dimension(850, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeKnowledgeBase();
        initializeUI();
    }

    // Store keyword-response pairs in a HashMap
    private void initializeKnowledgeBase() {

        addResponses("internet",
                "Try restarting your router and reconnecting.",
                "Check whether other devices can access the internet.",
                "Try resetting your network settings if the issue continues.");

        addResponses("wifi",
                "Check whether Wi-Fi is enabled on your device.",
                "Restart your router and reconnect to the network.",
                "Move closer to the router to improve signal strength.");

        addResponses("password",
                "Use the Forgot Password option on the login screen.",
                "Create a strong password with letters, numbers and symbols.",
                "Never share your password with anyone.");

        addResponses("slow computer",
                "Close unnecessary applications running in the background.",
                "Restart your computer and check available storage.",
                "Disable unnecessary startup applications.");

        addResponses("printer",
                "Check that your printer is switched on and connected.",
                "Make sure the printer has paper and sufficient ink.",
                "Try reinstalling or updating the printer driver.");

        addResponses("virus",
                "Run a full scan using a trusted antivirus program.",
                "Avoid opening suspicious email attachments.",
                "Keep your operating system and antivirus updated.");

        addResponses("software",
                "Try restarting the application.",
                "Check for available software updates.",
                "Reinstall the application if the problem continues.");

        addResponses("computer",
                "Please describe the computer problem in more detail.",
                "Restart your computer and check whether the issue persists.",
                "Tell me if you see an error message on your screen.");

        addResponses("hello",
                "Hello! Welcome to TechSupport. How can I help you?",
                "Hi there! Tell me about your technical issue.",
                "Welcome! I'm ready to help with your technical problems.");

        addResponses("help",
                "I can help with internet, Wi-Fi, passwords, printers, "
                        + "viruses, software and slow computers.",
                "Describe your problem, and I'll suggest some troubleshooting steps.");

        addResponses("thank",
                "You're welcome! Let me know if you need anything else.",
                "Happy to help! Is there anything else I can assist with?",
                "Glad I could help. Have a great day!");

        addResponses("bye",
                "Goodbye! Thank you for using TechSupport.",
                "Take care! Contact us again if you need assistance.");
    }

    private void addResponses(String keyword, String... responses) {
        knowledgeBase.put(
                keyword,
                new ArrayList<>(Arrays.asList(responses))
        );
    }

    // Build the graphical user interface
    private void initializeUI() {

        background = new AnimatedBackground();
        background.setLayout(new BorderLayout(18, 18));
        background.setBorder(new EmptyBorder(22, 25, 22, 25));
        setContentPane(background);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel brand = new JPanel(new GridLayout(2, 1, 0, 5));
        brand.setOpaque(false);

        JLabel title = new JLabel("TECH SUPPORT");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(CYAN);

        JLabel subtitle = new JLabel("YOUR PERSONAL IT HELP DESK");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 11));
        subtitle.setForeground(new Color(160, 185, 215));

        brand.add(title);
        brand.add(subtitle);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statusPanel.setOpaque(false);

        statusLabel = new JLabel("● SYSTEM ONLINE");
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        statusLabel.setForeground(new Color(90, 255, 170));
        statusPanel.add(statusLabel);

        JButton clearButton = createButton("Clear Chat");
        clearButton.addActionListener(e -> clearChat());

        statusPanel.add(clearButton);

        header.add(brand, BorderLayout.WEST);
        header.add(statusPanel, BorderLayout.EAST);

        background.add(header, BorderLayout.NORTH);

        // Main chat panel
        JPanel main = new JPanel(new BorderLayout(12, 12));
        main.setOpaque(false);

        JPanel welcome = new JPanel(new GridLayout(2, 1, 0, 5));
        welcome.setOpaque(false);

        JLabel welcomeTitle = new JLabel(
                "How can we help you today?"
        );
        welcomeTitle.setForeground(WHITE);
        welcomeTitle.setFont(new Font("SansSerif", Font.BOLD, 23));

        JLabel welcomeText = new JLabel(
                "Describe your technical issue below to get started."
        );
        welcomeText.setForeground(new Color(165, 185, 215));
        welcomeText.setFont(new Font("SansSerif", Font.PLAIN, 13));

        welcome.add(welcomeTitle);
        welcome.add(welcomeText);

        main.add(welcome, BorderLayout.NORTH);

        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(new Color(10, 18, 36, 235));
        chatPanel.setBorder(new EmptyBorder(18, 18, 18, 18));

        scrollPane = new JScrollPane(chatPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(
                new Color(65, 100, 145), 1
        ));
        scrollPane.getViewport().setBackground(PANEL);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        main.add(scrollPane, BorderLayout.CENTER);

        // Input area
        JPanel bottom = new JPanel(new BorderLayout(10, 10));
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(5, 0, 0, 0));

        inputField = new JTextField();
        inputField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        inputField.setForeground(WHITE);
        inputField.setCaretColor(CYAN);
        inputField.setBackground(new Color(19, 31, 56));
        inputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(70, 105, 150), 1
                ),
                new EmptyBorder(13, 14, 13, 14)
        ));

        inputField.setToolTipText("Type your technical problem here");
        inputField.addActionListener(e -> sendMessage());

        JButton sendButton = createButton("SEND  ➤");
        sendButton.setPreferredSize(new Dimension(125, 46));
        sendButton.addActionListener(e -> sendMessage());

        bottom.add(inputField, BorderLayout.CENTER);
        bottom.add(sendButton, BorderLayout.EAST);

        JLabel footer = new JLabel(
                "HASHMAP-POWERED SUPPORT  •  "
                        + "Try: Wi-Fi, password, printer, internet"
        );
        footer.setForeground(new Color(145, 170, 205));
        footer.setFont(new Font("SansSerif", Font.PLAIN, 11));

        JPanel bottomContainer = new JPanel(new BorderLayout(0, 10));
        bottomContainer.setOpaque(false);
        bottomContainer.add(bottom, BorderLayout.CENTER);
        bottomContainer.add(footer, BorderLayout.SOUTH);

        main.add(bottomContainer, BorderLayout.SOUTH);

        background.add(main, BorderLayout.CENTER);

        addBotMessage(
                "Hello! Welcome to TechSupport. "
                        + "Describe your technical issue, and I'll help you."
        );
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setForeground(WHITE);
        button.setBackground(new Color(26, 62, 100));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(65, 150, 210), 1
                ),
                new EmptyBorder(10, 14, 10, 14)
        ));

        Color normal = new Color(26, 62, 100);
        Color hover = new Color(30, 105, 145);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(normal);
            }
        });

        return button;
    }

    // Process user input
    private void sendMessage() {

        String message = inputField.getText().trim();

        if (message.isEmpty()) {
            return;
        }

        inputField.setText("");

        addUserMessage(message);

        String response = getResponse(message);

        // Small delay makes the response feel more natural
        statusLabel.setText("● ASSISTANT RESPONDING...");
        statusLabel.setForeground(new Color(255, 210, 100));

       javax.swing.Timer timer = new javax.swing.Timer(450, e -> {
    addBotMessage(response);
    statusLabel.setText("● SYSTEM ONLINE");
    statusLabel.setForeground(new Color(90, 255, 170));
});

timer.setRepeats(false);
timer.start();
        timer.setRepeats(false);
        timer.start();
    }

    // Detect matching keywords and select a response
    private String getResponse(String message) {

        String input = message.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9\\s-]", " ")
                .replaceAll("\\s+", " ")
                .trim();

        String bestKeyword = null;

        // Prefer the longest matching keyword phrase
        for (String keyword : knowledgeBase.keySet()) {
            if (containsKeyword(input, keyword)) {
                if (bestKeyword == null
                        || keyword.length() > bestKeyword.length()) {
                    bestKeyword = keyword;
                }
            }
        }

        if (bestKeyword != null) {

            ArrayList<String> responses =
                    knowledgeBase.get(bestKeyword);

            return responses.get(random.nextInt(responses.size()));
        }

        // Default responses for unknown messages
        String[] defaultResponses = {
                "I'm not sure about that issue yet. Try describing it "
                        + "using words like internet, Wi-Fi, printer or password.",
                "Could you provide more details about the problem? "
                        + "For example, tell me what happens and any error message.",
                "I couldn't find a matching keyword in my knowledge base. "
                        + "Please explain your technical issue in another way."
        };

        return defaultResponses[random.nextInt(defaultResponses.length)];
    }

    // Match complete words rather than accidental partial matches
    private boolean containsKeyword(String input, String keyword) {

        if (keyword.equals("wifi")) {
            return input.matches(".*\\bwi\\s*-?\\s*fi\\b.*");
        }

        if (keyword.equals("internet")) {
            return input.matches(".*\\binternet\\b.*")
                    || input.matches(".*\\bconnection\\b.*");
        }

        if (keyword.equals("slow computer")) {
            return input.matches(".*\\bslow\\s+(computer|pc|laptop)\\b.*")
                    || input.matches(".*\\bcomputer\\s+is\\s+slow\\b.*");
        }

        if (keyword.equals("thank")) {
            return input.matches(".*\\bthanks?\\b.*")
                    || input.matches(".*\\bthank\\s+you\\b.*");
        }

        return Arrays.asList(input.split("\\s+")).contains(keyword)
                || input.contains(" " + keyword + " ");
    }

    // Add a user message to the chat
    private void addUserMessage(String message) {
        addChatBubble("YOU", message, true);
    }

    // Add a support response to the chat
    private void addBotMessage(String message) {
        addChatBubble("TECH SUPPORT", message, false);
    }

    private void addChatBubble(String sender, String message,
                               boolean isUser) {

        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(7, 0, 7, 0));

        JPanel bubble = new JPanel(new BorderLayout(0, 7));
        bubble.setBackground(isUser
                ? new Color(25, 80, 125)
                : new Color(27, 39, 62));

        bubble.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        isUser
                                ? new Color(40, 160, 220)
                                : new Color(75, 105, 145), 1
                ),
                new EmptyBorder(12, 15, 12, 15)
        ));

        bubble.setMaximumSize(new Dimension(650, Integer.MAX_VALUE));

        JLabel senderLabel = new JLabel(sender);
        senderLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        senderLabel.setForeground(isUser
                ? CYAN
                : new Color(120, 220, 255));

        JTextArea messageText = new JTextArea(message);
        messageText.setFont(new Font("SansSerif", Font.PLAIN, 14));
        messageText.setForeground(WHITE);
        messageText.setOpaque(false);
        messageText.setEditable(false);
        messageText.setLineWrap(true);
        messageText.setWrapStyleWord(true);
        messageText.setFocusable(false);
        messageText.setColumns(35);

        JLabel timeLabel = new JLabel(
                LocalTime.now().format(
                        DateTimeFormatter.ofPattern("hh:mm a")
                )
        );
        timeLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        timeLabel.setForeground(new Color(155, 180, 210));

        bubble.add(senderLabel, BorderLayout.NORTH);
        bubble.add(messageText, BorderLayout.CENTER);
        bubble.add(timeLabel, BorderLayout.SOUTH);

        if (isUser) {
            row.add(bubble, BorderLayout.EAST);
        } else {
            row.add(bubble, BorderLayout.WEST);
        }

        chatPanel.add(row);
        chatPanel.add(Box.createVerticalStrut(3));

        chatPanel.revalidate();
        chatPanel.repaint();

        messageCount++;

        String historyEntry = "[" + timeLabel.getText() + "] "
                + sender + ": " + message;

        conversationHistory.add(historyEntry);

        SwingUtilities.invokeLater(() -> {
            JScrollBar bar = scrollPane.getVerticalScrollBar();
            bar.setValue(bar.getMaximum());
        });
    }

    // Clear visible messages and conversation history
    private void clearChat() {

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Clear the conversation and its history?",
                "Clear Chat",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {

            chatPanel.removeAll();
            chatPanel.revalidate();
            chatPanel.repaint();

            conversationHistory.clear();
            messageCount = 0;

            addBotMessage(
                    "Chat cleared! Hello again. How can I help you?"
            );
        }
    }

    // Animated background with moving glowing particles
    private class AnimatedBackground extends JPanel {

        private final java.util.List<Particle> particles =
                new ArrayList<>();

        private final javax.swing.Timer animationTimer;

        AnimatedBackground() {

            setOpaque(true);

            for (int i = 0; i < 65; i++) {
                particles.add(new Particle(
                        random.nextInt(1200),
                        random.nextInt(850),
                        0.3 + random.nextDouble() * 1.2,
                        1 + random.nextInt(3)
                ));
            }

            animationTimer = new javax.swing.Timer(35, e -> {
                for (Particle p : particles) {
                    p.x += p.speed;
                    p.y += Math.sin(p.x * 0.015) * 0.45;

                    if (p.x > getWidth()) {
                        p.x = 0;
                        p.y = random.nextInt(
                                Math.max(1, getHeight())
                        );
                    }
                }

                repaint();
            });

            animationTimer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            int w = getWidth();
            int h = getHeight();

            GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(7, 13, 30),
                    w, h, new Color(19, 32, 65)
            );

            g2.setPaint(gradient);
            g2.fillRect(0, 0, w, h);

            // Soft glowing circles
            g2.setColor(new Color(0, 130, 200, 18));
            g2.fillOval(w - 360, -150, 500, 500);

            g2.setColor(new Color(100, 50, 200, 16));
            g2.fillOval(-200, h - 350, 500, 500);

            // Draw animated particles
            for (Particle p : particles) {

                int alpha = 70 + (int)
                        (40 * Math.abs(Math.sin(p.x * 0.02)));

                g2.setColor(new Color(0, 210, 255, alpha));
                g2.fillOval(
                        (int) p.x,
                        (int) p.y,
                        p.size,
                        p.size
                );

                // Connect nearby particles with faint lines
                for (Particle other : particles) {

                    if (p == other) {
                        continue;
                    }

                    double dx = p.x - other.x;
                    double dy = p.y - other.y;
                    double distance = Math.sqrt(dx * dx + dy * dy);

                    if (distance < 90) {
                        g2.setColor(new Color(40, 140, 230, 13));
                        g2.drawLine(
                                (int) p.x, (int) p.y,
                                (int) other.x, (int) other.y
                        );
                    }
                }
            }

            g2.dispose();
        }
    }

    private static class Particle {
        double x;
        double y;
        double speed;
        int size;

        Particle(double x, double y, double speed, int size) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.size = size;
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception ignored) {
                // Continue with the default look and feel
            }

            TechSupportSystem app = new TechSupportSystem();
            app.setVisible(true);
        });
    }
}
