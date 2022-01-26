package com.cryptoPriceTracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import com.litesoftwares.coingecko.CoinGeckoApiClient;
import com.litesoftwares.coingecko.domain.Exchanges.Exchanges;
import com.litesoftwares.coingecko.domain.Search.TrendingCoin;
import com.litesoftwares.coingecko.domain.Search.TrendingCoinItem;
import com.litesoftwares.coingecko.impl.CoinGeckoApiClientImpl;

// This is a class that runs scheduled data fetches and extends TimerTask for the same

public class ScheduledDataFetch extends TimerTask{
	
	
	Connection conn;
	
	@Override
	public void run() {
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306?allowMultiQueries=true", 
					"root", "");
			CoinGeckoApiClient client = new CoinGeckoApiClientImpl();
			
			//Updating the exchangeList
			
			List<Exchanges> exchangeList = client.getExchanges(20, 1);
			String useAndDeleteExchange = "USE CryptoTracker; DELETE FROM ExchangeTable";
			Statement st = conn.createStatement();
			st.execute(useAndDeleteExchange);
			for(int i = 0; i < exchangeList.size(); i++) {
				Exchanges item = ((Exchanges)exchangeList.get(i));
				String insertExchange = "INSERT INTO ExchangeTable VALUES('"+item.getName()+ "','" + 
							String.valueOf(item.getYearEstablished()) + "','" + item.getUrl()
							+ "','" + String.valueOf(item.getTrustScore()) + "')";
				
				st.execute(insertExchange);
			}
			
			// Updating the trendingCoins List
			ArrayList<TrendingCoin> trendingCoins = (ArrayList<TrendingCoin>) client.getTrending().getCoins();
			String deleteTrending = "DELETE FROM Trending";
			st.execute(deleteTrending);
			for(int i = 0; i < trendingCoins.size(); i++) {
				TrendingCoinItem item = ((TrendingCoin)trendingCoins.get(i)).getItem();
				String insertTrending = "INSERT INTO Trending VALUES('"+item.getName()+ "','" + item.getId()+ "','" + item.getSymbol() + "','" + String.valueOf(item.getPriceBtc()) + "')";
				st.execute(insertTrending);
			}
			client.shutdown();
		}
			
		catch (SQLException e) {
				e.printStackTrace();
		}
		finally {
			try {
				conn.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
	}
}
