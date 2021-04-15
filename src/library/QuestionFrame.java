/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.text.*;

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
public class QuestionFrame extends javax.swing.JFrame {

    // -- //
    MainStartup MS = new MainStartup();
    public String version = MS.version; //THIS REALLY SHOULD BE CHANGED EACH NEW VERSION
    
    String pathway = "C:\\Ques";
    // -- //
    
    public Dimension size = getSize();
    public String userName = "";
    public String question = "First";
    public int QuesNum = 1;
    public String QuestionString = "";
    public String QuestionAnswer = "";
    public int lineOn = 0;
    
    public String questionFileName = "";
        
    public String questionFile = "";
    public String imageFile = "";
    public String introLine = "";
    
    public boolean userImage = false;
    public boolean continueWithSave = false;
    
    // progress bar total
    public int progressTotal = 1;
    public int progressCurrent = 0;
    
    /**
     * Creates new form QuestionFrame
     */
    public QuestionFrame() {        
        initComponents();
        extraComponents();
        
        versionLabel.setText("Copyright NathanSoftware.com version " + version);
        
        getRootPane().setDefaultButton(GoButton);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        
        beginProgram();
    }
    
    public void beginProgram() {
               
            
        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathway + "/QuesLoad.txt"));
            
            questionFile = reader.readLine();
            
            questionFileName = questionFile.substring(0, questionFile.indexOf(".csv"));

            
            // the name is found between the last / and the last "."
            if (questionFileName.contains("/")) {
                
                questionFileName = questionFileName.substring(questionFileName.lastIndexOf("/") + 1);
            } 
            // but we also check for forward slashes just in case
            if (questionFileName.contains("\\")) {
                questionFileName = questionFileName.substring(questionFileName.lastIndexOf("\\") + 1);
            }        
            
            
            //next get the image
            imageFile = reader.readLine();
            
            ImageIcon imageIcon = null;
            try {
                imageIcon = new ImageIcon(getClass().getResource(imageFile));
            } catch (Exception e) {
                //if it breaks its because the program couldn't load the class resources of location imageFile
                //so instead try to load it as a absolute path
                imageIcon = new ImageIcon(imageFile);
                userImage = true;
                
            }
            
            
            if ("Default".equals(imageFile)) {
                //do nothing leave the default image
            } else {
                MainPicture.setSize(680, 510);
                if (userImage == true) { //if its a user image then resize it to fit the top half of the screen
                    
                    /*ImageDrawer ID = new ImageDrawer();
                    
                    Graphics g = getGraphics();
                    
                    Image I = new ImageIO(imageFile);
                    
                    ID.drawScaledImage(I, this, g);*/
                    
                    MainPicture.setSize(680, 340);
                }
                MainPicture.setIcon(imageIcon);
            }
                   
            String lines = reader.readLine();
            
            //then get the first line, which is the intro line
            introLine = lines;
            
            if ("Default".equals(introLine)) {
                //if its the default then set it as such
                introLine = "Welcome to the Easy Survey Creator by Nathan Rais.";
            } else {
               // otherwise do nothing because its already what it should be
            }
            
            reader.close();
        } catch (Exception e) { //if theres any errors it must mean the QuesLoad file couldn't be loaded
            
            // ****** System.out.printlnln(e);
            
        }
        
        //checkForQuestionFile();
        
        // it says "This is the XXX survey." 
        //      unless you have written the word survey in the title
        //      then it says "This is the XXX". asuming that XXX contains the word survey.
        
        String surveyString = " survey";
        if (questionFileName.toLowerCase().contains("survey")) {
            surveyString = "";
        }
         
        // ****** System.out.printlnln("Setting up intro stuff, intro:" + introLine);
        
        int i = 1; // if it is not greater than 68 it is just 1 line
        if(introLine.length() > 68) { //if the intro is too long
            // then wrap the intro line

            // break the line every 68 characters with a newline char
            introLine = WordUtils.wrap(introLine, 68);

            // ****** System.out.printlnln("Too long question bumbing to next line");

            // break the string at any new line characters
            String[] lines = introLine.split("\n");
            // the number of different string created is the number of lines
            i = lines.length;
        }
        
        //then it opens with a question about your name
        String QuestionLine = introLine + "\nThis is the " + questionFileName + surveyString + ". What is your name?";
        
        // move the question line up so it doesn't run into the text field
        // for each newline it has been wrapped, move up the label by 20
        QuestionLabel.setLocation(10, 360 - (20*i));
        QuestionLabel.setText(QuestionLine);
        
    }
    
    public boolean checkForQuestionFile() {
        File f = new File(questionFile); //if there is a question file then do nothing
        if(f.exists() && !f.isDirectory()) {
            setupProgressBar();
            
            return true; // if it doesn't break return true
        }
        else { //otherwise bring up an error because the program cant run
            // kill the question frame and reload the menu
            goToMainMenu();
            
            // ****** System.out.printlnln("Error");
            JFrame error = new JFrame("");
            error.setAlwaysOnTop(true);
            JLabel errorLabel = new JLabel("ERROR: No Question File");
            errorLabel.setSize(180,100);
            errorLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            error.setSize(180,100);
            error.setLocationRelativeTo(null);
            error.setVisible(true);
            error.add(errorLabel);
            return false; // if it breaks return false
        }
    }
    
    // FUNCTION to Read the current question (run for each question in a survey)
    public void readQuestionFile(int questionNumber) throws FileNotFoundException, IOException {
        BOption1.setVisible(true);
        BOption2.setVisible(true);
        BOption3.setVisible(true);
        BOption4.setVisible(true);
        BOption5.setVisible(true);   
        FOption1.setVisible(true);
        FOption2.setVisible(true);
        FOption3.setVisible(true);
        FOption4.setVisible(true);
        FOption5.setVisible(true);   
        
        // basically this function opens and reads the file up to the question you are on
        // then it reads the question you are on and shows it
        
        if (checkForQuestionFile()) { //first check for a question file

            BufferedReader in = new BufferedReader(new FileReader(questionFile));

            //up to the QuesNum line read the lines and then continue
            for (int i = 1; i < QuesNum; i++) {
                in.readLine(); // read all the lines up to the question numbers line and just skip them
            }
            //when on the QuesNum line store it
            String line = in.readLine();


            //first figure out which question your on QuesNum

            String[] questionArray = new String[7];

            //then read that line of the file and split it and store it in a array
            try {
                // fill the question array with the items from the line splitting
                // Note: if there are not enough items the last few in the array will be null and ignored
                // Note: if there are too many items then it will break and end the survey
                System.arraycopy(line.split(","), 0, questionArray, 0, line.split(",").length);
                
                for (int i = 1; i < questionArray.length; i++) {
                    // ****** System.out.printlnln("Question " + i + ": " + questionArray[i]);
                }
            } catch (Exception e) {
                // ****** System.out.printlnln("Break cause their are no more questions");

                //if this breaks then it means there is nothing in the line (or too much in the line) so its just a fluke and doesn't actually have a question therefore just end
                QuestionLabel.setText("There are no more questions. Thank you " + userName + " for answering this survey.");
                question = "BeforeLast";
                GoButton.setText("Close");
            }

            //then display the different fields of the array into the text fields and radio buttons
            try {
                // ****** System.out.printlnln("Survey showing question");

                // first check if we need to par it
                String textLine = questionArray[1];
                QuestionString = questionArray[1]; //assign our text line BEFORE we wrap the line. This variable then is stored in the save file.
                // if the WordUtils.wrap'd line was used above then it would put new line chars in the save file
                int i = 1;           

                if(questionArray[1].length() > 68) { //if the questions too long
                    // then wrap the question

                    // break the line every 68 characters with a newline char
                    textLine = WordUtils.wrap(textLine, 68);

                    // ****** System.out.printlnln("Too long question bumbing to next line");

                    // break the string at any new line characters
                    String[] lines = textLine.split("\n");
                    // the number of different string created is the number of lines
                    i = lines.length;
                }            
                QuestionLabel.setText(textLine);

                // for each newline it has been wrapped, move up the label by 20
                QuestionLabel.setLocation(10, 380 - 20*i);

            } catch (Exception e) {
                QuestionLabel.setText("There are no more questions. Thank you " + userName + " for answering this survey.");
                question = "BeforeLast";
                GoButton.setText("Close");
            }
            
            setupOption(BOption1, FOption1, "(A)", questionArray[2]);
            setupOption(BOption2, FOption2, "(B)", questionArray[3]);
            setupOption(BOption3, FOption3, "(C)", questionArray[4]);
            setupOption(BOption4, FOption4, "(D)", questionArray[5]);
            setupOption(BOption5, FOption5, "(E)", questionArray[6]);


            //finnally close the reader
            in.close();
        }
    }
    
    // basically we get the option from the save file
    // if it contains "__" it is editable so stick the text in their and make that editable visible
    // else if it doesn't then stick the text in BOption checkbox
    // otherwise if their is no question at all hide it
    public void setupOption(JCheckBox boxPart, JTextField typePart, String letter, String arraySegment) {
        String option = arraySegment; 
        
        if (arraySegment == null) {
            // everything is hidden because there is no option
            boxPart.setVisible(false);
            typePart.setVisible(false);
        } else if (arraySegment.contains("__")) {
            option = arraySegment.substring(2); // remove the first two _ if it is a typing question
            
            // everything is left showing
            boxPart.setSelected(false);
            typePart.setText(option);
            boxPart.setText(letter + "  ");
            
        } else {
            // the typing part is hidden but the checkbox is left showing
            typePart.setVisible(false);
            boxPart.setSelected(false);
            boxPart.setText(letter + "  " + option);
        }
        
    }
    
    // this runs all the extra functions i want that i cant put into the initComponents part of the form because
    // it is locked from all editing
    private void extraComponents() {
        //get system variables
        if (!SystemUtils.IS_OS_WINDOWS) {
            pathway = SystemUtils.USER_HOME + "/Ques";
            // ****** System.out.printlnln("Home " + pathway);
        }
        
        Dimension size;
        
        
        ErrorLabel.setVisible(false);
        
        //setExtendedState(JFrame.MAXIMIZED_BOTH); 
        //frame.setUndecorated(true); //if unremoved then this hides the menu bar at the top
        setVisible(true);
        setSize(720, 620);
        setResizable(false);
        setLocationRelativeTo(null);
                
        Container c = getContentPane();               
        c.setBackground(Color.white);
        
        Container c2 = overwriteFileFrame.getContentPane();               
        c2.setBackground(Color.white);
        
        Container c3 = areYouSureFrame.getContentPane();               
        c3.setBackground(Color.white);
        
        OVFyesButton.setContentAreaFilled(false);
        OVFyesButton.setBorderPainted(false);
        OVFyesButton.setFocusPainted(false);
        OVFyesButton.setOpaque(false);
        
        OVFnoButton.setContentAreaFilled(false);
        OVFnoButton.setBorderPainted(false);
        OVFnoButton.setFocusPainted(false);
        OVFnoButton.setOpaque(false);
        
        RUSureYes.setContentAreaFilled(false);
        RUSureYes.setBorderPainted(false);
        RUSureYes.setFocusPainted(false);
        RUSureYes.setOpaque(false);
        
        RUSureNo.setContentAreaFilled(false);
        RUSureNo.setBorderPainted(false);
        RUSureNo.setFocusPainted(false);
        RUSureNo.setOpaque(false);
        
        ButtonReturn.setContentAreaFilled(false);
        ButtonReturn.setBorderPainted(false);
        ButtonReturn.setFocusPainted(false);
        ButtonReturn.setOpaque(false);  
        
        GoButton.setContentAreaFilled(false);
        GoButton.setBorderPainted(false);
        GoButton.setFocusPainted(false);
        GoButton.setOpaque(false);  
        
        
        BOption1.setVisible(false);
        BOption1.setSelectedIcon(new ImageIcon(getClass().getResource("/resources/ball-green.png")));
        BOption2.setVisible(false);
        BOption2.setSelectedIcon(new ImageIcon(getClass().getResource("/resources/ball-green.png")));
        BOption3.setVisible(false);
        BOption3.setSelectedIcon(new ImageIcon(getClass().getResource("/resources/ball-green.png")));
        BOption4.setVisible(false);
        BOption4.setSelectedIcon(new ImageIcon(getClass().getResource("/resources/ball-green.png")));
        BOption5.setVisible(false);
        BOption5.setSelectedIcon(new ImageIcon(getClass().getResource("/resources/ball-green.png"))); 
        FOption1.setVisible(false);
        FOption2.setVisible(false);
        FOption3.setVisible(false);
        FOption4.setVisible(false);
        FOption5.setVisible(false);  
        
        // Listen for changes in the text
        FOption1.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                // select all the text in focus
                FOption1.select(0, FOption1.getText().length());
                // check the box
                BOption1.setSelected(true);
                BOption1ActionPerformed(null);
            }

            @Override
            public void focusLost(FocusEvent e) {
                FOption1.select(0, 0);
            }
        });// Listen for changes in the text
        FOption2.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                // select all the text in focus
                FOption2.select(0, FOption2.getText().length());
                // check the box
                BOption2.setSelected(true);
                BOption2ActionPerformed(null);
            }

            @Override
            public void focusLost(FocusEvent e) {
                FOption2.select(0, 0);
            }
        });// Listen for changes in the text
        FOption3.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                // select all the text in focus
                FOption3.select(0, FOption3.getText().length());
                // check the box
                BOption3.setSelected(true);
                BOption3ActionPerformed(null);
            }

            @Override
            public void focusLost(FocusEvent e) {
                FOption3.select(0, 0);
            }
        });// Listen for changes in the text
        FOption4.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                // select all the text in focus
                FOption4.select(0, FOption4.getText().length());
                // check the box
                BOption4.setSelected(true);
                BOption4ActionPerformed(null);
            }

            @Override
            public void focusLost(FocusEvent e) {
                FOption4.select(0, 0);
            }
        });
        // Listen for changes in the text
        FOption5.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                // select all the text in focus
                FOption5.select(0, FOption5.getText().length());
                // check the box
                BOption5.setSelected(true);
                BOption5ActionPerformed(null);
            }

            @Override
            public void focusLost(FocusEvent e) {
                FOption5.select(0, 0);
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

        overwriteFileFrame = new javax.swing.JFrame();
        jLabel5 = new javax.swing.JLabel();
        OVFyesButton = new javax.swing.JButton();
        OVFnoButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        areYouSureFrame = new javax.swing.JFrame();
        jLabel6 = new javax.swing.JLabel();
        RUSureYes = new javax.swing.JButton();
        RUSureNo = new javax.swing.JButton();
        versionLabel = new javax.swing.JLabel();
        ErrorLabel = new javax.swing.JLabel();
        ButtonReturn = new javax.swing.JButton();
        GoButton = new javax.swing.JButton();
        FOption1 = new javax.swing.JTextField();
        FOption2 = new javax.swing.JTextField();
        FOption3 = new javax.swing.JTextField();
        FOption4 = new javax.swing.JTextField();
        FOption5 = new javax.swing.JTextField();
        InputField = new javax.swing.JTextField();
        BOption1 = new javax.swing.JCheckBox();
        BOption2 = new javax.swing.JCheckBox();
        BOption3 = new javax.swing.JCheckBox();
        BOption4 = new javax.swing.JCheckBox();
        BOption5 = new javax.swing.JCheckBox();
        QuestionLabel = new javax.swing.JTextPane();
        MainPicture = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        jLabel5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel5.setText("Would you like to overwrite this save file?");

        OVFyesButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/ballYes.png"))); // NOI18N
        OVFyesButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                OVFyesButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                OVFyesButtonMouseExited(evt);
            }
        });
        OVFyesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OVFyesButtonActionPerformed(evt);
            }
        });

        OVFnoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/ballNo.png"))); // NOI18N
        OVFnoButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                OVFnoButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                OVFnoButtonMouseExited(evt);
            }
        });
        OVFnoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OVFnoButtonActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel1.setText("A file with this username already exists");

        javax.swing.GroupLayout overwriteFileFrameLayout = new javax.swing.GroupLayout(overwriteFileFrame.getContentPane());
        overwriteFileFrame.getContentPane().setLayout(overwriteFileFrameLayout);
        overwriteFileFrameLayout.setHorizontalGroup(
            overwriteFileFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(overwriteFileFrameLayout.createSequentialGroup()
                .addGroup(overwriteFileFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(overwriteFileFrameLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(OVFyesButton)
                        .addGap(42, 42, 42)
                        .addComponent(OVFnoButton))
                    .addGroup(overwriteFileFrameLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5))
                    .addGroup(overwriteFileFrameLayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jLabel1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        overwriteFileFrameLayout.setVerticalGroup(
            overwriteFileFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(overwriteFileFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addGap(9, 9, 9)
                .addGroup(overwriteFileFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(OVFnoButton)
                    .addComponent(OVFyesButton))
                .addContainerGap(183, Short.MAX_VALUE))
        );

        jLabel6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel6.setText("Would you like to exit this survey?");

        RUSureYes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/ballYes.png"))); // NOI18N
        RUSureYes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                RUSureYesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                RUSureYesMouseExited(evt);
            }
        });
        RUSureYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RUSureYesActionPerformed(evt);
            }
        });

        RUSureNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/ballNo.png"))); // NOI18N
        RUSureNo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                RUSureNoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                RUSureNoMouseExited(evt);
            }
        });
        RUSureNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RUSureNoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout areYouSureFrameLayout = new javax.swing.GroupLayout(areYouSureFrame.getContentPane());
        areYouSureFrame.getContentPane().setLayout(areYouSureFrameLayout);
        areYouSureFrameLayout.setHorizontalGroup(
            areYouSureFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(areYouSureFrameLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(areYouSureFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addGroup(areYouSureFrameLayout.createSequentialGroup()
                        .addComponent(RUSureYes)
                        .addGap(42, 42, 42)
                        .addComponent(RUSureNo)))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        areYouSureFrameLayout.setVerticalGroup(
            areYouSureFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(areYouSureFrameLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addGroup(areYouSureFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(RUSureNo)
                    .addComponent(RUSureYes))
                .addContainerGap(183, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx32.gif")));
        setSize(new java.awt.Dimension(0, 0));
        getContentPane().setLayout(null);

        versionLabel.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        versionLabel.setText("Copyright NathanSoftware.com version 2.0.6.1");
        getContentPane().add(versionLabel);
        versionLabel.setBounds(480, 10, 220, 14);

        ErrorLabel.setForeground(new java.awt.Color(255, 0, 0));
        ErrorLabel.setText("Please type a different name so you do not overwrite previous save files (ex. add a number or last name)");
        getContentPane().add(ErrorLabel);
        ErrorLabel.setBounds(10, 440, 600, 14);

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
        getContentPane().add(ButtonReturn);
        ButtonReturn.setBounds(10, 10, 63, 40);

        GoButton.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        GoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/nextIcon.jpg"))); // NOI18N
        GoButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                GoButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                GoButtonMouseExited(evt);
            }
        });
        GoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GoButtonActionPerformed(evt);
            }
        });
        getContentPane().add(GoButton);
        GoButton.setBounds(480, 480, 70, 60);

        FOption1.setBackground(new Color(0,0,0,0));
        FOption1.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        FOption1.setText("enter text here");
        FOption1.setBorder(null);
        getContentPane().add(FOption1);
        FOption1.setBounds(120, 390, 550, 30);

        FOption2.setBackground(new Color(0,0,0,0));
        FOption2.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        FOption2.setText("enter text here");
        FOption2.setBorder(null);
        getContentPane().add(FOption2);
        FOption2.setBounds(120, 420, 550, 30);

        FOption3.setBackground(new Color(0,0,0,0));
        FOption3.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        FOption3.setText("enter text here");
        FOption3.setBorder(null);
        getContentPane().add(FOption3);
        FOption3.setBounds(120, 450, 550, 30);

        FOption4.setBackground(new Color(0,0,0,0));
        FOption4.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        FOption4.setText("enter text here");
        FOption4.setBorder(null);
        getContentPane().add(FOption4);
        FOption4.setBounds(120, 480, 550, 30);

        FOption5.setBackground(new Color(0,0,0,0));
        FOption5.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        FOption5.setText("enter text here");
        FOption5.setBorder(null);
        getContentPane().add(FOption5);
        FOption5.setBounds(120, 510, 550, 30);
        getContentPane().add(InputField);
        InputField.setBounds(60, 390, 420, 30);

        BOption1.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        BOption1.setText("(A)");
        BOption1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/ball-blue.png"))); // NOI18N
        BOption1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BOption1ActionPerformed(evt);
            }
        });
        getContentPane().add(BOption1);
        BOption1.setBounds(60, 390, 590, 30);

        BOption2.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        BOption2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/ball-blue.png"))); // NOI18N
        BOption2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BOption2ActionPerformed(evt);
            }
        });
        getContentPane().add(BOption2);
        BOption2.setBounds(60, 420, 590, 30);

        BOption3.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        BOption3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/ball-blue.png"))); // NOI18N
        BOption3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BOption3ActionPerformed(evt);
            }
        });
        getContentPane().add(BOption3);
        BOption3.setBounds(60, 450, 590, 30);

        BOption4.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        BOption4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/ball-blue.png"))); // NOI18N
        BOption4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BOption4ActionPerformed(evt);
            }
        });
        getContentPane().add(BOption4);
        BOption4.setBounds(60, 480, 590, 30);

        BOption5.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        BOption5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/ball-blue.png"))); // NOI18N
        BOption5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BOption5ActionPerformed(evt);
            }
        });
        getContentPane().add(BOption5);
        BOption5.setBounds(60, 510, 590, 30);

        QuestionLabel.setEditable(false);
        QuestionLabel.setBackground(new Color(0,0,0,0));
        QuestionLabel.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        QuestionLabel.setText("The txt\nmrgin");
        QuestionLabel.setOpaque(false);
        getContentPane().add(QuestionLabel);
        QuestionLabel.setBounds(10, 360, 550, 180);

        MainPicture.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/Monkey.jpg"))); // NOI18N
        getContentPane().add(MainPicture);
        MainPicture.setBounds(40, 0, 680, 510);

        progressBar.setRequestFocusEnabled(false);
        getContentPane().add(progressBar);
        progressBar.setBounds(20, 560, 670, 20);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void GoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GoButtonActionPerformed
        
        // reset font of the text fields (so that any that have been selected are now clearly no longer selected)
        BOption1.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        BOption2.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        BOption3.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        BOption4.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        BOption5.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        
        // ****** System.out.printlnln("Go Button pressed");
        
        // if the user did something bad ;) then don't save right away but run some checks
        if (continueWithSave == false) {
            
            // ****** System.out.printlnln("Not continueing with save, yet");
            
            userName = InputField.getText();
            
            if (userNameCheck()) { //if the user name is valid
                File saveFileName = new File(pathway + "/Save_" + userName + "_" + questionFileName + ".sur");

                if (saveFileName.exists()) {
                    checkForOverwrite();
                } else {
                    // ****** System.out.printlnln("Tested and their are no duplicates. Now continueing");
                    continueWithSave = true;

                    checkForGo();
                }
            }
        }
        else {
            // ****** System.out.printlnln("Continueing with save, stage: " + question);
            
            userName = InputField.getText();
            
            if (userNameCheck()) { //if the user name is valid
                checkForGo();
            }
        }
    }//GEN-LAST:event_GoButtonActionPerformed

    private void BOption1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BOption1ActionPerformed
        // when you select this button it bolds the font to make it clear which is choosen
        BOption1.setFont(new java.awt.Font("SansSerif", 1, 14));
        if (BOption1.isEnabled() == true) {
            if (BOption1.isSelected() == false) {
                BOption1.setSelected(true);
            } else {
                // when you select this button it un-bolds the font of the rest to make it clear which is choosen
                BOption2.setFont(new java.awt.Font("SansSerif", 0, 12));
                BOption3.setFont(new java.awt.Font("SansSerif", 0, 12));
                BOption4.setFont(new java.awt.Font("SansSerif", 0, 12));
                BOption5.setFont(new java.awt.Font("SansSerif", 0, 12));
                BOption2.setSelected(false);
                BOption3.setSelected(false);
                BOption4.setSelected(false);
                BOption5.setSelected(false);
            }
        }

    }//GEN-LAST:event_BOption1ActionPerformed

    private void BOption2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BOption2ActionPerformed
        // when you select this button it bolds the font to make it clear which is choosen
        BOption2.setFont(new java.awt.Font("SansSerif", 1, 14));        
        if (BOption2.isEnabled() == true) {
            if (BOption2.isSelected() == false) {
                BOption2.setSelected(true);
            } else {
                // when you select this button it un-bolds the font of the rest to make it clear which is choosen
                BOption1.setFont(new java.awt.Font("SansSerif", 0, 12));
                BOption3.setFont(new java.awt.Font("SansSerif", 0, 12));
                BOption4.setFont(new java.awt.Font("SansSerif", 0, 12));
                BOption5.setFont(new java.awt.Font("SansSerif", 0, 12));
                BOption1.setSelected(false);
                BOption3.setSelected(false);
                BOption4.setSelected(false);
                BOption5.setSelected(false);
            }
        }

    }//GEN-LAST:event_BOption2ActionPerformed

    private void BOption3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BOption3ActionPerformed
        // when you select this button it bolds the font to make it clear which is choosen
        BOption3.setFont(new java.awt.Font("SansSerif", 1, 14));
        if (BOption3.isEnabled() == true) {
            if (BOption3.isSelected() == false) {
                BOption3.setSelected(true);
            } else {
                // when you select this button it un-bolds the font of the rest to make it clear which is choosen
                BOption1.setFont(new java.awt.Font("SansSerif", 0, 12));
                BOption2.setFont(new java.awt.Font("SansSerif", 0, 12));
                BOption4.setFont(new java.awt.Font("SansSerif", 0, 12));
                BOption5.setFont(new java.awt.Font("SansSerif", 0, 12));
                BOption1.setSelected(false);
                BOption2.setSelected(false);
                BOption4.setSelected(false);
                BOption5.setSelected(false);
            }
        }

    }//GEN-LAST:event_BOption3ActionPerformed

    private void BOption4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BOption4ActionPerformed
        // when you select this button it bolds the font to make it clear which is choosen
        BOption4.setFont(new java.awt.Font("SansSerif", 1, 14));
        if (BOption4.isEnabled() == true) {
            if (BOption4.isSelected() == false) {
                BOption4.setSelected(true);
            } else {
                // when you select this button it un-bolds the font of the rest to make it clear which is choosen
                BOption1.setFont(new java.awt.Font("SansSerif", 0, 12));
                BOption2.setFont(new java.awt.Font("SansSerif", 0, 12));
                BOption3.setFont(new java.awt.Font("SansSerif", 0, 12));
                BOption5.setFont(new java.awt.Font("SansSerif", 0, 12));
                BOption1.setSelected(false);
                BOption2.setSelected(false);
                BOption3.setSelected(false);
                BOption5.setSelected(false);
            }
        }

    }//GEN-LAST:event_BOption4ActionPerformed

    private void BOption5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BOption5ActionPerformed
        if (BOption5.isEnabled() == true) {
            // when you select this button it bolds the font to make it clear which is choosen
            BOption5.setFont(new java.awt.Font("SansSerif", 1, 14));
            if (BOption5.isSelected() == false) {
                BOption5.setSelected(true);
            } else {
                // when you select this button it un-bolds the font of the rest to make it clear which is choosen
                BOption2.setFont(new java.awt.Font("SansSerif", 0, 12));
                BOption3.setFont(new java.awt.Font("SansSerif", 0, 12));
                BOption4.setFont(new java.awt.Font("SansSerif", 0, 12));
                BOption1.setFont(new java.awt.Font("SansSerif", 0, 12));
                BOption1.setSelected(false);
                BOption2.setSelected(false);
                BOption3.setSelected(false);
                BOption4.setSelected(false);
            }
        }

    }//GEN-LAST:event_BOption5ActionPerformed

    private void OVFyesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OVFyesButtonActionPerformed
        continueWithSave = true;
        
        overwriteFileFrame.dispose();
        
        checkForGo();
    }//GEN-LAST:event_OVFyesButtonActionPerformed

    private void OVFnoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OVFnoButtonActionPerformed
        continueWithSave = false;
        ErrorLabel.setVisible(true);
        ErrorLabel.setText("Please type a different name so you do not overwrite previous save files (ex. add a number or last name)");
        
        
        overwriteFileFrame.dispose();
    }//GEN-LAST:event_OVFnoButtonActionPerformed

    private void OVFyesButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OVFyesButtonMouseEntered
        OVFyesButton.setIcon(new ImageIcon(getClass().getResource("/resources/ballYesSelected.png")));
    }//GEN-LAST:event_OVFyesButtonMouseEntered

    private void OVFyesButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OVFyesButtonMouseExited
        OVFyesButton.setIcon(new ImageIcon(getClass().getResource("/resources/ballYes.png")));
    }//GEN-LAST:event_OVFyesButtonMouseExited

    private void OVFnoButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OVFnoButtonMouseEntered
        OVFnoButton.setIcon(new ImageIcon(getClass().getResource("/resources/ballNoSelected.png")));
    }//GEN-LAST:event_OVFnoButtonMouseEntered

    private void OVFnoButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OVFnoButtonMouseExited
        OVFnoButton.setIcon(new ImageIcon(getClass().getResource("/resources/ballNo.png")));
    }//GEN-LAST:event_OVFnoButtonMouseExited

    private void ButtonReturnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonReturnMouseEntered
        ButtonReturn.setIcon(new ImageIcon(getClass().getResource("/resources/homeIconSelected.jpg")));
    }//GEN-LAST:event_ButtonReturnMouseEntered

    private void ButtonReturnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonReturnMouseExited
        ButtonReturn.setIcon(new ImageIcon(getClass().getResource("/resources/homeIcon.jpg")));
    }//GEN-LAST:event_ButtonReturnMouseExited

    private void ButtonReturnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonReturnActionPerformed
        // display the are you sure message
        areYouSureFrame.dispose(); // first ensre there isn't already an active message
        
        areYouSureFrame.setVisible(true);
        areYouSureFrame.setSize(279, 157);
        areYouSureFrame.setResizable(false);
        areYouSureFrame.setLocationRelativeTo(null);
        
        List<Image> icons = new ArrayList<>();
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx16.png")));
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx32.gif")));
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx64.gif")));
        areYouSureFrame.setIconImages(icons);
    }//GEN-LAST:event_ButtonReturnActionPerformed

    private void GoButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_GoButtonMouseEntered
        GoButton.setIcon(new ImageIcon(getClass().getResource("/resources/nextIconSelected.jpg")));
    }//GEN-LAST:event_GoButtonMouseEntered

    private void GoButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_GoButtonMouseExited
        GoButton.setIcon(new ImageIcon(getClass().getResource("/resources/nextIcon.jpg")));
    }//GEN-LAST:event_GoButtonMouseExited

    private void RUSureYesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RUSureYesMouseEntered
        RUSureYes.setIcon(new ImageIcon(getClass().getResource("/resources/ballYesSelected.png")));
    }//GEN-LAST:event_RUSureYesMouseEntered

    private void RUSureYesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RUSureYesMouseExited
        RUSureYes.setIcon(new ImageIcon(getClass().getResource("/resources/ballYes.png")));
    }//GEN-LAST:event_RUSureYesMouseExited

    private void RUSureYesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RUSureYesActionPerformed
        goToMainMenu();
    }//GEN-LAST:event_RUSureYesActionPerformed

    private void RUSureNoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RUSureNoMouseEntered
        RUSureNo.setIcon(new ImageIcon(getClass().getResource("/resources/ballNoSelected.png")));
    }//GEN-LAST:event_RUSureNoMouseEntered

    private void RUSureNoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RUSureNoMouseExited
        RUSureNo.setIcon(new ImageIcon(getClass().getResource("/resources/ballNo.png")));
    }//GEN-LAST:event_RUSureNoMouseExited

    private void RUSureNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RUSureNoActionPerformed
        areYouSureFrame.dispose();
    }//GEN-LAST:event_RUSureNoActionPerformed

    public boolean userNameCheck() {
        boolean continueNow = true;
        
        // check for illegal characters in the user name
        if (userName.contains("\\")
                | userName.contains("/")
                | userName.contains(":")
                | userName.contains("*")
                | userName.contains("?")
                | userName.contains("\"")
                | userName.contains("<")
                | userName.contains(">")
                | userName.contains("|")
                ) {

            ErrorLabel.setText("A user name can't contain any of the following characters \\ / : * ? \" < > |");
            ErrorLabel.setVisible(true);

            // then wait for them to fix it and don't do anything
            continueNow = false;
        }
        // check for a too long/too short user name
        else if (userName.length() > 15) {
            ErrorLabel.setText("A user name must be less than 15 characters, add initials instead of your full name");
            ErrorLabel.setVisible(true);
            
            // then wait for them to fix it and don't do anything
            continueNow = false;
        }
        // CHECK IF THE USER NAME IS NOT BLANK
        else if (userName == null | userName.length() < 3) {
            ErrorLabel.setText("A user name must be more than 3 characters");
            ErrorLabel.setVisible(true);
            
            // then don't do anything
            continueNow = false;
        }
        else {
            // if it doesn't break on any of the other ones then we are good
            
            // CUSS WORD FILTER
            // check for cuss words and delete them
            // ****** System.out.printlnln("USERNAME NAME NAME: " + userName);

            // replace all cuss words (Note: the Pattern.quote() function is used to ensure that the string doesn't have any problems when finished)
            userName = userName.replaceAll("(?i)fuck", "");
            userName = userName.replaceAll("(?i)shit", "");
            userName = userName.replaceAll("(?i)piss", "");
            userName = userName.replaceAll("(?i)cunt", "");
            userName = userName.replaceAll("(?i)ass", "");
            userName = userName.replaceAll("(?i)crap", "");
            userName = userName.replaceAll("(?i)bitch", "");
            userName = userName.replaceAll("(?i)cock", "");
            userName = userName.replaceAll("(?i)penis", "");
            userName = userName.replaceAll("(?i)sex", "");

            // ****** System.out.printlnln("USERNAME NAME NAME: " + userName);
        }
        
        return continueNow;
    }
    
    
    public void setupProgressBar() {
        progressTotal = 1; // first reset it (it starts at 1 because there is a end screen)
        try {

            // check how many questions there are
            BufferedReader in = new BufferedReader(new FileReader(questionFile));

            boolean eof = false; // end of file (when you get the end it stops)

            while (!eof) {
                // read through the file till the end and count how many lines

                // we know the end because it reaches a null statement
                if(in.readLine() == null) {
                    eof = true;
                }
                // if its not a null then we count it
                else {
                    progressTotal++;
                }
            }

            // ****** System.out.printlnln("Prog: " + progressTotal);

            in.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(QuestionFrame.class.getName()).log(Level.SEVERE, null, ex);
            // this shouldn't happen because we only run this procedure if we have already confirmed a file exists
        } catch (IOException ex) {
            // this could happen... but i don't know why it would
            // perhaps it would happen if we get to a null line
            // then we would just ignore it
        }
        
        // setup the progress bar to use the progress total
        progressBar.setMaximum(progressTotal);
        progressBar.setValue(progressCurrent);
        this.repaint();
    }
    
    public void goToMainMenu() {
        // first ask if they are sure
        
        
        EasySurveyMenu ESF = new EasySurveyMenu();
        
        List<Image> icons = new ArrayList<>();
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx16.png")));
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx32.gif")));
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx64.gif")));
        ESF.setIconImages(icons);
        
        ESF.setVisible(true);
        dispose();
        
        areYouSureFrame.dispose();
    }
    
    public void checkForOverwrite() {
        overwriteFileFrame.setVisible(true);
        overwriteFileFrame.setSize(279, 157);
        overwriteFileFrame.setResizable(false);
        overwriteFileFrame.setLocationRelativeTo(null);
        
        List<Image> icons = new ArrayList<>();
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx16.png")));
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx32.gif")));
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx64.gif")));
        overwriteFileFrame.setIconImages(icons);
    }
    
    public void checkForGo() {
        boolean continueQues = true;
        ErrorLabel.setVisible(false);

        if (null != question) {
            progressCurrent ++; // increment the progress bar
            
            // Once you begin (i.e click the arrow) then we remove the home button so you can't go back again
            ButtonReturn.setVisible(false);
            
            switch (question) {

                case "Middle":
                    StringBuilder saveFileLinesSB = new StringBuilder();
                    //when you click this button first it stores what box you've checked
                    if (BOption1.isSelected()) {
                        //selectedBox = "A";
                        QuestionAnswer = BOption1.getText();
                        // if their is a text field being used then we get that text as well
                        if (FOption1.isVisible()) {
                            QuestionAnswer = BOption1.getText() + "__" + FOption1.getText(); // NOTE: we use the __ so we can remember it is a type-in-option
                        }
                    } else if (BOption2.isSelected()) {
                        //selectedBox = "B";
                        QuestionAnswer = BOption2.getText();
                        // if their is a text field being used then we get that text as well
                        if (FOption2.isVisible()) {
                            QuestionAnswer = BOption2.getText() + "__" + FOption2.getText(); // NOTE: we use the __ so we can remember it is a type-in-option
                        }
                    } else if (BOption3.isSelected()) {
                        //selectedBox = "C";
                        QuestionAnswer = BOption3.getText();
                        // if their is a text field being used then we get that text as well
                        if (FOption3.isVisible()) {
                            QuestionAnswer = BOption3.getText() + "__" + FOption3.getText(); // NOTE: we use the __ so we can remember it is a type-in-option
                        }
                    } else if (BOption4.isSelected()) {
                        //selectedBox = "D";
                        QuestionAnswer = BOption4.getText();
                        // if their is a text field being used then we get that text as well
                        if (FOption4.isVisible()) {
                            QuestionAnswer = BOption4.getText() + "__" + FOption4.getText(); // NOTE: we use the __ so we can remember it is a type-in-option
                        }
                    } else if (BOption5.isSelected()) {
                        //selectedBox = "E";
                        QuestionAnswer = BOption5.getText();
                        // if their is a text field being used then we get that text as well
                        if (FOption5.isVisible()) {
                            QuestionAnswer = BOption5.getText() + "__" + FOption5.getText(); // NOTE: we use the __ so we can remember it is a type-in-option
                        }
                    } else { //if none are selected then don't continue
                        continueQues = false;
                    }   
                    
                    File saveFile = new File(pathway + "/Save_" + userName + "_" + questionFileName + ".sur");

                    // if we have a valid selection then we continue and save the answer and increment
                    if (continueQues != false) {

                        BufferedReader newReader = null; //create a file reader (to read the file duh!)
                        try {
                            if (saveFile.exists()) { //if there is a save file then read it

                                newReader = new BufferedReader(new FileReader(saveFile));

                                // Don't overwrite the answer to the previous questions
                                // read all the lines up to the line you're on and store them in the string builder
                                for (int i = 1; i < QuesNum; i++) {
                                    String line = newReader.readLine();
                                    saveFileLinesSB.append(line).append("\r\n");
                                }
                                newReader.close(); // kill the reader
                            }
                            else { //otherwise create a new file
                                try {
                                    PrintWriter writer = new PrintWriter(saveFile, "UTF-8");
                                    writer.close();
                                } catch (Exception e) {
                                    // ****** System.out.printlnln("Couldn't create a new file");
                                }
                            }
                        } catch (Exception ex) {}

                        // then get the current answer
                        String answerLetter = QuestionAnswer.substring(0, QuestionAnswer.indexOf(' '));
                        String answerString = (QuestionAnswer.substring(QuestionAnswer.indexOf(" ") + 1));
                        saveFileLinesSB.append(QuesNum).append("," + QuestionString).append("," + answerLetter).append("," + answerString).append("\r\n");


                        String saveFileLines = saveFileLinesSB.toString();

                        //then open the save file and write to it

                        try {
                            BufferedWriter out = new BufferedWriter(new FileWriter(pathway + "/Save_" + userName + "_" + questionFileName + ".sur"));
                            out.write(saveFileLines); //rewrite the file with your lines instead
                            out.close();
                        } catch (IOException ex) {
                            Logger.getLogger(QuestionFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }


                        //then it loads the next question
                        QuesNum++;


                        // NOTE: now it trys to read another question
                        try {
                            readQuestionFile(QuesNum); //run the method readQuestonFile on question number (quesNum)
                        } catch (IOException ex) {
                            Logger.getLogger(QuestionFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }   break;



                // ----------    
                // THE FIRST LINE OF THE SURVEY ASKS THEIR NAME
                // ----------
                case "First":
                    // for the initial question we then load the question file
                    
                    ErrorLabel.setText("");

                    if (continueWithSave == true) {

                        InputField.setVisible(false);

                        BOption1.setVisible(true);
                        BOption2.setVisible(true);
                        BOption3.setVisible(true);
                        BOption4.setVisible(true);
                        BOption5.setVisible(true);

                        question = "Middle";
                        // ****** System.out.printlnln("Setting stage to middle!");


                        // NOW IT TRYS TO READ ANOTHER QUESTION
                        try {
                            readQuestionFile(QuesNum);
                        } catch (IOException ex) {
                            Logger.getLogger(QuestionFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                    
                    break;

                case "BeforeLast":
                    // then now we end the survey giver and recreat the main menu
                    dispose();
                    EasySurveyMenu ESF = new EasySurveyMenu();
                    List<Image> icons = new ArrayList<>();
                    icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx16.png")));
                    icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx32.gif")));
                    icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx64.gif")));
                    ESF.setIconImages(icons);
                    ESF.setVisible(true);
                    break;
                default:
                    break;

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
            java.util.logging.Logger.getLogger(QuestionFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QuestionFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QuestionFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QuestionFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new QuestionFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox BOption1;
    private javax.swing.JCheckBox BOption2;
    private javax.swing.JCheckBox BOption3;
    private javax.swing.JCheckBox BOption4;
    private javax.swing.JCheckBox BOption5;
    private javax.swing.JButton ButtonReturn;
    private javax.swing.JLabel ErrorLabel;
    private javax.swing.JTextField FOption1;
    private javax.swing.JTextField FOption2;
    private javax.swing.JTextField FOption3;
    private javax.swing.JTextField FOption4;
    private javax.swing.JTextField FOption5;
    private javax.swing.JButton GoButton;
    private javax.swing.JTextField InputField;
    private javax.swing.JLabel MainPicture;
    private javax.swing.JButton OVFnoButton;
    private javax.swing.JButton OVFyesButton;
    private javax.swing.JTextPane QuestionLabel;
    private javax.swing.JButton RUSureNo;
    private javax.swing.JButton RUSureYes;
    private javax.swing.JFrame areYouSureFrame;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JFrame overwriteFileFrame;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel versionLabel;
    // End of variables declaration//GEN-END:variables
}
