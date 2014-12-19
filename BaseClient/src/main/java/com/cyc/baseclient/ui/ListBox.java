package com.cyc.baseclient.ui;

/*
 * #%L
 * File: ListBox.java
 * Project: Base Client
 * %%
 * Copyright (C) 2013 - 2014 Cycorp, Inc.
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

/**
 * Provides a gui List component.
 *
 * @version $Id: ListBox.java 155483 2014-12-10 21:56:51Z nwinant $
 * @author Stephen L. Reed
 */

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ListBox extends JPanel {
    protected Action addAction, removeAction, updateAction;
    protected JList list = new JList();
    protected JTextField field = new JTextField();
    protected JButton addButton = new JButton("Add");
    protected JButton removeButton = new JButton("Remove");
    protected Object lastSelected = null;

    /**
     * Constructs a new ListBox object given a title and row count.
     *
     * @param title the list box title
     * @param rowCount the number of list items in the list box.
     */
    public ListBox(String title, int rowCount) {
        this(null, null, null, title, rowCount);
    }

    /**
     * Constructs a ListBox initialized with the given actions, title and row count.
     *
     * @param addAction the action to be taken when an item is added to the list box
     * @param removeAction the action to be taken when an item is removed from the list box
     * @param updateAction the action to be taken when an item is modified in the list box
     * @param title the list box title
     * @param rowCount the number of list items in the list box.
     */
    public ListBox(Action addAction,
                   Action removeAction,
                   Action updateAction,
                   String title,
                   int rowCount){
        super();
        this.addAction = addAction;
        this.removeAction = removeAction;

        setLayout(new BorderLayout());
        removeButton.setEnabled(false);
        list.setVisibleRowCount(rowCount);
        list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent ev) {
                int idx = ev.getFirstIndex();

                if (idx == -1)
                    lastSelected = null;
                else
                    lastSelected =
                        ((JList) ev.getSource()).getModel().getElementAt(idx);

                if (ev.getValueIsAdjusting())
                    return;

                if (((JList) ev.getSource()).getSelectedIndex() == -1)
                    removeButton.setEnabled(false);
                else
                    removeButton.setEnabled(true);
            }
        });

        setLayout(new BorderLayout());
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BorderLayout());
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(2, 2));

        listPanel.add(new JLabel(title), BorderLayout.NORTH);
        listPanel.add(new JScrollPane(list,
                                  JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                  JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),
                      BorderLayout.CENTER);

        gridPanel.add(addButton);
        gridPanel.add(field);

        gridPanel.add(removeButton);
        add(listPanel, BorderLayout.CENTER);
        add(gridPanel, BorderLayout.SOUTH);

        if (addAction != null)
            addButton.addActionListener(addAction);

        if (removeAction != null)
            removeButton.addActionListener(removeAction);

        // also update after add/remove
        if (updateAction != null) {
            addButton.addActionListener(updateAction);
            removeButton.addActionListener(updateAction);

            updateAction.actionPerformed(new ActionEvent(this, 0, null));
        }
    }

    // Change the action set
    public void setActions(Action add, Action remove, Action update) {
        if (addAction != null)
            addButton.removeActionListener(addAction);

        if (removeAction != null)
            removeButton.removeActionListener(removeAction);

        if (updateAction != null) {
            addButton.removeActionListener(updateAction);
            removeButton.removeActionListener(updateAction);
        }

        addAction = add;
        removeAction = remove;
        updateAction = update;

        if (addAction != null)
            addButton.addActionListener(addAction);

        if (removeAction != null)
            removeButton.addActionListener(removeAction);

        if (updateAction != null) {
            addButton.addActionListener(updateAction);
            removeButton.addActionListener(updateAction);

            updateAction.actionPerformed(new ActionEvent(this, 0, null));
        }
    }

    /**
     * Gets the selected value.
     */
    public Object getSelectedValue() {
        return lastSelected;
    }

    /**
     * Gets the list that the ListBox is modelling.
     */
    public JList getList() {
        return list;
    }

    /**
     * Gets the text field.
     */
    public JTextField getField() {
        return field;
    }
}
