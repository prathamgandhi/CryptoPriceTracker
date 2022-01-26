package com.cryptoPriceTracker;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.Dimension;

//This class lists down the exchange information 

public class ExchangeListPanel extends JPanel{
	
	private static final long serialVersionUID = 3L;

	public ExchangeListPanel(DashBoard dashBoard) {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		JButton backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dashBoard.changeCard(dashBoard.DASHPANEL);
			}
		});
		
		JLabel exchangeLabel = new JLabel("Popular Exchanges");
		exchangeLabel.setFont(new Font("Serif", Font.ITALIC, 20));
		exchangeLabel.setAlignmentX(CENTER_ALIGNMENT);
		
		//Initial model of the table
		DefaultTableModel model = new DefaultTableModel();
		JTable exchangeTable = new JTable(model);
		
		//The updateTableListener is a listener class that listens for updates to the tables and performs them
		UpdateTableListener updateExchangeTableListener = new UpdateTableListener(exchangeTable, "ExchangeTable");
		Timer timer = new Timer(12000, updateExchangeTableListener);
		timer.setInitialDelay(0);
		timer.start();
		
		JScrollPane jsPane = new JScrollPane();
		jsPane.setPreferredSize(new Dimension(800, 343));
		jsPane.setViewportView(exchangeTable);
		
		c.gridx = 1;
		c.gridy = 3;
		
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		
		add(backButton,c);
	
		c.insets = new Insets(50,0,0,0);
		c.weightx = 0.2;
		c.gridx = 2;
		c.gridy = 4;
		c.anchor = GridBagConstraints.CENTER;
		
		add(exchangeLabel,c);
		c.insets = new Insets(30, 0, 0, 0);
		c.gridx = 2;
		c.gridy = 5;
		
		add(jsPane,c);
		c.gridx = 2;
		c.gridy = 6;
		add(Box.createVerticalGlue(), c);
	}
}
