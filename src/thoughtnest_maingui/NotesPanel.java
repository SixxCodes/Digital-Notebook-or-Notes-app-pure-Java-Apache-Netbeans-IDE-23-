package thoughtnest_maingui;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;

public class NotesPanel extends JPanel{
    private static Border lineBorder;

    private static JPanel folderPanelLeft;
    private static JPanel notesPanelLeft;
    
    public static String currentAccount;
    private static CardLayout cardLayout;
    private static JPanel panelCard;
    
    public static JTextField searchFolderField;
    public static DefaultListModel<String> folderListModel;
    public static JList<String> folderList;
    
    public static JTextField searchNoteField;
    public static DefaultListModel<String> noteListModel;
    public static JList<String> notesList;
    
    private static JTextField titleField = new JTextField();
    private static JTextArea contentArea = new JTextArea();
    private static JPanel contentPanel;
    
    private static final Color BACKGROUND_COLOR = new Color(216, 191, 165);
    private static final Color OGB_COLOR = new Color(120, 78, 60);
    private static final Color DUOLMB_COLOR = new Color(169, 125, 97);

    public NotesPanel(JFrame frame, CardLayout cardLayout, JPanel panelCard){
        this.cardLayout = cardLayout;
        this.panelCard = panelCard;
        
        setSize(1000, 600);
        setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout());

        titleField = new JTextField();
        contentArea = new JTextArea();

        folderPanel();
        add(folderPanelLeft);
        notesPanelLeft();
        add(notesPanelLeft);
        
        //panel para ma-add ang duha ka-panel(folder,notes)
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(folderPanelLeft);
        leftPanel.add(notesPanelLeft);
        add(leftPanel);
        
        JLabel emptyLabel = new JLabel();
        JLabel textLabel = new JLabel("Your thoughts will appear here. Create a new one!", 
                SwingConstants.CENTER);
        textLabel.setFont(new Font("Comic Sans MS", Font.BOLD,18));
        textLabel.setBounds(27, 80, 600, 400);

        JLabel logoLabel = new JLabel(new ImageIcon("C:\\Users\\USER\\Documents\\NetBeansProjects\\ThoughtNest_MainGUI\\src\\thoughtnest_images\\"
                + "Logo.png"));
        logoLabel.setBounds(150, 50, 350, 400);

        JLabel emptyBg = new JLabel(new ImageIcon("C:\\Users\\USER\\Documents\\NetBeansProjects\\ThoughtNest_MainGUI\\src\\thoughtnest_images\\"
                + "emptyBg.png"));
        emptyBg.setBounds(0, 0, 664, 580);
        
        emptyLabel.add(textLabel);
        emptyLabel.add(logoLabel);
        emptyLabel.add(emptyBg);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(emptyLabel, BorderLayout.CENTER);
        contentPanel.setBackground(BACKGROUND_COLOR);
        add(contentPanel);

