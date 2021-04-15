/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import org.jsoup.Jsoup;

/**
 *
 * Copyright 2017 Nathan Rais 
 * 
 *      This file is part of The Easy Survey Creator.
 *
 *   The Easy Survey Creator by Nathan Rais is free software 
 *   but is licensed under the terms of the Creative Commons 
 *   Attribution NoDerivatives license (CC BY-ND). Under the CC BY-ND license 
 *   you may redistribute this as long as you give attribution and do not 
 *   modify any part of this software in any way.
 *
 *   The Easy Survey Creator is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   CC BY-ND license for more details.
 *
 *   You should have received a copy of the CC BY-ND license along with 
 *   The Easy Survey Creator. If not, see <https://creativecommons.org/licenses/by-nd/4.0/>.
 *
 */
public class AnswerFrame extends javax.swing.JFrame {
    
    // -- //
    MainStartup MS = new MainStartup();
    public String version = MS.version; //THIS REALLY SHOULD BE CHANGED EACH NEW VERSION
    // -- //
    
    public boolean going = true;
    public boolean allS = false;
    public boolean allU = false;
    public String surveyToGet = "";
    public String userNameVar = "";
    
    public String theDate = "";
    
    public int numberOfUsers = 0;
    
    public String fileToLoad = "";
    
    public boolean SearchByUserName = true;
    public boolean SearchBySurveyName = false;
    
    String CurrentTextString = "";
    
    String HTML = "<html>"; //FieldUserInfo.getText().substring(0, FieldUserInfo.getText().indexOf("<body>") + 6);
    
    // the rows and columns store repersent the questions and answeres, they store how many times they were answered
    int[][] summaryTally = new int[100][6]; // basically the matrix looks like this for each survey [question][answer]
    
    // the rows and columns store repersent the questions and answeres, they store what each question is and what the answers are
    String[][] summaryMatrix = new String[100][6]; // basically the matrix looks like this for each survey [question][answer]
    
    //      ?   A  B  C  D  E
    //  Q1
    //  Q2
    //  Q3
    //  Q4
    //  Q...
    
    
    
    /**
     * Creates new form AnswerFrame
     */
    public AnswerFrame() {
        initComponents();
        
        MainStartup main = new MainStartup();
        
        versionLabel.setText("Copyright NathanSoftware.com version " + main.version);
        
        Container c = getContentPane();               
        c.setBackground(Color.white);
        
        setLocationRelativeTo(null);
        
        //FieldUserInfo.setContentType("text/html");
        
        GoButton.setContentAreaFilled(false);
        GoButton.setBorderPainted(false);
        GoButton.setFocusPainted(false);
        GoButton.setOpaque(false);
        
        ButtonReturn.setContentAreaFilled(false);
        ButtonReturn.setBorderPainted(false);
        ButtonReturn.setFocusPainted(false);
        ButtonReturn.setOpaque(false);
        
        HelpButton.setContentAreaFilled(false);
        HelpButton.setBorderPainted(false);
        HelpButton.setFocusPainted(false);
        HelpButton.setOpaque(false);
        
        SaveButton.setContentAreaFilled(false);
        SaveButton.setBorderPainted(false);
        SaveButton.setFocusPainted(false);
        SaveButton.setOpaque(false);
        
        LicenseLabel.setContentAreaFilled(false);
        LicenseLabel.setBorderPainted(false);
        LicenseLabel.setFocusPainted(false);
        LicenseLabel.setOpaque(false);
        
        versionLabel.setText("Copyright NathanSoftware.com version " + version);
                
        programStart();
    }
    
    public void programStart() {
          
        //on the program start fill the box
        fillListBoxes();
        
    }
    
    public void fillListBoxes() {
        userListBox.removeAllItems(); //clear the box  
        surveyListBox.removeAllItems(); //clear the box  
        
        surveyListBox.addItem("All Surveys Found");
        userListBox.addItem("All Users Found");
        
        // get results from the Ques folder
        for (int i = 0; i < finderCSV("C:\\Ques\\").length; i++) {
            boolean insert = true;
            
            
            // first search all .sur files and store their names because they are the survey saves
            if (finderCSV("C:\\Ques\\")[i].getName().contains(".sur")) {
                    
                // SURVEY NAMES
                
                // the part after the underscore and before the .sur is found (that is the survey name)
                String item = finderCSV("C:\\Ques\\")[i].getName();
                item = item.substring(item.lastIndexOf("_") + 1, item.indexOf(".sur")); // NOTE: if a user places a "_" in a survey name it will break it
                
                // then check that this name doesn't already exist
                for (int ei = 0; ei < surveyListBox.getItemCount(); ei++) {
                    
                    // if the current item equals one of the survey names already here than don't put it in
                    if (item.equals(surveyListBox.getItemAt(ei))) {
                        insert = false;
                    }
                }
                
                // if insert is still true then insert the item into the survey list box
                if (insert == true) {
                    surveyListBox.addItem(item);
                }

                
                //USER NAMES
                
                insert = true;
                
                // the part after the "Save_" and before the last underscore is the user name
                String useritem = finderCSV("C:\\Ques\\")[i].getName();
                useritem = useritem.substring(useritem.indexOf("Save_") + 5, useritem.lastIndexOf("_")); // NOTE: if a user places a "_" in a survey name it will break it
                
                System.out.println(useritem);
                
                // then check that this name doesn't already exist in the user drop down box
                for (int ei = 0; ei < userListBox.getItemCount(); ei++) {
                    
                    // if the current item equals one of the user names already here than don't put it in
                    if (useritem.equals(userListBox.getItemAt(ei))) {
                        insert = false;
                    }
                }      
                
                // the insert is still true then insert the user into the list box
                if (insert == true) {
                    userListBox.addItem(useritem);
                }
                
            }

        }
        
    }
        
