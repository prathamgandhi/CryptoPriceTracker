package com.cryptoPriceTracker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JTable;

// The Table Listener that listens for any update to the data in the database

public class UpdateTableListener implements ActionListener{
	Connection conn;
	JTable changeTable;
	String tableName;
	public UpdateTableListener(JTable exchangeTable, String tableName) {
		this.changeTable = exchangeTable;
		this.tableName = tableName;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			
			// Fetches the data from the database, passes the ResultSet to the TableModelBuilder and changes
			// the original table.
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CryptoTracker", 
					"root", "");
			String getTable = "SELECT * FROM " + tableName;
			ResultSet rs = conn.createStatement().executeQuery(getTable);
			changeTable.setModel(new TableModelBuilder().buildTableModel(rs));
			changeTable.revalidate();
			conn.close();
		} catch (SQLException e1) {
			
			e1.printStackTrace();
		}
		
	}
	
}