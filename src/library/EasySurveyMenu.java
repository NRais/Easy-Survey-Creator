/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;

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
public class EasySurveyMenu extends javax.swing.JFrame {

    
    Frame passCheckFrame = null;
    String password = "";
    String dateLine = "";
    Date oldDate = null;
    int monthsToUpdate = 6;
    
    int dateY;
    int dateM;
    String defaultLines = "C:\\Ques\\Default.csv\r\n/resources/Monkey.jpg\r\nDefault\r\nDefault\r\n/Documents";
    String defaultCSVFileLines = "1,How are you doing today?,Very Good, Okay, Not that good, Terrible\r\n";
    boolean correctPassword = false;

    // -- //
    MainStartup MS = new MainStartup();
    public String version = MS.version;
    // -- //
    
    
    public boolean alreadyUpdateAsk = false;
    
    public void alreadyUpdateAsked() {
        alreadyUpdateAsk = true;
        System.out.println("update asked changed to true it now is: " + alreadyUpdateAsk);
    }
            
    int passTrys = 0;
    /**
     * Creates new form EasySurveyMenu
     */
     
    public EasySurveyMenu() {
        
        initComponents();
        
        Container cMain = this.getContentPane();               
        cMain.setBackground(Color.white);
        
        versionLabel.setText("Copyright NathanSoftware.com version " + version);
        
        this.setSize(720,500);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        
        getPassword.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if ("Enter password in this field".equals(getPassword.getText())) {
                    getPassword.selectAll();
                }
            }
        });

        enterPassword1.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if ("Enter password in this field".equals(enterPassword1.getText())) {
                    enterPassword1.selectAll();
                }
            }
        });
        
        updateFrame.getRootPane().setDefaultButton(ButtonWebsiteUpdate);
        getPasswordFrame.getRootPane().setDefaultButton(GPcreateNewSurveyButton);
        
        passwordFrame.setResizable(false);
        passwordFrame.setLocationRelativeTo(null);
        passwordFrame.setSize(371,183);
        
        passwordFrame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        
        getPasswordFrame.setResizable(false);
        getPasswordFrame.setLocationRelativeTo(null);
        getPasswordFrame.setSize(371,193);
        
        getPasswordFrame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                              
        buttonBackRemoval(LicenseLabel);
        buttonBackRemoval(GPHelpButton);
        buttonBackRemoval(PFnoButton1);
        buttonBackRemoval(PFyesButton1);
        buttonBackRemoval(ButtonWebsiteUpdate);
        buttonBackRemoval(ButtonWebsite);
        buttonBackRemoval(ButtonConductSurvey);
        buttonBackRemoval(ButtonAnalyseData);
        buttonBackRemoval(ButtonHelp);
        buttonBackRemoval(ButtonCreateSurvey);
        buttonBackRemoval(GPcreateNewSurveyButton);
        
                

        prestart();
        
        System.out.println("update asked? - " + alreadyUpdateAsk);
        
    }
    
    public void buttonBackRemoval(JButton button) {
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);        
    }
    
    public void runCheckDate() {
            checkDate();
    }
    
    public void prestart() {
        /// FOR LOADING THE PASSWORD AND VERIFYING QUESLOAD ///
        BufferedReader read = null;
        try {
            System.out.println("Trying to find quesload");
            read = new BufferedReader(new FileReader("C:\\Ques\\QuesLoad.txt"));
            
            //read the first three lines and skip
            read.readLine(); read.readLine(); read.readLine();
            
            //then store the fourth line which is the password line
            password = read.readLine();
            
            read.close();
        } catch (FileNotFoundException ex) {
            //if this happens there is no quesLoad file so make one
            //          *IF THERE IS NO QUESLOAD*
            System.out.println("couldn't find quesload");
            
            PrintWriter creator = null;
            try {
                File f = new File("C:\\Ques\\");
                f.mkdirs();

                creator = new PrintWriter("C:\\Ques\\QuesLoad.txt", "UTF-8");
            } catch (Exception e) {}
            creator.close();
            
            try {
                BufferedWriter write = new BufferedWriter(new FileWriter("C:\\Ques\\QuesLoad.txt"));
                write.write(defaultLines);
                write.close();
            } catch (IOException ex1) {
                Logger.getLogger(EasySurveyMenu.class.getName()).log(Level.SEVERE, null, ex1);
            }
            
        } catch (IOException ex) {
            System.out.println("no fourth line");
            //if this happens there is no forth line
            Logger.getLogger(EasySurveyMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        /// FOR DEFAULT CSV FILE ///
        BufferedReader readDefaultCSV = null;
        try {
            System.out.println("Trying to find quesload");
            readDefaultCSV = new BufferedReader(new FileReader("C:\\Ques\\Default.csv"));
            
            //read the first line for posterity's sake :)
            //just kidding its read to make sure that this file isn't broken
            readDefaultCSV.readLine();
                        
            readDefaultCSV.close();
        } catch (FileNotFoundException ex) {
            //if this happens there is no file
            CreateDefaultCSV();
            
        } catch (IOException ex) {
            System.out.println("No file Default.csv file obviously");
            //if this happens there is no line
            CreateDefaultCSV();
        }
        
        /// FOR CHECKING DATE INSTALLED ///
        BufferedReader readDateCheck = null;
        try {
            String versionNumber = "";
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            
            System.out.println("Trying to find date file");
            readDateCheck = new BufferedReader(new FileReader("Data.dat"));
            
            dateLine = readDateCheck.readLine(); //to make sure there is a line and to store it
            versionNumber = readDateCheck.readLine();
            
            if (versionNumber == null) {//if this happens then there is no 2nd line for the version number so make one
                versionNumber = version;
                CreateDateFile();
            }
            
            System.out.println("Version: " + version + " File Version: " + versionNumber);
            
            readDateCheck.close();
            
            // CHECK IF A NEW VERSION HAS BEEN INSTALLED
            // IF the version in the program and data file are equal
            // THEN we will store the date
            // ----
            // Otherwise the version number in the data file is different then the program
            // THEN we can assume that a new version has just been installed and we will reset the data file
            if (versionNumber.equals(version)) {

                System.out.println(dateLine);

                String[] dateStuff = dateLine.split("/");



                if (dateStuff[0].startsWith("0")) {
                    //if this is true then its 01,02,03,04,... so it won't register properly as an int
                    dateStuff[0] = dateStuff[0].substring(1); //then change it so it doesn't have the zero
                    System.out.println(dateStuff[0]);
                }
                if (dateStuff[2].contains(" ")) {
                    //if this is true then it 2016 xx:xx:xx so it won't register properly as an int
                    dateStuff[2] = dateStuff[2].substring(0 , dateStuff[2].indexOf(" ")); //then change it so it goes to the space and then stops
                    System.out.println(dateStuff[2]);
                }

                try {
                    oldDate = sdf.parse(dateLine); //turn the dateLine into a date and store it
                } catch (ParseException ex) {
                    Logger.getLogger(EasySurveyMenu.class.getName()).log(Level.SEVERE, null, ex);
                }

                // the Integer.parseInt below converts the string from the dateStuff array to an integer
                dateM = Integer.parseInt(dateStuff[0]); //this should be the month so store it as such
                dateY = Integer.parseInt(dateStuff[2]); //this should be the year so store it as such

            }
            else {//got the wrong date file so create a new one
                CreateDateFile();
            }
            
        } catch (FileNotFoundException ex) {
            //IF THERES NO FILE CREATE ONE
            
            CreateDateFile();
        } catch (IOException ex) {
            //IF THERES NO WORDS IN THE FILE WRITE SOME
            try {
                //first create a date / time format
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                //then open the file
                BufferedWriter writeDateCheck = new BufferedWriter(new FileWriter("Data.dat"));
                //then store the date
                System.out.println(new Date());
                String date = sdf.format(new Date());
                //then write to the file
                writeDateCheck.write(date);
                //then write the version number on the next line
                writeDateCheck.write("\r\n" + version);
                writeDateCheck.close(); //the close the file
                
                dateLine = date;
                
                try {
                    oldDate = sdf.parse(dateLine); //turn the dateLine into a date and store it
                    
                    storeOldDates(date);
                } catch (ParseException exD) {
                    Logger.getLogger(EasySurveyMenu.class.getName()).log(Level.SEVERE, null, exD);
                }
            } catch (IOException ex1) {CreateDateFile();}//this shouldn't throw an exception because i've already check for this file but if it does that means we need to creat this file (again)
            
        }
    }
    
        public void storeOldDates(String date) {
            String[] dateStuff = date.split("/");

            if (dateStuff[0].startsWith("0")) {
                //if this is true then its 01,02,03,04,... so it won't register properly as an int
                dateStuff[0] = dateStuff[0].substring(1); //then change it so it doesn't have the zero
                System.out.println(dateStuff[0]);
            }
            if (dateStuff[2].contains(" ")) {
                //if this is true then it 2016 xx:xx:xx so it won't register properly as an int
                dateStuff[2] = dateStuff[2].substring(0 , dateStuff[2].indexOf(" ")); //then change it so it goes to the space and then stops
                System.out.println(dateStuff[2]);
            }

            // the Integer.parseInt below converts the string from the dateStuff array to an integer
            dateM = Integer.parseInt(dateStuff[0]); //this should be the month so store it as such
            dateY = Integer.parseInt(dateStuff[2]); //this should be the year so store it as such
        }
    
    public void CreateDateFile() {
        //create a file creater
        PrintWriter creator = null;
        try {
            //create file
            creator = new PrintWriter("Data.dat", "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {}
        creator.close(); //close file creater

        try {
            //first create a date / time format
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                
            BufferedWriter write = new BufferedWriter(new FileWriter("Data.dat"));
            //get the current date
            System.out.println(new Date());
            String date = sdf.format(new Date());
            //then write to the file
            write.write(date);
            write.write("\r\n" + version);
            write.close();
            
            dateLine = date;
            
            try {
                oldDate = sdf.parse(dateLine); //turn the dateLine into a date and store it
                
                storeOldDates(date);
            } catch (ParseException exD) {
                Logger.getLogger(EasySurveyMenu.class.getName()).log(Level.SEVERE, null, exD);
            }

        } catch (IOException ex1) {
            //hmm... this probably only would get an error if it didn't have permissions
            //i can't do anything about that
            Logger.getLogger(EasySurveyMenu.class.getName()).log(Level.SEVERE, null, ex1);
        }
    }
    
    public void CreateDefaultCSV() {
        //if this happens there is no default.csv file so make one
            //       *IF THERE IS NO DEFAULT CSV*
            System.out.println("couldn't find default csv");
            
            PrintWriter creator = null;
            try {
                File f = new File("C:\\Ques\\");
                f.mkdirs();

                creator = new PrintWriter("C:\\Ques\\Default.csv", "UTF-8");
                
                creator.close();
            } catch (Exception e) {}
            
            
            try {
                BufferedWriter write = new BufferedWriter(new FileWriter("C:\\Ques\\Default.csv"));
                write.write(defaultCSVFileLines);
                write.close();
            } catch (IOException ex1) {
                Logger.getLogger(EasySurveyMenu.class.getName()).log(Level.SEVERE, null, ex1);
            }
    }
    
    public void checkDate() {
        //first get current date
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                
        System.out.println(new Date());
        String date = sdf.format(new Date());
        Date nowDate = new Date();
        
        //spilt the date into its components to be read
        String[] dateStuff = date.split("/");
            
        if (dateStuff[0].startsWith("0")) {
            //if this is true then its 01,02,03,04,... so it won't register properly as an int
            dateStuff[0] = dateStuff[0].substring(1); //then change it so it doesn't have the zero
            System.out.println(dateStuff[0]);
        }
        if (dateStuff[2].contains(" ")) {
            //if this is true then it 2016 xx:xx:xx so it won't register properly as an int
            dateStuff[2] = dateStuff[2].substring(0 , dateStuff[2].indexOf(" ")); //then change it so it goes to the space and then stops
            System.out.println(dateStuff[2]);
        }

        // the Integer.parseInt below converts the string from the dateStuff array to an integer
        int dateCurrentM = Integer.parseInt(dateStuff[0]); //this should be the month so store it as such
        int dateCurrentY = Integer.parseInt(dateStuff[2]); //this should be the year so store it as such
    
        System.out.println("Old date: " + oldDate);
        System.out.println("Old Month: " + dateM);
        System.out.println("Old Year: " + dateY);
        
        if (nowDate.after(oldDate)) {
            //then the new date is after the old one
            if (dateCurrentY > dateY) {
                //then a year has changed
                if (dateCurrentM >= dateM + monthsToUpdate) {
                    //then check if its also been over 6 months
                    //If so show update frame
                    updateDue();
                }
                else if (12 - dateM + dateCurrentM >= monthsToUpdate) { //12 - dateM will get how many months it was since last year when the file was made. For example: 12 - 4 (April) means it has been 8 months
                    // this added to how many months its been since this year has started will get the total months
                    // if its not clearly been more than 6 months then check if the combined year change adds up to more then 6 months
                    
                    //if so then show update frame
                    updateDue();
                }
            }
            else if (dateCurrentM >= dateM + monthsToUpdate) {
                //if it hasn't been a year then check if its been 6 months
                
                //if so show update frame
                updateDue();
            }
        }
    }
    
    public void updateDue() {
        Container cU = updateFrame.getContentPane();               
        cU.setBackground(Color.white);

        System.out.println("Showing update frame");
        updateFrame.setLocationRelativeTo(ButtonCreateSurvey); // this is the centre
        updateFrame.setResizable(false);
        updateFrame.setSize(405,329);
        updateFrame.setAlwaysOnTop(true);
        updateFrame.toFront();
        updateFrame.repaint();
            
        List<Image> icons = new ArrayList<>();
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx16.png")));
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx32.gif")));
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx64.gif")));
        
        updateFrame.setIconImages(icons);
        
        updateFrame.setVisible(true);
        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        passwordFrame = new javax.swing.JDialog();
        jLabel2 = new javax.swing.JLabel();
        PFnoButton1 = new javax.swing.JButton();
        PFyesButton1 = new javax.swing.JButton();
        enterPassword1 = new javax.swing.JTextField();
        getPasswordFrame = new javax.swing.JDialog();
        GPHelpButton = new javax.swing.JButton();
        GPlabel = new javax.swing.JLabel();
        getPassword = new javax.swing.JTextField();
        GPcreateNewSurveyButton = new javax.swing.JButton();
        updateFrame = new javax.swing.JFrame();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        pictureLabel = new javax.swing.JLabel();
        ButtonWebsiteUpdate = new javax.swing.JButton();
        LicenseLabel = new javax.swing.JButton();
        versionLabel = new javax.swing.JLabel();
        LabelMain = new javax.swing.JLabel();
        LabelTT = new javax.swing.JLabel();
        ButtonAnalyseData = new javax.swing.JButton();
        ButtonCreateSurvey = new javax.swing.JButton();
        ButtonConductSurvey = new javax.swing.JButton();
        ButtonHelp = new javax.swing.JButton();
        ButtonWebsite = new javax.swing.JButton();
        Background = new javax.swing.JLabel();

        jLabel2.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Would you like to set a password for the admin tools of this program?");

        PFnoButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/ballNo.png"))); // NOI18N
        PFnoButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                PFnoButton1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                PFnoButton1MouseExited(evt);
            }
        });
        PFnoButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PFnoButton1ActionPerformed(evt);
            }
        });

        PFyesButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/ballYes.png"))); // NOI18N
        PFyesButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                PFyesButton1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                PFyesButton1MouseExited(evt);
            }
        });
        PFyesButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PFyesButton1ActionPerformed(evt);
            }
        });

        enterPassword1.setText("Enter password in this field");
        enterPassword1.setSelectionEnd(0);

        javax.swing.GroupLayout passwordFrameLayout = new javax.swing.GroupLayout(passwordFrame.getContentPane());
        passwordFrame.getContentPane().setLayout(passwordFrameLayout);
        passwordFrameLayout.setHorizontalGroup(
            passwordFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, passwordFrameLayout.createSequentialGroup()
                .addGap(0, 40, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, passwordFrameLayout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addGroup(passwordFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(enterPassword1)
                    .addGroup(passwordFrameLayout.createSequentialGroup()
                        .addComponent(PFyesButton1)
                        .addGap(56, 56, 56)
                        .addComponent(PFnoButton1)))
                .addGap(70, 70, 70))
        );
        passwordFrameLayout.setVerticalGroup(
            passwordFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(passwordFrameLayout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(enterPassword1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addGroup(passwordFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PFyesButton1)
                    .addComponent(PFnoButton1))
                .addContainerGap(168, Short.MAX_VALUE))
        );

        getPasswordFrame.setBackground(new java.awt.Color(255, 255, 255));

        GPHelpButton.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        GPHelpButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/helpIcon.jpg"))); // NOI18N
        GPHelpButton.setToolTipText("Help");
        GPHelpButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                GPHelpButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                GPHelpButtonMouseExited(evt);
            }
        });
        GPHelpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GPHelpButtonActionPerformed(evt);
            }
        });

        GPlabel.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        GPlabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        GPlabel.setText("Enter the password of the admin tools:");

        getPassword.setText("Enter password in this field");
        getPassword.setSelectionEnd(0);

        GPcreateNewSurveyButton.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        GPcreateNewSurveyButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/ball-blue.png"))); // NOI18N
        GPcreateNewSurveyButton.setText("Continue");
        GPcreateNewSurveyButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                GPcreateNewSurveyButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                GPcreateNewSurveyButtonMouseExited(evt);
            }
        });
        GPcreateNewSurveyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GPcreateNewSurveyButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout getPasswordFrameLayout = new javax.swing.GroupLayout(getPasswordFrame.getContentPane());
        getPasswordFrame.getContentPane().setLayout(getPasswordFrameLayout);
        getPasswordFrameLayout.setHorizontalGroup(
            getPasswordFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(getPasswordFrameLayout.createSequentialGroup()
                .addGroup(getPasswordFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(getPasswordFrameLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(getPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(getPasswordFrameLayout.createSequentialGroup()
                        .addGap(130, 130, 130)
                        .addComponent(GPcreateNewSurveyButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, getPasswordFrameLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(getPasswordFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, getPasswordFrameLayout.createSequentialGroup()
                        .addComponent(GPlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(49, 49, 49))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, getPasswordFrameLayout.createSequentialGroup()
                        .addComponent(GPHelpButton)
                        .addGap(64, 64, 64))))
        );
        getPasswordFrameLayout.setVerticalGroup(
            getPasswordFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(getPasswordFrameLayout.createSequentialGroup()
                .addComponent(GPlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(getPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(GPcreateNewSurveyButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 151, Short.MAX_VALUE)
                .addComponent(GPHelpButton)
                .addContainerGap())
        );

        jLabel1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 0, 0));
        jLabel1.setText("NEW UPDATE AVAILABLE");

        jLabel3.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel3.setText("Your version of the Easy Survey Creator is more then 6 months old!");

        pictureLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/MenPlanetMapGraphic.jpg"))); // NOI18N

        ButtonWebsiteUpdate.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        ButtonWebsiteUpdate.setText("Click to download the newest version");
        ButtonWebsiteUpdate.setToolTipText("");
        ButtonWebsiteUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ButtonWebsiteUpdateMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ButtonWebsiteUpdateMouseExited(evt);
            }
        });
        ButtonWebsiteUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonWebsiteUpdateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout updateFrameLayout = new javax.swing.GroupLayout(updateFrame.getContentPane());
        updateFrame.getContentPane().setLayout(updateFrameLayout);
        updateFrameLayout.setHorizontalGroup(
            updateFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updateFrameLayout.createSequentialGroup()
                .addContainerGap(38, Short.MAX_VALUE)
                .addGroup(updateFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, updateFrameLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(91, 91, 91))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, updateFrameLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(41, 41, 41))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, updateFrameLayout.createSequentialGroup()
                        .addComponent(ButtonWebsiteUpdate)
                        .addGap(91, 91, 91))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, updateFrameLayout.createSequentialGroup()
                        .addComponent(pictureLabel)
                        .addGap(66, 66, 66))))
        );
        updateFrameLayout.setVerticalGroup(
            updateFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updateFrameLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(38, 38, 38)
                .addComponent(ButtonWebsiteUpdate)
                .addGap(18, 18, 18)
                .addComponent(pictureLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx32.gif")));
        getContentPane().setLayout(null);

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
        getContentPane().add(LicenseLabel);
        LicenseLabel.setBounds(20, 440, 660, 21);

        versionLabel.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        versionLabel.setText("Copyright NathanSoftware.com version XXXX");
        getContentPane().add(versionLabel);
        versionLabel.setBounds(483, 60, 220, 14);

        LabelMain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/MainLabelSml.png"))); // NOI18N
        getContentPane().add(LabelMain);
        LabelMain.setBounds(-3, 10, 720, 50);

        LabelTT.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        LabelTT.setText(" ");
        getContentPane().add(LabelTT);
        LabelTT.setBounds(390, 190, 109, 19);

        ButtonAnalyseData.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ButtonAnalyseData.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/ball-blue.png"))); // NOI18N
        ButtonAnalyseData.setText("Analyse Data");
        ButtonAnalyseData.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ButtonAnalyseData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ButtonAnalyseDataMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ButtonAnalyseDataMouseExited(evt);
            }
        });
        ButtonAnalyseData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonAnalyseDataActionPerformed(evt);
            }
        });
        getContentPane().add(ButtonAnalyseData);
        ButtonAnalyseData.setBounds(470, 100, 160, 40);

        ButtonCreateSurvey.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ButtonCreateSurvey.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/ball-blue.png"))); // NOI18N
        ButtonCreateSurvey.setText("Create Survey");
        ButtonCreateSurvey.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ButtonCreateSurvey.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ButtonCreateSurveyMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ButtonCreateSurveyMouseExited(evt);
            }
        });
        ButtonCreateSurvey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonCreateSurveyActionPerformed(evt);
            }
        });
        getContentPane().add(ButtonCreateSurvey);
        ButtonCreateSurvey.setBounds(80, 100, 170, 40);

        ButtonConductSurvey.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ButtonConductSurvey.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/ball-green.png"))); // NOI18N
        ButtonConductSurvey.setText("Conduct Survey");
        ButtonConductSurvey.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ButtonConductSurvey.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ButtonConductSurveyMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ButtonConductSurveyMouseExited(evt);
            }
        });
        ButtonConductSurvey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonConductSurveyActionPerformed(evt);
            }
        });
        getContentPane().add(ButtonConductSurvey);
        ButtonConductSurvey.setBounds(270, 100, 180, 40);

        ButtonHelp.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ButtonHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/ball-magenta.png"))); // NOI18N
        ButtonHelp.setText(" Help");
        ButtonHelp.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ButtonHelp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ButtonHelpMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ButtonHelpMouseExited(evt);
            }
        });
        ButtonHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonHelpActionPerformed(evt);
            }
        });
        getContentPane().add(ButtonHelp);
        ButtonHelp.setBounds(80, 210, 110, 40);

        ButtonWebsite.setFont(new java.awt.Font("Arial", 2, 11)); // NOI18N
        ButtonWebsite.setText("Visit NathansSoftware.com");
        ButtonWebsite.setToolTipText("Click to visit the website NathansSoftware.con");
        ButtonWebsite.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ButtonWebsiteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ButtonWebsiteMouseExited(evt);
            }
        });
        ButtonWebsite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonWebsiteActionPerformed(evt);
            }
        });
        getContentPane().add(ButtonWebsite);
        ButtonWebsite.setBounds(470, 220, 170, 23);

        Background.setBackground(new java.awt.Color(255, 255, 255));
        Background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/Menu.jpg"))); // NOI18N
        getContentPane().add(Background);
        Background.setBounds(-10, 0, 740, 450);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ButtonCreateSurveyMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonCreateSurveyMouseEntered
        ButtonCreateSurvey.setForeground(Color.CYAN);
        ButtonCreateSurvey.setIcon(new ImageIcon(getClass().getResource("/resources/ball-blueSelected.png")));
    }//GEN-LAST:event_ButtonCreateSurveyMouseEntered

    private void ButtonCreateSurveyMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonCreateSurveyMouseExited
        ButtonCreateSurvey.setForeground(null);
        ButtonCreateSurvey.setIcon(new ImageIcon(getClass().getResource("/resources/ball-blue.png")));
    }//GEN-LAST:event_ButtonCreateSurveyMouseExited

    private void ButtonAnalyseDataMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonAnalyseDataMouseEntered
        ButtonAnalyseData.setForeground(Color.CYAN);
        ButtonAnalyseData.setIcon(new ImageIcon(getClass().getResource("/resources/ball-blueSelected.png")));
    }//GEN-LAST:event_ButtonAnalyseDataMouseEntered

    private void ButtonAnalyseDataMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonAnalyseDataMouseExited
        ButtonAnalyseData.setForeground(null);
        ButtonAnalyseData.setIcon(new ImageIcon(getClass().getResource("/resources/ball-blue.png")));
    }//GEN-LAST:event_ButtonAnalyseDataMouseExited

    private void ButtonHelpMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonHelpMouseEntered
        ButtonHelp.setForeground(Color.CYAN);
        ButtonHelp.setIcon(new ImageIcon(getClass().getResource("/resources/ball-magentaSelected.png")));
    }//GEN-LAST:event_ButtonHelpMouseEntered

    private void ButtonHelpMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonHelpMouseExited
        ButtonHelp.setForeground(null);
        ButtonHelp.setIcon(new ImageIcon(getClass().getResource("/resources/ball-magenta.png")));
    }//GEN-LAST:event_ButtonHelpMouseExited

    private void ButtonHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonHelpActionPerformed
        URI domain = null;
        try {
            domain = new URI("http://nathansoftware.com/wordpress/easy-survey-creator-manual/"); //launches the default help page
        } catch (URISyntaxException ex) {
            Logger.getLogger(EasySurveyMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        openWebpage(domain);
    }//GEN-LAST:event_ButtonHelpActionPerformed

    private void ButtonConductSurveyMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonConductSurveyMouseEntered
        ButtonConductSurvey.setForeground(Color.CYAN);
        ButtonConductSurvey.setIcon(new ImageIcon(getClass().getResource("/resources/ball-greenSelected.png")));
    }//GEN-LAST:event_ButtonConductSurveyMouseEntered

    private void ButtonConductSurveyMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonConductSurveyMouseExited
        ButtonConductSurvey.setForeground(null);
        ButtonConductSurvey.setIcon(new ImageIcon(getClass().getResource("/resources/ball-green.png")));
    }//GEN-LAST:event_ButtonConductSurveyMouseExited

    private void ButtonWebsiteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonWebsiteMouseEntered
        ButtonWebsite.setForeground(Color.CYAN);
    }//GEN-LAST:event_ButtonWebsiteMouseEntered

    private void ButtonWebsiteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonWebsiteMouseExited
        ButtonWebsite.setForeground(null);
    }//GEN-LAST:event_ButtonWebsiteMouseExited

    private void ButtonWebsiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonWebsiteActionPerformed
        URI domain = null;
        try {
            domain = new URI("http://nathansoftware.com/wordpress/"); //launches the website
        } catch (URISyntaxException ex) {
            Logger.getLogger(EasySurveyMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        openWebpage(domain);
    }//GEN-LAST:event_ButtonWebsiteActionPerformed

    private void ButtonCreateSurveyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonCreateSurveyActionPerformed
        CreationFrame CF = new CreationFrame();
        
        
        List<Image> icons = new ArrayList<>();
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx16.png")));
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx32.gif")));
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx64.gif")));
        CF.setIconImages(icons);
        
        passCheckFrame = CF;
                
        adminPassCheck(passCheckFrame);        
    }//GEN-LAST:event_ButtonCreateSurveyActionPerformed
    
    private void ButtonAnalyseDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonAnalyseDataActionPerformed
        AnswerFrame AF = new AnswerFrame();
        
        List<Image> icons = new ArrayList<>();
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx16.png")));
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx32.gif")));
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx64.gif")));
        AF.setIconImages(icons);
        
        passCheckFrame = AF;
        
        adminPassCheck(passCheckFrame);
    }//GEN-LAST:event_ButtonAnalyseDataActionPerformed

    private void ButtonConductSurveyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonConductSurveyActionPerformed
        QuestionFrame QF = new QuestionFrame();
        
        List<Image> icons = new ArrayList<>();
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx16.png")));
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx32.gif")));
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx64.gif")));
        QF.setIconImages(icons);
        
        QF.setVisible(true);
        
        this.setVisible(false); //hide the original frame
    }//GEN-LAST:event_ButtonConductSurveyActionPerformed
                                       

    private void PFnoButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PFnoButton1ActionPerformed
        correctPassword = true; //you know the password when this is true it doesn't bother you by asking for it

        setPassword(false); //sets the password to null

        passwordFrame.dispose(); //then close the frame
    }//GEN-LAST:event_PFnoButton1ActionPerformed
                                    

    private void PFnoButton1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PFnoButton1MouseExited
        PFnoButton1.setIcon(new ImageIcon(getClass().getResource("/resources/ballNo.png")));
    }//GEN-LAST:event_PFnoButton1MouseExited

    private void PFnoButton1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PFnoButton1MouseEntered
        PFnoButton1.setIcon(new ImageIcon(getClass().getResource("/resources/ballNoSelected.png")));
    }//GEN-LAST:event_PFnoButton1MouseEntered
                                                                             

    private void PFyesButton1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PFyesButton1MouseExited
        PFyesButton1.setIcon(new ImageIcon(getClass().getResource("/resources/ballYes.png")));
    }//GEN-LAST:event_PFyesButton1MouseExited

    private void PFyesButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PFyesButton1ActionPerformed
        if ("Enter password in this field".equals(enterPassword1.getText()) | "".equals(enterPassword1.getText())) { //if it still says the enter password text or says nothing text then dont continue
            enterPassword1.setText("");
        } else {
        password = enterPassword1.getText(); //first store password

        setPassword(true); //then save password

        correctPassword = true; //then record that you know the password

        //then close the window
        passwordFrame.dispose();
        }
    }//GEN-LAST:event_PFyesButton1ActionPerformed

    private void PFyesButton1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PFyesButton1MouseEntered
        PFyesButton1.setIcon(new ImageIcon(getClass().getResource("/resources/ballYesSelected.png")));
    }//GEN-LAST:event_PFyesButton1MouseEntered

    private void GPHelpButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_GPHelpButtonMouseEntered
        GPHelpButton.setIcon(new ImageIcon(getClass().getResource("/resources/helpIconSelected.jpg")));
    }//GEN-LAST:event_GPHelpButtonMouseEntered

    private void GPHelpButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_GPHelpButtonMouseExited
        GPHelpButton.setIcon(new ImageIcon(getClass().getResource("/resources/helpIcon.jpg")));
    }//GEN-LAST:event_GPHelpButtonMouseExited

    private void GPHelpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GPHelpButtonActionPerformed
        URI domain = null;
        try {
            domain = new URI("http://nathansoftware.com/wordpress/easy-survey-creator-manual/"); //launches the password help window
        } catch (URISyntaxException ex) {
            Logger.getLogger(EasySurveyMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        openWebpage(domain);
    }//GEN-LAST:event_GPHelpButtonActionPerformed

    private void GPcreateNewSurveyButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_GPcreateNewSurveyButtonMouseEntered
        GPcreateNewSurveyButton.setIcon(new ImageIcon(getClass().getResource("/resources/ball-blueSelected.png")));
    }//GEN-LAST:event_GPcreateNewSurveyButtonMouseEntered

    private void GPcreateNewSurveyButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_GPcreateNewSurveyButtonMouseExited
        GPcreateNewSurveyButton.setIcon(new ImageIcon(getClass().getResource("/resources/ball-blue.png")));
    }//GEN-LAST:event_GPcreateNewSurveyButtonMouseExited

    private void GPcreateNewSurveyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GPcreateNewSurveyButtonActionPerformed
        if (getPassword.getText().equals(password)) { //if you've typed the right password
            correctPassword = true;
            getPasswordFrame.dispose();

            //then run the password check
            //and since the passwords already been verified  then it should launch
            adminPassCheck(passCheckFrame);
        }
        else { //if you got it wrong then tell you you've got it wrong
            GPlabel.setText("Incorrect Password, try again.");
            passTrys++;

            if (passTrys > 5) {
                GPlabel.setText("Incorrect Password, Press the help button to reset");
            }
        }
    }//GEN-LAST:event_GPcreateNewSurveyButtonActionPerformed

    private void ButtonWebsiteUpdateMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonWebsiteUpdateMouseEntered
        ButtonWebsiteUpdate.setForeground(Color.CYAN);
    }//GEN-LAST:event_ButtonWebsiteUpdateMouseEntered

    private void ButtonWebsiteUpdateMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonWebsiteUpdateMouseExited
        ButtonWebsiteUpdate.setForeground(null);
    }//GEN-LAST:event_ButtonWebsiteUpdateMouseExited

    private void ButtonWebsiteUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonWebsiteUpdateActionPerformed
        URI domain = null;
        try {
            domain = new URI("http://nathansoftware.com/wordpress/easy-survey-creator-2/"); //launches the website
        } catch (URISyntaxException ex) {
            Logger.getLogger(EasySurveyMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        openWebpage(domain);
        
        updateFrame.dispose();
    }//GEN-LAST:event_ButtonWebsiteUpdateActionPerformed

    private void LicenseLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LicenseLabelMouseClicked
        URI domain = null;
        try {
            domain = new URI("https://creativecommons.org/licenses/by-nd/4.0/"); //launches the password help window
        } catch (URISyntaxException ex) {
            Logger.getLogger(EasySurveyMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        openWebpage(domain);         
    }//GEN-LAST:event_LicenseLabelMouseClicked

    private void LicenseLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LicenseLabelMouseEntered
        LicenseLabel.setForeground(Color.CYAN);
    }//GEN-LAST:event_LicenseLabelMouseEntered

    private void LicenseLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LicenseLabelMouseExited
        LicenseLabel.setForeground(new Color(102,102,102));
    }//GEN-LAST:event_LicenseLabelMouseExited
    
    
    public void adminPassCheck(Frame jframe) {
        BufferedReader read = null;
        try {
            System.out.println("Trying to find quesload");
            read = new BufferedReader(new FileReader("C:\\Ques\\QuesLoad.txt"));
            
            //read the first three lines and skip
            read.readLine(); read.readLine(); read.readLine();
            
            //then store the fourth line which is the password line
            password = read.readLine();
            
            read.close();
        } catch (FileNotFoundException ex) {
            //if this happens there is no quesLoad file so make one
            //          *IF THERE IS NO QUESLOAD*
            System.out.println("couldn't find quesload");
            
            PrintWriter creator = null;
            try {
                File f = new File("C:\\Ques\\");
                f.mkdirs();

                creator = new PrintWriter("C:\\Ques\\QuesLoad.txt", "UTF-8");
            } catch (Exception e) {}
            creator.close();
            
            try {
                BufferedWriter write = new BufferedWriter(new FileWriter("C:\\Ques\\QuesLoad.txt"));
                write.write(defaultLines);
                write.close();
            } catch (IOException ex1) {
                Logger.getLogger(EasySurveyMenu.class.getName()).log(Level.SEVERE, null, ex1);
            }
            
        } catch (IOException ex) {
            System.out.println("no fourth line");
            //if this happens there is no forth line
            Logger.getLogger(EasySurveyMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if ("none".equals(password)) { //then the user has choosen to have none and so do nothing
            System.out.println("password = none");
            jframe.setVisible(true);
            this.setVisible(false); //hide the original frame
        }
        else if ("Default".equals(password)) { //then this is the first time they've clicked this and so let them choose a password
            Container cf = passwordFrame.getContentPane();               
            cf.setBackground(Color.white);
            
            System.out.println("password = default (i.e. not set yet)");
            passwordFrame.setResizable(false);
            passwordFrame.setSize(371,183);
            
            List<Image> icons = new ArrayList<>();
            icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx16.png")));
            icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx32.gif")));
            icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx64.gif")));
            passwordFrame.setIconImages(icons);
            
            passwordFrame.setLocationRelativeTo(null);
            
                        
            enterPassword1.setText("Enter password in this field");
            
            enterPassword1.selectAll();
            
            passwordFrame.setVisible(true);
        }
        else if ("".equals(password)) { //then it must be broken so ask for password
            Container cd = passwordFrame.getContentPane();               
            cd.setBackground(Color.white);   
            
            System.out.println("password = blank (so is broken)");
            passwordFrame.setResizable(false);
            passwordFrame.setSize(371,183);
            
            passwordFrame.setLocationRelativeTo(null);
            
            List<Image> icons = new ArrayList<>();
            icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx16.png")));
            icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx32.gif")));
            icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx64.gif")));
            passwordFrame.setIconImages(icons);
                                         
            
            enterPassword1.setText("Enter password in this field");
            
            enterPassword1.selectAll();
      
            passwordFrame.setVisible(true);
        }
        else if (password == null) { //then it must be broken so ask for password
            Container cp = passwordFrame.getContentPane();               
            cp.setBackground(Color.white);  
            
            System.out.println("password = null (so is broken)");
            passwordFrame.setResizable(false);
            passwordFrame.setSize(371,183);
            
            List<Image> icons = new ArrayList<>();
            icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx16.png")));
            icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx32.gif")));
            icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx64.gif")));
            passwordFrame.setIconImages(icons);
            
            passwordFrame.setLocationRelativeTo(null);
                              
            
            enterPassword1.setText("Enter password in this field");
            
            enterPassword1.selectAll();
            
            passwordFrame.setVisible(true);

        }
        else { //then there is a password and they must enter it
            System.out.println("password exists they must type it in");
            if (correctPassword == true) {
                jframe.setVisible(true);
        
                this.setVisible(false); //hide the original frame
                
            } else {
                Container cr = getPasswordFrame.getContentPane();               
                cr.setBackground(Color.white);
                
                getPasswordFrame.setResizable(false);
                getPasswordFrame.setSize(365,190);
                
                
                List<Image> icons = new ArrayList<>();
                icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx16.png")));
                icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx32.gif")));
                icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx64.gif")));
                getPasswordFrame.setIconImages(icons);
                
                getPasswordFrame.setLocationRelativeTo(null);
                
                getPassword.setText("Enter password in this field");
                                               
                
                getPassword.selectAll();
                
                getPasswordFrame.setVisible(true);
            }
        }
    }
    
    public void setPassword(boolean havePass) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader read = new BufferedReader(new FileReader("C:\\Ques\\QuesLoad.txt"));
            
            for (int i = 0; i < 3; i++) { //read the first 3 lines in the quesload file
                String line = read.readLine();
                sb.append(line + "\r\n");
            }
            //then add the password line to the string builder
            if (havePass == true) {
                sb.append(password + "\r\n");
            } else { //otherwise set it to an option so it doesn't bother you!
                sb.append("none" + "\r\n");
            }
            
            // then stick the location line at the end
            String line = read.readLine();
            sb.append(line);
        } catch (Exception ex) {
        }
        
        String lines = sb.toString();
        
        try {
            BufferedWriter write = new BufferedWriter(new FileWriter("C:\\Ques\\QuesLoad.txt"));
            
            write.write(lines);
            write.close();
        } catch (IOException ex) {
            Logger.getLogger(EasySurveyMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }    
    
    
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
            java.util.logging.Logger.getLogger(EasySurveyMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EasySurveyMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EasySurveyMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EasySurveyMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EasySurveyMenu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Background;
    private javax.swing.JButton ButtonAnalyseData;
    private javax.swing.JButton ButtonConductSurvey;
    private javax.swing.JButton ButtonCreateSurvey;
    private javax.swing.JButton ButtonHelp;
    private javax.swing.JButton ButtonWebsite;
    private javax.swing.JButton ButtonWebsiteUpdate;
    private javax.swing.JButton GPHelpButton;
    private javax.swing.JButton GPcreateNewSurveyButton;
    private javax.swing.JLabel GPlabel;
    private javax.swing.JLabel LabelMain;
    private javax.swing.JLabel LabelTT;
    private javax.swing.JButton LicenseLabel;
    private javax.swing.JButton PFnoButton1;
    private javax.swing.JButton PFyesButton1;
    private javax.swing.JTextField enterPassword1;
    private javax.swing.JTextField getPassword;
    private javax.swing.JDialog getPasswordFrame;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JDialog passwordFrame;
    private javax.swing.JLabel pictureLabel;
    private javax.swing.JFrame updateFrame;
    private javax.swing.JLabel versionLabel;
    // End of variables declaration//GEN-END:variables
}
