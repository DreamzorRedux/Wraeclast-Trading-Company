/*
 * This is a simple class that extracts the config file variables and put them
 * in a list for future use.
 */
package wtc;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jean-Luc Poissonnet alias Dreamzor
 */
public class fileOutput {
    List<String> fileOutputList = new ArrayList<>();
    
    public List<String> getVariables(){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream("wtcConfig.txt"), "UTF-8"));
            
            String line = "temp";
          
            while (line != null){
                line = br.readLine();
                fileOutputList.add(line);
            }
        }
        catch (IOException ex) {
            System.out.println(ex);
        }             
        return fileOutputList;
    }   
}
