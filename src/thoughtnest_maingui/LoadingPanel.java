package thoughtnest_maingui;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.ImageIcon;

public class LoadingPanel extends JPanel {
    private final JProgressBar progressBar;
    private final JLabel loadingLabel;

    public LoadingPanel(){
        setSize(350, 400);//size sa panel
        setBackground(new Color(216, 191, 165));
        setLayout(null);

        loadingLabel = new JLabel("Loading");
        loadingLabel.setBounds(150, 260, 220, 25);
        add(loadingLabel);
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setBounds(60, 280, 220, 25);
        progressBar.setForeground(new Color(120, 78, 60));
        progressBar.setBorderPainted(false);
        add(progressBar);

        JLabel logoLabel = new JLabel(new ImageIcon("C:\\Users\\USER\\Documents\\NetBeansProjects\\ThoughtNest_MainGUI\\src\\thoughtnest_images\\"
                + "Logo.png"));
        logoLabel.setBounds(-5, -24, 350, 400);
        add(logoLabel);

        JLabel bgLabel = new JLabel(new ImageIcon("C:\\Users\\USER\\Documents\\NetBeansProjects\\ThoughtNest_MainGUI\\src\\thoughtnest_images\\"
                + "bg.jpg"));
        bgLabel.setBounds(0, 0, 350, 360);
        add(bgLabel);

    }

    public void updateProgress(int value){
        progressBar.setValue(value);
        loadingLabel.setText("Loading" + ".".repeat(value / 25 % 4));
    }
    
}