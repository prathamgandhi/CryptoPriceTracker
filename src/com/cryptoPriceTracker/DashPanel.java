package com.cryptoPriceTracker;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Font;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class DashPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	public DashPanel(DashBoard dashBoard, JFrame dashFrame, SearchPanel sPanel){
		
		DefaultTableModel model = new DefaultTableModel();
		JTable trendingTable = new JTable(model);
		UpdateTableListener updateTrendingTableListener = new UpdateTableListener(trendingTable, "Trending");
		
		// Swing timer to update the trending table from the event thread
		Timer timer = new Timer(12000, updateTrendingTableListener);
		timer.setInitialDelay(0);
		timer.start();
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		JPanel operationPanel = new JPanel();
		operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.Y_AXIS));
	
		JLabel cryptoSearch = new JLabel("Crypto Search");
		cryptoSearch.setFont(new Font("Serif", Font.ITALIC, 50));
		
		JPanel trendingCoins = new JPanel();
		JLabel trendingLabel = new JLabel("Trending");
		trendingCoins.setLayout(new BoxLayout(trendingCoins, BoxLayout.Y_AXIS));
		trendingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		trendingCoins.add(trendingLabel);
		trendingCoins.add(Box.createVerticalStrut(20));
		JScrollPane jsPane = new JScrollPane();
		jsPane.setViewportView(trendingTable);
		jsPane.setPreferredSize(new Dimension(700, 134));
		trendingCoins.add(jsPane);
		
		JPanel publicHoldings = new JPanel();
		JLabel holdingLabel = new JLabel("Public Holdings");
		publicHoldings.add(holdingLabel);
		
		JPanel searchBarPanel = new JPanel();
		HintTextField searchField = new HintTextField("Search some crypto here...");
		searchField.setPreferredSize(new Dimension(400, 20));
		JButton searchButton = new JButton("Search");
		
		searchButton.addActionListener(new SearchButtonListener(dashBoard, dashFrame, searchField, sPanel));
		
		
		JPanel helperButtons = new JPanel();
		JButton popularExchanges = new JButton("List popular exchanges");
		JButton coinList = new JButton("List popular coins");
		
		popularExchanges.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//moving to exchange list card panel
				dashBoard.changeCard(dashBoard.EXCHANGELISTPANEL);
			}
		});
		
		coinList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//moving to coinlist card panel
				dashBoard.changeCard(dashBoard.COINLISTPANEL);
			}
		});
		
		helperButtons.add(popularExchanges);
		helperButtons.add(coinList);
		
		searchBarPanel.add(searchField);
		searchBarPanel.add(searchButton);
		
		cryptoSearch.setAlignmentX(CENTER_ALIGNMENT);
		cryptoSearch.setBorder(BorderFactory.createEmptyBorder(5,5,20,5));
		searchBarPanel.setAlignmentX(CENTER_ALIGNMENT);
		searchBarPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		helperButtons.setAlignmentX(CENTER_ALIGNMENT);
		helperButtons.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
		
		operationPanel.add(cryptoSearch);
		operationPanel.add(searchBarPanel);
		operationPanel.add(helperButtons);
		operationPanel.setOpaque(true);
		
		c.weighty = 0.8;
		c.gridx = 1;
		c.gridy = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0;
		add(operationPanel , c);
		c.gridx = 1;
		c.gridy = 2;
		c.weighty = 0.9;
		
		add(trendingCoins, c);
		c.weighty = 1;
		c.gridx = 1;
		c.gridy = 3;
		add(Box.createVerticalGlue(), c);
		
	}
}