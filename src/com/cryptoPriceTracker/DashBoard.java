/*
	Entry point of the program
	
	Author : Pratham Gandhi
	Roll No. : 20115903
	Branch : CSE

*/
package com.cryptoPriceTracker;

import java.awt.CardLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Timer;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.litesoftwares.coingecko.impl.CoinGeckoApiClientImpl;
import com.litesoftwares.coingecko.CoinGeckoApiClient;
import com.litesoftwares.coingecko.domain.Search.TrendingCoin;
import com.litesoftwares.coingecko.domain.Search.TrendingCoinItem;

public class DashBoard{
	
	public JFrame dashFrame = new JFrame();
	private CardLayout cardLayout = new CardLayout(20, 10);
	
	//The use of card layout to change the panes ensures lesser overhead which comes while creating
	//new JFrames.
	
	public String DASHPANEL = "Dash Board Panel";
    public String SEARCHPANEL = "After Search Panel";
    public String COINLISTPANEL = "Coin List Panel";
	public String EXCHANGELISTPANEL = "Exchange List Panel";
    
	public DashBoard(){
		
		dashFrame.setLocationRelativeTo(null);
		dashFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		dashFrame.getContentPane().setLayout(cardLayout);
		
		
		SearchPanel searchPanel = new SearchPanel(this, dashFrame);	//The panel where search operations are performed
		DashPanel dashPanel = new DashPanel(this, dashFrame, searchPanel);
		 // Panel for dashBoard, passing searchPanel to control searchPanel components from here
		CoinListPanel coinListPanel = new CoinListPanel(this);
		//	Panel to List the known coins
		ExchangeListPanel exchangeListPanel = new ExchangeListPanel(this);
		//	Panel to list current popular exchanges
		
		dashFrame.add(dashPanel, DASHPANEL);
		dashFrame.add(searchPanel, SEARCHPANEL);
		dashFrame.add(coinListPanel, COINLISTPANEL);
		dashFrame.add(exchangeListPanel, EXCHANGELISTPANEL);
		
		dashFrame.pack();
		dashFrame.setVisible(true);
	}
	
