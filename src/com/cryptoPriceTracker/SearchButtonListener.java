package com.cryptoPriceTracker;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Color;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.litesoftwares.coingecko.CoinGeckoApiClient;
import com.litesoftwares.coingecko.domain.Coins.CoinFullData;
import com.litesoftwares.coingecko.exception.CoinGeckoApiException;
import com.litesoftwares.coingecko.impl.CoinGeckoApiClientImpl;

// A search button listener that scans the database for info on a coin if present else fetches from 
// coingecko api, if not present there as well, returns an error dialog
// Newly fetched data are successfully fed into the database and used further when no Internet is present
// Also directly updates the market panel and the description panel

public class SearchButtonListener implements ActionListener{
	
	DashBoard dashBoard;
	JFrame dashFrame;
	HintTextField searchField;
	SearchPanel sPanel;
	public SearchButtonListener(DashBoard dashBoard, JFrame dashFrame, HintTextField searchField, SearchPanel sPanel) {
		this.dashBoard = dashBoard;
		this.dashFrame = dashFrame;
		this.searchField = searchField;
		this.sPanel = sPanel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String searchQuery = searchField.getText();
		Connection conn;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CryptoTracker?allowMultiQueries=true", 
					"root", "");
			String checkData = "SELECT EXISTS(SELECT * FROM CoinInfo WHERE ID='" + searchQuery + "') AS OUTPUT";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(checkData);
			rs.next();
			
			// Checks if a database entry with the given searchQuery as ID is already present
			boolean present = false;
			if(rs.getInt("OUTPUT") == 1) {
				present = true;
			}
			
