package com.cyc.session.internal;

/*
 * #%L
 * File: CycServerPanel.java
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

import com.cyc.session.CycServer;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * A GUI panel which prompts the user for a Cyc hostname & base port.
 * 
 * @author nwinant
 */
public class CycServerPanel extends JPanel {

  public CycServerPanel(CycServer server) {
    hostField = new JTextField(server.getHostName(), 20);
    portField = new JComboBox(getBasePorts());
    if (server.getBasePort() != null) {
      portField.setSelectedItem(server.getBasePort());
    }
    add(new JLabel("Cyc host and base port:"));
    add(hostField);
    add(portField);
    
    addComponentListener(new ComponentAdapter() {
      @Override
      public void componentShown(ComponentEvent ce) {
        hostField.requestFocusInWindow();
      }
    });
  }
  
  public CycServerPanel(String defaultHost, Integer defaultPort) {
    this(new CycServer(defaultHost, defaultPort));
  }
  
  public CycServerPanel() {
    this(CycServer.DEFAULT);
  }
  
  
  // Public
  
  public CycServer getCycServer() {
    return new CycServer(getHostName(), getBasePort());
  }
  
  
  // Protected
  
  final protected Integer[] getBasePorts() {
    return new Integer[]{3600, 3620, 3640, 3660, 3680};
  }
  
  protected String getHostName() {
    return hostField.getText();
  }
  
  protected Integer getBasePort() {
    if (portField.getSelectedItem() != null) {
      return Integer.parseInt(portField.getSelectedItem().toString());
    }
    return null;
  }
  
  
  // Internal  
  
  final private JTextField hostField;
  final private JComboBox portField;
  protected static final String TITLE="Set Cyc Connection";
  
  // Static
  
  /**
   * Presents a CycConnectionPanel to the user via a JOptionPane, and
   * returns the user's input wrapped in a CycServer object.
   * 
   * @param server
   * @return Returns a CycServer object wrapping the user's input, or null if they cancel.
   */
  private static CycServer wrapInJOptionPane(CycServerPanel panel) {
    final Object[] options = { "OK", "Cancel" };
    int result = JOptionPane.showOptionDialog(null, panel, TITLE,
              JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
    if (result == JOptionPane.OK_OPTION) {
      return panel.getCycServer();
    }
    return null;
  }
  
  /**
   * Creates a CycConnectionPanel, presents it to the user via a JOptionPane, and
   * returns the user's input wrapped in a CycServer object.
   * 
   * @param server
   * @return Returns a CycServer object wrapping the user's input, or null if they cancel.
   */
  public static CycServer promptUser(CycServer server) {
    return wrapInJOptionPane(new CycServerPanel(server));
  }
  
  /**
   * Creates a CycConnectionPanel, presents it to the user via a JOptionPane, and
   * returns the user's input wrapped in a CycServer object.
   * 
   * @param defaultHost 
   * @param defaultPort 
   * @return Returns a CycServer object wrapping the user's input, or null if they cancel.
   */
  public static CycServer promptUser(String defaultHost, Integer defaultPort) {
    return wrapInJOptionPane(new CycServerPanel(defaultHost, defaultPort));
  }
  
  /**
   * Creates a CycConnectionPanel, presents it to the user via a JOptionPane, and
   * returns the user's input wrapped in a CycServer object.
   * 
   * @return Returns a CycServer object wrapping the user's input, or null if they cancel.
   */
  public static CycServer promptUser() {
    return wrapInJOptionPane(new CycServerPanel());
  }

}
