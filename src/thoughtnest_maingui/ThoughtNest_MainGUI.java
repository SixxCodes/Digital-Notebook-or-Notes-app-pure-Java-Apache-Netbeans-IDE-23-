package thoughtnest_maingui;
import java.awt.CardLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ThoughtNest_MainGUI{

    public static void main(String[] args){
        //ipang-load ang mga na-save na users
        userInfo.loadUsers();
        
        //frame
        JFrame frame = new JFrame();//frame
        frame.setTitle("ThoughtNest");//title(top left sa frame)
        frame.setSize(350, 400);//size
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//close tibuok program if exit
        frame.setResizable(false);//dli pwede ma-resize ang window
        frame.setLocationRelativeTo(null);//naa sa tunga sa screen and window if i-run

        //app icon sa frame
        ImageIcon appIcon = new ImageIcon("C:\\Users\\USER\\Documents\\NetBeansProjects\\ThoughtNest_MainGUI\\src\\thoughtnest_images\\"
                + "icon.jpg");//path sa image
        frame.setIconImage(appIcon.getImage());
        
        //cards
        CardLayout cardLayout = new CardLayout();
        JPanel panelCard = new JPanel(cardLayout);
        
        LoginPanel loginPanel = new LoginPanel(cardLayout, panelCard, frame);//instash8 gikan pikas class
        LoadingPanel loadingPanel = new LoadingPanel();
        NotesPanel notesPanel = new NotesPanel(frame, cardLayout, panelCard);//instash8 gikan pikas class
        //instansh8 sila para sad mabutang sa cards, ug para mabutang sa main
        
        panelCard.add(loginPanel, "LogIn Panel");
        panelCard.add(loadingPanel, "Loading Panel");
        panelCard.add(notesPanel, "Notes Panel");
        frame.add(panelCard);
        
        //pakita
        frame.setVisible(true);
    }
}