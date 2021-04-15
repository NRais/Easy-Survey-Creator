/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.text.StyledDocument;
import org.apache.commons.lang3.SystemUtils;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 *
 * Copyright 2018 Nathan Rais 
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
    
    String pathway = "C:\\Ques";
    // -- //
    
    public boolean going = true;
    public boolean allS = false;
    public boolean allU = false;
    
    public String theDate = "";
    public String summaryType = "Text";
    
    public int numberOfUsers = 0;
    
    public String fileToLoad = "";
    
    public boolean SearchByUserName = true;
    public boolean SearchBySurveyName = false;
    
    String CurrentTextString = "";
    
    String HTML = "<html>"; //FieldUserInfo.getText().substring(0, FieldUserInfo.getText().indexOf("<body>") + 6);
    
    // the rows and columns store repersent the questions and answeres, they store how many times they were answered
    int[][] summaryTally = new int[100][6]; // basically the matrix looks like this for each survey [question][answer1tally][answer2tally][...]
    
    // the rows and columns store repersent the questions and answeres, they store what each question is and what the answers are
    String[][] summaryMatrix = new String[100][6]; // basically the matrix looks like this for each survey [question][answer][answer][answer][...]
    
    //  (The?)   A  B  C  D  E
    //  Q1
    //  Q2
    //  Q3
    //  Q...
    
    Result[] listOfResults = null;
    List<String> surveyArray =  new ArrayList<String>();
    List<String> userArray =  new ArrayList<String>();
    
    //////
    // so basically for every save survey result file we create a new Result Object
    public class Result {
        // results important parameters are UserName, and SurveyName (which allow it to be found when searched for)
        // and also Location (which allows the contents to be found and displayed)

        private String UserName;
        private String SurveyName;

        private File Location;

        Result(String usrNm, String srvyNm, File lcNm) {
            // initialize object
            UserName = usrNm;
            SurveyName = srvyNm;
            Location = lcNm;
        }

        public String getUserName() {
            return UserName;
        }
        public String getSurveyName() {
            return SurveyName;
        }
        public File getResultFile() {
            return Location;
        }
    }
    ////// 
    
    
    /**
     * Creates new form AnswerFrame
     */
    public AnswerFrame() {
        initComponents();
        
        //get system variables
        if (!SystemUtils.IS_OS_WINDOWS) {
            pathway = SystemUtils.USER_HOME + "/Ques";
            // ****** System.out.printlnln("Home " + pathway);
        }
        
        MainStartup main = new MainStartup();
        
        versionLabel.setText("Copyright NathanSoftware.com version " + main.version);
        
        Container c = getContentPane();               
        c.setBackground(Color.white);
        
        setLocationRelativeTo(null);
        setResizable(false);
        
        //FieldUserInfo.setContentType("text/html");
        
        ButtonReturn.setContentAreaFilled(false);
        ButtonReturn.setBorderPainted(false);
        ButtonReturn.setFocusPainted(false);
        ButtonReturn.setOpaque(false);
        
        SaveButton.setContentAreaFilled(false);
        SaveButton.setBorderPainted(false);
        SaveButton.setFocusPainted(false);
        SaveButton.setOpaque(false);
        
        LicenseLabel.setContentAreaFilled(false);
        LicenseLabel.setBorderPainted(false);
        LicenseLabel.setFocusPainted(false);
        LicenseLabel.setOpaque(false);
        
        versionLabel.setText("Copyright NathanSoftware.com version " + version);
                
        getResults(); //fill the drop down boxes
        
        // TODO make this better, currently userArray.toArray() returns an Object[] so it has to cast all the oobjects back and forth in order to get a correct model
        JComboBox testBox = new JComboBox(userArray.toArray());
        userListBox.setModel(testBox.getModel());
        
        JComboBox testBox2 = new JComboBox(surveyArray.toArray());
        surveyListBox.setModel(testBox2.getModel());
        
        
    }
    
    public File[] finderSUR( String dirName){
        File dir = new File(dirName);

        return dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String filename)
            { return filename.endsWith(".sur"); }
        } );

    }
    
    /// get results from files and put them into the list boxes
    public void getResults() {

        // create a new result array of the length of the files it has found from the finderSur procedure
        listOfResults = new Result[finderSUR(pathway).length];
        
        // get results from the folder of all SURVEY files
        // for each survey save we search it and extract its user and its survey
        for (int i = 0; i < finderSUR(pathway).length; i++) {

                boolean insert = true;

                // SURVEY NAMES

                // the part after the underscore and before the .sur is found (that is the survey name)
                String surveyItem = finderSUR(pathway)[i].getName();
                surveyItem = surveyItem.substring(surveyItem.lastIndexOf("_") + 1, surveyItem.indexOf(".sur")); // NOTE: if a user places a "_" in a survey name it will break it

                // then check that this name doesn't already exist
                for (int ei = 0; ei < surveyArray.size(); ei++) {

                    // if the current item equals one of the survey names already here than don't put it in
                    if (surveyItem.equals(surveyArray.get(ei))) {
                        insert = false;
                    }
                }

                
                // if insert is still true (and the item is not blank) then insert the item into the survey list box
                if (insert == true && !surveyItem.equals("")) {
                    surveyArray.add(surveyItem);
                }


                //USER NAMES

                insert = true;

                // the part after the "Save_" and before the last underscore is the user name
                String useritem = finderSUR(pathway)[i].getName();
                useritem = useritem.substring(useritem.indexOf("Save_") + 5, useritem.lastIndexOf("_")); // NOTE: if a user places a "_" in a survey name it will break it

                // ****** System.out.printlnln(useritem);

                // then check that this name doesn't already exist in the user drop down box
                for (int ei = 0; ei < userArray.size(); ei++) {

                    // if the current item equals one of the user names already here than don't put it in
                    if (useritem.equalsIgnoreCase(userArray.get(ei))) {
                        insert = false;
                    }
                }

                // the insert is still true then insert the user into the list box
                if (insert == true) {
                    userArray.add(useritem);
                }

                // for this item we will create a Result object
                listOfResults[i] = new Result(useritem, surveyItem, finderSUR(pathway)[i]);

        }

        // then at the end of the process we want to sort the two arrays alphebetically
        try {
            Collections.sort(userArray);
            Collections.sort(surveyArray);
        } catch (Exception e) {
            // if it can't sort it is because their is nothing in it so we don't care
        }

        // then we want to add at the start of both lists "All users Found", "All surveys found"
        userArray.add(0, "All Users Found");
        surveyArray.add(0, "All Surveys Found");
        
    }
    
    
    
    /// search the files and display their results in a cohesive manner (also prepare to give a summary)
    public void ShowResults() {

        // then we want to get the data:

        // get list of surveys to show

        // we are going to iterate through a loop of all our Result objects (stored in listOfResults)
        // and we are going to display any that meet the text field criteria

        // if we are checking "All Surveys Of"/"All Users of" then any user/survey name meets that criteria

        // get the value (in our list of values) that is currently selected by our spinner
        String userNameFilter = userListBox.getItemAt(userListBox.getSelectedIndex());
        String surveyNameFilter = surveyListBox.getItemAt(surveyListBox.getSelectedIndex());

        // a string builder to store all the stuff in
        StringBuilder textToShow = new StringBuilder();


        for (int i = 0; i < listOfResults.length; i++) {
            // IF we want all users OR the current user is the one we want
            //      AND
            // IF we want all surveys OR the current survey is the one we want
            if (userNameFilter.equals("All Users Found") | listOfResults[i].getUserName().equals(userNameFilter) &&
                surveyNameFilter.equals("All Surveys Found") | listOfResults[i].getSurveyName().equals(surveyNameFilter)) {

                // now we want to display this object!
                // Load the file
                File thisResult = listOfResults[i].getResultFile();
                
                // if its not the first time then we put to line breaks befor the next survey
                if (!textToShow.toString().equals("")) {
                    textToShow.append("<br><br>");
                }
                
                ////////////// STEP 1 we display the current data and name and info

					// SimpleDateFormat
					SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
					String theDate = sdf.format(thisResult.lastModified());
					
					// File's Names
					// take the file name and cut it at the last "_"
					// (Ex. Save_john_Default -> Default)
					String theSurvey = thisResult.getName().substring(thisResult.getName().lastIndexOf("_") + 1);
					
					// File's User
					// take the file name and cut it between the first "_" and the last "_"
					// (Ex. Save_john_Default -> john)
					String theUser = thisResult.getName().substring(thisResult.getName().indexOf("_") + 1,thisResult.getName().lastIndexOf("_"));
					
					// Combine them all together to make a line
					// (Ex. Survey: Blah Blah , User: Nahanah, Date: 01/10/12 10:45:99)
					String mainLine = "<b>Survey: <i>" + theSurvey + "</i>,  User: <i>" + theUser + "</i>, Date: <i>" + theDate + "</i></b> <br><br>";

                    // add line breaks and the line to our string builder
                    textToShow.append(mainLine);

                ////////////// STEP 2 display file text

					// SO that requires first us reading the file with a BufferedReader
                    BufferedReader reader = null;
                    try {
                        // this is the reader that reads the current file stored
                        reader = new BufferedReader(new FileReader(thisResult));
                    } catch (Exception e) {} // the file cant be opened so we must assume their is some error

                    boolean EOF = false; // end of file boolean
                    String line = ""; // string to store lines as they are read
                    try {
                        line = reader.readLine(); // read and store the 1st line prior to entering the loop to ensure the file is not nothing
                    } catch (Exception e) {
                        System.out.println("Line read error");
                        
                        EOF = true;
                    } // if it breaks then the file is empty

                    // ensure that the line is not blank
                    if (line == null) {
                        EOF = true;
                    }
                    
                    // ****** System.out.printlnln("FirstLine: " + line);
                    // /// /// LOOP we will search each further line and read it and store it
                    while (!EOF) {
                        // ****** System.out.printlnln("THE LINE: " + line);
                        // split the current line into different sections (seperated by comas)
                        String[] item = line.split(",");

                        // the different sections of the array each repersent different info to be displayed
                        // (sequentially) : QuestionNum , Question , AnswerLetter, Answer

                        String thisAnswerLine = "";
                        try {
                            // (Ex. Question: How are you    Was answered - (A) - Very Good)
                            thisAnswerLine = "<b>Question " + item[0] + "</b>: <i>" + item[1] + "</i><br>   " + " Was answered - <b>" + item[2] + "</b> - <i>" + item[3] + "</i><br>";
                        } catch (Exception e) {
                            // -- ERROR CATCH --
                            // if the save file is incorrect (for exmaple it doesn't have enough items) then this will throw
                            // in this case we will display an error message and move on

                            thisAnswerLine = "[error] : save file error";
                        }

                        // ** SUMMARY MATRIX SETUP **
                        // for the summary matrix we store the data of this question answer in the matrix
                        if (userListBox.getSelectedIndex() == 0 && surveyListBox.getSelectedIndex() != 0) { // if we are finding "All Users Found" & not "All surveys found"; store results for summary
                            // Figure out the current number of the question
                            int questionNumber = 0;

                            try {
                                questionNumber = Integer.parseInt(item[0]);
                            } catch (Exception e) {
                                // ****** System.out.printlnln("Corrupt error");
                                // it broke, that means the survey has been corrupted.
                                item[0] = "[error]";
                            }
                            //

                            // Figure out the answer that has been given A=1 B=2 C=3 D=4 E=5
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
                            //
                            String answer = item[3];
                            
                            // IF IT HAS "__"
                            // for the summary we will not store the individual answers but instead a generic response
                            if (item[3].contains("__")) {
                                answer = "User typed response";
                            }
                            
                            // increment that this answer has been choosen again
                            summaryTally[questionNumber][questionAnswer] += 1;

                            summaryMatrix[questionNumber][0] = item[1]; // store what the question is
                            summaryMatrix[questionNumber][questionAnswer] = answer; // store what the answer is
                        }

                        // **  **

                        // add the line to our string builder
                        textToShow.append(thisAnswerLine);

                        // /// /// then try and read the next line
                        try {
                            line = reader.readLine();
                        } catch (Exception ex) {line = null;} // if it can't read the line then it should stop because the line must not exist (i.e. EOF)


                        // if the line is still null than nothing was read so we end the loop
                        if (line == null) {
                            EOF = true;
                            break;
                        }
                    }
                    
                try {
                    reader.close();
                } catch (Exception ex) {
                    // whoops the reader crashed, no body cares
                }


            }

            //////////////// STEP 3 (once we have searched through all the files) display the results of the string builder in the text view
            FieldUserInfo.setText(HTML + textToShow.toString());
            
        }
        
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

        SaveButton = new javax.swing.JButton();
        versionLabel = new javax.swing.JLabel();
        MainLabel = new javax.swing.JLabel();
        ButtonReturn = new javax.swing.JButton();
        InfoLabel2 = new javax.swing.JLabel();
        userListBox = new javax.swing.JComboBox<>();
        InfoLabel3 = new javax.swing.JLabel();
        surveyListBox = new javax.swing.JComboBox<>();
        DateLabel = new javax.swing.JLabel();
        LicenseLabel = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        FieldUserInfo = new javax.swing.JTextPane();
        summaryComboBox = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx32.gif")));

        SaveButton.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        SaveButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/menu/crystal-blue.png"))); // NOI18N
        SaveButton.setText("Export");
        SaveButton.setToolTipText("<html>Click to save this file</html");
        SaveButton.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        SaveButton.setIconTextGap(-1);
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

        versionLabel.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        versionLabel.setText("Copyright NathanSoftware.com version 2.0.2");

        MainLabel.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        MainLabel.setText("Survey Answer Analysis");

        ButtonReturn.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        ButtonReturn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/homeIcon.jpg"))); // NOI18N
        ButtonReturn.setText(" Main Menu");
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

        userListBox.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        userListBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All users" }));
        userListBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userListBoxActionPerformed(evt);
            }
        });

        InfoLabel3.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        InfoLabel3.setText("Select the criteria you want to analyze:");

        surveyListBox.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        surveyListBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All surveys" }));
        surveyListBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                surveyListBoxActionPerformed(evt);
            }
        });

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

        FieldUserInfo.setEditable(false);
        FieldUserInfo.setContentType("text/html"); // NOI18N
        FieldUserInfo.setFocusable(false);
        jScrollPane1.setViewportView(FieldUserInfo);

        summaryComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Text Summary", "Pie Chart Summary", "Bar Graph Summary" }));
        summaryComboBox.setEnabled(false);
        summaryComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                summaryComboBoxItemStateChanged(evt);
            }
        });

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/Iconx16.png"))); // NOI18N
        jLabel3.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(LicenseLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(DateLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 556, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(versionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(summaryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(67, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(91, 91, 91)
                        .addComponent(InfoLabel2))
                    .addComponent(ButtonReturn))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(113, 113, 113)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(MainLabel)
                                .addGap(9, 9, 9))
                            .addComponent(InfoLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(SaveButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(userListBox, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(70, 70, 70)
                        .addComponent(surveyListBox, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(ButtonReturn)
                        .addGap(29, 29, 29)
                        .addComponent(InfoLabel2))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(MainLabel)
                                .addGap(3, 3, 3)
                                .addComponent(InfoLabel3))
                            .addComponent(SaveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(userListBox, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(surveyListBox, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(DateLabel))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(summaryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(versionLabel)))
                        .addGap(5, 5, 5)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addComponent(LicenseLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
        SaveButton.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-blueSelected.png")));
        //SaveButton.setForeground(Color.CYAN);
    }//GEN-LAST:event_SaveButtonMouseEntered

    private void SaveButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SaveButtonMouseExited
        SaveButton.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-blue.png")));
        //SaveButton.setForeground(null);
    }//GEN-LAST:event_SaveButtonMouseExited

    private void SaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveButtonActionPerformed
        
        // TODO File export = new File("C:\\Users\\Nuser\\Documents\\newFile.png");      
        // PrintFrameToPDF(export);
        

        // if there is something in the text field allow the user to save it
        
        // first get rid of all the html and new line characters
        String sb = FieldUserInfo.getText();
        
        System.out.println(sb);
        
        /* NO LONGER PARSING JSOUP TODO
        try {
            System.out.println("FILE:\n" + sb);
            //sb = Jsoup.parse(sb).text();
            
            
        } catch (Exception e) {
            System.out.println("ERROR UNABLE TO PARSE HTML using JSON");
        }*/
        
        // if it now is not nothing then allow it to be saved
        if (!sb.equals("")) {
            System.out.println("FILE:\n" + sb);
            saveMap(sb);
        } else {
            // nothing in save file
            System.out.println("Nothing in textfield.");
        }
    }//GEN-LAST:event_SaveButtonActionPerformed
    static Image iconToImage(Icon icon) {
        if (icon instanceof ImageIcon) {
            
           return ((ImageIcon)icon).getImage();
        } 
        else {
            
           int w = icon.getIconWidth();
           int h = icon.getIconHeight();
           GraphicsEnvironment ge = 
             GraphicsEnvironment.getLocalGraphicsEnvironment();
           GraphicsDevice gd = ge.getDefaultScreenDevice();
           GraphicsConfiguration gc = gd.getDefaultConfiguration();
           BufferedImage image = gc.createCompatibleImage(w, h);
           Graphics2D g = image.createGraphics();
           icon.paintIcon(null, g, 0, 0);
           g.dispose();
           return image;
           
        }
    }
    
    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }
    
    public void PrintFrameToPDF(File file) {
        
        JLabel selectedLabel = null;
  
        JTextPane source = FieldUserInfo;
        Component c = source.getComponent(0);
        if( c != null )
        {
            Component[] inner = ( ( Container )c ).getComponents();
            for( int n = 0; n < inner.length; n++ )
            {
                if( inner[ n ] instanceof JLabel )
                {
                    selectedLabel = ( JLabel )inner[ n ];
                }
            }
            if( selectedLabel != null )
            {
                if( selectedLabel.getIcon() != null )
                {
                    System.out.println( "The selected label has an icon" );
                }
                else
                {
                    System.out.println( "The selected label was " + selectedLabel.getText() );
                }
            }
        }
        
                    // then try to write it
                    System.out.println("OUTPUT3");
                    
        // for every component in the document
        for (int j = 0; j < FieldUserInfo.getComponents().length; j++){
            
                    // then try to write it
                    System.out.println("OUTPUT2" + FieldUserInfo.getComponent(j));
            //if it is a jlabel
            if(FieldUserInfo.getComponent(j) instanceof JLabel) {
                JLabel label = (JLabel)FieldUserInfo.getComponent(j);
                String text = label.getText();
                // Then get its icon as an image
                
                Image i = iconToImage(label.getIcon());
                
                try {
                    // then try to write it
                    System.out.println("OUTPUT");
                    ImageIO.write(toBufferedImage(i), "png", file);
                } catch (IOException ex) {
                    Logger.getLogger(AnswerFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
        
        
        /*try {
            FileWriter writer = new FileWriter("C:\\Users\\Nuser\\Documents\\new.html");
            FieldUserInfo.write(writer);
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(AnswerFrame.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }


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

    private void surveyListBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_surveyListBoxActionPerformed
        FireAction();
    }//GEN-LAST:event_surveyListBoxActionPerformed

    private void userListBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userListBoxActionPerformed
        FireAction();
    }//GEN-LAST:event_userListBoxActionPerformed
    
    // TEST SUBSTRING
    // TODO finish this
    // if the string isn't null we substring
    // otherwise we ignore
    
    // THEN after we get the string we check if we want to pad it with blank spaces up to 35 chars
    // note: we only pad the graph with blank spaces so it looks correctly, we don't pad the pie chart
    public String testSubstring(String s, boolean fill) {
        
        
        // if it has spaces at the start remove them 
        // if it is null then we will save it as blank
        
        try {
            // loop until there are no white spaces at the begining of the string
            while (s.charAt(0) == ' ') {
                s = s.substring(1);
            }
        } catch (Exception e) {
            // ignore so the string is nothing
            System.out.println("CATCH NULL STRING EXCEPTION");
            s = "";
        }
        
        // if it is too long
        // then shorten so it is not too many characters
        if (s.length() > 35) {
            s = s.substring(0, 35) + "...";
        }
        
        // fill with blank spaces
        while (s.length() < 35 && fill) {
            s = s + " ";
        }
        
        
        return s;
    }
    
    
    // FUNCTON THAT RESIZES AN IMAGE
    // take a image and resize it to an X and Y     
    public static Image resizeToBig(Image original, int biggerWidth, int biggerHeight, int type) {
        
        Image resizedImage = original;
        
        resizedImage = resizedImage.getScaledInstance(biggerWidth, biggerHeight, type);
        
        return resizedImage;
    }
    
    
    //////////////////////////////////////////
    public static ImageIcon resizeImageAspect(Dimension frame, ImageIcon img, boolean fillFrame) {
         // generate a buffered image so we can measure its size
            Image myImage = img.getImage();
                    
            
            BufferedImage i = toBufferedImage(myImage);
            
            // the X to Y ratio is the aspect (for example 4:3 or 16:9)
            
            // EITHER FILL THE FRAME
            if (fillFrame) {
                // if the images X to Y is greater than the frames X to Y then scale based upon the Y axis so it will fill the whole thing
                // otherwise scale it the other direction

                if ((double)i.getWidth()/(double)i.getHeight() >= (double)frame.getWidth()/(double)frame.getHeight()) {
                    // assuming that the image is proportionally more widescreen than the 800*600 frame we scale the image to match the Y direction and be a proportional X
                    System.out.println(":::: " + (double)((double)frame.getHeight()/(double)i.getHeight())*i.getWidth() + "," + frame.getHeight());

                    myImage = resizeToBig(myImage, (int)((double)((double)frame.getHeight()/(double)i.getHeight())*i.getWidth()), (int)frame.getHeight(), java.awt.Image.SCALE_SMOOTH);

                } else {
                    System.out.println(":::: " + frame.getWidth() + "," + (int)((double)((double)frame.getWidth()/(double)i.getWidth())*i.getHeight()));

                    // in the other dimension
                    myImage = resizeToBig(myImage, (int)frame.getWidth(), (int)((double)((double)frame.getWidth()/(double)i.getWidth())*i.getHeight()), java.awt.Image.SCALE_SMOOTH);
                }
            } 
            // OR INSTEAD FIT INSIDE THE FRAME
            else {
                // if the images X to Y is greater than the frames X to Y (that means we should scale based up the X direction so it all fits)
                // otherwise we will scale assuming the image is taller than it is wide and so we scale by the Y direction
                
                if ((double)i.getWidth()/(double)i.getHeight() >= (double)frame.getWidth()/(double)frame.getHeight()) {
                    
                    System.out.println(":::: " + frame.getWidth() + "," + (int)((double)((double)frame.getWidth()/(double)i.getWidth())*i.getHeight()));

                    myImage = resizeToBig(myImage, (int)frame.getWidth(), (int)((double)((double)frame.getWidth()/(double)i.getWidth())*i.getHeight()), java.awt.Image.SCALE_SMOOTH);
                    
                } else {
                    System.out.println(":::: " + (double)((double)frame.getHeight()/(double)i.getHeight())*i.getWidth() + "," + frame.getHeight());

                    myImage = resizeToBig(myImage, (int)((double)((double)frame.getHeight()/(double)i.getHeight())*i.getWidth()), (int)frame.getHeight(), java.awt.Image.SCALE_SMOOTH);

                }
            }
            
            return new ImageIcon(myImage);
    }
    
    private void summaryComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_summaryComboBoxItemStateChanged
        /// TEST ///
        /*for (String[] xDimention : summaryMatrix) {
            for (int y = 0; y < xDimention.length; y++) {
                System.out.println("Summary:" + xDimention[y]);
            }
        }*/
        
        System.out.println("Messing with summary box and now it is : " + summaryComboBox.getSelectedItem());
        
        // CHECK FOR ERRORS
        // ensure that it doesn't break
        if (summaryType == null) {
            summaryType = "";
        }
        
        // if something has changed (they didn't click the same one) then we load it
        // we make this check so we don't waste time reloading what we already have
        if (!summaryType.equals(summaryComboBox.getSelectedItem())) {
            
            summaryType = (String) summaryComboBox.getSelectedItem(); //store the new summary type

            System.out.println("SUMMARY: " + summaryType);

            // SPINNING CURSOR
            // during the process changes the mouse cursor
            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));

            // summarize the current stuff in the text field
            //SummaryButton.setEnabled(false);

            // when they click this we give them a summary of the data if it is valid
                    // valid means that the USER is "All users found"
                    // AND the SURVEY is NOT "All questions found"
                    // AND the summaryMatrix has been filled with something
                    if (userListBox.getSelectedIndex() == 0
                            && surveyListBox.getSelectedIndex() != 0
                            && summaryMatrix[1][0] != null) {

                        // if they gave choosen item 0 (all users found) then we can give a summary
                        createSummaryOfResults();

                        //then put the scrollbar at the top again
                        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                           public void run() { 
                               jScrollPane1.getVerticalScrollBar().setValue(0);
                           }
                        });   
                    }

            // NORMAL CURSOR
            // at the end changes the cursor back
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_summaryComboBoxItemStateChanged
                    
    //////////
    
    public void FireAction() {
        // during the process changes the mouse cursor
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                // when the search button is hit

                // (clear previous stuff)
                    //** reset the summaryMatrix **
                    summaryMatrix = null;
                    summaryMatrix = new String[100][6];

                    summaryTally = null;
                    summaryTally = new int[100][6];
                    
                    // if a summary is possible
                    if (userListBox.getSelectedIndex() == 0 && surveyListBox.getSelectedIndex() != 0) {
                        summaryComboBox.setEnabled(true);
                    } else {
                        summaryComboBox.setEnabled(false);
                    }
                    
                summaryType = ""; // reset summary type
                summaryComboBox.setSelectedIndex(-1);
                jLabel3.setText("Get Summary:");
                
                System.out.println("SUMMARY: " + summaryType);

                ShowResults();
                
                
        // at the end changes the cursor back
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    public void createSummaryOfResults() {
        // if there is actually something in the field to get a summary of
        if (FieldUserInfo.getText() != null && !FieldUserInfo.getText().equals("")) {
            // SETUP VARIABLES
                // first establish what survey we are summarizing
                String surveyName = surveyListBox.getItemAt(surveyListBox.getSelectedIndex());

                // then get how many users there are (check how many people answered the first question of the survey)
                numberOfUsers = 0;
                
                for (int i = 0; i < summaryTally[0].length; i++) {
                    numberOfUsers += summaryTally[1][i];
                }


            // THEN
            // Clear text field
                FieldUserInfo.setText("");

            // THEN
            // Figure out what type of summary to display
            if ("Text Summary".equals(summaryComboBox.getSelectedItem())) {
                generateTextSummary(surveyName);
                
            } else if ("Pie Chart Summary".equals(summaryComboBox.getSelectedItem())) {
                generatePieChart(surveyName);
                
            } else if ("Bar Graph Summary".equals(summaryComboBox.getSelectedItem())) {
                generateGraphChart(surveyName);
                
            }
            

        }
    }
    
    public void generateTextSummary(String surveyName) {
        // setup a stringbuilder to store all the text to be put in the file
        StringBuilder summaryTextString = new StringBuilder();
                
        // #1 Write the first line which summarizes results

            // if the word survey is not included in the survey name (Ex. "Baptist Triunal") then we add the word "Survey" -> "Baptist Triunal Survey"
            String surveyString = " survey";
            if (surveyName.contains("survey")) {
                surveyString = "";
            }

            System.out.println("NUm: " + numberOfUsers);
            String firstLine = "<b>" + numberOfUsers + "</b> users took the <i>" + surveyName + "</i>" + surveyString + ":";

            summaryTextString.append(firstLine);

        // #2 Write a line about each question
            // iterate through all the questions (we search through the summary matrix until we get something that is null
            int j = 1; // start from 1 and go through the questions
            while (summaryMatrix[j][0] != null) {
                // write down the question first (ex. Question 1 ("what is the name of your pet"):)
                String nextQuestion = "<br><br><b>Question " + j + "</b> <i>(" + summaryMatrix[j][0] + "):</i>";

                summaryTextString.append(nextQuestion);

                // create a unordered array to store our stuff in
                String[] unorderedArray = new String[5];


                // now iterate through all the results for this question (we search through this line of the summary matrix down the line)
                // for example item [j][0] is the Q, item [j][1] is the first answer item [j][2] is the next...
                for (int a = 1; a < summaryMatrix[j].length; a++) {

                    if (summaryTally[j][a] != 0) {

                        // get the tally and the number of users as float variables
                        float tally = summaryTally[j][a];
                        float users = numberOfUsers;

                        double percent = tally*100/users; // calculate a percentage variables

                        percent = round(percent, 1); // round the percentage

                        // store the data about this answer option in are unordered array
                        unorderedArray[a -1] = "Option " + a + " <i>(" + summaryMatrix[j][a] + ")</i> was choosen <b>" + summaryTally[j][a] + " times</b> (" + percent + "%)";
                    }
                }

                // THEN get rid of null elements in array (because likely we will have some answers or things that have 0% answers or are null
                List<String> list = new ArrayList<>();

                for(String s : unorderedArray) {
                    if(s != null && s.length() > 0) {
                        list.add(s);
                    }
                }

                // then asign our unordered array this new shrunk list
                unorderedArray = list.toArray(new String[list.size()]);


                Comparator<String> comp = new Comparator<String>() {
                    @Override
                    public int compare(String a, String b) {
                        // parse the integers to compare with out of the strings
                        int itemB = Integer.parseInt(b.substring(b.indexOf("was choosen <b>") + 15, b.indexOf(" times</b>")));
                        int itemA = Integer.parseInt(a.substring(a.indexOf("was choosen <b>") + 15, a.indexOf(" times</b>")));

                        // compare those integers
                        return Integer.valueOf(itemB).compareTo(Integer.valueOf(itemA));
                    }
                };
                // sort the array
                Arrays.sort(unorderedArray, comp);

                // put stuff into the next question string
                for (String unorderedArray1 : unorderedArray) {
                    String nextLine = "<br>   " + unorderedArray1;
                    summaryTextString.append(nextLine);
                }


                j ++; // check the next one
            }

        // #3 lastly write the text to the field
        FieldUserInfo.setText(HTML + summaryTextString.toString());
    }
    
    
    public void generateGraphChart(String surveyName) {
        jScrollPane1.getVerticalScrollBar().setBlockIncrement(500); // TODO lock to the increment
        
        // #1 Write the first line which summarizes results

        // if the word survey is not included in the survey name (Ex. "Baptist Triunal") then we add the word "Survey" -> "Baptist Triunal Survey"
        String surveyString = " survey";
        if (surveyName.contains("survey")) {
            surveyString = "";
        }

        String firstLine = "<b>" + numberOfUsers + "</b> users took the <i>" + surveyName + "</i>" + surveyString + ":";
        FieldUserInfo.setText(HTML + firstLine + "\n<center>");  // and center all the charts

        // #2 ITERATE THROUGH THE SURVEY QUESTIONS
        int quesNum = 1; // start from 1 and go through the questions
        while (summaryMatrix[quesNum][0] != null) {


            DefaultCategoryDataset dataset = new DefaultCategoryDataset( );  

            final String category = "";  // for now it is blank   
            
            // GRAPH CHECK VALUE (STRING) LENGTH
            // if the value's lengths add to less than 50 then we leave them (all on 1 line)
            // ELSE we extend them each to 50 chars so they are 'each' on 1 line
            boolean fillBlank = false;
            int totalChars = 0;

            for (int i = 1; i <= 5; i++) {
                try {
                    totalChars += summaryMatrix[quesNum][i].length();
                } catch (Exception e) {
                    // oops... no more chars
                    // this means the option was null and there are no more options
                    break;
                }
            }

            // 35 is the cut off
            if (totalChars >= 35) {
                fillBlank = true;
            }
            // ----

            dataset = graphTestAdd(dataset, quesNum , 1, category, fillBlank);
            dataset = graphTestAdd(dataset, quesNum , 2, category, fillBlank);
            dataset = graphTestAdd(dataset, quesNum , 3, category, fillBlank);
            dataset = graphTestAdd(dataset, quesNum , 4, category, fillBlank);
            dataset = graphTestAdd(dataset, quesNum , 5, category, fillBlank);
            

            Image img = new GraphChartImage(summaryMatrix[quesNum][0], dataset).chart;

            int x = 450;
            int y = 450;

            // get teh document
            StyledDocument doc = FieldUserInfo.getStyledDocument();

            //  Add a new line
            try
            {
                doc.insertString(doc.getLength(), "\n\n", null );
            }
            catch(Exception e) { System.out.println(e); }
            
            // FieldUserInfo.insertIcon ( resizeImageAspect(new Dimension(x,y), new ImageIcon(img), false) ); //new ImageIcon( resizeToBig(img, x, y,  java.awt.Image.SCALE_SMOOTH) ));
            
            JLabel j = new JLabel();
            j.setIcon(resizeImageAspect(new Dimension(x,y), new ImageIcon(img), false));
            
            FieldUserInfo.insertComponent( j );
            
            quesNum++;
        }
    }
    
    private DefaultCategoryDataset graphTestAdd(DefaultCategoryDataset dataset, int quesNum, int quesPart, String category, boolean fillBlank) {
              
        
        // if some people actually answered it
        if (summaryTally[quesNum][quesPart] > 0) {
            dataset.addValue( summaryTally[quesNum][quesPart] , testSubstring(summaryMatrix[quesNum][quesPart], fillBlank) , category );
        } else {
            
            System.out.println("GOGOGO");
        }
        
        return dataset;
    }
    
    public DefaultPieDataset pieTestAdd(DefaultPieDataset data, String name, int value) {
        // if its 0 then we don't add it
        if (value > 0) {
            data.setValue(name, value);
        }
        
        return data;
    }
    
    public void generatePieChart(String surveyName) {
        jScrollPane1.getVerticalScrollBar().setBlockIncrement(500); // TODO lock to the increment
        
        // #1 Write the first line which summarizes results

        // if the word survey is not included in the survey name (Ex. "Baptist Triunal") then we add the word "Survey" -> "Baptist Triunal Survey"
        String surveyString = " survey";
        if (surveyName.contains("survey")) {
            surveyString = "";
        }

        String firstLine = "<b>" + numberOfUsers + "</b> users took the <i>" + surveyName + "</i>" + surveyString + ":";
        FieldUserInfo.setText(HTML + firstLine + "\n<center>");  // and center all the charts

        // #2 ITERATE THROUGH THE SURVEY QUESTIONS
        int quesNum = 1; // start from 1 and go through the questions
        while (summaryMatrix[quesNum][0] != null) {


            DefaultPieDataset data = new DefaultPieDataset();


            data = pieTestAdd(data, testSubstring(summaryMatrix[quesNum][1], false), summaryTally[quesNum][1]);
            data = pieTestAdd(data, testSubstring(summaryMatrix[quesNum][2], false), summaryTally[quesNum][2]);
            data = pieTestAdd(data, testSubstring(summaryMatrix[quesNum][3], false), summaryTally[quesNum][3]);
            data = pieTestAdd(data, testSubstring(summaryMatrix[quesNum][4], false), summaryTally[quesNum][4]);
            data = pieTestAdd(data, testSubstring(summaryMatrix[quesNum][5], false), summaryTally[quesNum][5]);

            Image img = new PieChartImage(summaryMatrix[quesNum][0], data).getChart();


            // get teh document
            StyledDocument doc = FieldUserInfo.getStyledDocument();

            //  Add a new line
            try
            {
                doc.insertString(doc.getLength(), "\n\n", null );
            }
            catch(Exception e) { System.out.println(e); }
            
            // we set the x,y manually here because by default the pie is stretched into an oval ;)
            int x = 480;
            int y = 400;
            
            //FieldUserInfo.insertIcon ( );
            
            
            JLabel j = new JLabel();
            j.setIcon(new ImageIcon( resizeToBig(img, x, y,  java.awt.Image.SCALE_SMOOTH) )); //resizeImageAspect(new Dimension(x,y), new ImageIcon(img), false));
            
            FieldUserInfo.insertComponent( j );
            
            quesNum++;
        }
        
        
    }
       
    public static double round(double value, int numberOfDigitsAfterDecimalPoint) {
        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(numberOfDigitsAfterDecimalPoint,
                BigDecimal.ROUND_HALF_UP);
        return bigDecimal.doubleValue();
    }    
    
    public void saveMap(String textToSave) {
        // ---- LOAD/DEAL WITH PREVIOUS SAVED LOCATION ----
            String savedLocation = "/Documents";

            StringBuilder newSaveFile = new StringBuilder();

            int linesInSaveFile = 0;
            
            // load the data file and find the remembered directory
            BufferedReader read = null;
            try {
                // ****** System.out.printlnln("Trying to find quesload");
                read = new BufferedReader(new FileReader(pathway + "/QuesLoad.txt"));

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

                // ****** System.out.printlnln("Save Location*: " + savedLocation);

                read.close();
            } catch (Exception ex) {
                //if this happens there is no quesLoad file so make one
                //          *IF THERE IS NO QUESLOAD*
                // ****** System.out.printlnln("couldn't find quesload");

                // well that sucks, somebody corrupted the save file since opening the program (it is also checked in the main frame)

                // oh well just give an error
                versionLabel.setText("(Error: please restart program)");

            }

            // IF THERE IS NO SAVED LOCATION but 4 other lines give an error message and fix it
            if ((savedLocation == null | "".equals(savedLocation)) && linesInSaveFile == 4) {
                // ****** System.out.printlnln("BIG PROBLEM!!");

                versionLabel.setText("(Error: setting file rectified, please try again)");

                try {
                    // NOW WE HAVE TO ADD A FIFTH LINE to the file
                    FileWriter appender = new FileWriter(pathway + "/QuesLoad.txt" , true);

                    appender.write("\r\n/Documents"); // add a fifth line

                    // THIS IS SUPER IMPORTANT because previous versions didn't have this fifth line
                    savedLocation = "/Documents"; // and fix the problem in the variable which is null

                    appender.close();
                } catch (IOException ex) {
                    Logger.getLogger(AnswerFrame.class.getName()).log(Level.SEVERE, null, ex);
                    // if this happens their is something seriously wrong!

                    versionLabel.setText("(Error: please restart program, if error persists: support at nathansoftware.com)");
                }
            }

            // ****** System.out.printlnln("Save Location*: " + savedLocation);
        
        // ----- ACTUALLY SAVE THE STUFF ----
        System.out.println("Actually saving the stuff");
        
        System.out.println("SB: " + textToSave);
        
        String sb2 = textToSave.replaceAll("\n", "\r\n"); //make it so that all the next lines that appear in the text field are properly translated when being put in the text document
        
        /* *** CURRENTLY UNUSED ***
        // fix the date to put into the files name
        String fileNameDate = theDate;
        
        fileNameDate = fileNameDate.replace("/","-"); // we want to remove any forward slashes
        //fileNameDate = fileNameDate.substring(0, fileNameDate.indexOf(":") - 3); // and cut off the time part of the date (everything the colon (and also the 3 characters right before the colon)) so just to have the day-month-year
        // **** */
        
        // load the name and survey from the list boxes
        String userNameVar = userListBox.getItemAt(userListBox.getSelectedIndex());
        String surveyToGet = surveyListBox.getItemAt(surveyListBox.getSelectedIndex());
        String summaryText;
        
        // check that the text is not null (it will be null if no summary is selected)
        if (summaryComboBox.getSelectedItem() == null) {
            summaryText = ""; // if null than ignore
        } else {
            summaryText = " - " + (String) summaryComboBox.getSelectedItem(); // otherwise write the answer
        }
        
        System.out.println("Now creating file chooser");
        
        JFileChooser chooser = new JFileChooser();
        // ****** System.out.printlnln("Save Location*: " + savedLocation);
        // set the directory to be at the saved location and set the field to contain the file name of the current save info
        chooser.setSelectedFile(new File(savedLocation + "/" + "Results - " + userNameVar + " - " + surveyToGet + summaryText)); //TODO add date info
        int retrival = chooser.showSaveDialog(null);
        
        // IF YOU SELECT THE APPROVE OPTION IT DOES ALL THIS::
        if (retrival == JFileChooser.APPROVE_OPTION) {
            System.out.println("APPROVE");
            
            
            String fileEx = "";
            // if the files doesn't contain a dot assume the person didn't put an extension
            if (!chooser.getSelectedFile().getName().contains(".")) {
                fileEx = ".html";
            }
            System.out.println("APPROVE " + chooser.getSelectedFile().getName());
            
            
            // CREATE THE RESULTS FILE
            try {
                
                BufferedWriter fw = new BufferedWriter(new FileWriter(chooser.getSelectedFile().getAbsolutePath() + fileEx));
                fw.write(sb2);
                fw.close();
                
                System.out.println("APPROVE " + chooser.getSelectedFile().getAbsolutePath() + fileEx);
            } catch (Exception ex) {
                System.out.println("FAIL");
            }
        
            // now save the location selected for next time
            savedLocation = chooser.getSelectedFile().getParent(); //get parent just gets the path
            
            // ****** System.out.printlnln("SV : " + savedLocation);


            // add to my file string the saved location (we do this so that the next time it will show you the same folder)
            newSaveFile.append(savedLocation);

            // write the new save file location so we can access it next time
            try {
                BufferedWriter write = new BufferedWriter(new FileWriter(pathway + "/QuesLoad.txt"));
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
    private javax.swing.JLabel InfoLabel2;
    private javax.swing.JLabel InfoLabel3;
    private javax.swing.JButton LicenseLabel;
    private javax.swing.JLabel MainLabel;
    private javax.swing.JButton SaveButton;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> summaryComboBox;
    private javax.swing.JComboBox<String> surveyListBox;
    private javax.swing.JComboBox<String> userListBox;
    private javax.swing.JLabel versionLabel;
    // End of variables declaration//GEN-END:variables
}