			String name, symbol, description, lastUpdated, id;
			Double  priceUSD, priceINR, twentyfourhrHigh, twentyfourhrLow, marketCap, circulatingSupply, percentChange;
			try {
				CoinGeckoApiClient client = new CoinGeckoApiClientImpl();
				CoinFullData coinData = client.getCoinById(searchQuery, false, false, true, false, false, false);
				
				// Fetching all the data online
				// In case of any error fetching data, local database is checked for the entry
				// If not present in local, error box is shown
				
				name = coinData.getName();
				symbol = coinData.getSymbol();
				description = coinData.getDescription().get("en").replaceAll("\\<.*?>","");
				priceUSD = Double.parseDouble(String.valueOf(coinData.getMarketData().getCurrentPrice().get("usd")));
				priceINR = Double.parseDouble(String.valueOf(coinData.getMarketData().getCurrentPrice().get("inr")));
				twentyfourhrHigh = Double.parseDouble(String.valueOf(coinData.getMarketData().getHigh24h().get("usd")));
				twentyfourhrLow = Double.parseDouble(String.valueOf(coinData.getMarketData().getLow24h().get("usd")));
				marketCap = coinData.getMarketData().getMarketCap().get("usd");
				circulatingSupply = coinData.getMarketData().getCirculatingSupply();
				percentChange = coinData.getMarketData().getPriceChangePercentage24h();
				lastUpdated = Instant.parse(coinData.getLastUpdated()).atOffset(ZoneOffset.ofHoursMinutes(5, 30)).format(DateTimeFormatter.ofPattern( "uuuu-MM-dd HH:mm:ss" ));;
				if(present) {
					
					// if entry is present then just update the entry
					
					PreparedStatement pst = conn.prepareStatement("UPDATE CoinInfo SET priceUSD = ?, priceINR = ?, 24hrHIGH = ?, 24hrLOW = ?,"
							+ "MarketCap = ?, CirculatingSupply = ?, 24hrPercentChange = ?, LastUpdated = ? WHERE ID = ?");
					pst.setDouble(1, priceUSD);
					pst.setDouble(2, priceINR);
					pst.setDouble(3, twentyfourhrHigh);
					pst.setDouble(4, twentyfourhrLow);
					pst.setDouble(5, marketCap);
					pst.setDouble(6, circulatingSupply);
					pst.setDouble(7, percentChange);
					pst.setString(8, lastUpdated);
					pst.setString(9, searchQuery);
					pst.executeUpdate();
				}
				else if (!present){
					
					// If not present then insert the entry
	
					String insertData = "INSERT INTO CoinInfo (ID, Name, Symbol, Description, PriceUSD,"
							+ "PriceINR, 24hrHIGH, 24hrLOW, MarketCap, CirculatingSupply, 24hrPercentChange, LastUpdated)"
							+ "VALUES ('" + searchQuery + "','" + name + "','" + symbol + "','" + description + "','" +
							priceUSD + "','" + priceINR + "','" + twentyfourhrHigh + "','" + twentyfourhrLow + "','" + 
							marketCap + "','" + circulatingSupply + "','" + percentChange + "','" + lastUpdated + "')";
					st.executeUpdate(insertData);
				}
				client.shutdown();
				String updateDataInGUI = "SELECT * FROM CoinInfo WHERE ID = '" + searchQuery + "'";
				ResultSet updateRS = st.executeQuery(updateDataInGUI);
				updateRS.next();
				
				sPanel.mp.circulatingSupply.setText(String.valueOf(updateRS.getDouble("CirculatingSupply")));
				sPanel.mp.marketCap.setText(String.valueOf(updateRS.getDouble("MarketCap")));
				sPanel.mp.percentChange.setText(String.valueOf(updateRS.getDouble("24hrPercentChange")));
				sPanel.mp.priceINR.setText(String.valueOf(updateRS.getDouble("PriceINR")));
				sPanel.mp.priceUSD.setText(String.valueOf(updateRS.getDouble("PriceUSD")));
				sPanel.mp.twentyfourhrHigh.setText(String.valueOf(updateRS.getDouble("24hrHIGH")));
				sPanel.mp.twentyfourhrLow.setText(String.valueOf(updateRS.getDouble("24hrLOW")));
				sPanel.mp.lastUpdated.setText(updateRS.getString("LastUpdated"));
				sPanel.dp.coinName.setText(updateRS.getString("Name"));
				sPanel.dp.coinSymbol.setText(updateRS.getString("Symbol"));
				sPanel.dp.descriptionArea.setText(updateRS.getString("Description"));
				
				if(updateRS.getDouble("24hrPercentChange") >= 0) {
					sPanel.mp.percentChange.setForeground(Color.GREEN);
				}
				else {
					sPanel.mp.percentChange.setForeground(Color.RED);
				}
				sPanel.searchBar.setText(searchQuery);
				conn.close();
				dashBoard.changeCard(dashBoard.SEARCHPANEL);
			}
			catch(CoinGeckoApiException c1) {
				System.out.println("Error fetching data online, trying in the local database");
				c1.printStackTrace();
				if(!present) {
					JDialog modal = createDialog(dashFrame);
					modal.setVisible(true);
				}
			}
			
		}
		catch(SQLException s1) {
			createDialog(dashFrame);
		}
		
	}
	
	// Error dialog box creater
	
	private static JDialog createDialog(JFrame frame) {
	    JDialog modalDialog = new JDialog(frame, "Error Dialog", JDialog.ModalityType.DOCUMENT_MODAL);
	    modalDialog.setBounds(500, 400, 400, 200);
	    Container dialogContainer = modalDialog.getContentPane();
	    dialogContainer.setLayout(new BorderLayout());
	    JPanel panel2 = new JPanel();
	    JButton okButton = new JButton("OK");
	    okButton.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent ae) {
	        modalDialog.setVisible(false);
	      }
	    });
	    panel2.add(okButton);
	    panel2.add(Box.createVerticalStrut(30));
	    JPanel panel1 = new JPanel();
	    panel1.setLayout(new FlowLayout());
	    panel1.add(new JLabel("Some error occurred :("));
	    JPanel panel0 = new JPanel();
	    panel0.add(Box.createVerticalStrut(30));
	    dialogContainer.add(panel0, BorderLayout.NORTH);
	    dialogContainer.add(panel1, BorderLayout.CENTER);
	    dialogContainer.add(panel2, BorderLayout.SOUTH);
	    return modalDialog;
	  }
	
}

