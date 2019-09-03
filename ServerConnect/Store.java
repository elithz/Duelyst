package cs309;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * @author Zach
 */
public class Store extends JFrame {
	WebSocket wb;
	int allCards[] = {1,2,3,4,5,6,7,8,9,10};
	String userName;
	int pageNum;
	JLabel pageLabel, pointsLabel;
	JPanel cardsPane;
	/**
	 * 
	 * @param wb, pass through the websocket to connect to the sever
	 * 		(Make sure server.js is running first)
	 * @param name, name of the user is needed to display number of points
	 * 		as well as buy cards for the user
	 */
	public Store(WebSocket wb, String name) {
		this.wb = wb;
		this.userName = name;
		allCards = wb.getAllCards();
		int x, y;
		x = 1200;
		y = 1000;
		pageNum = 1;
		this.setSize(x, y);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    JPanel buttonPane = new JPanel();
	    this.add(buttonPane, BorderLayout.NORTH);
	    
	    JButton backButton = new JButton("Back");
	    JButton prevButton = new JButton("Previous");
	    prevButton.addActionListener(new prevClick());
	    pageLabel = new JLabel("Page "+pageNum);
	    JButton nextButton = new JButton("Next");
	    nextButton.addActionListener(new nextClick());
	    pointsLabel = new JLabel(userName+": "+wb.getPoints(userName)+" pts");
	    
	    buttonPane.add(backButton, BorderLayout.WEST);
	    buttonPane.add(prevButton, BorderLayout.CENTER);
	    buttonPane.add(pageLabel, BorderLayout.CENTER);
	    buttonPane.add(nextButton, BorderLayout.CENTER);
	    buttonPane.add(pointsLabel, BorderLayout.EAST);
	    
	    cardsPane = new JPanel();
	    cardsPane.setLayout(new GridLayout(3,3));
	    this.add(cardsPane, BorderLayout.CENTER);
	    refreshPage();
	    
	    
	  }

	public void refreshPage(){
		cardsPane.removeAll();
		cardsPane.repaint();
		int remainder = allCards.length - (pageNum-1)*9;
		int end = Math.min(9, remainder );
		for(int i = 0; i < end; i++){
			int cardNum = (pageNum-1)*9+i;
			JPanel grid = cardGrid(allCards[cardNum]);
			cardsPane.add(grid, 0.5);
		}
	}
	  
	  private JPanel cardGrid(int i) {
		Color textColor = new Color(200,200,150);
		Color backColor = new Color(200,200,200);
		String str = wb.getCard(i);
		int points = wb.getPoints(userName);
		Card card = new Card(str);
		
		
		JPanel grid = new JPanel();
		JPanel cardPane = new CardPane(card);

		grid.setBackground(backColor);
		
		grid.add(cardPane, BorderLayout.NORTH);
		JButton buy = new JButton("Pay "+card.price+" Pts");
		buy.addActionListener(new buyClick(card.price, card.num));
		if(card.price > points){
			buy.setEnabled(false);
		}
		grid.add(buy);
		buy.setAlignmentX(CENTER_ALIGNMENT);
		grid.setLayout(new BoxLayout(grid, BoxLayout.PAGE_AXIS));
		grid.setAlignmentY(CENTER_ALIGNMENT);
		return grid;
	}

	
	class prevClick implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(pageNum > 1 ){
				pageNum--;
				refreshPage();
				pageLabel.setText("Page "+pageNum);
			}
		}
	}
	
	class nextClick implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(pageNum <= allCards.length/9){
				pageNum++;
				refreshPage();
				pageLabel.setText("Page "+pageNum);
			}
		}
	}
	
	class buyClick implements ActionListener{
		private int price, cardNum;
		public buyClick(int price, int cardNum) {
			this.price = price;
			this.cardNum = cardNum;
		}

		public void actionPerformed(ActionEvent e){
			wb.editPoints(userName, -price);
			pointsLabel.setText(userName+": "+wb.getPoints(userName)+" pts");
			refreshPage();
			wb.addtoLib(userName, cardNum);
		}
	}
	
}