	public static void main(String [] args) throws SQLException {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					Thread.sleep(2000);
					//	To prevent race conditions from occurring
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				new DashBoard();
			}
		});
		CoinGeckoApiClient client = new CoinGeckoApiClientImpl();
		
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306?allowMultiQueries=true", 
				"root", "");
		try {
			
			//All basic DB initialization functions
			createDatabase(conn);
			createTrendingTable(conn);
			createCoinInfoTable(conn);
			createExchangeTable(conn);
			createCoinListTable(conn);
			
			
			//setting this from the EDT to ensure its immediate display without race conditions
			ArrayList<TrendingCoin> trendingCoins = (ArrayList<TrendingCoin>) client.getTrending().getCoins();
			String sql = "DELETE FROM Trending";
			conn.createStatement().execute(sql);
			for(int i = 0; i < trendingCoins.size(); i++) {
				TrendingCoinItem item = ((TrendingCoin)trendingCoins.get(i)).getItem();
				sql = "INSERT INTO Trending VALUES('"+item.getName()+ "','" + item.getId()+ "','" + item.getSymbol() + "','" + String.valueOf(item.getPriceBtc()) + "')";
				conn.createStatement().execute(sql);
			}
				
			//Timer sets all the regular timely scheduling tasks on a background thread
			try {
				Timer timeit = new Timer("ScheduledDataFetch");
				ScheduledDataFetch task = new ScheduledDataFetch();	
				timeit.schedule(task, 10000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		catch(Exception e) {
			System.out.println("Exception occurred " + e);
		}
		finally {
			conn.close();
		}
		client.shutdown();
		
	}
	
	
	public void nextCard() {
		cardLayout.next(dashFrame.getContentPane());
	}
	
	public void previousCard() {
		cardLayout.previous(dashFrame.getContentPane());	
	}
	
	public void changeCard(String id) {
		cardLayout.show(dashFrame.getContentPane(), id);
	}
	private static void createTrendingTable(Connection conn) throws SQLException{
		String sqlCreate = "CREATE TABLE IF NOT EXISTS Trending"
        + "  (Name           VARCHAR(40),"
        + "	  ID			 TEXT,"
        + "   Symbol         VARCHAR(20),"
        + "   Price_BTC      TEXT)";

		Statement stmt = conn.createStatement();
		stmt.execute(sqlCreate);
	}
	private static void createDatabase(Connection conn) throws SQLException{
		String sql = "CREATE DATABASE IF NOT EXISTS CryptoTracker;"
				+ "USE CryptoTracker";
		Statement stmt = conn.createStatement();
		stmt.execute(sql);
	}
	private static void createCoinInfoTable(Connection conn) throws SQLException{
		String sql = "CREATE TABLE IF NOT EXISTS CoinInfo"
				+ "(ID TEXT,"
				+ "Name TEXT,"
				+ "Symbol VARCHAR(5),"
				+ "Description TEXT,"
				+ "PriceUSD DOUBLE,"
				+ "PriceINR DOUBLE,"
				+ "24hrHIGH DOUBLE,"
				+ "24hrLOW DOUBLE,"
				+ "MarketCap DOUBLE,"
				+ "CirculatingSupply DOUBLE,"
				+ "24hrPercentChange DOUBLE,"
				+ "LastUpdated TEXT)";
		
		Statement stmt = conn.createStatement();
		stmt.execute(sql);
	}
	private static void createExchangeTable(Connection conn) throws SQLException{
		String sql = "CREATE TABLE IF NOT EXISTS ExchangeTable"
				+ "(Name TEXT,"
				+ "Year_Established TEXT,"
				+ "Link TEXT,"
				+ "Trust_Score INT)";
		Statement stmt = conn.createStatement();
		stmt.execute(sql);
	}
	private static void createCoinListTable(Connection conn) throws SQLException{
		String sql = "CREATE TABLE IF NOT EXISTS CoinListTable"
				+ "(No INT NOT NULL AUTO_INCREMENT,"
				+ "Name TEXT,"
				+ "ID TEXT,"
				+ "Symbol TEXT,"
				+ "PRIMARY KEY (No))";
		Statement stmt = conn.createStatement();
		stmt.execute(sql);
		sql = "SELECT EXISTS(SELECT 1 FROM Trending) AS OUTPUT";
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
		if(rs.getInt("OUTPUT") == 0) {
			sql = "INSERT INTO CoinListTable(Name, ID, Symbol) VALUES ('Bitcoin', 'bitcoin', 'btc');"
				+ "INSERT INTO CoinListTable(Name, ID, Symbol) VALUES ('Ethereum', 'ethereum', 'eth');"
				+ "INSERT INTO CoinListTable(Name, ID, Symbol) VALUES ('Binance Coin', 'binancecoin', 'bnb');"
				+ "INSERT INTO CoinListTable(Name, ID, Symbol) VALUES ('Tether', 'tether', 'usdt');"
				+ "INSERT INTO CoinListTable(Name, ID, Symbol) VALUES ('Solana', 'solana', 'sol');"
				+ "INSERT INTO CoinListTable(Name, ID, Symbol) VALUES ('Cardano', 'cardano', 'ada');"
				+ "INSERT INTO CoinListTable(Name, ID, Symbol) VALUES ('USD Coin', 'usd-coin', 'usdc');"
				+ "INSERT INTO CoinListTable(Name, ID, Symbol) VALUES ('XRP', 'ripple', 'xrp');"
				+ "INSERT INTO CoinListTable(Name, ID, Symbol) VALUES ('Terra', 'terra-luna', 'luna');"
				+ "INSERT INTO CoinListTable(Name, ID, Symbol) VALUES ('Polkadot', 'polkadot', 'dot')";
			stmt.execute(sql);
		}
	}
}