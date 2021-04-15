/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.text.JTextComponent;
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
public class ContextMenuMouseListener extends MouseAdapter {
    private JPopupMenu popup = new JPopupMenu();
    private JPopupMenu popup1 = new JPopupMenu();

    private Action up1Action;
    private Action up5Action;
    private Action upAllAction;
    private Action down1Action;
    private Action down5Action;
    private Action downAllAction;
    
    private Action editAction;
    private Action duplicateAction;
    private Action deleteAction;

    private JTextComponent textComponent;

    private enum Actions { UNDO, CUT, COPY, PASTE, SELECT_ALL };
    
    public ContextMenuMouseListener(CreationFrame thisFrame) {
        // initializing all the buttons to their correct action
        up1Action = thisFrame.moveQ(thisFrame, -1);
        up5Action = thisFrame.moveQ(thisFrame, -5);
        upAllAction = thisFrame.moveQ(thisFrame, -100000);
        down1Action = thisFrame.moveQ(thisFrame, 1);
        down5Action = thisFrame.moveQ(thisFrame, 5);
        downAllAction = thisFrame.moveQ(thisFrame, 100000);
        
        // create a new sub menu for the move up and down
        JMenu moveUp = new JMenu("Move Up");
        JMenu moveDown = new JMenu("Move Down");
        
        moveUp.add(up1Action);
        moveUp.add(up5Action);
        moveUp.add(upAllAction);
        moveDown.add(down1Action);
        moveDown.add(down5Action);
        moveDown.add(downAllAction);
        popup.add(moveUp);
        popup.add(moveDown);
        popup.addSeparator();

        editAction = new AbstractAction("Edit") {

            @Override
            public void actionPerformed(ActionEvent ae) {
                thisFrame.modifyQuestionBoolean = true;
                thisFrame.addQuestionBoolean = false;
                
                thisFrame.createNewQuestionFrame(true, thisFrame.currentQuestionButton);
            }
        };

        popup.add(editAction);

        duplicateAction = new AbstractAction("Duplicate") {

            @Override
            public void actionPerformed(ActionEvent ae) {
                thisFrame.modifyQuestionBoolean = false;
                thisFrame.addQuestionBoolean = true;
                
                
                // fill the text fields but not in order to modify but in order to duplicate
                thisFrame.createNewQuestionFrame(true, thisFrame.currentQuestionButton);
            }
        };

        popup.add(duplicateAction);

        deleteAction = new AbstractAction("Delete") {

            @Override
            public void actionPerformed(ActionEvent ae) {
                thisFrame.Qdelete(thisFrame.currentQuestionButton);
            }
        };

        popup.add(deleteAction);
        popup.addSeparator();
    }

    public void mouseClicked(MouseEvent e) {
       
            //textComponent = (JTextComponent) e.getSource();
            //textComponent.requestFocus();

            boolean upable = true;
            boolean downable = true;

            up1Action.setEnabled(upable);
            up5Action.setEnabled(upable);
            upAllAction.setEnabled(upable);
            down1Action.setEnabled(downable);
            down5Action.setEnabled(downable);
            downAllAction.setEnabled(downable);
            
            editAction.setEnabled(true);
            duplicateAction.setEnabled(true);
            deleteAction.setEnabled(true);

            int nx = e.getX();

            if (nx > 500) {
                nx = nx - popup.getSize().width;
            }

            popup.show(e.getComponent(), nx, e.getY() - popup.getSize().height);
    }
    
    public Action createAction(String name, String text) {
       Action instantAction = new AbstractAction(name) {

           @Override
           public void actionPerformed(ActionEvent ae) {
                // Store some variables to be used
                StringSelection selection = new StringSelection(text); //the string to add
                Transferable backupString = null; //some transferable object to store the current clipboard
                Clipboard clipboard1 = Toolkit.getDefaultToolkit().getSystemClipboard(); //the current clipboard
                
                backupString = clipboard1.getContents(selection); //store the current clipboard
                
                clipboard1.setContents(selection, selection); //assign the clipboard to be the string to add
                
                textComponent.paste(); //paste the new clipboard
                
                clipboard1.setContents(backupString, selection); //reset the clipboard to be the original clipboard
                
                    
                    //textComponent.setText(textComponent.getText() + thisFrame.advCharL +  "FILE_NAME" + thisFrame.advCharR);
            }
                           
       };
       
       return instantAction;
    }
}