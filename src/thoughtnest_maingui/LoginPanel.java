package thoughtnest_maingui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPanel extends JPanel{
    
    private static JLabel logoLabel;
    private static JLabel bgLabel;
    
    private static JLabel anggaLabel;
    private static JTextField anggaText;
    
    private static JLabel keyLabel;
    private static JPasswordField keyText;
    private static JCheckBox showPasswordCheckbox;
    
    private static JButton createButton;
    private static JButton enterButton;
    
    private static final Color BACKGROUND_COLOR = new Color(216, 191, 165);
    private static final Color OGB_COLOR = new Color(120, 78, 60);
    private static final Color DUOLMB_COLOR = new Color(169, 125, 97);
    
    public LoginPanel(CardLayout cardLayout, JPanel panelCard, JFrame frame){
        
        setLayout(null);//aron free ibutang maski aha
        setBackground(BACKGROUND_COLOR);

        //Nickname
        anggaLabel = new JLabel("Nickname:");
        anggaLabel.setBounds(137, 195, 80, 25);
        add(anggaLabel);

        String NICKNAME_PLACEHOLDER = "Enter your nickname";
        anggaText = new JTextField(NICKNAME_PLACEHOLDER);
        anggaText.setBounds(85, 220, 165, 25);
        anggaText.setHorizontalAlignment(JTextField.CENTER);
        anggaText.setForeground(Color.GRAY); //Placeholder color
        anggaText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
            if (anggaText.getText().equals(NICKNAME_PLACEHOLDER)) {
                anggaText.setText("");
                anggaText.setForeground(Color.BLACK);
            }
        }

            @Override
            public void focusLost(FocusEvent e) {
                if (anggaText.getText().isEmpty()) {
                    anggaText.setText(NICKNAME_PLACEHOLDER);
                    anggaText.setForeground(Color.GRAY);
                }
            }
        });

        add(anggaText);
        
        //Password
        keyLabel = new JLabel("Key:");
        keyLabel.setBounds(154, 245, 80, 25);
        add(keyLabel);
        
        String PASSWORD_PLACEHOLDER = "Enter your key";
        keyText = new JPasswordField(PASSWORD_PLACEHOLDER);
        keyText.setBounds(85, 270, 165, 25);
        keyText.setHorizontalAlignment(JTextField.CENTER);
        keyText.setEchoChar((char) 0); //Display text for placeholder
        keyText.setForeground(Color.GRAY); //Placeholder text color
        keyText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
            if (new String(keyText.getPassword()).equals(PASSWORD_PLACEHOLDER)) {
                keyText.setText("");
                keyText.setEchoChar('•'); //Hide characters pag mag-type
                keyText.setForeground(Color.BLACK);
            }
        }

            @Override
            public void focusLost(FocusEvent e) {
                if (new String(keyText.getPassword()).isEmpty()) {
                    keyText.setText(PASSWORD_PLACEHOLDER);
                    keyText.setEchoChar((char) 0); //Display ang placeholder
                    keyText.setForeground(Color.GRAY);
                }
            }
        });
        add(keyText);
        
        showPasswordCheckbox();
        add(showPasswordCheckbox);
        createButton();
        add(createButton);
        enterButton(cardLayout, panelCard, frame);
        add(enterButton);
       
        //logo
        logoLabel = new JLabel(new ImageIcon("C:\\Users\\USER\\Documents\\NetBeansProjects\\ThoughtNest_MainGUI\\src\\thoughtnest_images\\"
                + "Logo.png"));
        logoLabel.setBounds(-5, -24, 350, 400);
        add(logoLabel);
        
        bgLabel = new JLabel(new ImageIcon("C:\\Users\\USER\\Documents\\NetBeansProjects\\ThoughtNest_MainGUI\\src\\thoughtnest_images\\"
                + "bg.jpg"));
        bgLabel.setBounds(0, 0, 350, 360);
        add(bgLabel);
        
        setVisible(true); 

    }
    
    public static String getPasswordString() {
        return new String(keyText.getPassword()); 
    }

    public static void clearFields(){
        anggaText.setText("");
        keyText.setText("");//pang clear lang sa password field
    }
    
    public static void showPasswordCheckbox(){
        
        ImageIcon openEyeIcon = new ImageIcon(new ImageIcon("C:\\Users\\USER\\Documents\\NetBeansProjects\\ThoughtNest_MainGUI\\src\\thoughtnest_images\\"
                + "open_eye.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        ImageIcon closedEyeIcon = new ImageIcon(new ImageIcon("C:\\Users\\USER\\Documents\\NetBeansProjects\\ThoughtNest_MainGUI\\src\\thoughtnest_images\\"
                + "close_eye.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));

        showPasswordCheckbox = new JCheckBox();
        showPasswordCheckbox.setBounds(250, 272, 26, 20);
        showPasswordCheckbox.setBackground(BACKGROUND_COLOR);
        
        showPasswordCheckbox.setIcon(closedEyeIcon);
        showPasswordCheckbox.setSelectedIcon(openEyeIcon); 
        
        showPasswordCheckbox.addActionListener(e -> {
            if (showPasswordCheckbox.isSelected()) {
                keyText.setEchoChar((char) 0);
            }else{
                keyText.setEchoChar('•');
            }
        });
    }
    
    public static void createButton(){
        createButton = new JButton("Create");
        createButton.setBounds(80, 300, 80, 25);
        mouseDuol(createButton, OGB_COLOR, DUOLMB_COLOR);

        createButton.addActionListener(e -> {
            String nickname = anggaText.getText();
            String password = new String(keyText.getPassword());

            if (nickname.isEmpty() || password.isEmpty()){
                JOptionPane.showMessageDialog(null, 
                        "Please enter both nickname and key.", 
                        "Input Error", 
                        JOptionPane.ERROR_MESSAGE);
            }else if(nickname.equals("Enter your nickname") && password.equals("Enter your key")){
                JOptionPane.showMessageDialog(null, 
                        "Please enter a proper nickname and key.", 
                        "Input Error", 
                        JOptionPane.ERROR_MESSAGE);
            }else if(userInfo.addUser(nickname, password)){
                JOptionPane.showMessageDialog(null, 
                        "Account created successfully!", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                keyText.setText("");
            }else{
                JOptionPane.showMessageDialog(null, 
                        "Nickname already exists. Try another.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
    //button to notes panel
    public static void enterButton(CardLayout cardLayout, JPanel panelCard, JFrame frame){
        enterButton = new JButton("Enter");
        enterButton.setBounds(175, 300, 80, 25);
        mouseDuol(enterButton, OGB_COLOR, DUOLMB_COLOR);

        //press this = next panel
        enterButton.addActionListener(e -> {
            String nickname = anggaText.getText();
            String key = new String(keyText.getPassword());

            if(userInfo.authenticateUser(nickname, key)){
                cardLayout.show(panelCard, "Loading Panel");

                new SwingWorker<Void, Integer>(){
                    @Override
                    protected Void doInBackground() throws Exception {
                        for (int i = 0; i <= 100; i++) {
                            Thread.sleep(45);
                            publish(i);
                        }
                    return null;
                    }

                    @Override
                    protected void process(java.util.List<Integer> chunks) {
                        int progress = chunks.get(chunks.size() - 1);
                        ((LoadingPanel) panelCard.getComponent(1)).updateProgress(progress);
                    }

                    @Override
                    protected void done() {

                        cardLayout.show(panelCard, "Notes Panel");
                        frame.setSize(1000, 600);
                        frame.setResizable(false);
                        frame.setLocationRelativeTo(null);
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
                        NotesPanel.setCurrentAccount(nickname);
                    }
                }.execute();
            }else{
                JOptionPane.showMessageDialog(frame, 
                        "Log in failed. Check your nickname and key.", 
                        "Authentication Error", 
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
    public static void mouseDuol(JButton button, Color ogColor, Color duolColor){
        button.setBackground(ogColor);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(duolColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(ogColor);
            }
        });
    }
}