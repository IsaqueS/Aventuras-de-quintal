package com.isaque.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Save {
    
    public static final String userSystem = System.getProperty("os.name");
    private static String dir, dirSave;
    private static boolean isStarted = false;
    
    private static void startSave(){
        if (!isStarted){
            if ("Windows 10".equals(userSystem) || "Windows 8".equals(userSystem) || "Windows 8.1".equals(userSystem)){
                dir = System.getProperty("user.home") + "\\AppData\\Local\\AventurasDeQuintal\\";
            } else {
                dir = System.getProperty("user.home") + "\\.AventurasDeQuintal\\";
            }
            dirSave = dir + "Save.txt";
            Save.isStarted = true;
        } else {
            return;
        }
        
    }
    
    public static void saveLevel(int level) throws IOException{
        startSave();
        
        BufferedWriter writer = null;
        try {
            File file = new File(dir);
            if (!file.exists()){
                file.mkdir();
            }
            writer = new BufferedWriter(new FileWriter(dirSave));
        } catch (IOException ex) {
            Logger.getLogger(Save.class.getName()).log(Level.SEVERE, null, ex);
        }
        //.newLine();
        writer.write(Integer.toString(level));
        writer.flush();
        writer.close();
    }
    
    public static String loadLevel() throws IOException{
        startSave();
        
        String level = "";
        File file = new File(dirSave);
        if (file.exists()){
            try {
                BufferedReader reader = new BufferedReader(new FileReader(dirSave));
                level = reader.readLine();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Save.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (level == "") level = "1";
        return level;
    }
}
