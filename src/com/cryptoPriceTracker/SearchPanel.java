package com.cryptoPriceTracker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dimension;

// The search Panel from where the user can see the info related to a particular coin


public class SearchPanel extends JPanel{
	
	private static final long serialVersionUID = 4L;
	DescriptionPanel dp;
	MarketPanel mp;
	HintTextField searchBar;
	public SearchPanel(DashBoard dashboard, JFrame dashFrame) {
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		searchBar = new HintTextField("Search some crypto here...");
		searchBar.setPreferredSize(new Dimension(400, 20));
		JButton searchButton = new JButton("Search");
		JPanel searchBarPanel = new JPanel();
		JButton backButton = new JButton("Home");
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dashboard.previousCard();
			}
		});
		
		// The entire panel is sent to the SearchButtonListener which can use it to access its 
		// child panels and their components.
		
		searchButton.addActionListener(new SearchButtonListener(dashboard, dashFrame, searchBar, this));
		backButton.setBorder(new RoundedBorder(5));
		
		searchBarPanel.add(backButton);
		searchBarPanel.add(Box.createHorizontalStrut(10)); 
		searchBarPanel.add(searchBar);
		searchBarPanel.add(Box.createHorizontalStrut(10));
		searchBarPanel.add(searchButton);
		
		JTabbedPane tp = new JTabbedPane();
		dp = new DescriptionPanel();
		mp = new MarketPanel();
		
		tp.add("Description", dp);
		tp.add("Market Details", mp);
		
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets(5, 0, 10, 5);
		add(searchBarPanel, c);
		c.insets = new Insets(5, 75, 10, 0);
		c.gridx = 0;
		c.gridy = 3;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 6;
		c.gridheight = 6;
		add(tp, c);
	}
}