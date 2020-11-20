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
    public static void saveLevel(int level) throws IOException{
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter("Save.txt"));
        } catch (IOException ex) {
            Logger.getLogger(Save.class.getName()).log(Level.SEVERE, null, ex);
        }
        //.newLine();
        writer.write(Integer.toString(level));
        writer.flush();
        writer.close();
    }
    
    public static String loadLevel() throws IOException{
        String level = "";
        File file = new File("Save.txt");
        if (file.exists()){
            try {
                BufferedReader reader = new BufferedReader(new FileReader("Save.txt"));
                level = reader.readLine();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Save.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (level == "") level = "1";
        return level;
    }
}
