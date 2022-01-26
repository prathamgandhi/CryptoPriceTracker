package com.cryptoPriceTracker;


import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

// Class for designing the market panel tabbed pane
 
public class MarketPanel extends JPanel{
	
	private static final long serialVersionUID = 8L;
	
	JTextField priceUSD = new JTextField(), priceINR = new JTextField(), twentyfourhrHigh = new JTextField()
			, twentyfourhrLow = new JTextField(), marketCap = new JTextField(), 
			circulatingSupply = new JTextField(), percentChange = new JTextField(), lastUpdated = new JTextField();
	
	public MarketPanel() {
		JLabel priceUSDLabel = new JLabel("Price USD"), priceINRLabel = new JLabel("Price INR")
			,twentyfourhrHighLabel = new JLabel("24hr High"), twentyfourhrLowLabel = new JLabel("24hr LOW"), 
			marketCapLabel = new JLabel("Market Cap"), circulatingSupplyLabel = new JLabel("Circulating Supply"),
			percentChangeLabel = new JLabel("Percent Change/24hrs"), lastUpdatedLabel = new JLabel("Last Updated");
		priceUSD.setEditable(false);
		priceINR.setEditable(false);
		twentyfourhrHigh.setEditable(false);
		twentyfourhrLow.setEditable(false);
		marketCap.setEditable(false);
		circulatingSupply.setEditable(false);
		percentChange.setEditable(false);
		lastUpdated.setEditable(false);
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(10, 30, 30, 10);
		add(priceUSDLabel, c);
		c.gridy = 2;
		add(priceINRLabel, c);
		c.gridy = 3;
		add(twentyfourhrHighLabel, c);
		c.gridy = 4;
		add(twentyfourhrLowLabel, c);
		c.gridy = 5;
		add(marketCapLabel, c);
		c.gridy = 6;
		add(circulatingSupplyLabel, c);
		c.gridy = 7;
		add(percentChangeLabel, c);
		c.gridy = 8;
		add(lastUpdatedLabel, c);
		c.weightx = 1;
		c.gridx = 2;
		c.gridy = 1;
		c.anchor = GridBagConstraints.LINE_END;
		add(priceUSD, c);
		c.gridy = 2;
		add(priceINR, c);
		c.gridy = 3;
		add(twentyfourhrHigh, c);
		c.gridy = 4;
		add(twentyfourhrLow, c);
		c.gridy = 5;
		add(marketCap, c);
		c.gridy = 6;
		add(circulatingSupply, c);
		c.gridy = 7;
		add(percentChange, c);
		c.gridy = 8;
		add(lastUpdated, c);
			
	}
}