/*
 * Copyright 2015 Cycorp, Inc..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cyc.session.internal;

/*
 * #%L
 * File: CycDialogPane.java
 * Project: Session API Implementation
 * %%
 * Copyright (C) 2013 - 2015 Cycorp, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.awt.Component;
import java.awt.HeadlessException;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * An extension of javax.swing.JOptionPane which provides much of the convenience of JOptionPane,
 * while making it easier to change settings on the JDialog before prompting the user.
 * 
 * For example, to display a JOptionPane which is always on top of other windows:
 * 
 * <code>
   CycDialogPanel panel = new CycDialogPanel();
   String title = "Set Cyc Connection";
   final Object[] options = {"OK", "Cancel"};
   final CycDialogPane pane = CycDialogPane.create(panel, title, options);
   pane.getDialog().setAlwaysOnTop(true);
   pane.prompt();
   int result = pane.getSelectedValue();
   if (result == JOptionPane.OK_OPTION) {
     return panel.getCycServer();
   }
   return null;
 <code>
 * 
 * @author nwinant
 */
public class CycDialogPane extends JOptionPane {
  
  // Fields
  
  final private String title;
  private JDialog dialog;
  
  
  // Constructors
  
  public CycDialogPane(Object message, String title, int messageType, int optionType,
          Icon icon, Object[] options, Object initialValue) {
    super(message, messageType, optionType, icon, options, initialValue);
    this.title = title;
  }
  
  
  // Public methods
  
  /**
   * Releases all resources associated with the dialog.
   */
  private void releaseDialog() {
    getDialog().dispose();
    this.dialog = null;
  }
  
  /**
   * Get the current dialog, creating a new one if needed.
   * 
   * @return JDialog
   */
  public JDialog getDialog() {
    if (this.dialog == null) {
      this.dialog = this.createDialog(title);
    }
    return this.dialog;
  }
  
  /**
   * Present the user with the dialog. Once the user has responded, all resources associated with
   * the dialog will be released.
   */
  public void prompt() {
    getDialog().setVisible(true);
    releaseDialog();
  }
  
  public int getSelectedValue() {
    final Object selectedValue = this.getValue();
    if (selectedValue == null) {
      return CLOSED_OPTION;
    }
    if (options == null) {
      if (selectedValue instanceof Integer) {
        return ((Integer) selectedValue).intValue();
      }
      return CLOSED_OPTION;
    }
    for (int counter = 0, maxCounter = options.length;
            counter < maxCounter; counter++) {
      if (options[counter].equals(selectedValue)) {
        return counter;
      }
    }
    return CLOSED_OPTION;
  }
  
  
  // Factory methods
  
  /**
   * Brings up a dialog with a specified icon, where the initial
   * choice is determined by the <code>initialValue</code> parameter and
   * the number of choices is determined by the <code>optionType</code>
   * parameter.
   * 
   * Method signature closely follows:
   * 
   * JOptionPane.showOptionDialog(
   *     Component parentComponent,
   *     Object message, 
   *     String title, 
   *     int optionType, 
   *     int messageType,
   *     Icon icon, 
   *     Object[] options, 
   *     Object initialValue)
   * 
   * @param parentComponent determines the <code>Frame</code>
   *                  in which the dialog is displayed;  if
   *                  <code>null</code>, or if the
   *                  <code>parentComponent</code> has no
   *                  <code>Frame</code>, a
   *                  default <code>Frame</code> is used
   * @param panel     the <code>JPanel</code> to display
   * @param title     the title string for the dialog
   * @param optionType an integer designating the options available on the
   *                  dialog: <code>DEFAULT_OPTION</code>,
   *                  <code>YES_NO_OPTION</code>,
   *                  <code>YES_NO_CANCEL_OPTION</code>,
   *                  or <code>OK_CANCEL_OPTION</code>
   * @param messageType an integer designating the kind of message this is,
   *                  primarily used to determine the icon from the
   *                  pluggable Look and Feel: <code>ERROR_MESSAGE</code>,
   *                  <code>INFORMATION_MESSAGE</code>,
   *                  <code>WARNING_MESSAGE</code>,
   *                  <code>QUESTION_MESSAGE</code>,
   *                  or <code>PLAIN_MESSAGE</code>
   * @param icon      the icon to display in the dialog
   * @param options   an array of objects indicating the possible choices
   *                  the user can make; if the objects are components, they
   *                  are rendered properly; non-<code>String</code>
   *                  objects are
   *                  rendered using their <code>toString</code> methods;
   *                  if this parameter is <code>null</code>,
   *                  the options are determined by the Look and Feel
   * @param initialValue the object that represents the default selection
   *                  for the dialog; only meaningful if <code>options</code>
   *                  is used; can be <code>null</code>
   * @return an integer indicating the option chosen by the user,
   *                  or <code>CLOSED_OPTION</code> if the user closed
   *                  the dialog
   * @throws HeadlessException if
   <code>GraphicsEnvironment.isHeadless</code> returns
   <code>true</code>
   * @see java.awt.GraphicsEnvironment#isHeadless
   */
  public static CycDialogPane create(Component parentComponent, 
          JPanel panel, String title, int optionType, int messageType, 
          Icon icon, Object[] options, Object initialValue) 
          throws HeadlessException {
    final CycDialogPane pane = new CycDialogPane(
            panel,
            title,
            messageType,
            optionType,
            icon,
            options,
            initialValue);
    pane.setInitialValue(initialValue);
    pane.setComponentOrientation(((parentComponent == null)
            ? JOptionPane.getRootFrame() : parentComponent).getComponentOrientation());
    
    //final JDialog dialog = pane.createDialog(title);
    pane.selectInitialValue();
    //dialog.setAlwaysOnTop(true);
    //dialog.setVisible(true);
    //dialog.dispose();
    return pane;
  }
  
  public static CycDialogPane create(CycServerPanel panel, String title, Object[] options) {
    final Component parentComponent = null;
    final int optionType = JOptionPane.OK_CANCEL_OPTION;
    final int messageType = JOptionPane.QUESTION_MESSAGE;
    final Icon icon = null;
    final Object initialValue = null;
    return create(parentComponent, panel, title, optionType, messageType, icon, options, initialValue);
  }
}