        //splitpane sa left and right
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
                leftPanel, 
                contentPanel);
        splitPane.setDividerLocation(340);
        add(splitPane, BorderLayout.CENTER);

        NotesBTS.loadFoldersAndNotes();
    }
    
    private static JButton addFolderButton;
    private static JButton editFolderButton;
    private static JButton deleteFolderButton;
    
    public static void folderPanel(){
        //mismong panel sa folder sa left dapit
        folderPanelLeft = new JPanel(new BorderLayout());
        lineBorder = BorderFactory.createLineBorder(OGB_COLOR, 2); 
        folderPanelLeft.setBorder(BorderFactory.createTitledBorder(lineBorder,"Nests"));
        folderPanelLeft.setBackground(BACKGROUND_COLOR);
        
        //search: north
        String SEARCHFOLDER_PLACEHOLDER = "Search Nest...";
        searchFolderField = new JTextField(SEARCHFOLDER_PLACEHOLDER);
        searchFolderField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                NotesBTS.filterFolderList();
            }
        });
        addPlaceholderBehavior(searchFolderField, SEARCHFOLDER_PLACEHOLDER);
        folderPanelLeft.add(searchFolderField, BorderLayout.NORTH);

        //list model: center
        folderListModel = new DefaultListModel<>();
        folderList = new JList<>(folderListModel);
        folderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        folderList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()){//dapat wla ga-adjust ang click, if click release: final
                String selectedFolder = folderList.getSelectedValue();
                if (selectedFolder != null) {
                NotesBTS.refreshNotes(selectedFolder);
                }
            }
        });
        folderPanelLeft.add(new JScrollPane(folderList), BorderLayout.CENTER);

        //panel para sa buttons sa folder lang dapit: north
        JPanel folderButtonPanel = new JPanel();
        folderButtonPanel.setBackground(BACKGROUND_COLOR);

        //tanan buttons naa sa folderButtonPanel
        addFolderButton();
        folderButtonPanel.add(addFolderButton);
        editFolderButton();
        folderButtonPanel.add(editFolderButton);
        deleteFolderButton();
        folderButtonPanel.add(deleteFolderButton);
        folderPanelLeft.add(folderButtonPanel, BorderLayout.SOUTH);
        
        JPanel leftMargin = new JPanel();
        leftMargin.setPreferredSize(new Dimension(10, 0));
        leftMargin.setBackground(BACKGROUND_COLOR);
        
        JPanel rightMargin = new JPanel();
        rightMargin.setPreferredSize(new Dimension(10, 0));
        rightMargin.setBackground(BACKGROUND_COLOR);

        folderPanelLeft.add(leftMargin,BorderLayout.WEST);
        folderPanelLeft.add(rightMargin,BorderLayout.EAST);
        
    }
    
    public static void addFolderButton(){
        addFolderButton = new JButton("Add Nest");
        addFolderButton.setBounds(175, 300, 80, 25);
        mouseDuol(addFolderButton, OGB_COLOR, DUOLMB_COLOR);
        addFolderButton.addActionListener(e -> {
            String folderName = JOptionPane.showInputDialog("Enter new nest name:");
            if (folderName != null && !folderName.trim().isEmpty()){
                NotesBTS.addFolder(currentAccount, folderName.trim());
                NotesBTS.refreshFolders();//update and model
            }else{
                JOptionPane.showMessageDialog(null, 
                "Please input a proper name.", 
                "Empty Nest Name", 
                JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
    public static void editFolderButton(){
        editFolderButton = new JButton("Edit Nest Title");
        editFolderButton.setBounds(175, 300, 80, 25);
        mouseDuol(editFolderButton, OGB_COLOR, DUOLMB_COLOR);
        editFolderButton.addActionListener(e -> {
            String selectedFolder = folderList.getSelectedValue();
            if (selectedFolder != null){
                String newFolderName = JOptionPane.showInputDialog(
                        "Enter new nest name:", 
                        selectedFolder);
                if (newFolderName != null && !newFolderName.trim().isEmpty()){
                    //kwaon tanan notes sa oldnamefolder, ibutang sa newnamefolder
                    List<String> notesInOldFolder = NotesBTS.getNotesInFolder(currentAccount, selectedFolder);
                    NotesBTS.addFolder(currentAccount, newFolderName);
            
                        for (String noteTitle : notesInOldFolder){
                            String noteContent = NotesBTS.getNoteContent(
                                currentAccount, 
                                selectedFolder, 
                                noteTitle);
                            NotesBTS.addNoteToFolder(
                                currentAccount, 
                                newFolderName, 
                                noteTitle, 
                                noteContent);
                        }
                    NotesBTS.deleteFolder(currentAccount, selectedFolder); 
                    folderListModel.removeElement(selectedFolder); 
                    folderListModel.addElement(newFolderName); 
                }else{
                    JOptionPane.showMessageDialog(null, 
                        "Please enter a proper name.");
                }
            }else{
                JOptionPane.showMessageDialog(null, 
                        "Please select a nest to edit.");
            }
        });

    }
    
    public static void deleteFolderButton(){
        deleteFolderButton = new JButton("Delete Nest");
        deleteFolderButton.setBounds(175, 300, 80, 25);
        mouseDuol(deleteFolderButton, OGB_COLOR, DUOLMB_COLOR);
        deleteFolderButton.addActionListener(e -> {
            String selectedFolder = folderList.getSelectedValue();
            if (selectedFolder != null) {
                int confirm = JOptionPane.showConfirmDialog(null, 
                "Are you sure you want to delete this nest?", 
                "Delete Nest", 
                JOptionPane.YES_NO_OPTION);
        
                if (confirm == JOptionPane.YES_OPTION){
                    NotesBTS.deleteFolder(currentAccount, selectedFolder); 
                    folderListModel.removeElement(selectedFolder);
                }
            }else{
                JOptionPane.showMessageDialog(null, 
                        "Please select a nest to delete.");
            }
        });
    }

    public static void notesPanelLeft(){
        //mismong panel sa notes sa left dapit
        notesPanelLeft = new JPanel(new BorderLayout());
        lineBorder = BorderFactory.createLineBorder(OGB_COLOR, 2); 
        notesPanelLeft.setBorder(BorderFactory.createTitledBorder(lineBorder,"Thoughts"));
        notesPanelLeft.setBackground(BACKGROUND_COLOR);

        //search: north
        String SEARCHTHOUGHT_PLACEHOLDER = "Search All Thoughts...";
        searchNoteField = new JTextField(SEARCHTHOUGHT_PLACEHOLDER);
        searchNoteField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                NotesBTS.filterNotes();
            }
        });
        addPlaceholderBehavior(searchNoteField, SEARCHTHOUGHT_PLACEHOLDER);
        notesPanelLeft.add(searchNoteField, BorderLayout.NORTH);

        //list model: center
        noteListModel = new DefaultListModel<>();
        notesList = new JList<>(noteListModel);
        notesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        notesList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedNote = notesList.getSelectedValue();
                if (selectedNote != null) {
                    String content = NotesBTS.getNoteContent(
                        currentAccount, 
                        folderList.getSelectedValue(), 
                        selectedNote);
                    contentArea.setText(content != null ? content : "");//ternary conditional operator
                    titleField.setText(selectedNote);
                }
            }
            showNoteEditor();
        });
        notesPanelLeft.add(new JScrollPane(notesList), BorderLayout.CENTER);
        
        String TITLEFIELD_PLACEHOLDER  = "Thought Title Here";
        String TEXTAREA_PLACEHOLDER = "Enter Thought Content Here";
        
        //add note button: south
        JButton addNoteButton = new JButton("Add New Thought");
        mouseDuol(addNoteButton, OGB_COLOR, DUOLMB_COLOR);
        addNoteButton.addActionListener(e -> {
            titleField = new JTextField(TITLEFIELD_PLACEHOLDER);
            addPlaceholderBehavior(titleField, TITLEFIELD_PLACEHOLDER);
     
            contentArea = new JTextArea(TEXTAREA_PLACEHOLDER);
            addPlaceholderBehavior(contentArea, TEXTAREA_PLACEHOLDER);
            
            showNoteEditor(); 
        });
        notesPanelLeft.add(addNoteButton, BorderLayout.SOUTH); 
        
        JPanel leftMargin = new JPanel();
        leftMargin.setPreferredSize(new Dimension(10, 0));
        leftMargin.setBackground(BACKGROUND_COLOR);
        
        JPanel rightMargin = new JPanel();
        rightMargin.setPreferredSize(new Dimension(10, 0));
        rightMargin.setBackground(BACKGROUND_COLOR);

        notesPanelLeft.add(leftMargin,BorderLayout.WEST);
        notesPanelLeft.add(rightMargin,BorderLayout.EAST);
        
    }
    
    private static JButton saveButton;
    private static JButton deleteButton;
    private static JButton moveButton;
    
    public static void showNoteEditor(){
        contentPanel.removeAll();
    
        JLabel leftBg = new JLabel(new ImageIcon("C:\\Users\\USER\\Documents\\NetBeansProjects\\ThoughtNest_MainGUI\\src\\thoughtnest_images\\"
                + "left.png"));
        leftBg.setBounds(0, 0, 300, 20);

        JPanel leftMargin = new JPanel();
        leftMargin.setPreferredSize(new Dimension(60, 0));
        leftMargin.setBackground(new Color(232,217,202));
        leftMargin.add(leftBg);

        JLabel rightBg = new JLabel(new ImageIcon("C:\\Users\\USER\\Documents\\NetBeansProjects\\ThoughtNest_MainGUI\\src\\thoughtnest_images\\"
                + "right.png"));
        rightBg.setBounds(0, 0, 300, 20);
        
        JPanel rightMargin = new JPanel();
        rightMargin.setPreferredSize(new Dimension(60, 0));
        rightMargin.setBackground(new Color(232,217,202));
        rightMargin.add(rightBg);

        titleField.setFont(new Font("Comic Sans MS", Font.BOLD, 25));
        titleField.setPreferredSize(new Dimension(0, 60));
        titleField.setHorizontalAlignment(JTextField.CENTER);
        titleField.setBackground(new Color(232,217,202));
        titleField.setBorder(new EmptyBorder(5, 5, 5, 5));

        contentArea.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);

        contentPanel.add(leftMargin, BorderLayout.WEST);
        contentPanel.add(rightMargin, BorderLayout.EAST);
        contentPanel.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        contentPanel.add(titleField, BorderLayout.NORTH);

        //buttons para sa tanan panel sa shownoteeditor
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(187,142,123));

        deleteButton = new JButton("Delete Thought");
        mouseDuol(deleteButton, OGB_COLOR, DUOLMB_COLOR);
        deleteButton.addActionListener(e -> {deleteButton();});

        //settings
        JButton settingsButton = new JButton(new ImageIcon("C:\\Users\\USER\\Documents\\NetBeansProjects\\ThoughtNest_MainGUI\\src\\thoughtnest_images\\brown-gear-icon-hi.png"));//picture sa settings
        settingsButton.setBackground(BACKGROUND_COLOR);
        settingsButton.setBorderPainted(false);
        settingsButton.setPreferredSize(new Dimension(25, 25));

        JPopupMenu settingsMenu = new JPopupMenu();
        
        JMenuItem logoutOption = new JMenuItem("Logout");
        logoutOption.setBackground(BACKGROUND_COLOR);
        logoutOption.addActionListener(e -> {
            LoginPanel.clearFields();//empty tanan textcomponents
            cardLayout.show(panelCard, "LogIn Panel");//pakita balik ang loginPanel
            
            returnOriginalLogin();//para mubalik sa loginpanel size and position

        });
        
        JMenuItem deleteAccountOption = new JMenuItem("Delete Account");
        deleteAccountOption.setBackground(BACKGROUND_COLOR);
        deleteAccountOption.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(
            contentPanel,
            "Are you sure you want to delete your account?",
            "Delete Account",
            JOptionPane.YES_NO_OPTION
            );
            if (response == JOptionPane.YES_OPTION){
                if (NotesBTS.confirmPasswordDeleteAcc(LoginPanel.getPasswordString(), contentPanel)){ 
                    userInfo.deleteUser(currentAccount);
                    LoginPanel.clearFields();
                    cardLayout.show(panelCard, "LogIn Panel");
                    returnOriginalLogin();//para mubalik sa loginpanel size and position
                    JOptionPane.showMessageDialog(contentPanel, 
                        "Create new account to start your new journey!",
                        "Account deleted successfully!",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(contentPanel, 
                    "Password incorrect. Account not deleted.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        settingsMenu.add(logoutOption);
        settingsMenu.add(deleteAccountOption);

        settingsButton.addActionListener(e -> settingsMenu.show(settingsButton, 0, settingsButton.getHeight()));
        
        saveButton();
        buttonPanel.add(saveButton);
        buttonPanel.add(deleteButton);
        moveButton();
        buttonPanel.add(moveButton);
        buttonPanel.add(settingsButton);
        
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.setVisible(true);

        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    public static void returnOriginalLogin(){
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(contentPanel);
        frame.setSize(350, 400);
        frame.setLocationRelativeTo(null);
    }
    
    public static void saveButton(){
        saveButton = new JButton("Save Thought");
        mouseDuol(saveButton, OGB_COLOR, DUOLMB_COLOR);
        
        saveButton.addActionListener(e -> {
            String newTitle = titleField.getText().trim();
            String content = contentArea.getText().trim();
            String selectedFolder = folderList.getSelectedValue();

            if (!newTitle.isEmpty() && selectedFolder != null){
                String oldTitle = notesList.getSelectedValue();

                if (oldTitle != null && !oldTitle.equals(newTitle)){
                    NotesBTS.deleteNoteFromFolder(currentAccount, selectedFolder, oldTitle);
                }
                NotesBTS.addNoteToFolder(currentAccount, selectedFolder, newTitle, content);
                JOptionPane.showMessageDialog(null, 
                    "Thought saved!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                NotesBTS.refreshNotes(selectedFolder);
            }else{
                JOptionPane.showMessageDialog(null, 
                    "Please create or select a nest first.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
    public static void deleteButton(){
        String selectedNote = notesList.getSelectedValue();
        String selectedFolder = folderList.getSelectedValue();

        if (selectedNote != null && selectedFolder != null){
            int confirmation = JOptionPane.showConfirmDialog(null, 
                "Are you sure you want to delete the note: " + selectedNote + "?", 
                "Delete Note", 
                JOptionPane.YES_NO_OPTION);
        
            if (confirmation == JOptionPane.YES_OPTION){
                    
                NotesBTS.deleteNoteFromFolder(currentAccount, selectedFolder, selectedNote);
                NotesBTS.refreshNotes(selectedFolder);
                userInfo.saveUsers();
            }
        }else{
            JOptionPane.showMessageDialog(null, "Please select a thought to delete.");
        }
    }
    
    public static void moveButton(){
        moveButton = new JButton("Move Thought");
        mouseDuol(moveButton, OGB_COLOR, DUOLMB_COLOR);
        
        moveButton.addActionListener((ActionEvent e) -> {
            String selectedNote = notesList.getSelectedValue();
            String currentFolder = folderList.getSelectedValue();
            
            if (selectedNote != null && currentFolder != null){
                String[] folders = NotesBTS.getFolders(currentAccount).toArray(new String[0]);//kwaon tanan folders
                JComboBox<String> folderComboBox = new JComboBox<>(folders);//ibutang combobox ang gikuha na all folders
                
                int result = JOptionPane.showConfirmDialog(
                        null,
                        folderComboBox,
                        "Move Thought to which Nest?",
                        JOptionPane.OK_CANCEL_OPTION
                );
                
                if (result == JOptionPane.OK_OPTION){
                    String selectedFolder = (String) folderComboBox.getSelectedItem();
                    if (selectedFolder != null){
                        String noteContent = contentArea.getText();
                        String noteTitle = titleField.getText();
                        
                        NotesBTS.addNoteToFolder(
                                currentAccount,
                                selectedFolder,
                                noteTitle,
                                noteContent);
                        NotesBTS.getNotesInFolder(
                                currentAccount,
                                currentFolder).remove(selectedNote);
                        userInfo.saveUsers();
                        NotesBTS.deleteToMoveNote();
                        NotesBTS.refreshNotes(selectedFolder);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null,
                        "Please select a thought and a nest to move to.");
            }
        });
    }
    
    private static void addPlaceholderBehavior(JTextComponent textComponent, String placeholder){
    //textcomponent: superclass sa duha
        textComponent.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textComponent.getText().equals(placeholder)) {
                    textComponent.setText("");
                    textComponent.setForeground(new Color(86, 56, 44));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textComponent.getText().isEmpty()) {
                    textComponent.setText(placeholder);
                    textComponent.setForeground(new Color(86, 56, 44));
                }
            }
        });
    }

    public static void mouseDuol(JButton button, Color ogColor, Color duolColor){
        button.setBackground(ogColor);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent e){
                button.setBackground(duolColor);
            }

            @Override
            public void mouseExited(MouseEvent e){
                button.setBackground(ogColor);
            }
        });
    }

    public static void setCurrentAccount(String nickname){
        NotesPanel.currentAccount = nickname;
        NotesBTS.loadFoldersAndNotes();
    }
}