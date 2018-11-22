/*
 * This program is meant to facilitate trading in Path of Exile. It is made so
 * it doesn't break the terms of service: 1 button click performs 1 server side
 * action.
 * 
 * To summarize, it reads the Client.txt line by line realtime during gameplay
 * and detects strings that are specific to trade requests. The application
 * window pops up and the user can click a button to send 1 chat message
 * (a thank you message, /invite, /kick, etc) via keypress emulation.
 */

package wtc;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author Jean-Luc Poissonnet alias Dreamzor
 */
public class WTC {

    /**
     * @param args the command line arguments
     */
    static mainFrame mainWindow;
    
    static String buyerName;
    static String itemName;
    static String itemPos;
    static String itemPrice;
    
    static String clientLocation;
    static String waitMsg;
    static String soldMsg;
    static String thxMsg;
    
    static int intNewPanelName = 0;
    
    static String activeCardName;
    static String strNewPanelName = "0";
    
    
    public static void main(String[] args) {
        //Variables
        Boolean found1;
        Boolean found2;
        Boolean found3;
        
        String line = "temp";
        String tradeText1 = "Hi, I would like to buy your";
        String tradeText2 = "Hi, I'd like to buy your";
               
        //Show main frame
        mainWindow = new mainFrame();
        mainWindow.setFocusableWindowState(false);
        mainWindow.setVisible(true);

        
        //Show tray icon
        try {
        URL url = WTC.class.getResource("trayImg.png");
        Image img = ImageIO.read(url);
        
        SystemTray tray = SystemTray.getSystemTray();
        TrayIcon trayIcon = new TrayIcon(img, "Wraeclast Trading Company");
        
        PopupMenu popup = new PopupMenu();
        MenuItem show = new MenuItem("Show");
        show.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showActionPerformed(evt);
            }
        });
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitItemActionPerformed(evt);
            }
        });        
        
        popup.add(show);
        popup.add(exitItem);
        
        trayIcon.setPopupMenu(popup);
        trayIcon.setImageAutoSize(true);
        tray.add(trayIcon);
        }
        catch (IOException | AWTException ex) {
            System.out.println("Error with tray img\n" + ex);
        }
        
        //Verify if wtcConfig file exists and take action if not
        File configFile = new File("wtcConfig.txt");
        if (configFile.exists() == false){
            try{
                PrintWriter writeText = new PrintWriter("wtcConfig.txt", "UTF-8");
                writeText.println();
                writeText.println("Can you wait 1m plz?");
                writeText.println("Sold sry");
                writeText.println("Tyvm :)");
                writeText.close();
            }
            catch (IOException ex) {
                System.out.println(ex);
            }
            
            new optionFrame().setVisible(true);
            
            //Pausing thread while Client.txt path isn't found.
            clientLocation = "";
            while (clientLocation.isEmpty() == true){
                try{
                Thread.sleep(500);
                fileOutput fo = new fileOutput();
                List<String> fileOutputList = fo.getVariables();
                clientLocation = fileOutputList.get(0);
                }
                catch (InterruptedException ex){
                System.out.println("Something went wrong at: while (clientLocation.isEmpty() == true)");
                }     
            } 
        }
        
        //Get variables from wtcConfig.txt
        fileOutput fo = new fileOutput();
        List<String> fileOutputList = fo.getVariables();
        clientLocation = fileOutputList.get(0);
        waitMsg = fileOutputList.get(1);
        soldMsg = fileOutputList.get(2);
        thxMsg = fileOutputList.get(3);
        
              
        //Client.txt reading
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(clientLocation), "UTF-8"));
            
            //Go to the end of Client.txt
            while (line != null){
            line = br.readLine();  
            }
            
            //Real-time reading of new lines
            while (true) {
                line = br.readLine();
                if (line == null) {
                        Thread.sleep(100);
                }
                else {                 
                    found1 = line.contains(tradeText1);
                    found2 = line.contains(tradeText2);
                    found3 = line.contains("@From");
                    
                    //If trade request detected
                    if ((found1 == true && found3 == true) || (found2 == true && found3 == true)){
                        
                        //Get seller name depending on guild tag or no guild tag
                        if (line.contains("> ") == true)
                        {
                                buyerName = line.substring((line.indexOf("> ")+2), line.indexOf(": Hi"));
                        }
                        else
                        {
                                buyerName = line.substring((line.indexOf("@From")+6), line.indexOf(": Hi"));
                        }
                        
                        //Extract variables from item trade text
                        if (found1 == true)	{

                                //Item name
                                if (line.contains(" (T") == true) {
                                        //If a map item
                                        itemName = line.substring((line.indexOf("to buy your ")+12), line.indexOf(" (T"));
                                }
                                else {
                                        //If not a map item
                                        itemName = line.substring((line.indexOf("to buy your ")+12), line.indexOf(" listed for "));
                                }

                                //Item price
                                itemPrice = line.substring((line.indexOf("listed for ")+11), line.indexOf(" in "));

                                //Item position
                                itemPos = line.substring((line.indexOf("tab \"")+5), line.indexOf("\";"));
                        }

                        //Extract variables from bulk trade text
                        else {		  
                                if (line.contains(" (T") == true) {
                                        //Map name
                                        itemName = line.substring((line.indexOf("to buy your ")+12), line.indexOf(" (T"));
                                }
                                else {
                                        //Currency name
                                        itemName = line.substring((line.indexOf("to buy your ")+12), line.indexOf(" for "));
                                }

                                //Item price
                                itemPrice = line.substring((line.indexOf("for my ")+7), line.indexOf(" in "));

                                //Item position
                                itemPos = " ";
                        }                        
                        
                        //Popup WTC window without being focused
                        
                        mainWindow.setAlwaysOnTop(true);
                        
                        //Create new trade panel
                        mainWindow.addCard();
                        
                        
                    }  
                }  
            }
        }
        catch (UnsupportedEncodingException ex) {
            System.out.println("Problem with Client.txt: encoding exception");
        }
        catch (FileNotFoundException ex) {
            System.out.println("Problem with Client.txt: file not found");
        }
        catch (IOException ex) {
            System.out.println("Problem while reading Client.txt : IOException");
        }
        catch (InterruptedException ex) {
            System.out.println("Problem while moniroting Client.txt: InterruptedException");
        }
    }
    
    static void showActionPerformed(ActionEvent evt){
        mainWindow.toFront();
    }
    static void exitItemActionPerformed(ActionEvent evt){
        System.exit(0);
    }
}