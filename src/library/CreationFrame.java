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
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.lang3.SystemUtils;

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
public class CreationFrame extends javax.swing.JFrame {

    String saveCheckGoal = "exit";
    
    JButton[] questionsB = null;
    
    String pathway = "C:\\Ques";
    
   
    public String fileName = "";
    public boolean loadOldBoolean = true;
    public boolean addQuestionBoolean = true;
    public boolean doneStuff = false;
    
    public boolean modifyQuestionBoolean = false;
    
    int currentQuestionButton = 0;
    
    public String arrowIcon = "down";
    
    /**
     * Creates new form CreationFrame
     */
    public CreationFrame() {
                                
        initComponents();
        
        //get system variables
        if (!SystemUtils.IS_OS_WINDOWS) {
            pathway = SystemUtils.USER_HOME + "/Ques";
            // ****** System.out.printlnln("Home " + pathway);
        }
        
        /// ADD BUTTONS!

        extraOptionsFrame.getRootPane().setDefaultButton(EOsaveChangesButton);
        extraOptionsFrame.getRootPane().setDefaultButton(EOsaveChangesButton);

        extraComponents();

        // by default it does not exit the program when the close button is hit
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        // now we deal with the cases ourself
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    //check if stuff has been done, close the window only if you have saved already
                    if (doneStuff == true) {
                        saveFrame("exit"); // by having 'exit' it tells the frame to... (guess what?)... exit after the save check is finished
                    }
                    // if not then close the window as requested by the user
                    else {
                        System.exit(0);
                    }
                }
        });
        
        questionFieldFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
                public void windowClosing(java.awt.event.WindowEvent createNewQuestionaire) {
                    //clear all the text fields
                    CNQQuestionField.setText("");
                    CNQOption1.setText("");
                    CNQOption2.setText("");
                    CNQOption3.setText("");
                    CNQOption4.setText("");
                    CNQOption5.setText("");
                    // clear all the check boxes
                    typeCheckBoxA.setSelected(false);
                    typeCheckBoxB.setSelected(false);
                    typeCheckBoxC.setSelected(false);
                    typeCheckBoxD.setSelected(false);
                    typeCheckBoxE.setSelected(false);

                    questionFieldFrame.dispose();
                }
        });
        
    }
    
    // INITIALIZE - THE FIRST TIME THE FRAME IS LOADED
    public void initialize() {
                
        // close any other windows
        saveCheckFrame.dispose();
        extraOptionsFrame.dispose();
        newQuestionaireFrame.dispose();
        questionFieldFrame.dispose();
            
        loadFileFrame();
        // ---
    }
        
    // adds a new button when a question is added
    
    public void createButton(int size, String text) {
        
        ContextMenuMouseListener click = new ContextMenuMouseListener(this); //initialize the edit menu
        
        
        JButton[] temporary = new JButton[size + 1]; // create a new temporary array one question longer
                                
        if (size != 0) { //of theres anything to copy
            System.arraycopy(questionsB, 0, temporary, 0, size); // for each item in the original array paste it into the new array
        }

        questionsB = temporary; // then reassign the questionsB array to be the temp array. This will leave one extra spot open to add the newest JButton

        questionsB[size] = new JButton(text);
        questionsB[size].setHorizontalAlignment(SwingConstants.LEFT); //sets the text to the left

        // this preclicks all the buttons once because they don't seem to register the first time after they are created

        questionPanel.setLayout(new java.awt.GridLayout(size + 2, 1)); //fix the layout so it resizes the columns correctly!


        questionPanel.add(questionsB[size]); //add the newest button to the panel

        //refresh the panels
        questionPanel.repaint();
        questionScrollPane.repaint();
        this.validate();

        questionsB[size].addMouseListener(new MouseAdapter() { //add a mouse listener to that newly added object
            public void mouseClicked(MouseEvent evt) { 
                Object item = evt.getSource(); // figure out which button was just clicked!

                int index = Arrays.asList(questionsB).indexOf(item); //figure out what number that button is
                
                // ****** System.out.printlnln("BUTTON " + index + " clicked. It says: " + questionsB[index].getText());
                
                // WHEN THIS BUTTON IS CLICKED we determine if it is a left or right click
                    if(SwingUtilities.isRightMouseButton(evt)){
                        //if its right then bring up a option menu
                        currentQuestionButton = index;
                        
                        // ****** System.out.printlnln("RIGHT!");
                        
                        click.mouseClicked(evt);
                    }
                    else {
                        //if its left bring up the edit menu
                        modifyQuestionBoolean = true;
                        addQuestionBoolean = false;
                        
                        currentQuestionButton = index;
                        
                        createNewQuestionFrame(true, index);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
                    }
                    
                
            }
        });
    }
    
    public void getQuestionFileText() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));        
        StringBuilder currentQuestion = null;
        
        questionPanel.removeAll();
        
        //refresh the panels
        questionPanel.repaint();
        questionScrollPane.repaint();
        this.validate();
        
        questionsB = null;
        
        for (int i = 0; i < 500; i++) { //checks the lines up to 500 (because no ones allowed to have more than 500 questions)
            currentQuestion = new StringBuilder(); //reset stringbuilder
            
            String line = reader.readLine(); //read a line in the file. Each line is a question
            
                      
            if (line == null) { //once it reaches a blank line then it stops 
                reader.close();
                break;
            }
            
            String[] item = line.split(",");
            
            //first check if this question is full (i.e. it has Number, Starting text, AND at least 1 option)
            if (item.length > 2) {
                System.out.println("FULL ITEM");
                
                // then read the file and create the appropriate lines into the text box
                try {
                    currentQuestion.append("\"" + item[1] + "\" --> ");
                    currentQuestion.append("\"" + item[2] + "\"");
                    currentQuestion.append( " , \"" + item[3] + "\"");
                    currentQuestion.append( " , \"" + item[4] + "\"");
                    currentQuestion.append( " , \"" + item[5] + "\"");
                    currentQuestion.append( " , \"" + item[6] + "\"");
                } catch (Exception e) {
                    // if the question doesn't have as many options as expected then it will break but thats not a bad thing :)
                }
                
                createButton(i,currentQuestion.toString());
                
            } else {
                System.out.println("UNFULL ITEM");
            }
        }
                
    }
    
    public void setActive() {
        //then set this as the active survey in the QuesLoad file
        //first read the quesload file
        
        String picture = null; //create variables for lines 2,3 and 4 of the quesLoad file
        String mainText = null;
        String password = null;
        String saveLocation = null;
        try {
            //read the quesload file
            BufferedReader inLoad = new BufferedReader(new FileReader(pathway + "/QuesLoad.txt"));
            inLoad.readLine(); //read the first line but dont do anything with it (active survey which we are replacing now)
            
            picture = inLoad.readLine(); //read and store the second line (picture to show in surveys)
            mainText = inLoad.readLine(); //read and store the third line (text to show in surveys)
            password = inLoad.readLine(); //read and store the forth line (Admin tools password)
            saveLocation = inLoad.readLine(); //read and store the fifth line (Analysis frame stored save location)
        
            inLoad.close();
        } catch (IOException ex) {
            Logger.getLogger(CreationFrame.class.getName()).log(Level.SEVERE, null, ex); //if theres and exception log it
        }

        //then write the quesload file
        try {
            BufferedWriter outLoad = new BufferedWriter(new FileWriter(pathway + "/QuesLoad.txt"));
            
            outLoad.write(fileName); //write the filename of the active survey as the first line
            
            outLoad.write("\r\n" + picture); //then write a new line (\r\n) and then the picture
            outLoad.write("\r\n" + mainText); //then write a new line and then the intro text
            outLoad.write("\r\n" + password); //then write a new line and the password
            outLoad.write("\r\n" + saveLocation); //then write a new line and the save location
            outLoad.close();
        } catch (IOException ex) {
            Logger.getLogger(CreationFrame.class.getName()).log(Level.SEVERE, null, ex); //if theres an exception log it
        }
    }
    
    public void saveChanges(String pathway) {
        // open the base file to allow you to check if the current survey is the active survey
        BufferedReader getDefault = null;
        try {
            getDefault = new BufferedReader(new FileReader(pathway + "/QuesLoad.txt"));
            
        } catch (FileNotFoundException ex) {
            // oops no main default file...
            ContextCreateLabel.setText("[error] No data file found. Please restart program.");
            ContextCreateLabel.setForeground(Color.red);
        }
        
        String DS = "";
        
        try {
            DS = getDefault.readLine();
            getDefault.close();
        } catch (IOException ex) {
            // couldn't read line so give error
            ContextCreateLabel.setText("[error] Data file could not be read. Please restart program.");
            ContextCreateLabel.setForeground(Color.red);
        }
        
        // if the survey file is not already the active survey then notify the user to set it as the active survey
        if (!DS.equals(fileName)) {
            ContextCreateLabel.setText("Press 'Set as active survey' to assign this survey as the survey to be used.");
            ContextCreateLabel.setForeground(Color.red);
        }
        else {
            // the file is already the default so tell the user
            ContextCreateLabel.setText("Thank you! Survey Saved.");
            ContextCreateLabel.setForeground(Color.black);
        }
        
        // SAVING CHANGES //
        
        // ensure our filename is an absolute path so we can use it to write to
        // if it contains a \ then it is a absolute path so its fine otherwise make it a absolute path
        if (fileName.contains("/") | fileName.contains("\\")) {
            //do nothing
        } else {
            String NfileName = pathway + "/" + fileName + ".csv";
            fileName = NfileName;
        }
                   
        // use a stringbuilder to store all the various text
        StringBuilder questionFile = new StringBuilder();
                
        // use a array to store all the many questions
        String[] questions = new String[questionsB.length];
        
        // check each button
        for (int i = 0; i < questionsB.length; i++) { //for each button store its text in the array
            questions[i] = questionsB[i].getText();
        }
        
        //once its got the text then spilt it into arrays by line to get each question
        
        //// Just for show to help the creator
        //// for (int x = 0; x < questions.length; x++) {
        ////     // ****** System.out.printlnln("array" + x + " : " + questions[x]);
        //// }
        
        // check each BUTTON
        // then spilt the questions by quotation marks to get each item
        for (int i = 0; i < questions.length; i++) {
            
            // use another array to store each option for the questions
            String[] items = questions[i].split("\"");
            
            //// Just for show to help the creator
             for (int x = 0; x < items.length; x++) {
                 System.out.println("item" + x + " : " + items[x]);
             }
            
            //for each question line first make sure it isn't blank but has at least two options
            if (items.length < 4) { //this checks how many items there are in this line (ex. Line: "hello", "goodbye") - this line has items: (hello) - (" , ") - (goodbye)
                // then stop because this line shouldn't be added if it has less then two items
            } else {
                       
                questionFile.append(i + 1); //first store the number which is 1 + whatever i is (because i starts at 0 but the questions start at 1)

                for (int j = 1; j < items.length; j += 2) { //this goes through every other item and checks it
                    
                    System.out.println("J: " + j);
                    
                    //if the text field being checked contains a (") or (,) then replace with (') or blank space respectively
                    String newQ = items[j].replaceAll("\"", "'");
                    String newQ2 = newQ.replaceAll(",", " ");
                    
                    // MAIN QUESTION LENGTH IS NOW 256 CHARS allowed
                    if (items[j].length() < 256) {    //if the item isn't too long then write it       
                        questionFile.append("," + newQ2); //then for each other item in the item array store a , and then it
                    }
                    else { //otherwise tell them but write it anyway
                        ContextCreateLabel.setText("Question " + (i + 1) + " has a item which is long, please revise or it may look incorrect.");
                        ContextCreateLabel.setForeground(Color.red);
                        questionFile.append("," + newQ2);
                    }
                }
                questionFile.append("\n"); //finnally for each question store a next line so they don't run up on each other

            }
        }
        
        // ****** System.out.printlnln(fileName);
        
        String quesFile = questionFile.toString();
        String finalQuesFile = quesFile.replaceAll("\n", "\r\n");
        
        // ****** System.out.printlnln(finalQuesFile);
        
        
        //then write the survey lines to your save file
        try {
            BufferedWriter outQues = new BufferedWriter(new FileWriter(fileName));
            outQues.write(finalQuesFile);
            outQues.close();
        } catch (IOException ex) {
            Logger.getLogger(CreationFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        doneStuff = false;
        // ****** System.out.printlnln("donestuff false");
        
    }

    public void extraComponents() {
        setResizable(false);
        setLocationRelativeTo(null);
        
        MainStartup main = new MainStartup();
        
        versionLabel.setText("Copyright NathanSoftware.com version " + main.version);
        
        Container c = getContentPane();               
        c.setBackground(Color.white);
        
        Container c2 = extraOptionsFrame.getContentPane();               
        c2.setBackground(Color.white);
        
        Container c3 = saveCheckFrame.getContentPane();               
        c3.setBackground(Color.white);
        
        buttonBackRemoval(GoButton);
        buttonBackRemoval(LicenseLabel);
        buttonBackRemoval(SSCcreateNewSurveyButton);
        buttonBackRemoval(SSCloadSurveyButton);
        buttonBackRemoval(SSCassignedSurveyButton);
        buttonBackRemoval(HelpButton);
        buttonBackRemoval(SetDefaultButton);
        buttonBackRemoval(ButtonReturn);
        buttonBackRemoval(AddNewQuestionButton);
        buttonBackRemoval(SaveChangesButton);
        buttonBackRemoval(ExtraOptionsButton);
        buttonBackRemoval(CNQGoButton);
        buttonBackRemoval(SSCdeleteSurveyButton);
        buttonBackRemoval(ExportButton);
        buttonBackRemoval(ImportButton);
        buttonBackRemoval(arrowButton);
    }
    
    public void buttonBackRemoval(JButton button) {
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        extraOptionsFrame = new javax.swing.JFrame();
        EOsaveChangesButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        EOintroTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        EOfileSelectButton = new javax.swing.JButton();
        EOimageLocationName = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        EOpassTextField = new javax.swing.JTextField();
        saveCheckFrame = new javax.swing.JFrame();
        jLabel5 = new javax.swing.JLabel();
        SCFyesButton = new javax.swing.JButton();
        SCFnoButton = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        startSurveyCreatorFrame = new javax.swing.JFrame();
        jLabel6 = new javax.swing.JLabel();
        SSCloadSurveyButton = new javax.swing.JButton();
        SSCcreateNewSurveyButton = new javax.swing.JButton();
        SSCdeleteSurveyButton = new javax.swing.JButton();
        ImportButton = new javax.swing.JButton();
        ExportButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        arrowButton = new javax.swing.JButton();
        SSCassignedSurveyButton = new javax.swing.JButton();
        questionFieldFrame = new javax.swing.JFrame();
        qfMainLabel = new javax.swing.JLabel();
        CNQText1 = new javax.swing.JLabel();
        CNQQuestionField = new javax.swing.JTextField();
        labels1 = new javax.swing.JLabel();
        CNQText2 = new javax.swing.JLabel();
        CNQOption1 = new javax.swing.JTextField();
        CNQOption2 = new javax.swing.JTextField();
        CNQOption3 = new javax.swing.JTextField();
        CNQOption4 = new javax.swing.JTextField();
        CNQOption5 = new javax.swing.JTextField();
        labels2 = new javax.swing.JLabel();
        labels3 = new javax.swing.JLabel();
        labels4 = new javax.swing.JLabel();
        labels5 = new javax.swing.JLabel();
        sideLabel = new javax.swing.JLabel();
        CNQGoButton = new javax.swing.JButton();
        typeCheckBoxA = new javax.swing.JCheckBox();
        jLabel11 = new javax.swing.JLabel();
        typeCheckBoxB = new javax.swing.JCheckBox();
        typeCheckBoxC = new javax.swing.JCheckBox();
        typeCheckBoxD = new javax.swing.JCheckBox();
        typeCheckBoxE = new javax.swing.JCheckBox();
        helpFrame = new javax.swing.JFrame();
        jLabel9 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jLabel10 = new javax.swing.JLabel();
        versionLabel = new javax.swing.JLabel();
        MainCreateLabel = new javax.swing.JLabel();
        SaveChangesButton = new javax.swing.JButton();
        AddNewQuestionButton = new javax.swing.JButton();
        ExtraOptionsButton = new javax.swing.JButton();
        ButtonReturn = new javax.swing.JButton();
        SetDefaultButton = new javax.swing.JButton();
        questionScrollPane = new javax.swing.JScrollPane();
        questionPanel = new javax.swing.JPanel();
        LicenseLabel = new javax.swing.JButton();
        HelpButton = new javax.swing.JButton();
        ContextCreateLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        GoButton = new javax.swing.JButton();

        EOsaveChangesButton.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        EOsaveChangesButton.setText("Save Changes");
        EOsaveChangesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EOsaveChangesButtonActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel1.setText("Enter the location of the image to use for all surveys:");
        jLabel1.setToolTipText("");

        jLabel3.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel3.setText("Enter the text to display before the all surveys:");

        EOintroTextField.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        EOintroTextField.setText("(no change)");

        jLabel4.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Extra Options");

        EOfileSelectButton.setText("...");
        EOfileSelectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EOfileSelectButtonActionPerformed(evt);
            }
        });

        EOimageLocationName.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        EOimageLocationName.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "No Change", "Bunny", "Cat", "Monkey", "Wizard", "Woman" }));

        jLabel7.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel7.setText("Enter a password for admin tools: (type  \"none\" to have none)");
        jLabel7.setToolTipText("");

        EOpassTextField.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        EOpassTextField.setText("(no change)");

        javax.swing.GroupLayout extraOptionsFrameLayout = new javax.swing.GroupLayout(extraOptionsFrame.getContentPane());
        extraOptionsFrame.getContentPane().setLayout(extraOptionsFrameLayout);
        extraOptionsFrameLayout.setHorizontalGroup(
            extraOptionsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(extraOptionsFrameLayout.createSequentialGroup()
                .addGroup(extraOptionsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(extraOptionsFrameLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel3))
                    .addGroup(extraOptionsFrameLayout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(EOintroTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(extraOptionsFrameLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(extraOptionsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addGroup(extraOptionsFrameLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(EOfileSelectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(extraOptionsFrameLayout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addGroup(extraOptionsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(EOpassTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(EOimageLocationName, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(extraOptionsFrameLayout.createSequentialGroup()
                        .addGap(124, 124, 124)
                        .addComponent(EOsaveChangesButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        extraOptionsFrameLayout.setVerticalGroup(
            extraOptionsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(extraOptionsFrameLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel4)
                .addGap(11, 11, 11)
                .addGroup(extraOptionsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(EOfileSelectButton))
                .addGap(6, 6, 6)
                .addComponent(EOimageLocationName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(EOpassTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addGap(6, 6, 6)
                .addComponent(EOintroTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(EOsaveChangesButton)
                .addContainerGap())
        );

        jLabel5.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        jLabel5.setForeground(java.awt.Color.red);
        jLabel5.setText("You are exiting without saving or setting your survey as active.");

        SCFyesButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/ballYes.png"))); // NOI18N
        SCFyesButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                SCFyesButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                SCFyesButtonMouseExited(evt);
            }
        });
        SCFyesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SCFyesButtonActionPerformed(evt);
            }
        });

        SCFnoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/ballNo.png"))); // NOI18N
        SCFnoButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                SCFnoButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                SCFnoButtonMouseExited(evt);
            }
        });
        SCFnoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SCFnoButtonActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel8.setText("Would you like to save your changes?");

        javax.swing.GroupLayout saveCheckFrameLayout = new javax.swing.GroupLayout(saveCheckFrame.getContentPane());
        saveCheckFrame.getContentPane().setLayout(saveCheckFrameLayout);
        saveCheckFrameLayout.setHorizontalGroup(
            saveCheckFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(saveCheckFrameLayout.createSequentialGroup()
                .addGroup(saveCheckFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(saveCheckFrameLayout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addGroup(saveCheckFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(saveCheckFrameLayout.createSequentialGroup()
                                .addComponent(SCFyesButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(SCFnoButton))
                            .addComponent(jLabel8)))
                    .addGroup(saveCheckFrameLayout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jLabel5)))
                .addContainerGap(47, Short.MAX_VALUE))
        );
        saveCheckFrameLayout.setVerticalGroup(
            saveCheckFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(saveCheckFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addGap(10, 10, 10)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(saveCheckFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SCFnoButton)
                    .addComponent(SCFyesButton))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jLabel6.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Select one of the options below");

        SSCloadSurveyButton.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        SSCloadSurveyButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/menu/crystal-green.png"))); // NOI18N
        SSCloadSurveyButton.setText("Load Survey");
        SSCloadSurveyButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        SSCloadSurveyButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                SSCloadSurveyButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                SSCloadSurveyButtonMouseExited(evt);
            }
        });
        SSCloadSurveyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SSCloadSurveyButtonActionPerformed(evt);
            }
        });

        SSCcreateNewSurveyButton.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        SSCcreateNewSurveyButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/menu/crystal-blue.png"))); // NOI18N
        SSCcreateNewSurveyButton.setText("Create New Survey");
        SSCcreateNewSurveyButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                SSCcreateNewSurveyButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                SSCcreateNewSurveyButtonMouseExited(evt);
            }
        });
        SSCcreateNewSurveyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SSCcreateNewSurveyButtonActionPerformed(evt);
            }
        });

        SSCdeleteSurveyButton.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        SSCdeleteSurveyButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/menu/crystal-red.png"))); // NOI18N
        SSCdeleteSurveyButton.setText("Delete Survey");
        SSCdeleteSurveyButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        SSCdeleteSurveyButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                SSCdeleteSurveyButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                SSCdeleteSurveyButtonMouseExited(evt);
            }
        });
        SSCdeleteSurveyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SSCdeleteSurveyButtonActionPerformed(evt);
            }
        });

        ImportButton.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        ImportButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/ball-purple.png"))); // NOI18N
        ImportButton.setText("Import");
        ImportButton.setToolTipText("Import a survey to be used");
        ImportButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ImportButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ImportButtonMouseExited(evt);
            }
        });
        ImportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ImportButtonActionPerformed(evt);
            }
        });

        ExportButton.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        ExportButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/ball-teal.png"))); // NOI18N
        ExportButton.setText("Export");
        ExportButton.setToolTipText("Export a survey for others to use");
        ExportButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ExportButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ExportButtonMouseExited(evt);
            }
        });
        ExportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExportButtonActionPerformed(evt);
            }
        });

        arrowButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/arrowDown.png"))); // NOI18N
        arrowButton.setText("other");
        arrowButton.setToolTipText("More options");
        arrowButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                arrowButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                arrowButtonMouseExited(evt);
            }
        });
        arrowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arrowButtonActionPerformed(evt);
            }
        });

        SSCassignedSurveyButton.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        SSCassignedSurveyButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/menu/crystal-yellow.png"))); // NOI18N
        SSCassignedSurveyButton.setText("Change active survey");
        SSCassignedSurveyButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        SSCassignedSurveyButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                SSCassignedSurveyButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                SSCassignedSurveyButtonMouseExited(evt);
            }
        });
        SSCassignedSurveyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SSCassignedSurveyButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout startSurveyCreatorFrameLayout = new javax.swing.GroupLayout(startSurveyCreatorFrame.getContentPane());
        startSurveyCreatorFrame.getContentPane().setLayout(startSurveyCreatorFrameLayout);
        startSurveyCreatorFrameLayout.setHorizontalGroup(
            startSurveyCreatorFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(startSurveyCreatorFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(startSurveyCreatorFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, startSurveyCreatorFrameLayout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, startSurveyCreatorFrameLayout.createSequentialGroup()
                        .addGap(0, 45, Short.MAX_VALUE)
                        .addGroup(startSurveyCreatorFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, startSurveyCreatorFrameLayout.createSequentialGroup()
                                .addGroup(startSurveyCreatorFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, startSurveyCreatorFrameLayout.createSequentialGroup()
                                        .addComponent(ImportButton)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(ExportButton)
                                        .addGap(34, 34, 34))
                                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(47, 47, 47))
                            .addGroup(startSurveyCreatorFrameLayout.createSequentialGroup()
                                .addGap(214, 214, 214)
                                .addComponent(arrowButton))))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, startSurveyCreatorFrameLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(startSurveyCreatorFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SSCloadSurveyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SSCassignedSurveyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SSCcreateNewSurveyButton)
                    .addComponent(SSCdeleteSurveyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36))
        );
        startSurveyCreatorFrameLayout.setVerticalGroup(
            startSurveyCreatorFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(startSurveyCreatorFrameLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SSCcreateNewSurveyButton)
                .addGap(1, 1, 1)
                .addComponent(SSCassignedSurveyButton)
                .addGap(0, 0, 0)
                .addComponent(SSCloadSurveyButton)
                .addGroup(startSurveyCreatorFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(startSurveyCreatorFrameLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(arrowButton))
                    .addGroup(startSurveyCreatorFrameLayout.createSequentialGroup()
                        .addComponent(SSCdeleteSurveyButton)
                        .addGap(0, 38, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addGroup(startSurveyCreatorFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ExportButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ImportButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
        );

        questionFieldFrame.setTitle("Question");

        qfMainLabel.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        qfMainLabel.setText("QUESTION FRAME");

        CNQText1.setText("Question:");

        labels1.setText("(A) :");
        labels1.setToolTipText("");

        CNQText2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CNQText2.setText("Options:");

        labels2.setText("(B) :");
        labels2.setToolTipText("");

        labels3.setText("(C) :");
        labels3.setToolTipText("");

        labels4.setText("(D) :");
        labels4.setToolTipText("");

        labels5.setText("(E) :");
        labels5.setToolTipText("");

        sideLabel.setFont(new java.awt.Font("Tahoma", 2, 10)); // NOI18N
        sideLabel.setText("enter a question and options users can choose");

        CNQGoButton.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        CNQGoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/menu/crystal-blue.png"))); // NOI18N
        CNQGoButton.setText("GO");
        CNQGoButton.setIconTextGap(-36);

        typeCheckBoxA.setToolTipText("Allow user to type their own answer");

        jLabel11.setText("Editable:");
        jLabel11.setToolTipText("Allow user to type their own answer");

        typeCheckBoxB.setToolTipText("Allow user to type their own answer");

        typeCheckBoxC.setToolTipText("Allow user to type their own answer");

        typeCheckBoxD.setToolTipText("Allow user to type their own answer");

        typeCheckBoxE.setToolTipText("Allow user to type their own answer");

        javax.swing.GroupLayout questionFieldFrameLayout = new javax.swing.GroupLayout(questionFieldFrame.getContentPane());
        questionFieldFrame.getContentPane().setLayout(questionFieldFrameLayout);
        questionFieldFrameLayout.setHorizontalGroup(
            questionFieldFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(questionFieldFrameLayout.createSequentialGroup()
                .addGroup(questionFieldFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(questionFieldFrameLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(CNQGoButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(questionFieldFrameLayout.createSequentialGroup()
                        .addGroup(questionFieldFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(questionFieldFrameLayout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(CNQText1)
                                .addGap(18, 18, 18)
                                .addComponent(CNQQuestionField, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(questionFieldFrameLayout.createSequentialGroup()
                                .addGap(55, 55, 55)
                                .addGroup(questionFieldFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(labels1)
                                    .addComponent(labels2)
                                    .addComponent(labels3)
                                    .addComponent(labels4)
                                    .addComponent(labels5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(questionFieldFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(questionFieldFrameLayout.createSequentialGroup()
                                        .addComponent(CNQOption2, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(typeCheckBoxB))
                                    .addGroup(questionFieldFrameLayout.createSequentialGroup()
                                        .addComponent(CNQOption3, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(typeCheckBoxC))
                                    .addGroup(questionFieldFrameLayout.createSequentialGroup()
                                        .addComponent(CNQOption4, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(typeCheckBoxD))
                                    .addGroup(questionFieldFrameLayout.createSequentialGroup()
                                        .addComponent(CNQOption5, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(typeCheckBoxE))
                                    .addGroup(questionFieldFrameLayout.createSequentialGroup()
                                        .addComponent(CNQOption1, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(typeCheckBoxA))))
                            .addGroup(questionFieldFrameLayout.createSequentialGroup()
                                .addGap(85, 85, 85)
                                .addComponent(CNQText2, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel11)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(questionFieldFrameLayout.createSequentialGroup()
                        .addGap(257, 257, 257)
                        .addComponent(qfMainLabel)
                        .addGap(18, 18, 18)
                        .addComponent(sideLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)))
                .addContainerGap())
        );
        questionFieldFrameLayout.setVerticalGroup(
            questionFieldFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(questionFieldFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(questionFieldFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(qfMainLabel)
                    .addComponent(sideLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(questionFieldFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CNQText1)
                    .addComponent(CNQQuestionField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(questionFieldFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(questionFieldFrameLayout.createSequentialGroup()
                        .addGroup(questionFieldFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(questionFieldFrameLayout.createSequentialGroup()
                                .addGroup(questionFieldFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(questionFieldFrameLayout.createSequentialGroup()
                                        .addGroup(questionFieldFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(questionFieldFrameLayout.createSequentialGroup()
                                                .addGroup(questionFieldFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(CNQText2)
                                                    .addComponent(jLabel11))
                                                .addGap(18, 18, 18)
                                                .addGroup(questionFieldFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(labels1)
                                                    .addComponent(CNQOption1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(typeCheckBoxA))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(questionFieldFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(CNQOption2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(labels2)))
                                            .addComponent(typeCheckBoxB))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(questionFieldFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(CNQOption3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(labels3)))
                                    .addComponent(typeCheckBoxC))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(questionFieldFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(CNQOption4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labels4)))
                            .addComponent(typeCheckBoxD))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(questionFieldFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(CNQOption5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labels5)))
                    .addComponent(typeCheckBoxE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CNQGoButton, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
                .addGap(23, 23, 23))
        );

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("HELP");

        jTextPane1.setEditable(false);
        jTextPane1.setText("- For more options click \"Options\" at the bottom right\n\n- To EDIT  questions click on them. Make changes. Then click \"Go\" to approve your changes.\n\n- To REARRANGE questions right-click on them. Select \"Move up\" or \"Move Down\" in the popup. Then choose how far you want it to move in the popup that is shown.\n\n- To DELETE  questions right-click on them and select \"Delete\"\n");
        jScrollPane1.setViewportView(jTextPane1);

        jLabel10.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("NOTE: For a full manual see www.nathansoftware.com");
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel10MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel10MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel10MouseExited(evt);
            }
        });

        javax.swing.GroupLayout helpFrameLayout = new javax.swing.GroupLayout(helpFrame.getContentPane());
        helpFrame.getContentPane().setLayout(helpFrameLayout);
        helpFrameLayout.setHorizontalGroup(
            helpFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(helpFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(helpFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE))
                .addContainerGap())
        );
        helpFrameLayout.setVerticalGroup(
            helpFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(helpFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addContainerGap(50, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Survey Creation Menu");
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx32.gif")));
        setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);

        versionLabel.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        versionLabel.setText("Copyright NathanSoftware.com version 2.0.2");

        MainCreateLabel.setFont(new java.awt.Font("Arial Black", 3, 14)); // NOI18N
        MainCreateLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        MainCreateLabel.setText("Welcome to the Creator Menu");

        SaveChangesButton.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        SaveChangesButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/menu/crystal-blue.png"))); // NOI18N
        SaveChangesButton.setText("Save");
        SaveChangesButton.setEnabled(false);
        SaveChangesButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                SaveChangesButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                SaveChangesButtonMouseExited(evt);
            }
        });
        SaveChangesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveChangesButtonActionPerformed(evt);
            }
        });

        AddNewQuestionButton.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        AddNewQuestionButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/menu/crystal-purple.png"))); // NOI18N
        AddNewQuestionButton.setText("Add new question");
        AddNewQuestionButton.setToolTipText("Add a new question to this survey");
        AddNewQuestionButton.setEnabled(false);
        AddNewQuestionButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                AddNewQuestionButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                AddNewQuestionButtonMouseExited(evt);
            }
        });
        AddNewQuestionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddNewQuestionButtonActionPerformed(evt);
            }
        });

        ExtraOptionsButton.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        ExtraOptionsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/menu/crystal-blue.png"))); // NOI18N
        ExtraOptionsButton.setText("Advanced Configurations");
        ExtraOptionsButton.setToolTipText("Click to configure advanced options");
        ExtraOptionsButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ExtraOptionsButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ExtraOptionsButtonMouseExited(evt);
            }
        });
        ExtraOptionsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExtraOptionsButtonActionPerformed(evt);
            }
        });

        ButtonReturn.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        ButtonReturn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/homeIcon.jpg"))); // NOI18N
        ButtonReturn.setText("     Main Menu");
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

        SetDefaultButton.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        SetDefaultButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/menu/crystal-blue.png"))); // NOI18N
        SetDefaultButton.setText("Set as Active survey");
        SetDefaultButton.setToolTipText("Set this survey as the active survey to be used");
        SetDefaultButton.setEnabled(false);
        SetDefaultButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                SetDefaultButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                SetDefaultButtonMouseExited(evt);
            }
        });
        SetDefaultButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SetDefaultButtonActionPerformed(evt);
            }
        });

        questionPanel.setToolTipText("Right click on a question for advanced options");
        questionPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        questionPanel.setLayout(new java.awt.GridLayout(10, 1));
        questionScrollPane.setViewportView(questionPanel);

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

        ContextCreateLabel.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        ContextCreateLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ContextCreateLabel.setText(" ");

        jLabel2.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel2.setText("Help");

        GoButton.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        GoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/menu/crystal-red.png"))); // NOI18N
        GoButton.setText("Options");
        GoButton.setToolTipText("Load or Create a survey");
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(LicenseLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 660, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(ExtraOptionsButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(GoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(questionScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 636, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(SaveChangesButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(SetDefaultButton))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(203, 203, 203)
                                .addComponent(versionLabel)))))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(AddNewQuestionButton)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(ContextCreateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(ButtonReturn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(HelpButton)
                .addGap(19, 19, 19))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(MainCreateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ButtonReturn)
                        .addComponent(jLabel2))
                    .addComponent(HelpButton))
                .addGap(39, 39, 39)
                .addComponent(MainCreateLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ContextCreateLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AddNewQuestionButton)
                    .addComponent(SaveChangesButton)
                    .addComponent(SetDefaultButton))
                .addGap(6, 6, 6)
                .addComponent(questionScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(GoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(ExtraOptionsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(versionLabel)
                .addGap(1, 1, 1)
                .addComponent(LicenseLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void GoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GoButtonActionPerformed
    ContextCreateLabel.setForeground(Color.black);
    
    if (doneStuff == true) { //if you've configured settings and done stuff then if you hit menu it prompts you to save (if you haven't)
        saveFrame("loadFileFrame");
    } else {
        
        GoButton.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-red.png")));
        
        // close any other windows
        saveCheckFrame.dispose();
        extraOptionsFrame.dispose();
        newQuestionaireFrame.dispose();
        questionFieldFrame.dispose();
            
        loadFileFrame();
    }
        
            
    }//GEN-LAST:event_GoButtonActionPerformed

    private void SaveChangesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveChangesButtonActionPerformed
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        try {
            saveChanges(pathway + "/");
        } catch (Exception e) {
            // if it can't save the survey file print error
            ContextCreateLabel.setForeground(Color.red);   //
            ContextCreateLabel.setText("Unable to save the survey, error in config file.");
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        try {
            TimeUnit.MILLISECONDS.sleep(500); //it goes to fast ;)
        } catch (InterruptedException ex) {
            Logger.getLogger(CreationFrame.class.getName()).log(Level.SEVERE, null, ex);
            // I don't know why this would break?? hopefully it doesn't!
        }
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_SaveChangesButtonActionPerformed

    private void AddNewQuestionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddNewQuestionButtonActionPerformed
        addQuestionBoolean = true;
        modifyQuestionBoolean = false;
        
        // close any other windows
        saveCheckFrame.dispose();
        startSurveyCreatorFrame.dispose();
        extraOptionsFrame.dispose();
        newQuestionaireFrame.dispose();
        
        createNewQuestionFrame(false, 0);
    }//GEN-LAST:event_AddNewQuestionButtonActionPerformed

    private void ExtraOptionsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExtraOptionsButtonActionPerformed
        // close any other windows
        saveCheckFrame.dispose();
        startSurveyCreatorFrame.dispose();
        newQuestionaireFrame.dispose();
        questionFieldFrame.dispose();
        
        // CREATE EXTRA OPTIONS FRAME
        
        extraOptionsFrame.setSize(370,305);
        extraOptionsFrame.setResizable(false);
        extraOptionsFrame.setLocationRelativeTo(null);
        extraOptionsFrame.setVisible(true);
        
        // Fill text fields with correct text
        
        //create variables for lines 2,3,4 and 5 of the quesLoad file
        String picture = null;      // not used
        String mainText = null;
        String password = null;
        String saveLocation = null; // not used
        try {
            //read the quesload file
            BufferedReader getExtraStuff = new BufferedReader(new FileReader(pathway + "/QuesLoad.txt"));
            getExtraStuff.readLine(); //read the first line but dont do anything with it (active survey)
            
            picture = getExtraStuff.readLine(); //read and store the second line (picture to show in surveys)
            mainText = getExtraStuff.readLine(); //read and store the third line (text to show in surveys)
            password = getExtraStuff.readLine(); //read and store the forth line (Admin tools password)
            saveLocation = getExtraStuff.readLine(); //read and store the fifth line (Analysis frame stored save location)
        
            getExtraStuff.close();
        } catch (IOException ex) {
            Logger.getLogger(CreationFrame.class.getName()).log(Level.SEVERE, null, ex); //if theres and exception log it
        }
        EOpassTextField.setText(password);
        EOintroTextField.setText(mainText);
        
        
        
        EOpassTextField.addFocusListener(new FocusListener() {
            @Override public void focusLost(final FocusEvent pE) {}
            @Override public void focusGained(final FocusEvent pE) {
                    EOpassTextField.selectAll();
            }
        });
        
        EOintroTextField.addFocusListener(new FocusListener() {
            @Override public void focusLost(final FocusEvent pE) {}
            @Override public void focusGained(final FocusEvent pE) {
                    EOintroTextField.selectAll();
            }
        });
        
        List<Image> icons = new ArrayList<>();
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx16.png")));
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx32.gif")));
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx64.gif")));
        extraOptionsFrame.setIconImages(icons);
    }//GEN-LAST:event_ExtraOptionsButtonActionPerformed

    private void EOfileSelectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EOfileSelectButtonActionPerformed
            
            String imageName = "";
        
            FileNameExtensionFilter filter = new FileNameExtensionFilter("IMAGE FILES", "jpg", "png" , "bmp" , "gif");

            JFileChooser folderChooser = new JFileChooser("C:\\Users/");
            
            folderChooser.setFileHidingEnabled(true); //but the files will still show if you have it set to show on your computer
            
            folderChooser.setFileSelectionMode(JFileChooser.FILES_ONLY); //this makes it so you can only select a file
            folderChooser.setFileFilter(filter);
            int returnValue = folderChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                imageName = folderChooser.getSelectedFile().getAbsolutePath(); //this takes the file you have choosen and gets its full path name
                
                                
                EOimageLocationName.addItem(imageName);
                EOimageLocationName.setSelectedIndex(EOimageLocationName.getItemCount() - 1);
            }
    }//GEN-LAST:event_EOfileSelectButtonActionPerformed

    private void EOsaveChangesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EOsaveChangesButtonActionPerformed
        String introLine = EOintroTextField.getText();
        String imageLocation = EOimageLocationName.getItemAt(EOimageLocationName.getSelectedIndex());
        String passwordLine = EOpassTextField.getText();
        String saveLocation = "/Documents";             // default
        String firstLine = "";
        
        // NOTE: this has been changed to be up to 256 chars, which is about 4 lines
        if (introLine.length() > 256) {
            //then it will propably be too long so tell them about it and don't let them save
            jLabel3.setText("Your intro text is too long please adjust it");
            jLabel3.setForeground(Color.red);   //and change the font colour to red so they notice that your giving them some info
        }
        else { //if it's not too long then do normal stuff
            jLabel3.setText("Enter the text to display before the all surveys:");
            jLabel3.setForeground(Color.black);             //and make sure the font isn't red anymore
        
            
        
            if ("(no change)".equals(passwordLine)) { //then they've don't want it changed so don't change anything
                passwordLine = "No Change";
            }
            if ("".equals(passwordLine)) { //if the password line is blank we can assume they dont want one
                passwordLine = "none";
            }
            if ("".equals(introLine)) { //blank sets it to default
                introLine = "Default";
            }
            if ("(no change)".equals(introLine)) { //no change... (this is really to obvious to explain) ...doesn't change anything
                introLine = "No Change";
            }
            if ("".equals(imageLocation)) { //blanks sets it to default
                imageLocation = "Default";
            }
            if (imageLocation.equals("Bunny")) {
                imageLocation = "/resources/Bunny.jpg";
            }
            if (imageLocation.contains("Wizard")) {
                imageLocation = "/resources/Wizard.jpg";
            }
            if (imageLocation.contains("Cat")) {
                imageLocation = "/resources/Cat.jpg";
            }
            if (imageLocation.contains("Monkey")) {
                imageLocation = "/resources/Monkey.jpg";
            }  
            if (imageLocation.contains("Woman")) {
                imageLocation = "/resources/Woman.jpg";
            }        


            // open the save file and store the info
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(pathway + "/QuesLoad.txt"));
                firstLine = reader.readLine(); //read and store line 1
                
                if (imageLocation.equals("No Change")) { //if the user doesn't choose to change the image then don't duh!
                    imageLocation = reader.readLine(); //and so set the image variable as whatever it already is in the file
                } else {reader.readLine();} //else ignore line 2
                
                if (introLine.equals("No Change")) { //if the user doesn't choose to change the intro line then don't... duh!
                    introLine = reader.readLine(); //and so set the intro line variable as whatever it already is in the file
                } else {reader.readLine();} //else ignore line 3
                
                if ("No Change".equals(passwordLine)) { //if this line is No Change then dont change it so just overwrite it with what ever it used to say
                    passwordLine = reader.readLine();
                }
                
                // NOTE: you don't have to read the last line because it is not relavant now
                // BUT: it needs to not be overwritten so it is stored anyway
                saveLocation = reader.readLine();
                
                reader.close();
            } catch (Exception e) {
            }

            try {
                BufferedWriter out = new BufferedWriter(new FileWriter(pathway + "/QuesLoad.txt"));
                out.write(firstLine + "\r\n"); //rewrite the file with your lines instead
                out.write(imageLocation + "\r\n");
                out.write(introLine + "\r\n");
                out.write(passwordLine + "\r\n");
                out.write(saveLocation);
                out.close();
            } catch (Exception e) {

            }

            //clear the text boxes and then dispose the frame
            EOintroTextField.setText("");
            EOimageLocationName.setSelectedIndex(1);
            extraOptionsFrame.dispose();
        }
    }//GEN-LAST:event_EOsaveChangesButtonActionPerformed

    private void ButtonReturnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonReturnActionPerformed
        if (doneStuff == true) { //if you've configured settings and done stuff then if you hit menu it prompts you to save (if you haven't)
            saveFrame("EasySurveyMenu");
        } else { //otherwise it just returns you to the main menu
            EasySurveyMenu ESF = new EasySurveyMenu();

            List<Image> icons = new ArrayList<>();
            icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx16.png")));
            icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx32.gif")));
            icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx64.gif")));
            ESF.setIconImages(icons);
            
            ESF.setVisible(true);
            dispose();
            
            // close any other windows
            saveCheckFrame.dispose();
            startSurveyCreatorFrame.dispose();
            extraOptionsFrame.dispose();
            newQuestionaireFrame.dispose();
            questionFieldFrame.dispose();
        }
    }//GEN-LAST:event_ButtonReturnActionPerformed

    private void SCFyesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SCFyesButtonActionPerformed
        saveChanges(pathway + "/"); //first save
        
        saveCheckContinue();
    }//GEN-LAST:event_SCFyesButtonActionPerformed

    private void SCFnoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SCFnoButtonActionPerformed
        saveCheckContinue();
    }//GEN-LAST:event_SCFnoButtonActionPerformed

    private void SCFyesButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SCFyesButtonMouseEntered
        SCFyesButton.setIcon(new ImageIcon(getClass().getResource("/resources/ballYesSelected.png")));
    }//GEN-LAST:event_SCFyesButtonMouseEntered

    private void SCFyesButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SCFyesButtonMouseExited
        SCFyesButton.setIcon(new ImageIcon(getClass().getResource("/resources/ballYes.png")));
    }//GEN-LAST:event_SCFyesButtonMouseExited

    private void SCFnoButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SCFnoButtonMouseEntered
        SCFnoButton.setIcon(new ImageIcon(getClass().getResource("/resources/ballNoSelected.png")));
    }//GEN-LAST:event_SCFnoButtonMouseEntered

    private void SCFnoButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SCFnoButtonMouseExited
        SCFnoButton.setIcon(new ImageIcon(getClass().getResource("/resources/ballNo.png")));
    }//GEN-LAST:event_SCFnoButtonMouseExited

    private void GoButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_GoButtonMouseEntered
            GoButton.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-redSelected.png")));
    }//GEN-LAST:event_GoButtonMouseEntered

    private void GoButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_GoButtonMouseExited
            GoButton.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-red.png")));
    }//GEN-LAST:event_GoButtonMouseExited

    private void ExtraOptionsButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ExtraOptionsButtonMouseEntered
        ExtraOptionsButton.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-blueSelected.png")));
        ExtraOptionsButton.setForeground(Color.CYAN);
    }//GEN-LAST:event_ExtraOptionsButtonMouseEntered

    private void ExtraOptionsButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ExtraOptionsButtonMouseExited
        ExtraOptionsButton.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-blue.png")));
        ExtraOptionsButton.setForeground(null);
    }//GEN-LAST:event_ExtraOptionsButtonMouseExited

    private void ButtonReturnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonReturnMouseEntered
        ButtonReturn.setIcon(new ImageIcon(getClass().getResource("/resources/homeIconSelected.jpg")));
    }//GEN-LAST:event_ButtonReturnMouseEntered

    private void ButtonReturnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonReturnMouseExited
        ButtonReturn.setIcon(new ImageIcon(getClass().getResource("/resources/homeIcon.jpg")));
    }//GEN-LAST:event_ButtonReturnMouseExited

    private void AddNewQuestionButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AddNewQuestionButtonMouseEntered
        if (AddNewQuestionButton.isEnabled() == true) {
            AddNewQuestionButton.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-purpleSelected.png")));
            AddNewQuestionButton.setForeground(Color.CYAN);
        }
    }//GEN-LAST:event_AddNewQuestionButtonMouseEntered

    private void AddNewQuestionButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AddNewQuestionButtonMouseExited
        if (AddNewQuestionButton.isEnabled() == true) {
            AddNewQuestionButton.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-purple.png")));
            AddNewQuestionButton.setForeground(null);
        }
    }//GEN-LAST:event_AddNewQuestionButtonMouseExited

    private void SaveChangesButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SaveChangesButtonMouseEntered
        if (SaveChangesButton.isEnabled() == true) {
            SaveChangesButton.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-blueSelected.png")));
            SaveChangesButton.setForeground(Color.CYAN);
        }
    }//GEN-LAST:event_SaveChangesButtonMouseEntered

    private void SaveChangesButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SaveChangesButtonMouseExited
        if (SaveChangesButton.isEnabled() == true) {
            SaveChangesButton.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-blue.png")));
            SaveChangesButton.setForeground(null);
        }
    }//GEN-LAST:event_SaveChangesButtonMouseExited

    private void SetDefaultButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SetDefaultButtonMouseEntered
        if (SetDefaultButton.isEnabled() == true) {        
            SetDefaultButton.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-blueSelected.png")));
            SetDefaultButton.setForeground(Color.CYAN);
        }
    }//GEN-LAST:event_SetDefaultButtonMouseEntered

    private void SetDefaultButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SetDefaultButtonMouseExited
        if (SetDefaultButton.isEnabled() == true) {
            SetDefaultButton.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-blue.png")));
            SetDefaultButton.setForeground(null);
        }
    }//GEN-LAST:event_SetDefaultButtonMouseExited

    private void SetDefaultButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SetDefaultButtonActionPerformed
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        // change the mouse to be swirling
        
        try {
            setActive();
            
            TimeUnit.MILLISECONDS.sleep(500); //it goes to fast ;)
            
            ContextCreateLabel.setText("Thank you, " + fileName + " has been set as the active survey!");
            ContextCreateLabel.setForeground(Color.black);
        } catch (Exception e) {
            // if it can't set it as the active the survey file print error
            ContextCreateLabel.setForeground(Color.red);   //
            ContextCreateLabel.setText("Unable to set as active survey, error in config file.");
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        
        // change the mouse back
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        
    }//GEN-LAST:event_SetDefaultButtonActionPerformed

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

    private void HelpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HelpButtonActionPerformed
        Container c = helpFrame.getContentPane();               
        c.setBackground(Color.white);
        
        helpFrame.setSize(420,280);
        helpFrame.setResizable(false);
        helpFrame.setLocationRelativeTo(null);
        helpFrame.setVisible(true);
    }//GEN-LAST:event_HelpButtonActionPerformed

    private void HelpButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HelpButtonMouseEntered
        HelpButton.setIcon(new ImageIcon(getClass().getResource("/resources/helpIconSelected.jpg")));
    }//GEN-LAST:event_HelpButtonMouseEntered

    private void HelpButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HelpButtonMouseExited
        HelpButton.setIcon(new ImageIcon(getClass().getResource("/resources/helpIcon.jpg")));
    }//GEN-LAST:event_HelpButtonMouseExited

    private void arrowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arrowButtonActionPerformed
        // when you click on the "more" button
        if ("down".equals(arrowIcon)) {
            // if it currently is pointing down then we change it to say and then point up
            // ****** System.out.printlnln("Arrow up");
            arrowIcon = "up";
            arrowButton.setIcon(new ImageIcon(getClass().getResource("/resources/arrowUpSelected.png")));
            startSurveyCreatorFrame.setSize(354,380);
            startSurveyCreatorFrame.repaint();
        }
        else {
            // if it currently is pointing up then we change it to say and then point down
            // ****** System.out.printlnln("Arrow down");
            arrowIcon = "down";
            arrowButton.setIcon(new ImageIcon(getClass().getResource("/resources/arrowDownSelected.png")));
            startSurveyCreatorFrame.setSize(354,320); // NOTE: normal size
            startSurveyCreatorFrame.repaint();
        }
    }//GEN-LAST:event_arrowButtonActionPerformed

    private void arrowButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_arrowButtonMouseExited
        if ("down".equals(arrowIcon)) {
            arrowButton.setForeground(Color.BLACK);
            arrowButton.setIcon(new ImageIcon(getClass().getResource("/resources/arrowDown.png")));
        }
        else {
            arrowButton.setForeground(Color.BLACK);
            arrowButton.setIcon(new ImageIcon(getClass().getResource("/resources/arrowUp.png")));
        }
    }//GEN-LAST:event_arrowButtonMouseExited

    private void arrowButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_arrowButtonMouseEntered
        if ("down".equals(arrowIcon)) {
            arrowButton.setForeground(Color.GRAY);
            arrowButton.setIcon(new ImageIcon(getClass().getResource("/resources/arrowDownSelected.png")));
        }
        else {
            arrowButton.setForeground(Color.GRAY);
            arrowButton.setIcon(new ImageIcon(getClass().getResource("/resources/arrowUpSelected.png")));
        }
    }//GEN-LAST:event_arrowButtonMouseEntered

    private void ExportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExportButtonActionPerformed
        startSurveyCreatorFrame.dispose();

        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV (Comma Seperated Data Files)", "csv");

        // string used to store the pathway of the selected file
        String pathwayUsed = "";

        // string to store survey name
        String surveyToGet = "";

        JFileChooser fileChooser = new JFileChooser(pathway + "/");
            fileChooser.setDialogTitle("SELECT A FILE TO EXPORT");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY); //this makes it so you can only select a file
            fileChooser.setFileFilter(filter);
            fileChooser.setApproveButtonText("Export File");
            fileChooser.setApproveButtonToolTipText("Export the selected survey");
            // Allow multiple selection
            fileChooser.setMultiSelectionEnabled(true);

            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                // when you choose a file it stores its entire path and name
                pathwayUsed = fileChooser.getSelectedFile().getAbsolutePath();

                // and it store the surveys name to use for the new survey you export
                surveyToGet = fileChooser.getSelectedFile().getName();

                // ****** System.out.printlnln("pathway: " + pathwayUsed );

                BufferedReader reader = null;
                try { // this is the reader that reads the current file to be exported
                    reader = new BufferedReader(new FileReader(pathwayUsed));
                } catch (Exception e) {
                    // the file cant be opened so we must assume their is some error
                }

                // ****** System.out.printlnln("Reader open for exporting");

                // now we read through the file
                String fileText = "";

                // this loop runs until the end of the file when it reaches a null line, then it breaks
                // this reads and stores the survey that you want to export
                for (int i = 0; i > -1; i++) {
                    String line = "";
                    try {
                        line = reader.readLine();
                        // ****** System.out.printlnln("Read: " + line);
                    } catch (IOException ex) {
                        // ****** System.out.printlnln("End of file");
                    }

                    if (line == null) {
                        i++;
                        try {
                            reader.close();
                        } catch (IOException e) {
                            // reader broke oh well, its already closed
                        }
                        break;
                    }

                    // for all lines expcept the 1st add in a new line char
                    if (!"".equals(fileText)) {
                        fileText = fileText + "\r\n" +line;
                    }
                    else {
                        fileText = line;
                    }
                }

                // ****** System.out.printlnln("FN: " + pathwayUsed + " FT: " + fileText);

                // Export the file //
                JFileChooser exporter = new JFileChooser();
                exporter.setDialogTitle("CHOOSE A LOCATION TO EXPORT");
                exporter.setCurrentDirectory(new File("/Documents"));
                exporter.setSelectedFile(new File(surveyToGet)); // put the name of the survey into the filechoose so you don't have to retype
                int retrival = exporter.showSaveDialog(null);
                if (retrival == JFileChooser.APPROVE_OPTION) {
                    String fileEx = "";
                    // if the files doesn't contain a dot assume the person didn't put an extension
                    if (!exporter.getSelectedFile().getName().contains(".")) {
                        fileEx = ".csv";
                    }
                    try(
                        // now it writes all the text into the file you have choosen
                        FileWriter fw = new FileWriter(exporter.getSelectedFile() + fileEx)) {
                        fw.write(fileText);
                    } catch (Exception ex) {
                        // couldn't write the file...
                    }
                    MainCreateLabel.setText("Survey successfully exported");
                }
            }
    }//GEN-LAST:event_ExportButtonActionPerformed

    private void ExportButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ExportButtonMouseExited
        ExportButton.setIcon(new ImageIcon(getClass().getResource("/resources/ball-teal.png")));
    }//GEN-LAST:event_ExportButtonMouseExited

    private void ExportButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ExportButtonMouseEntered
        ExportButton.setIcon(new ImageIcon(getClass().getResource("/resources/ball-tealSelected.png")));
    }//GEN-LAST:event_ExportButtonMouseEntered

    private void ImportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ImportButtonActionPerformed
        startSurveyCreatorFrame.dispose();

        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV (Comma Seperated Data Files)", "csv");

        String thePathway = "";

        JFileChooser fileChooser = new JFileChooser("/Documents");
        fileChooser.setDialogTitle("SELECT A FILE TO IMPORT");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY); //this makes it so you can only select a file
        fileChooser.setFileFilter(filter);
        fileChooser.setApproveButtonText("Choose File");
        fileChooser.setApproveButtonToolTipText("Import the selected survey");
        // Allow multiple selection
        fileChooser.setMultiSelectionEnabled(true);

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            // when you choose a file it stores it
            thePathway = fileChooser.getSelectedFile().getName();

            File locationFile = fileChooser.getSelectedFile();

            // the path to our file / the name of the file
            File importLocal = new File(pathway + "/" + thePathway);

                // ****** System.out.printlnln("importing " + importLocal.getAbsolutePath());

                if (!locationFile.renameTo(importLocal)) {
                    // cant import file
                    // ****** System.out.printlnln("Can't import file");
                    ContextCreateLabel.setText("Error importing file! Survey with that name already exists");
                    ContextCreateLabel.setForeground(Color.red);
                } else {
                    fileName = importLocal.getAbsolutePath();
                    loadFile();
                }
            }
    }//GEN-LAST:event_ImportButtonActionPerformed

    private void ImportButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ImportButtonMouseExited
        ImportButton.setIcon(new ImageIcon(getClass().getResource("/resources/ball-purple.png")));
    }//GEN-LAST:event_ImportButtonMouseExited

    private void ImportButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ImportButtonMouseEntered
        ImportButton.setIcon(new ImageIcon(getClass().getResource("/resources/ball-purpleSelected.png")));
    }//GEN-LAST:event_ImportButtonMouseEntered

    private void SSCdeleteSurveyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SSCdeleteSurveyButtonActionPerformed
        loadOldBoolean = true;
        SSCdeleteSurveyButton.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-red.png")));

        startSurveyCreatorFrame.dispose();

        // first CLOSE THE CURRENT SURVEY

        if (loadOldBoolean == true) { //only run the action if it hasn't just run to avoid repeats

            File[] selectedFiles;

            FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV (Comma Seperated Data Files)", "csv");

            JFileChooser fileDeleter = new JFileChooser(pathway + "/");
                fileDeleter.setFileSelectionMode(JFileChooser.FILES_ONLY); //this makes it so you can only select a file
                fileDeleter.setFileFilter(filter);
                fileDeleter.setApproveButtonText("Delete");
                fileDeleter.setDialogTitle("Select files to Delete");
                fileDeleter.setApproveButtonToolTipText("Delete the selected files");
                // Allow multiple selection
                fileDeleter.setMultiSelectionEnabled(true);

                int returnValue = fileDeleter.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {

                    selectedFiles = fileDeleter.getSelectedFiles();

                    loadOldBoolean = false;

                    // If some file is selected
                    if(selectedFiles!=null)
                    {
                        // If user confirms to delete
                        if(askConfirm()==JOptionPane.YES_OPTION)
                        {

                            // Call Files.delete(), if any problem occurs
                            // the exception can be printed, it can be
                            // analysed
                            for(File f:selectedFiles)
                            try {
                                java.nio.file.Files.delete(f.toPath());
                            } catch (IOException ex) {}

                            // Rescan the directory after deletion
                            fileDeleter.rescanCurrentDirectory();
                        }
                    }

                }
            }
    }//GEN-LAST:event_SSCdeleteSurveyButtonActionPerformed

    private void SSCdeleteSurveyButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SSCdeleteSurveyButtonMouseExited
        SSCdeleteSurveyButton.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-red.png")));
    }//GEN-LAST:event_SSCdeleteSurveyButtonMouseExited

    private void SSCdeleteSurveyButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SSCdeleteSurveyButtonMouseEntered
        SSCdeleteSurveyButton.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-redSelected.png")));
    }//GEN-LAST:event_SSCdeleteSurveyButtonMouseEntered

    private void SSCcreateNewSurveyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SSCcreateNewSurveyButtonActionPerformed
        loadOldBoolean = true;
        createNewButtonActionPerformed(evt);
    }//GEN-LAST:event_SSCcreateNewSurveyButtonActionPerformed

    private void SSCcreateNewSurveyButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SSCcreateNewSurveyButtonMouseExited
        SSCcreateNewSurveyButton.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-blue.png")));
    }//GEN-LAST:event_SSCcreateNewSurveyButtonMouseExited

    private void SSCcreateNewSurveyButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SSCcreateNewSurveyButtonMouseEntered
        SSCcreateNewSurveyButton.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-blueSelected.png")));
    }//GEN-LAST:event_SSCcreateNewSurveyButtonMouseEntered

    private void SSCloadSurveyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SSCloadSurveyButtonActionPerformed
        loadOldBoolean = true;
        loadOldButtonActionPerformed(evt);
    }//GEN-LAST:event_SSCloadSurveyButtonActionPerformed

    private void SSCloadSurveyButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SSCloadSurveyButtonMouseExited
        SSCloadSurveyButton.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-green.png")));
    }//GEN-LAST:event_SSCloadSurveyButtonMouseExited

    private void SSCloadSurveyButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SSCloadSurveyButtonMouseEntered
        SSCloadSurveyButton.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-greenSelected.png")));
    }//GEN-LAST:event_SSCloadSurveyButtonMouseEntered

    private void jLabel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseClicked
        URI domain = null;
        try {
            domain = new URI("http://nathansoftware.com/wordpress/easy-survey-creator-manual/"); //launches the website and ESC manual
        } catch (URISyntaxException ex) {
            Logger.getLogger(CreationFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        openWebpage(domain);
    }//GEN-LAST:event_jLabel10MouseClicked

    private void jLabel10MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseEntered
        jLabel10.setForeground(Color.CYAN);
    }//GEN-LAST:event_jLabel10MouseEntered

    private void jLabel10MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseExited
        jLabel10.setForeground(null);
    }//GEN-LAST:event_jLabel10MouseExited

    private void SSCassignedSurveyButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SSCassignedSurveyButtonMouseEntered
        SSCassignedSurveyButton.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-yellowSelected.png")));
    }//GEN-LAST:event_SSCassignedSurveyButtonMouseEntered

    private void SSCassignedSurveyButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SSCassignedSurveyButtonMouseExited
        SSCassignedSurveyButton.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-yellow.png")));
    }//GEN-LAST:event_SSCassignedSurveyButtonMouseExited

    private void SSCassignedSurveyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SSCassignedSurveyButtonActionPerformed
        // bring up a window so you can select which survey to be active
        
        // kill and reset the old window
        SSCassignedSurveyButton.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-yellow.png")));
        startSurveyCreatorFrame.dispose();
        
        loadOldBoolean = true; // take note that the thing has been clicked on
        
        if (loadOldBoolean == true) { //only run the action if it hasn't just run to avoid repeats
            FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV (Comma Seperated Data Files)", "csv"); // only csv files can be found

            JFileChooser fileActivator = new JFileChooser(pathway + "/");

            fileActivator.setFileSelectionMode(JFileChooser.FILES_ONLY); //this makes it so you can only select a file
            fileActivator.setFileFilter(filter);
            fileActivator.setApproveButtonText("Select");
            fileActivator.setApproveButtonToolTipText("Set the selected survey as active");
            fileActivator.setDialogTitle("SELECT A SURVEY TO GIVE");
            
            // Allow multiple selection
            fileActivator.setMultiSelectionEnabled(true);

            int returnValue = fileActivator.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {

                // get the surveys path
                String pathwayString = fileActivator.getSelectedFile().getAbsolutePath();

                // ****** System.out.printlnln("PATH: " + pathwayString);

                loadOldBoolean = false; // ensure that this thing is not fired more than once

                // then set that as the active survey
                
                String tempFile = fileName; // store this on the side (the current open file)
                try {
                    
                    fileName = pathwayString; // set the filename to be saved 
                    setActive();

                    ContextCreateLabel.setText("Thank you, " + fileName + " has been set as the active survey!");
                    ContextCreateLabel.setForeground(Color.black);
                    
                    fileName = tempFile; // reset the filename to the current open file
                } catch (Exception e) {
                    
                    fileName = tempFile; // reset the filename to the current open file
                    
                    // if it can't set it as the active the survey file print error
                    ContextCreateLabel.setForeground(Color.red);   //
                    ContextCreateLabel.setText("Unable to set as active survey, error in config file.");
                    this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }

            }
        }
        
        // -- //
    }//GEN-LAST:event_SSCassignedSurveyButtonActionPerformed
    
    public int askConfirm()
    {
        // Ask the user whether he/she wants to confirm deleting
        // Return the option chosen by the user either YES/NO
        return JOptionPane.showConfirmDialog(this,"Are you sure want to delete this file?","Confirm",JOptionPane.YES_NO_OPTION);
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
    
    
    public void saveFrame(String toGo) {
        SCFyesButton.setContentAreaFilled(false);
        SCFyesButton.setBorderPainted(false);
        SCFyesButton.setFocusPainted(false);
        SCFyesButton.setOpaque(false);
        
        SCFnoButton.setContentAreaFilled(false);
        SCFnoButton.setBorderPainted(false);
        SCFnoButton.setFocusPainted(false);
        SCFnoButton.setOpaque(false);
        
        saveCheckFrame.setVisible(true);
        saveCheckFrame.setSize(386,165);
        saveCheckFrame.setLocationRelativeTo(null);
        saveCheckFrame.setResizable(false);
        
        List<Image> icons = new ArrayList<>();
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx16.png")));
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx32.gif")));
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx64.gif")));
        saveCheckFrame.setIconImages(icons);
        
        saveCheckGoal = toGo;
    }
    
    public void loadOldButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (loadOldBoolean == true) { //only run the action if it hasn't just run to avoid repeats
            SSCloadSurveyButton.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-green.png")));
            
            FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV (Comma Seperated Data Files)", "csv", "csv");

            JFileChooser folderChooser = new JFileChooser(pathway + "/");
            folderChooser.setFileSelectionMode(JFileChooser.FILES_ONLY); //this makes it so you can only select a file
            folderChooser.setFileFilter(filter);
            int returnValue = folderChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                fileName = folderChooser.getSelectedFile().getAbsolutePath(); //this takes the file you have choosen and gets its full path name
                //fileName = selectedFile.substring(0, selectedFile.indexOf('.')); // this gets everything before the . in the name

                //FieldFileName.setText(fileName); //then puts it in the file in the name
            
                startSurveyCreatorFrame.dispose();

                loadFile();
                            
            }
        }
        
    }
    
    public void loadFile() {
        //clear the panel on the main window and the array of buttons
        questionPanel.removeAll();

        questionsB = null;

        try {
            getQuestionFileText();
        } catch (IOException ex) {
            Logger.getLogger(CreationFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        loadOldBoolean = false;

        String name = "";
        if (name.contains("/")) {
            name = fileName.substring(fileName.lastIndexOf("/") + 1);
        } else {
            name = fileName.substring(fileName.lastIndexOf("\\") + 1);
        }
        
        MainCreateLabel.setText("Survey:  \"" + name + "\"");
        ContextCreateLabel.setText("Click on a question to edit it");

        /*
        if (jLabel2.getText().length() > 50) { //if the strings to long
            //then shorten it
            jLabel2.setText("\"" + name + "\"");
        } //otherwise dont worry about it*/

        //then un disable the buttons at the bottom
        AddNewQuestionButton.setEnabled(true);
        SetDefaultButton.setEnabled(true);
        SaveChangesButton.setEnabled(true);
    }
    
    public void createNewButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (loadOldBoolean == true) {
            SSCcreateNewSurveyButton.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-blue.png")));
            
            newQuestionaireFrameCreate();

            startSurveyCreatorFrame.dispose();
            
            loadOldBoolean = false;
        }
    }
    
    public void CNQGoButtonActionPerformed(java.awt.event.ActionEvent evt, int questionNumber) {
            //first check and make sure that you have added enough questions!
            // NOTE: that it is valid either to have typed text or to have checked the "user types" box
            // so display an error if:
            // - There is no question
            // - OR A has no words AND A has no check box
            // AND 
            // - B has no words AND B has no check box
            
            if ("".equals(CNQQuestionField.getText()) | 
                    (("".equals(CNQOption1.getText()) && !typeCheckBoxA.isSelected()) && 
                      "".equals(CNQOption2.getText()) && !typeCheckBoxB.isSelected())
                ) {          
                // ****** System.out.printlnln("Not enough options in the new question - QField = " + CNQQuestionField.getText());
                CNQText2.setForeground(Color.red);
                CNQText2.setText("(type a question and use field A & B)");
            }
            
            // second check for any incorrect characters in the questions!
            else if (CNQQuestionField.getText().contains("\"") | CNQQuestionField.getText().contains(",") |
                    CNQOption1.getText().contains("\"") | CNQOption1.getText().contains(",") |
                    CNQOption2.getText().contains("\"") | CNQOption2.getText().contains(",") |
                    CNQOption3.getText().contains("\"") | CNQOption3.getText().contains(",") |
                    CNQOption4.getText().contains("\"") | CNQOption4.getText().contains(",") |
                    CNQOption5.getText().contains("\"") | CNQOption5.getText().contains(",") ) {
                
                // ****** System.out.printlnln("Bad characters");
                CNQText2.setForeground(Color.red);
                CNQText2.setText("(do not include double quotation marks or commas)");               
                
                
            }
            // ENSURE THE QUESTION AND OPTIONS ARE NOT TOO LONG!
            else if (CNQOption1.getText().length() > 68) {
                CNQText2.setForeground(Color.red);
                CNQText2.setText("(option (A) is too long)");        
            }
            else if (CNQOption2.getText().length() > 68) {
                CNQText2.setForeground(Color.red);
                CNQText2.setText("(option (B) is too long)");        
            }
            else if (CNQOption3.getText().length() > 68) {
                CNQText2.setForeground(Color.red);
                CNQText2.setText("(option (C) is too long)");        
            }
            else if (CNQOption4.getText().length() > 68) {
                CNQText2.setForeground(Color.red);
                CNQText2.setText("(option (D) is too long)");        
            }
            else if (CNQOption5.getText().length() > 68) {
                CNQText2.setForeground(Color.red);
                CNQText2.setText("(option (E) is too long)");        
            }
            
            // we recommend nothing greater than 256, we allow nothing greater than 356
            else if (CNQQuestionField.getText().length() > 356) {
                CNQText2.setForeground(Color.red);
                CNQText2.setText("(your question is too long)");   
            }
            else {
                // ****** System.out.printlnln("All successful");
                
            //if all goes successful then make sure the color and text are all corrected
            CNQText2.setForeground(Color.black);
            CNQText2.setText("Options:");
            
            /// ------------- Check boxes ------------------
            // this is to properly save the check boxes which store if a user is allowed to type into the field
            // store all the check boxes to be iterated through
            JCheckBox[] typeBoxes = new JCheckBox[5];
                       
            typeBoxes[0] = typeCheckBoxA;
            typeBoxes[1] = typeCheckBoxB;
            typeBoxes[2] = typeCheckBoxC;
            typeBoxes[3] = typeCheckBoxD;
            typeBoxes[4] = typeCheckBoxE;
            
            // create a string to store if the boxes are on or not and what to add
            String[] boxString = new String[5];
            
            for (int i = 0; i < 5; i++) {
                // if this box is selected
                if (typeBoxes[i].isSelected()) {
                    // then we write the correct activation string
                    boxString[i] = "__";
                } else {
                    // otherwise we won't make any changes by using a blank string
                    boxString[i] = "";
                }
            }
            

            //then get the info from the fields

            StringBuilder newQues = new StringBuilder();

            // then create this question by appending the text fields together
            // NOTE: if there is a "user type" check box then we use __ to repersent it
            
            // TODOX
            
            newQues.append("\"").append(CNQQuestionField.getText()).append("\"");
            newQues.append(" --> ");
            
            newQues.append("\"").append(boxString[0]).append(CNQOption1.getText()).append("\"");
            // if they don't create question 2,3,4, or 5 then dont add it
            if (!"".equals(CNQOption2.getText())) {
                newQues.append(" ,\"").append(boxString[1]).append(CNQOption2.getText()).append("\"");
            }
            if (!"".equals(CNQOption3.getText())) {
                newQues.append(" ,\"").append(boxString[2]).append(CNQOption3.getText()).append("\"");
            }
            if (!"".equals(CNQOption4.getText())) {
                newQues.append(" ,\"").append(boxString[3]).append(CNQOption4.getText()).append("\"");
            }
            if (!"".equals(CNQOption5.getText())) {
                newQues.append(" ,\"").append(boxString[4]).append(CNQOption5.getText()).append("\"");
            }
                
        if (modifyQuestionBoolean == true) {

            // ****** System.out.printlnln("ITEM: " + questionNumber);
            
            questionsB[questionNumber].setText(newQues.toString());
            
            modifyQuestionBoolean = false;
        }                
        else if (addQuestionBoolean == true) {        

                int size = 0;
                
                try { // if this breaks then it must be the nullith question
                    if (questionsB.length == 0) {
                        // ****** System.out.printlnln("First item!");
                    }
                    else {
                        size = questionsB.length;
                    }
                } catch (Exception e) {
                    // so assign a variable of 0
                    size = 0;
                }
                
                // creates a button
                createButton(size, newQues.toString());
 
                addQuestionBoolean = false;
        }
        
        //clear all the text fields
        CNQQuestionField.setText("");
        CNQOption1.setText("");
        CNQOption2.setText("");
        CNQOption3.setText("");
        CNQOption4.setText("");
        CNQOption5.setText("");
        // clear all the check boxes
        typeCheckBoxA.setSelected(false);
        typeCheckBoxB.setSelected(false);
        typeCheckBoxC.setSelected(false);
        typeCheckBoxD.setSelected(false);
        typeCheckBoxE.setSelected(false);

        questionFieldFrame.dispose(); //close the frame

        doneStuff = true;
        // ****** System.out.printlnln("donestuff true");
        
        CNQText2.setForeground(Color.black);
        CNQText2.setText("Options:");
        }
    }
    
    public void newQuesGoButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (!"Enter survey name here".equals(newQuesTF.getText())) {
            
            questionPanel.removeAll(); //clear the text field so that it doesn't have old files stuff on it
            questionPanel.repaint();
            
            questionsB = null; // remove all the buttons

            fileName = newQuesTF.getText();

            //then create the file you have just decided to make
            PrintWriter writer = null;
            try {
                File f = new File(pathway + "/");
                f.mkdirs();

                writer = new PrintWriter(pathway + "/" + fileName + ".csv", "UTF-8");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(CreationFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(CreationFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            writer.close();

            newQuesTF.setText("Enter survey name here");
            newQuestionaireFrame.dispose();

            String name = fileName;
            if (name.contains("/")) {
                name = fileName.substring(fileName.lastIndexOf("/") + 1);
            } else {
                name = fileName.substring(fileName.lastIndexOf("\\") + 1);
            }

            MainCreateLabel.setText("Survey:  \"" + name + "\"");
            ContextCreateLabel.setText("Click 'Add new question'");

            /*
            if (name.length() > 50) { //if the strings contains that word
                //then don't type it
                jLabel2.setText("\"" + name + "\"");
            }*/

            SetDefaultButton.setEnabled(true);
            AddNewQuestionButton.setEnabled(true);
            SaveChangesButton.setEnabled(true);
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
            java.util.logging.Logger.getLogger(CreationFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CreationFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CreationFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CreationFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CreationFrame().setVisible(true);
                
                List<Image> icons = new ArrayList<>();
            }
        });
    }
        
    
    // JFRAME 2
    JFrame newQuestionaireFrame = new JFrame("New Survey");

    JTextField newQuesTF = new JTextField("Enter survey name here");

    JPanel newQuesPanel = new JPanel();
    
    JLabel newQuesLabel = new JLabel("Click Create and then click Add new question");
    JLabel extraQuesLabel = new JLabel("(do not include an underscore in the survey name)");
    
    JButton newQuesGoButton = new JButton("Create");
        
    
    // THIS IS THE FRAME THAT ALLOWS YOU TO LOAD YOUR QUESTION FILE OR CREATE A NEW ONE
    public void loadFileFrame() {
        
        //creates a container so that the new frames color can be changed to white
        Container c = startSurveyCreatorFrame.getContentPane();               
        c.setBackground(Color.white);
                             
        
        //sets functions for the new  frame
        
        startSurveyCreatorFrame.setResizable(false); //sets it so it cant be resized because that messes up the components positions
        startSurveyCreatorFrame.setSize(354,320); //sets its size
        startSurveyCreatorFrame.setLocationRelativeTo(null); //makes it appear in the screens center
        startSurveyCreatorFrame.setVisible(true); //sets the frame to be able to be seen
        
        
        
        jLabel6.setSize(startSurveyCreatorFrame.getWidth(), jLabel6.getHeight()); // setup the top label so it is the correct width based upon the frame size
        
        List<Image> icons = new ArrayList<>();
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx16.png")));
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx32.gif")));
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx64.gif")));
        startSurveyCreatorFrame.setIconImages(icons);
        
        arrowButton.setIcon(new ImageIcon(getClass().getResource("/resources/arrowDown.png")));
        arrowIcon = "down";
        
        startSurveyCreatorFrame.toFront();
    }
    
       
    // this frame brings up the options to create a new survey
    public void newQuestionaireFrameCreate() {
        
        //creates a container so that the background of this frame can be set as white
        Container c = newQuestionaireFrame.getContentPane();         
        c.setBackground(Color.white);
        
        newQuestionaireFrame.getRootPane().setDefaultButton(newQuesGoButton);
        
        newQuesPanel.setBackground(Color.white);
        
        
        newQuesGoButton.setToolTipText("Create a new survey with this name"); //when you put your mouse over the button this message appears
        newQuesGoButton.setFont(new java.awt.Font("Arial", 0, 11)); //this sets the font
        newQuesGoButton.addActionListener(new java.awt.event.ActionListener() { //create a new action listener for the button in this frame
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newQuesGoButtonActionPerformed(evt); //when an action is registered (i.e. a click) then it runs this method
            }
        });
        
        // LABELS (preassigned above)
        
        newQuesLabel.setFont(new java.awt.Font("Arial", 0, 9)); //this sets the font
        extraQuesLabel.setFont(new java.awt.Font("Arial", 0, 9)); //this sets the font
        
        newQuestionaireFrame.setResizable(false); //sets this frame unresizeable
        newQuestionaireFrame.setSize(300,170); //sets the size
        newQuestionaireFrame.setLocationRelativeTo(null); //sets the frame to appear in the screens middle
        newQuestionaireFrame.setVisible(true); //sets the frame visible ('cause otherwise you cant see it and that would be dumb)
        
        List<Image> icons = new ArrayList<>();
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx16.png")));
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx32.gif")));
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx64.gif")));
        newQuestionaireFrame.setIconImages(icons);
        
        
        newQuestionaireFrame.add(newQuesPanel); //adds the panel to the center of the screen
        
        newQuesTF.setText("Enter survey name here");
        newQuesTF.selectAll(); //selects eveything in the text field so you don't have to have the hassle of selecting it to type your name
        
        
        //adds the Text field, go button and label to the panel
        newQuesPanel.add(newQuesTF);
        newQuesPanel.add(newQuesGoButton);
        newQuesPanel.add(newQuesLabel);
        newQuesPanel.add(extraQuesLabel);
        
        
        
    }
        
    // THE FRAME FOR QUESTIONS
    
    // this frame allows you to create a new question for your survey or edit questions
    public void createNewQuestionFrame(boolean buttonArg, int questionNumber) {
        // create a survey so i can make the back color of this frame to white               
        Container c = questionFieldFrame.getContentPane();               
        c.setBackground(Color.white);
        
        
        questionFieldFrame.getRootPane().setDefaultButton(newQuesGoButton);
                
        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx16.png"));
        questionFieldFrame.setIconImage(image);
        
        // sets the text of these bunch of fields (this is so you can tell which option your making)
        
        questionFieldFrame.setResizable(false); //sets unresizeable
        questionFieldFrame.setSize(640,445); //sets size
        questionFieldFrame.setLocationRelativeTo(null); //sets frame to appear in the screens middle
        questionFieldFrame.setVisible(true); //this is pretty obvious (so you can see the frame)
        
        CNQText2.setForeground(Color.black);
        CNQText2.setText("Options:");
        
        //clear all the text fields
        CNQQuestionField.setText("");
        CNQOption1.setText("");
        CNQOption2.setText("");
        CNQOption3.setText("");
        CNQOption4.setText("");
        CNQOption5.setText("");
        
        // if we are editing this then we add in text to all the fields
        if (buttonArg == true) {
            // load the checkboxes so we can edit them
            JCheckBox[] typeBoxes = new JCheckBox[5];
                       
            typeBoxes[0] = typeCheckBoxA;
            typeBoxes[1] = typeCheckBoxB;
            typeBoxes[2] = typeCheckBoxC;
            typeBoxes[3] = typeCheckBoxD;
            typeBoxes[4] = typeCheckBoxE;
            
            
            
            String[] item = new String[12];
            
            int number = questionsB[questionNumber].toString().split("\"").length - 1;
            
            // ****** System.out.printlnln("NUMBER: " + number);
            
            for (int i = 0; i < number; i++) {
                item[i] = questionsB[questionNumber].toString().split("\"")[i]; //set the item to the array index
                
                // if it is a text field (type-able) item 
                // and it is not 1 (which is the question field) but is a odd (which are the answer fields)
                
                if (item[i].contains("__") && (i != 1) && (i%2 > 0)) {
                    item[i] = item[i].substring(2); // then remove those underscores
                    
                    // and get check the check box
                    int checkBox = (i-1) / 2; // the 3,5,7,9,11 are the 1,2,3,4,5 boxes
                    checkBox = checkBox - 1; // then we -1 further to get the 0,1,2,3,4 items  in the array
                    
                    typeBoxes[checkBox].setSelected(true);
                }
            }                
            
            //if any of the 11 are not filled then they will be blank
            
            CNQQuestionField.setText(item[1]);
            CNQOption1.setText(item[3]);
            CNQOption2.setText(item[5]); 
            CNQOption3.setText(item[7]); 
            CNQOption4.setText(item[9]); 
            CNQOption5.setText(item[11]);
            
            CNQGoButton.setToolTipText("Modify the question"); //if you hover you mouse over this button this info will appear
        } else {
            CNQGoButton.setToolTipText("Create the new question"); //if you hover you mouse over this button this info will appear
        }
            
        CNQGoButton.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-blue.png")));
        CNQGoButton.setSelectedIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-blueSelected.png")));
        CNQGoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                    CNQGoButtonActionPerformed(evt, questionNumber);
            }
        });
    }
    
    
    // QUESTION ACTIONS //
    
    public void Qdelete(int theQ) {
        // DELETING
        questionPanel.removeAll();
        
        // ****** System.out.printlnln("DELETING " + theQ + " " + currentQuestionButton);
        
        boolean rightQuestion = false;
        
        JButton[] temp = new JButton[questionsB.length - 1]; //create a new fake array with 1 less components
        for (int i = 0; i < questionsB.length; i++) {
            
            
            if (rightQuestion == true) {
                // ****** System.out.printlnln(" passed right question");
                temp[i - 1] = questionsB[i];
            }
            
            //if we have the right question the delete it and remember that
            else if (i == theQ) {
                // ****** System.out.printlnln(" the right question");
                rightQuestion = true;
                // forget about this item
            }
            
            // if we have not reached the right question nor have passed it then
            else {
                // ****** System.out.printlnln("wrong");
                temp[i] = questionsB[i]; //paste the questionsB array into the temporary array
            }
        }
        
        questionsB = temp; //set the new questionsB array
        
        for (int z = 0; z <questionsB.length; z++) {
            questionPanel.add(questionsB[z]); //add the newest button to the panel
        }
        
        //refresh the panels
        questionPanel.repaint();
        questionScrollPane.repaint();
        this.validate();

        
    }
    
    public Action moveQ(CreationFrame thisFrame,int amount) {
        
        // Set the button text correctly
        String tag = "" + amount;
        
        if (amount*amount == 100000*100000) {
            tag = "All the way";
        }
        else if (amount < 0) {
            tag = "" + amount*-1;
        }
        
        Action moveUpAction = new AbstractAction(tag) {

            @Override
            public void actionPerformed(ActionEvent ae) {
                int PlaceToArrive = currentQuestionButton + amount;
                int ButtonToMove = currentQuestionButton;                

                // this means all the way down
                if (PlaceToArrive >= questionsB.length) {
                    PlaceToArrive = questionsB.length - 1; //the bottom
                }
                
                // this mean the top
                if (PlaceToArrive < 0) {
                    PlaceToArrive = 0; //the top
                }
                
                // ****** System.out.printlnln("PTA: " + PlaceToArrive + ", BTM: " + ButtonToMove);
                
                JButton[] temp = new JButton[questionsB.length]; //create a new temporary array to store the questions
                
                for (int i = 0; i < questionsB.length; i++) {
                    
                    // ****** System.out.printlnln("i = " + i);
                    
                    if (i == PlaceToArrive) {
                        // ****** System.out.printlnln("i == ");
                        temp[i] = questionsB[ButtonToMove];
                        // ****** System.out.printlnln("right one to one");
                    }
                    
                    // Button selected is less than the Goal
                    else if (ButtonToMove < PlaceToArrive) {
                        // ****** System.out.printlnln("btm<pta");
                        if (i < ButtonToMove) {
                            // ****** System.out.printlnln("<btm");
                            // ****** System.out.printlnln("one to one");
                            temp[i] = questionsB[i]; // keep array constant (the same as the original)
                        }
                        
                        if (i >= ButtonToMove && i < PlaceToArrive) {
                            // ****** System.out.printlnln("one to two");
                            temp[i] = questionsB[i + 1]; // paste into the array the next up (+1) question
                        }
                        
                        if (i > PlaceToArrive) {
                            // ****** System.out.printlnln(">pta");
                            // ****** System.out.printlnln("one to one");
                            temp[i] = questionsB[i]; // keep array constant (the same as the original)
                        }
                    }

                    // Button selected is greater than the Goal
                    else if (ButtonToMove > PlaceToArrive) {
                        // ****** System.out.printlnln("btm>pta");
                        if (i < PlaceToArrive) {
                            // ****** System.out.printlnln("<pta");
                            // ****** System.out.printlnln("one to one");
                            temp[i] = questionsB[i]; // keep array constant (the same as the original)
                        }
                        
                        if (i > PlaceToArrive && i <= ButtonToMove) {
                            // ****** System.out.printlnln("one to zero");
                            temp[i] = questionsB[i - 1]; // paste into the array the next up (+1) question
                        }
                        
                        if (i > ButtonToMove) {
                            // ****** System.out.printlnln(">btm");
                            // ****** System.out.printlnln("one to one");
                            temp[i] = questionsB[i]; // keep array constant (the same as the original)
                        }
                    }
                    else if (PlaceToArrive == ButtonToMove) { //if the buttons not going anywhere
                        // ****** System.out.printlnln("not moving");
                        // ****** System.out.printlnln("one to one");
                        
                        temp[i] = questionsB[i]; // keep array constant (the same as the original)
                    }
                }
                
                
                
                // RELOAD EVERYTHING
                questionsB = temp; //set the new questionsB array

                questionPanel.removeAll();
                
                for (int z = 0; z <questionsB.length; z++) {
                    questionPanel.add(questionsB[z]); //add the newest button to the panel
                }

                //refresh the panels
                questionPanel.repaint();
                questionScrollPane.repaint();
                thisFrame.validate();
            }
        };
        
        return moveUpAction;
    }
    
    public void saveCheckContinue() {
        if ("EasySurveyMenu".equals(saveCheckGoal)) {
            //then open the main menu and dispose the SCF frame and the create frame
            EasySurveyMenu ESF = new EasySurveyMenu();

            List<Image> icons = new ArrayList<>();
            icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx16.png")));
            icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx32.gif")));
            icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/Iconx64.gif")));
            ESF.setIconImages(icons);

            ESF.setVisible(true);

            saveCheckFrame.dispose();
            dispose();
        }
        else if ("loadFileFrame".equals(saveCheckGoal)) {
            GoButton.setIcon(new ImageIcon(getClass().getResource("/resources/menu/crystal-red.png")));
        
            // close any other windows
            saveCheckFrame.dispose();
            extraOptionsFrame.dispose();
            newQuestionaireFrame.dispose();
            questionFieldFrame.dispose();
            saveCheckFrame.dispose();
            
            loadFileFrame();
               
        }
        else if ("exit".equals(saveCheckGoal)) {
            System.exit(0);
        }
    }
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddNewQuestionButton;
    private javax.swing.JButton ButtonReturn;
    private javax.swing.JButton CNQGoButton;
    private javax.swing.JTextField CNQOption1;
    private javax.swing.JTextField CNQOption2;
    private javax.swing.JTextField CNQOption3;
    private javax.swing.JTextField CNQOption4;
    private javax.swing.JTextField CNQOption5;
    private javax.swing.JTextField CNQQuestionField;
    private javax.swing.JLabel CNQText1;
    private javax.swing.JLabel CNQText2;
    private javax.swing.JLabel ContextCreateLabel;
    private javax.swing.JButton EOfileSelectButton;
    private javax.swing.JComboBox<String> EOimageLocationName;
    private javax.swing.JTextField EOintroTextField;
    private javax.swing.JTextField EOpassTextField;
    private javax.swing.JButton EOsaveChangesButton;
    private javax.swing.JButton ExportButton;
    private javax.swing.JButton ExtraOptionsButton;
    private javax.swing.JButton GoButton;
    private javax.swing.JButton HelpButton;
    private javax.swing.JButton ImportButton;
    private javax.swing.JButton LicenseLabel;
    private javax.swing.JLabel MainCreateLabel;
    private javax.swing.JButton SCFnoButton;
    private javax.swing.JButton SCFyesButton;
    private javax.swing.JButton SSCassignedSurveyButton;
    private javax.swing.JButton SSCcreateNewSurveyButton;
    private javax.swing.JButton SSCdeleteSurveyButton;
    private javax.swing.JButton SSCloadSurveyButton;
    private javax.swing.JButton SaveChangesButton;
    private javax.swing.JButton SetDefaultButton;
    private javax.swing.JButton arrowButton;
    private javax.swing.JFrame extraOptionsFrame;
    private javax.swing.JFrame helpFrame;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JLabel labels1;
    private javax.swing.JLabel labels2;
    private javax.swing.JLabel labels3;
    private javax.swing.JLabel labels4;
    private javax.swing.JLabel labels5;
    private javax.swing.JLabel qfMainLabel;
    private javax.swing.JFrame questionFieldFrame;
    private javax.swing.JPanel questionPanel;
    private javax.swing.JScrollPane questionScrollPane;
    private javax.swing.JFrame saveCheckFrame;
    private javax.swing.JLabel sideLabel;
    private javax.swing.JFrame startSurveyCreatorFrame;
    private javax.swing.JCheckBox typeCheckBoxA;
    private javax.swing.JCheckBox typeCheckBoxB;
    private javax.swing.JCheckBox typeCheckBoxC;
    private javax.swing.JCheckBox typeCheckBoxD;
    private javax.swing.JCheckBox typeCheckBoxE;
    private javax.swing.JLabel versionLabel;
    // End of variables declaration//GEN-END:variables
}
