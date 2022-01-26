package com.cryptoPriceTracker;


import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Insets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;


public class CoinListPanel extends JPanel{
	
	private static final long serialVersionUID = 5L;

	public CoinListPanel(DashBoard dashBoard) {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		JButton backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//move back to the dash panel
				dashBoard.changeCard(dashBoard.DASHPANEL);
			}
		});
		
		JLabel coinLabel = new JLabel("Popular Coins of All Time");
		coinLabel.setFont(new Font("Serif", Font.ITALIC, 20));
		coinLabel.setAlignmentX(CENTER_ALIGNMENT);
		JTable coinTable = null;
		try {
			//initializing the coin table
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CryptoTracker", 
					"root", "");
			String getTrending = "SELECT * FROM CoinListTable";
			ResultSet rs = conn.createStatement().executeQuery(getTrending);
			coinTable = new JTable(new TableModelBuilder().buildTableModel(rs));
			conn.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		JScrollPane jsPane = new JScrollPane();
		jsPane.setPreferredSize(new Dimension(500, 182));
		jsPane.setViewportView(coinTable);
		
		c.gridx = 1;
		c.gridy = 3;
		
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		
		add(backButton,c);
	
		c.insets = new Insets(50,0,0,0);
		c.weightx = 0.2;
		c.gridx = 2;
		c.gridy = 4;
		c.anchor = GridBagConstraints.CENTER;
		
		add(coinLabel,c);
		c.insets = new Insets(30, 0, 0, 0);
		c.gridx = 2;
		c.gridy = 5;
		
		add(jsPane,c);
		c.gridx = 2;
		c.gridy = 6;
		add(Box.createVerticalGlue(), c);
	}
}