    public File[] finderCSV(String dirName){
    	File dir = new File(dirName);

    	return dir.listFiles(new FilenameFilter() { 
    	         public boolean accept(File dir, String filename)
    	              { return true; } //filename.endsWith(".csv");
    	} );

    }
    
    public File[] finderSUR( String dirName){
    	File dir = new File(dirName);

    	return dir.listFiles(new FilenameFilter() { 
    	         public boolean accept(File dir, String filename)
    	              { return filename.endsWith(".sur"); }
    	} );

    }
        
    public void getUserInfo() {
        //first clear the text field
        FieldUserInfo.setText("");
        
        CurrentTextString = "";
               
        if (allS == true | allU == true) { //then get all surveys for this user
            //finderSave() finds all the saves of this user
            getAllSaveInfo();            
            
        } else { //otherwise get specific survey
        
            checkForUserFile();

            if (going == true) {
                getAllSaveInfo();
            }
        }
    }
    
    public void checkForUserFile() {
        String surveyCaps = surveyToGet.toLowerCase();
        String userCaps = userNameVar.toLowerCase();
        
        File f = new File("NON EXISTENT FILE"); //if no file returns with the correct name then have a fake name so no file is found
        
        //so basically file f is going to be a file in the ques folder that has the right user name and contains the right survey name
        // ! we have a for loop which checks each file !
        for (int i =0; i < finderSUR("C:\\Ques\\").length; i++) {
            
            // if the file we are checking contains the user we have inputed and the survey we have inputed
            if (finderSUR("C:\\Ques\\")[i].toString().toLowerCase().contains(surveyCaps) && finderSUR("C:\\Ques\\")[i].toString().toLowerCase().contains(userCaps)) {
                fileToLoad = finderSUR("C:\\Ques\\")[i].toString(); // then that is the file we want to load
                
                f = new File(fileToLoad); //then set the file f as that file
                System.out.println(fileToLoad);
            }
        }

        // if somefile has been set as f (and its not a folder)
        
        if(f.exists() && !f.isDirectory()) {
            
            
            InfoLabel1.setForeground(Color.black); // reset label in case it was changed because of an error
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

            theDate = sdf.format(f.lastModified());
            
            System.out.println("After Format : " + theDate);

            DateLabel.setText("These answers entered at: " + theDate);
            going = true;
        }
        else { //otherwise check to see if the user name file exists in all lower case
            String userNameVarLC = userNameVar.toLowerCase();
            
            File f2 = new File("C:\\Ques\\Save_" + userNameVarLC + "_" + surveyToGet + ".sur");
            if (f2.exists() && !f2.isDirectory()) {
                System.out.println("Found the file in lowercase");
                // reset label in case it was changed because of an error
                InfoLabel1.setForeground(Color.black);
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		
                theDate = sdf.format(f2.lastModified());
                
                System.out.println("After Format : " + theDate);
                
                DateLabel.setText("These answers entered at: " + theDate);
                going = true;
                
            }
            // if it still doesn't then bring an error
            else {
                going = false;
                System.out.println("Error");
                InfoLabel1.setForeground(Color.red);
                InfoLabel1.setText("User save file not found please try a differn't name:");
                System.out.println("Couldnt find the file");
                DateLabel.setText("");
            }
        }
    }
    
    
    //////////////////////
    // LOAD SAVE AND PUT INTO FIELD
    //////////////////////
    public void getAllSaveInfo() {
        InfoLabel1.setText("User survey saves: ");
        
        DateLabel.setText("");
        
        File f = new File("C:\\Ques\\");
        
        File[] matchingFiles = null;
        
        if (allS == true && allU == true) {
            matchingFiles = f.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    // all surveys are shown
                    return name.endsWith("sur");
                }
            });
        
        }
        else if (allU == true) {
            System.out.println("All U!!! - survey name is " + surveyToGet);
            
            matchingFiles = f.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    
                    String currentFile = name.toLowerCase();
                    
                    boolean accepting = false;
                    
                    String partialFileName = "";
                    
                    // for each user being checked
                    for(int i = 0; i < userListBox.getItemCount(); i++) {
                        
                        // check if the filename contains any users in it
                        if (currentFile.contains(userListBox.getItemAt(i).toLowerCase())) {
                            
                            // if the file name has a user then remove it
                            partialFileName = currentFile.replace(userListBox.getItemAt(i).toLowerCase(), "");
                                          
                            // then check if the filename without the user is correct
                            if (partialFileName.equals("save_" + "_" + surveyToGet.toLowerCase() + ".sur")) {
                                accepting = true;
                            }
                        }
                    }
                    
                    return accepting;
                }               
            });
        }
        else if (allS == true) {
            System.out.println("All S!!! - user name is " + userNameVar);
            
            matchingFiles = f.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    
                    String currentFile = name.toLowerCase();
                    
                    boolean accepting = false;
                    
                    String partialFileName = "";
                    
                    // for each survey being checked
                    for(int i = 0; i < surveyListBox.getItemCount(); i++) {
                        
                        // check if the filename contains any surveys in it
                        if (currentFile.contains(surveyListBox.getItemAt(i).toLowerCase())) {
                            
                            // if the file name has a survey then remove it
                            partialFileName = currentFile.replace(surveyListBox.getItemAt(i).toLowerCase(), "");
                                          
                            // then check if the filename without the survey is correct
                            if (partialFileName.equals("save_" + userNameVar.toLowerCase() + "_" + ".sur")) {
                                accepting = true;
                            }
                        }
                    }

                    return accepting;
                    
                }
            });
        }
        else { // if none are selected then store the single file:
            
            // this GETS A SINGLE FILE AND SHOWS IT!
            
            
            matchingFiles = f.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    
                    //First work out the entire save files name which includes: "Save_" & username & "_" & surveyname 
                    String fullNameLowerCase = "save_" + userNameVar.toLowerCase() + "_" + surveyToGet.toLowerCase() + ".sur";
                    
                    System.out.println(fullNameLowerCase);
                    
                    // the save matching the exact details is saved
                    return name.toLowerCase().equals(fullNameLowerCase);
                }               
            });
            
