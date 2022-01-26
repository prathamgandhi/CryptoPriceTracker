package com.cryptoPriceTracker;

import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

//This is the tabbed pane for displaying the description of the searched coin
//All the components are controlled from the SearchButtonListener
public class DescriptionPanel extends JPanel {
	
	private static final long serialVersionUID = 6L;
	JTextArea descriptionArea = new JTextArea();
	JTextField coinName = new JTextField();
	JTextField coinSymbol = new JTextField();
	
	public DescriptionPanel() {
		
		coinName.setEditable(false);
		coinSymbol.setEditable(false);
		descriptionArea.setEditable(false);
		descriptionArea.setLineWrap(true);
		descriptionArea.setWrapStyleWord(true);
		descriptionArea.setPreferredSize(new Dimension(700, 400));
		
		JLabel coinNameLabel = new JLabel("Coin Name");
		JLabel coinSymbolLabel = new JLabel("Coin Symbol");
		JLabel descriptionAreaLabel = new JLabel("Description");
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 1;
		c.gridy = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(20, 20, 20, 20);
		add(coinNameLabel, c);
		c.gridx = 2;
		c.anchor = GridBagConstraints.LINE_END;
		add(coinName, c);
		c.gridx = 1;
		c.gridy = 2;
		c.anchor = GridBagConstraints.LINE_START;
		add(coinSymbolLabel, c);
		c.gridx = 2;
		c.anchor = GridBagConstraints.LINE_END;
		add(coinSymbol, c);
		c.gridx = 1;
		c.gridy = 3;
		c.anchor = GridBagConstraints.LINE_START;
		add(descriptionAreaLabel, c);
		c.gridx = 2;
		c.weighty = 1;
		c.gridheight = 8;
		c.anchor = GridBagConstraints.LINE_END;
		add(descriptionArea, c);
		
	}
}