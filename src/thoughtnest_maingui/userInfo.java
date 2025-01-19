package thoughtnest_maingui;
import java.io.*;//input and output
import java.util.*;//Map and Lists
import javax.swing.JOptionPane;

public class userInfo{
    
    private static final String USER_FILE = "userData.txt";//ang txt file
    private static HashMap<String, String> users = new HashMap<>();//hashmap para sa key: nickname, value: key
    
    
    public static boolean addUser(String nickname, String key) {
        if (users.containsKey(nickname)){//aron wlay duplicate
            return false;
        }
        users.put(nickname, key);//ibutang na ang nickname and key sa users map
        NotesBTS.addUserNotes(nickname);//create na ug notes structure para sa new user
        saveUsers();
        return true;
    }
    
    public static void saveUsers(){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE))){
            
            for (String nickname : users.keySet()){
                writer.write(nickname + "✧" + users.get(nickname));
                writer.newLine();//para dli sumpay pa-right
                NotesBTS.saveNotesForUser(writer, nickname);//i-save ang ginabutang ni user nga folders and notes
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        
    }
    
    public static void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            String currentUser = null; //initialize currentUser as wla sa
            String currentFolder = null; //variable para ma-keep track ang current folder

            while ((line = reader.readLine()) != null){//check tanan lines. if line null, false, end loop
                if (line.contains("✧")) {//sa account, ✧ delimeter
                    String[] parts = line.split("✧");//split into two parts
                    if (parts.length == 2) {
                        currentUser = parts[0];
                        if (!users.containsKey(currentUser)){ //Only add if not present
                            users.put(parts[0], parts[1]);
                            NotesBTS.addUserNotes(currentUser);
                        }
                    }
                }else if(line.startsWith("[Folder]")){//sa folder
                    currentFolder = line.substring(8).trim();//Update ang currentFolder
                    if (!NotesBTS.getFolders(currentUser).contains(currentFolder)){//Check if folder exist
                        NotesBTS.addFolder(currentUser, currentFolder);
                    }
                } else if (line.contains("ᴗ")) { //sa notes
                    String[] noteParts = line.split("ᴗ", 2);
                    if (noteParts.length == 2 && currentFolder != null){
                        String title = noteParts[0].trim();
                        String content = noteParts[1].trim();
                        if (!NotesBTS.getNotesInFolder(currentUser, currentFolder).contains(title)){//wla double
                            NotesBTS.addNoteToFolder(currentUser, currentFolder, title, content);
                        }
                    }
                }
            }
        }catch(IOException e){
            JOptionPane.showMessageDialog(null,//if new, naay welcome message ug wa pay nabuhat na txt file
            "Welcome our new thought leader!",
            "Welcome",
            JOptionPane.INFORMATION_MESSAGE);
        }
    }
        
    public static boolean authenticateUser(String nickname, String key){//if ang gi-enter na nn and key is naa and match bah
        return users.containsKey(nickname) && key.equals(users.get(nickname));
    }
    
    public static boolean deleteUser(String nickname){
        if (users.containsKey(nickname)){
            users.remove(nickname);
            NotesBTS.deleteUserNotes(nickname);//after ma-delete ang account, kani is pang-delete pud sa iyahang notes
            saveUsers();//para ma-delete pati tong naa sa txt 
            return true;
        }
        return false;//nickname not found
    }
}