/* // --- DEPRECATED             
            if (matchingFiles.length > 1) {
                /// OOOPS there are more than one file with the same name 
                // (this should never happen and is deprecated so it should be removed later)
                for (int m = 0; m < 1000; m++) {
                    if (matchingFiles[m] != null) {
                        String fileName = matchingFiles[1].getName();
                        fileName = fileName.substring(fileName.indexOf("Save_") + 5, fileName.indexOf(".sur"));
                        
                        if (fileName.equals(userNameVar + "_" + surveyToGet)) { //if file name now does not equal it exactly then remove it
                            matchingFiles = null; //clear the matching files array
                            
                            // then reassign it to include ONLY the new file
                            matchingFiles = f.listFiles(new FilenameFilter() {
                                public boolean accept(File dir, String name) {
                                    // the save matching the exact details is saved
                                    return name.toLowerCase().equals(("Save_" + userNameVar + "_" + surveyToGet + ".sur").toLowerCase());
                                }               
                            });
                        }
                    }
                    else {
                        break; //if it equals null then stop searching
                    }
                }
            }
// ---        */    
        }
//////////////////////////////
//    FOR EACH A FILE
//////////////////////////////

        System.out.println("We have " + matchingFiles.length + " users");
        
        numberOfUsers = matchingFiles.length;
        
        // READING THE FILE //
        for (int i = 0; i < matchingFiles.length; i ++) { //for all matching saves
            
                // Storing survey stuff::

                // get the user name of the current file
                String userName = matchingFiles[i].getName().substring(matchingFiles[i].getName().indexOf("_") + 1, matchingFiles[i].getName().lastIndexOf("_"));
                
                System.out.println("Name - " + userName);
                
                System.out.println("Current String " + CurrentTextString);

                
                BufferedReader reader = null;
                try {
                    // this is the reader that reads the current file stored
                    reader = new BufferedReader(new FileReader("C:\\Ques\\" + matchingFiles[i].getName()));
                } catch (Exception e) {
                    // the file cant be opened so we must assume their is some error
                    
                }
                    
                // checks if the text field is blank (i.e. checks if this is the first survey placed into the text field)
                if (!"".equals(CurrentTextString)) {
                    
                    FieldUserInfo.setText(HTML + CurrentTextString + "<br/>"); //if the text field isn't blank then put another line into it before continueing
                
                    CurrentTextString = CurrentTextString + "<br/>";
                }
                
                // CREATE SURVEY TEXT::
                
                // DATE CREATION - 
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

                System.out.println("After Format : " + sdf.format(matchingFiles[i].lastModified()));

                
                theDate = sdf.format(matchingFiles[i].lastModified());
                    
                FieldUserInfo.setText(HTML + CurrentTextString + "<b>Survey: <i>" + matchingFiles[i].getName().substring(matchingFiles[i].getName().lastIndexOf("_") + 1, matchingFiles[i].getName().indexOf(".")) + "</i>,  User: <i>" + userName + "</i>, Date: <i>" + theDate + "</i></b> <br/><br/>"); //first put the save files name
                
                CurrentTextString = CurrentTextString + "<b>Survey: <i>" + matchingFiles[i].getName().substring(matchingFiles[i].getName().lastIndexOf("_") + 1, matchingFiles[i].getName().indexOf(".")) + "</i>,  User: <i>" + userName + "</i>, Date: <i>" + theDate + "</i></b> <br/><br/>";
                
                // Then read each line of the file (no more than 1000... so actually there is a limit to the number of questions)
                for (int x = 0; x < 1000; x++) {
                    String line = null;
                    try {
                        line = reader.readLine(); // read and store the line
                    } catch (IOException ex) {
                        // if it can't read the line then it should stop
                        line = null;
                    }
                    if (line == null) { //once it reaches a blank line then it stops the for loop
                        try {
                            reader.close();
                        } catch (Exception e) {
                            // reader is already closed
                        }
                        break; // STOP
                    }
                    
                    String[] item = line.split(","); // splits the current line found
                    
                    
                    // // // // // // //
                    /// NOTE: if the lines to long for any of the items it doesn't work???
                    
                    try {
                        // puts the text in the text field
                        FieldUserInfo.setText(HTML + CurrentTextString + "<b>Question " + item[0] + "</b>: <i>" + item[1] + "</i><br/>   " + " Was answered - <b>" + item[2] + "</b> - <i>" + item[3] + "</i><br/>");
                    
                    
                        CurrentTextString = CurrentTextString + "<b>Question " + item[0] + "</b>: <i>" + item[1] + "</i><br/>   " + " Was answered - <b>" + item[2] + "</b> - <i>" + item[3] + "</i><br/>";
                    } catch (Exception e) {
                        
                        System.out.println("Ooops broke when trying to load a question line");
                        
                        // if it has failed then print an error message and fix the cursor
                        FieldUserInfo.setText(FieldUserInfo.getText() + "\n [error: save file error");
                        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        
                        // then move on to the next file
                        break;
                    }
                    
                    int questionAnswer = 0;
                    
                    switch (item[2]) {
                        case "(A)":
                            questionAnswer = 1;
                            break;
                        case "(B)":
                            questionAnswer = 2;
                            break;
                        case "(C)":
                            questionAnswer = 3;
                            break;
                        case "(D)":
                            questionAnswer = 4;
                            break;
                        case "(E)":
                            questionAnswer = 5;
                            break;
                        default:
                            break;
                    }
                    
                    int questionNumber = 0;
                    
                    try {
                        questionNumber = Integer.parseInt(item[0]);
                    } catch (Exception e) {
                        System.out.println("Corrupt error");
                        // it broke, that means the survey has been corrupted.
                        item[0] = "[error]";
                    }
                        
                    System.out.println("Q " + questionNumber + " , updated +1 for " + questionAnswer );
                    
                    // for any given save figure out which question it is and update the answer in the matrix
                    /*int value = 0;
                    try {
                        value = Integer.parseInt(summaryMatrix[questionNumber][questionAnswer]) + 1; // turn the current value into an integer and add 1 to it
                        
                        summaryMatrix[questionNumber][questionAnswer] = "" + value; // assign it to the array
                    } catch (Exception e) {
                        // if it breaks the integer must not be any integer... therefore it must be null
                        summaryMatrix[questionNumber][questionAnswer] = "" + 1;
                    }*/
                    
                    summaryTally[questionNumber][questionAnswer] += 1;
                    
                    summaryMatrix[questionNumber][0] = item[1]; // store what the question is 
                    summaryMatrix[questionNumber][questionAnswer] = item[3]; // store what the answer is
                    
                    //System.out.println("A " + item[3]);
                }
                // sticks a line after the end of the file
                FieldUserInfo.setText(HTML + CurrentTextString + "<br/>----------------------<br/>");
                
                CurrentTextString = CurrentTextString + "<br/>----------------------<br/>";

                
                // PRINT STUFF:
                String info = FieldUserInfo.getText(); //just for the command prompt everything else is actually stuck into the text field

                System.out.println("PRINTING STUFF ------" + info);

        }
        ///////////////////////

        //then put the scrollbar at the top again
       javax.swing.SwingUtilities.invokeLater(new Runnable() {
          public void run() { 
              jScrollPane1.getVerticalScrollBar().setValue(0);
          }
       });           
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        versionLabel = new javax.swing.JLabel();
        SaveButton = new javax.swing.JButton();
        MainLabel = new javax.swing.JLabel();
        GoButton = new javax.swing.JButton();
        InfoLabel1 = new javax.swing.JLabel();
        ButtonReturn = new javax.swing.JButton();
        InfoLabel2 = new javax.swing.JLabel();
        userListBox = new javax.swing.JComboBox<>();
        InfoLabel3 = new javax.swing.JLabel();
        surveyListBox = new javax.swing.JComboBox<>();
        DateLabel = new javax.swing.JLabel();
        LicenseLabel = new javax.swing.JButton();
        HelpButton = new javax.swing.JButton();
        SummaryButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        FieldUserInfo = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx32.gif")));

        versionLabel.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        versionLabel.setText("Copyright NathanSoftware.com version 2.0.2");

        SaveButton.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        SaveButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/ball-blue.png"))); // NOI18N
        SaveButton.setText("Save Survey Answers");
        SaveButton.setToolTipText("<html>Click to save this file</html");
        SaveButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                SaveButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                SaveButtonMouseExited(evt);
            }
        });
        SaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveButtonActionPerformed(evt);
            }
        });

        MainLabel.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        MainLabel.setText("Survey Answer Analysis");

        GoButton.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        GoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/ballGO.png"))); // NOI18N
        GoButton.setToolTipText("<html>Choose a user and/or a survey and press Go</html>");
        GoButton.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/ballGOSelected.png"))); // NOI18N
        GoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GoButtonActionPerformed(evt);
            }
        });

        InfoLabel1.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        InfoLabel1.setText("Press Go to load user data file");

        ButtonReturn.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        ButtonReturn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/homeIcon.jpg"))); // NOI18N
        ButtonReturn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ButtonReturnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ButtonReturnMouseExited(evt);
            }
        });
        ButtonReturn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonReturnActionPerformed(evt);
            }
        });

        InfoLabel2.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N

        userListBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All users" }));
        userListBox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                userListBoxMouseClicked(evt);
            }
        });

        InfoLabel3.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        InfoLabel3.setText("Select the criteria you want to analyze:");

        surveyListBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All surveys" }));

        DateLabel.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        DateLabel.setText(" ");

        LicenseLabel.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        LicenseLabel.setForeground(new java.awt.Color(102, 102, 102));
        LicenseLabel.setText("This software and all of its components are copyrighted under the Creative Commons Attribution-NoDerivatives 4.0 License");
        LicenseLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LicenseLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                LicenseLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                LicenseLabelMouseExited(evt);
            }
        });

        HelpButton.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        HelpButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/helpIcon.jpg"))); // NOI18N
        HelpButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                HelpButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                HelpButtonMouseExited(evt);
            }
        });
        HelpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HelpButtonActionPerformed(evt);
            }
        });

        SummaryButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/Iconx16.gif"))); // NOI18N
        SummaryButton.setText("Summary");
        SummaryButton.setToolTipText("<html>To summarize a survey you must choose a survey and select \"All Users\"<br>Summarys show the number of users that have choosen each option for a survey</html>");
        SummaryButton.setEnabled(false);
        SummaryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SummaryButtonActionPerformed(evt);
            }
        });

        FieldUserInfo.setEditable(false);
        FieldUserInfo.setContentType("text/html"); // NOI18N
        jScrollPane1.setViewportView(FieldUserInfo);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(versionLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(SaveButton))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 527, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(InfoLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(107, 107, 107)
                                .addComponent(SummaryButton)
                                .addGap(1, 1, 1)
                                .addComponent(DateLabel)))))
                .addGap(77, 77, 77))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(91, 91, 91)
                                .addComponent(InfoLabel2))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(ButtonReturn)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(152, 152, 152)
                                        .addComponent(InfoLabel3))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(162, 162, 162)
                                        .addComponent(MainLabel)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(HelpButton))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(57, 57, 57)
                                        .addComponent(userListBox, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(28, 28, 28)
                                        .addComponent(surveyListBox, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(210, 210, 210)
                                        .addComponent(GoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 161, Short.MAX_VALUE))))
                    .addComponent(LicenseLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 660, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ButtonReturn)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(MainLabel)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(56, 56, 56)
                                .addComponent(InfoLabel2))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(InfoLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(userListBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(surveyListBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(GoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(HelpButton))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(InfoLabel1)
                            .addComponent(DateLabel))
                        .addGap(6, 6, 6))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(SummaryButton)
                        .addGap(2, 2, 2)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SaveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(versionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LicenseLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void GoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GoButtonActionPerformed
        
        // during the process changes the mouse cursor
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                
        // reset the summaryMatrix 
        summaryMatrix = null;
        summaryMatrix = new String[100][6];
        
        summaryTally = null;
        summaryTally = new int[100][6];
        
        // first we get the results of the two list boxes
        userNameVar = userListBox.getItemAt(userListBox.getSelectedIndex());
        surveyToGet = surveyListBox.getItemAt(surveyListBox.getSelectedIndex());
        
        if ("All Users Found".equals(userNameVar)) { //then we want to display all users for whatever survey is selected
            allU = true;
            
            // if selected "All users of this survey" then allow a summary
            SummaryButton.setEnabled(true);
        } else {
            allU = false;
        }
        if ("All Surveys Found".equals(surveyToGet)) { //then we want to display all surveys for whatever user is selected
            allS = true;
            
            // except you can't have a summary if you've choose All Surveys Found
            SummaryButton.setEnabled(false);
        } else {
            allS = false;
        }
        
        getUserInfo();
        
        // at the end changes the cursor back
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_GoButtonActionPerformed

    private void ButtonReturnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonReturnMouseEntered
        ButtonReturn.setIcon(new ImageIcon(getClass().getResource("/resources/homeIconSelected.jpg")));
    }//GEN-LAST:event_ButtonReturnMouseEntered

    private void ButtonReturnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonReturnMouseExited
        ButtonReturn.setIcon(new ImageIcon(getClass().getResource("/resources/homeIcon.jpg")));
    }//GEN-LAST:event_ButtonReturnMouseExited

    private void ButtonReturnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonReturnActionPerformed
        EasySurveyMenu ESF = new EasySurveyMenu();
        
        List<Image> icons = new ArrayList<>();
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx16.png")));
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx32.gif")));
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx64.gif")));
        ESF.setIconImages(icons);
        
        ESF.setVisible(true);
        dispose();
    }//GEN-LAST:event_ButtonReturnActionPerformed

    private void SaveButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SaveButtonMouseEntered
        SaveButton.setIcon(new ImageIcon(getClass().getResource("/resources/ball-blueSelected.png")));
        SaveButton.setForeground(Color.CYAN);
    }//GEN-LAST:event_SaveButtonMouseEntered

    private void SaveButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SaveButtonMouseExited
        SaveButton.setIcon(new ImageIcon(getClass().getResource("/resources/ball-blue.png")));
        SaveButton.setForeground(null);
    }//GEN-LAST:event_SaveButtonMouseExited

    private void SaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveButtonActionPerformed
        // if there is something in the text field allow the user to save it
        
        // first get rid of all the html and new line characters
        String sb = FieldUserInfo.getText();
        
        System.out.println(sb);
        
        sb = Jsoup.parse(sb).text();
        
        // if it now is not nothing then allow it to be saved
        if (!sb.equals("")) {
            System.out.println(sb);
            saveMap();
        } else {
            // nothing in save file
            System.out.println("Nothing in textfield.");
        }
    }//GEN-LAST:event_SaveButtonActionPerformed

    private void LicenseLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LicenseLabelMouseClicked
        URI domain = null;
        try {
            domain = new URI("https://creativecommons.org/licenses/by-nd/4.0/"); //launches the copyright help window
        } catch (URISyntaxException ex) {
            Logger.getLogger(EasySurveyMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        openWebpage(domain);
    }//GEN-LAST:event_LicenseLabelMouseClicked

    public static void openWebpage(URI uri) {
       Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
       if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
           try {
               desktop.browse(uri);
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
    }
    
    private void LicenseLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LicenseLabelMouseEntered
        LicenseLabel.setForeground(Color.CYAN);
    }//GEN-LAST:event_LicenseLabelMouseEntered

    private void LicenseLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LicenseLabelMouseExited
        LicenseLabel.setForeground(new Color(102,102,102));
    }//GEN-LAST:event_LicenseLabelMouseExited

    private void HelpButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HelpButtonMouseEntered
        HelpButton.setIcon(new ImageIcon(getClass().getResource("/resources/helpIconSelected.jpg")));
    }//GEN-LAST:event_HelpButtonMouseEntered

    private void HelpButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HelpButtonMouseExited
        HelpButton.setIcon(new ImageIcon(getClass().getResource("/resources/helpIcon.jpg")));
    }//GEN-LAST:event_HelpButtonMouseExited

    private void HelpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HelpButtonActionPerformed
        URI domain = null;
        try {
            domain = new URI("http://nathansoftware.com/wordpress/support/"); 
        } catch (URISyntaxException ex) {
            Logger.getLogger(EasySurveyMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        openWebpage(domain);
    }//GEN-LAST:event_HelpButtonActionPerformed

    private void SummaryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SummaryButtonActionPerformed
        // summarize the current stuff in the text field
        SummaryButton.setEnabled(false);
        
        // if the text field has stuff in it
        if (!"".equals(FieldUserInfo.getText()) && FieldUserInfo.getText() != null) {
            
            FieldUserInfo.setText(""); //clear text field
            
            String surveyString = " survey";
            if (surveyToGet.toLowerCase().contains("survey")) {
                surveyString = "";
            }
            // put in first line
            System.out.println("NumUsers: " + numberOfUsers);
            
            CurrentTextString = "<b>" + numberOfUsers + "</b> users took the <i>" + surveyToGet + "</i>" + surveyString + ":";

            for (int q = 1; q < summaryMatrix.length; q++) {
                
                // create a new array to store the options in
                String[] unorderedArray = new String[5];
                
                if (summaryMatrix[q][0] == null) { // then the question does not exist. The survey doesn't have that many
                    break;
                }
                //if (q != 1) {
                    FieldUserInfo.setText(HTML + CurrentTextString + "<br/><br/>"); // for all the q's // ! -> but #1 put a new line
                    CurrentTextString = CurrentTextString + "<br/><br/>";
                //}
                FieldUserInfo.setText(HTML + CurrentTextString + "<b>Question " + q + "</b> <i>(" + summaryMatrix[q][0] + "):</i>");
                CurrentTextString = CurrentTextString + "<b>Question " + q + "</b> <i>(" + summaryMatrix[q][0] + "):</i>";
                
                for (int a = 1; a < summaryMatrix[0].length; a++) {
                    
                    if (summaryTally[q][a] != 0) {
                        System.out.println("Q" + q + " (" + summaryMatrix[q][0] + "): Op " + a + " (" + summaryMatrix[q][a] + ") : " + summaryTally[q][a]);                        
                        
                        float tally = summaryTally[q][a];
                        float users = numberOfUsers;
                        
                        double percent = tally*100/users;
                        
                        percent = round(percent, 1);
                        // store the option
                        unorderedArray[a -1] = "Option " + a + " <i>(" + summaryMatrix[q][a] + ")</i> was choosen <b>" + summaryTally[q][a] + " times</b> (" + percent + "%)";
                    }
                }
                
                // get rid of null elements in array
                List<String> list = new ArrayList<>();

                for(String s : unorderedArray) {
                   if(s != null && s.length() > 0) {
                      list.add(s);
                   }
                }

                unorderedArray = list.toArray(new String[list.size()]);
                
                // sort the array
                Arrays.sort(unorderedArray,(a,b) -> Integer.compare(
                        Integer.parseInt(b.substring(b.indexOf("was choosen <b>") + 15, b.indexOf(" times</b>"))),
                        Integer.parseInt(a.substring(a.indexOf("was choosen <b>") + 15, a.indexOf(" times</b>")))
                ));
                
                // put stuff into field
                for (String unorderedArray1 : unorderedArray) {
                    FieldUserInfo.setText(HTML + CurrentTextString + "<br/>    " + unorderedArray1);
                    CurrentTextString = CurrentTextString + "<br/>   " + unorderedArray1;
                }
                
            }
            
            //then put the scrollbar at the top again
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
               public void run() { 
                   jScrollPane1.getVerticalScrollBar().setValue(0);
               }
            });   
        }
    }//GEN-LAST:event_SummaryButtonActionPerformed

    private void userListBoxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_userListBoxMouseClicked
        if (!userListBox.getSelectedItem().equals("All Users Found")) {
            SummaryButton.setEnabled(false);
        }
    }//GEN-LAST:event_userListBoxMouseClicked

    public static double round(double value, int numberOfDigitsAfterDecimalPoint) {
        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(numberOfDigitsAfterDecimalPoint,
                BigDecimal.ROUND_HALF_UP);
        return bigDecimal.doubleValue();
    }    
    
    public void saveMap() {
        String savedLocation = "/Documents";
        
        StringBuilder newSaveFile = new StringBuilder();
        
        int linesInSaveFile = 0;
        
        // load the data file and find the remembered directory
        BufferedReader read = null;
        try {
            System.out.println("Trying to find quesload");
            read = new BufferedReader(new FileReader("C:\\Ques\\QuesLoad.txt"));
            
            //read the first 4 lines and store them to put back into the file later
            for (int i = 0; i < 4; i ++) {
                String line = read.readLine();
                newSaveFile.append(line).append("\r\n");
                if (line != null && !"".equals(line)) {
                    linesInSaveFile ++; //count new lines
                }
            }
            
            //then store the fourth line which is the password line
            savedLocation = read.readLine(); 
            
            System.out.println("Save Location*: " + savedLocation);
                        
            read.close();
        } catch (Exception ex) {
            //if this happens there is no quesLoad file so make one
            //          *IF THERE IS NO QUESLOAD*
            System.out.println("couldn't find quesload");
            
            // well that sucks, somebody corrupted the save file since opening the program (it is also checked in the main frame)
            
            // oh well just give an error
            InfoLabel1.setText(InfoLabel1.getText() + " (Error: please restart program)");
            
        }
        
        // IF THERE IS NO SAVED LOCATION but 4 other lines give an error message and fix it
        if ((savedLocation == null | "".equals(savedLocation)) && linesInSaveFile == 4) {
            System.out.println("BIG PROBLEM!!");
            
            InfoLabel1.setText(InfoLabel1.getText() + " (Error: setting file rectified, please try again)");
            
            try {
                // NOW WE HAVE TO ADD A FIFTH LINE to the file
                FileWriter appender = new FileWriter("C:\\Ques\\QuesLoad.txt" , true);
                
                appender.write("\r\n/Documents"); // add a fifth line
                
                // THIS IS SUPER IMPORTANT because previous versions didn't have this fifth line
                savedLocation = "/Documents"; // and fix the problem in the variable which is null
                
                appender.close();
            } catch (IOException ex) {
                Logger.getLogger(AnswerFrame.class.getName()).log(Level.SEVERE, null, ex);
                // if this happens their is something seriously wrong!
                
                InfoLabel1.setText(InfoLabel1.getText() + " (Error: please restart program, if error persists: support at nathansoftware.com)");
            }
        }
        
        
        String sb = CurrentTextString;
        
        sb = sb//.replace("<head>", "").replace("</head>", "")
               // .replace("<body>", "").replace("</body>", "")
               // .replace("<html>", "").replace("</html>", "")
                .replace("<b>", "").replace("</b>", "")
                .replace("<i>", "").replace("</i>", "")
                .replace("<br/>", "\n");
        
        String sb2 = sb.replaceAll("\n", "\r\n"); //make it so that all the next lines that appear in the text field are properly translated when being put in the text document
        
        // fix the date to put into the files name
        String fileNameDate = theDate;
        
        fileNameDate = fileNameDate.replace("/","-"); // we want to remove any forward slashes
        fileNameDate = fileNameDate.substring(0, fileNameDate.indexOf(":") - 3); // and cut off the time part of the date (everything the colon (and also the 3 characters right before the colon)) so just to have the day-month-year
        
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(savedLocation));
        chooser.setSelectedFile(new File(surveyToGet + " - " + userNameVar + " - " + fileNameDate));
        int retrival = chooser.showSaveDialog(null);
        
        // IF YOU SELECT THE APPROVE OPTION IT DOES ALL THIS::
        if (retrival == JFileChooser.APPROVE_OPTION) {
            String fileEx = "";
            // if the files doesn't contain a dot assume the person didn't put an extension
            if (!chooser.getSelectedFile().getName().contains(".")) {
                fileEx = ".txt";
            }
            try(
                FileWriter fw = new FileWriter(chooser.getSelectedFile() + fileEx)) {
                fw.write(sb2);
            } catch (Exception ex) {
            }
        
            // now save the location selected
            savedLocation = chooser.getSelectedFile().getAbsolutePath();

            // and cut off the name of the file (because .getAbsolutePath() includes the survey name and we just want the directory)
            // we substring it from 0 -> the last \
            savedLocation  = savedLocation.substring(0 , savedLocation.lastIndexOf("\\"));


            // add to my file string the saved location
            newSaveFile.append(savedLocation);

            // write the new save file!
            try {
                BufferedWriter write = new BufferedWriter(new FileWriter("C:\\Ques\\QuesLoad.txt"));
                write.write(newSaveFile.toString());
                write.close();
            } catch (IOException ex1) {
                Logger.getLogger(EasySurveyMenu.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AnswerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AnswerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AnswerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AnswerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AnswerFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButtonReturn;
    private javax.swing.JLabel DateLabel;
    private javax.swing.JTextPane FieldUserInfo;
    private javax.swing.JButton GoButton;
    private javax.swing.JButton HelpButton;
    private javax.swing.JLabel InfoLabel1;
    private javax.swing.JLabel InfoLabel2;
    private javax.swing.JLabel InfoLabel3;
    private javax.swing.JButton LicenseLabel;
    private javax.swing.JLabel MainLabel;
    private javax.swing.JButton SaveButton;
    private javax.swing.JButton SummaryButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> surveyListBox;
    private javax.swing.JComboBox<String> userListBox;
    private javax.swing.JLabel versionLabel;
    // End of variables declaration//GEN-END:variables
}
