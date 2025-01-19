package thoughtnest_maingui;
import java.awt.Component;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class NotesBTS{
    
    private static final HashMap<String, HashMap<String, HashMap<String, String>>> userNotes = new HashMap<>();  
    //Map structure: {nickname -> {folder -> {noteTitle -> noteContent}}}

    //folder management area
    public static void addFolder(String nickname, String folderName){
        userNotes.get(nickname).put(folderName, new HashMap<>());//kwaon ang nickname nya butngag bag-ong folder
        userInfo.saveUsers();//update ang txt file
    }

    public static void deleteFolder(String nickname, String folderName){
        if (userNotes.containsKey(nickname) && userNotes.get(nickname).containsKey(folderName)){
            userNotes.get(nickname).remove(folderName); 
            userInfo.saveUsers();//update ang txt file
        }
        //if ang userNotes naay ani nga nickname AND naa syay ato nga folder sa sulod,-i-remove.
    }
    
    public static List<String> getFolders(String nickname){//i-retrieve tanan folders, para sa move button kay naay combobox
        if (!userNotes.containsKey(nickname)){//if wla daw ang nickname sa userNotes, empty nga arrayList ang ipakita
            return new ArrayList<>();
        }
        return new ArrayList<>(userNotes.get(nickname).keySet());//if naa, retrieve tanan folders ana nga account
    }
    
    
    
    
    
    //usernotes management area
    public static void addUserNotes(String nickname){//create na ug notes structure para sa new user
        userNotes.put(nickname, new HashMap<>());
    }

    public static void deleteUserNotes(String nickname){//i-remove or delete ang tanan data sa user
        userNotes.remove(nickname);
    }
    
    //notes management area
    public static void addNoteToFolder(String nickname, String folder, String title, String content){
        if (folder == null || folder.trim().isEmpty()) {
            return; //Prevent add notes to null or empty folder
        }
        if (!userNotes.containsKey(nickname)){//if wla pa, create-an ug bag-o
            userNotes.put(nickname, new HashMap<>());
        }
        HashMap<String, HashMap<String, String>> sections = userNotes.get(nickname);
        if (!sections.containsKey(folder)){
            sections.put(folder, new HashMap<>());
        }
        sections.get(folder).put(title, content);
        userInfo.saveUsers();//update txt file
    }

    public static void addNoteToLastFolder(String nickname, String title, String content){
        HashMap<String, HashMap<String, String>> sections = userNotes.get(nickname);
        String lastFolder = sections.keySet().stream().reduce((first, second) -> second).orElse(null);
        if (lastFolder != null) {
            sections.get(lastFolder).put(title, content);
        }
    }
    
    public static void deleteNoteFromFolder(String nickname, String folder, String title){
        if (userNotes.containsKey(nickname) && userNotes.get(nickname).containsKey(folder)){
        userNotes.get(nickname).get(folder).remove(title);
        }
    }

    public static List<String> getNotesInFolder(String nickname, String folder){//return tanan notes specific folder
        if (userNotes.containsKey(nickname) && userNotes.get(nickname).containsKey(folder)){
            return new ArrayList<>(userNotes.get(nickname).get(folder).keySet());
        }
        return new ArrayList<>();
    }

    public static String getNoteContent(String nickname, String folder, String title){//return notes content specific folder
        if (userNotes.containsKey(nickname) && userNotes.get(nickname).containsKey(folder)){//check if exist ang folder
            return userNotes.get(nickname).get(folder).get(title);
        }
        return null;
    }
    
    
    
    
    
    //saving and loading area
    //tigsulat sa txt file
    public static void saveNotesForUser(BufferedWriter writer, String nickname) throws IOException{//gamit sa userInfo.saveUsers
        HashMap<String, HashMap<String, String>> sections = userNotes.get(nickname);//nickname and its folders and notes
        for (String folder : sections.keySet()){
            writer.write("[Folder] " + folder); 
            writer.newLine();//para di magsumpay
            HashMap<String, String> notes = sections.get(folder);//folder and its note title and content
            for (String title : notes.keySet()) {
                writer.write(title + "ᴗ" + notes.get(title));  
                writer.newLine();//para di magsumpay
            }
        }
    }
    
    public static void loadFoldersAndNotes(){//load tanan folders and notes sa UI
        refreshFolders();
        if (!NotesPanel.folderListModel.isEmpty()){//if not empty. executes rag naay folders available i-display
            String firstFolder = NotesPanel.folderListModel.get(0);
            refreshNotes(firstFolder);
        }
    }
    
    
    
    
    
    //UI management
    public static void refreshFolders(){//to make sure updated permi ang folderListModel
        NotesPanel.folderListModel.clear();//i-clear tanan naa sa model
        List<String> folders = getFolders(NotesPanel.currentAccount);//kwaon tanan folders sa currentaccount
        for (String folder : folders){
            NotesPanel.folderListModel.addElement(folder);//i-add na pud tanan nga naa lang sa list
        }
    }
    
    public static void refreshNotes(String folder){//to make sure updated permi ang noteListModel
        NotesPanel.noteListModel.clear();//i-clear tanan naa sa model
        List<String> notes = getNotesInFolder(NotesPanel.currentAccount, folder);//kwaon tanan notes sa currentaccount
        for (String noteTitle : notes){
            NotesPanel.noteListModel.addElement(noteTitle);//i-add na pud tanan nga naa lang sa list
        }
    }
    
    public static void filterFolderList(){
        String searchText = NotesPanel.searchFolderField.getText().toLowerCase().trim();
        NotesPanel.folderListModel.clear();//wla tanan lists after mag-type, ang naa lang is ang mu-satisfy sa next nga code
        for (String folder : getFolders(NotesPanel.currentAccount)){//kwaon tanan folders, sulod sa loop
            if (folder.toLowerCase().contains(searchText)){//if ang letter na type is naa sa folder, show element sa model
                NotesPanel.folderListModel.addElement(folder);
            }
        }
    }
    
    public static void filterNotes(){
        String searchText = NotesPanel.searchNoteField.getText().toLowerCase().trim();
        NotesPanel.noteListModel.clear();
        Map<String, List<String>> allNotes = /*(Map<String, List<String>>)*/ getAllNotesForCurrentUser();
        for (Map.Entry<String, List<String>> entry : allNotes.entrySet()){
            String folder = entry.getKey();
            List<String> notesInFolder = entry.getValue();
        
            for (String note : notesInFolder){
                if (note.toLowerCase().contains(searchText)){
                    NotesPanel.noteListModel.addElement(note + " (Nest: " + folder + ")");
                }
            }
        }
    }
    
    
    
    
    
    //utilities area
    private static Map<String, List<String>> getAllNotesForCurrentUser(){//kwaon lang tanan notes
        Map<String, List<String>> allNotes = new HashMap<>();
        List<String> folders = getFolders(NotesPanel.currentAccount); 

        for (String folder : folders) {
            List<String> notesInFolder = getNotesInFolder(NotesPanel.currentAccount, folder); 
            allNotes.put(folder, notesInFolder); 
        }
        return allNotes; 
    }
    
    //confirm password sa delete account sa NotesPanel nga class
    public static boolean confirmPasswordDeleteAcc(String expectedPassword, Component parentComponent){
        JPasswordField passwordField = new JPasswordField();
        int option = JOptionPane.showConfirmDialog(parentComponent, passwordField, 
            "Re-enter your password to confirm", 
            JOptionPane.OK_CANCEL_OPTION, 
            JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String enteredPassword = new String(passwordField.getPassword());
            return enteredPassword.equals(expectedPassword);
        }
        return false;
    }
    
    public static void deleteToMoveNote(){
        String selectedNote = NotesPanel.notesList.getSelectedValue();
        String selectedFolder = NotesPanel.folderList.getSelectedValue();

        if (selectedNote != null && selectedFolder != null){
            int confirmation = JOptionPane.showConfirmDialog(null, 
                "Are you sure you want to move the note: \"" + selectedNote + "\"?", 
                "Move Note", 
                JOptionPane.YES_NO_OPTION);
        
            if (confirmation == JOptionPane.YES_OPTION){
                deleteNoteFromFolder(NotesPanel.currentAccount, selectedFolder, selectedNote);
                NotesBTS.refreshNotes(selectedFolder);//aron mawala tong gi-delete nga note sa model, refres
            }
        }
        userInfo.saveUsers();
    }
}