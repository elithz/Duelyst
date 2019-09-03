package cs309;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.AncestorListener;
import javax.swing.table.DefaultTableModel;

/**
 * 
 * @author Zach
 *
 */

public class Board extends JFrame {
	private JLabel headerLabel;
	private JPanel gridBoard;
	private JPanel handPane;
	private JPanel selectPane;
	private JButton drawBtn;
	private JButton unselectBtn;
	private JButton endTurnBtn;
	private ArrayList<Card> deck;
	private boolean startGame, first;
	private ArrayList<Card> cardsNPlay = new ArrayList<>();
	
	private Player player;
	private String playerName;
	private int actionPts;
	private CardButton[][] grid = new CardButton[10][10];
	
	private Card selected;
	private CardPane selectedCP;
	private CardButton selectedBtn;
	private static Object lock;
	private static boolean serverResponse = false;
	
	static private int x = 1300;
	static private int y = 1400;
	static private boolean session;
	
	
	
	public Board(Player player, int turn) {
		lock = new Object();
		startGame = first = false;
		if(turn == 1)
			first = true;
		this.player = player;
		playerName = player.name;
		deck = player.shuffleDeck();
		Card hero = player.getHero();
		this.setSize(x, y);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		
		
		JPanel bottomPane = new JPanel();
		bottomPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		this.add(bottomPane, BorderLayout.SOUTH);
		
		//Make Header
		JPanel header = new JPanel();
//		header.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		headerLabel = new JLabel();
		headerLabel.setFont(new Font("Serif", Font.BOLD, 40));
		header.add(headerLabel);
		this.add(header, BorderLayout.NORTH);
		
		
		//Makes the grid board
		gridBoard = new JPanel();
		gridBoard.setLayout(new GridLayout(10,10));
//		gridBoard.setPreferredSize(new Dimension(1000, 1000));
		this.add(gridBoard, BorderLayout.WEST);
		fillGrid();
		
		//Makes selected Card panel
		selectPane = new JPanel();
		selectPane.setLayout(new BoxLayout(selectPane, BoxLayout.PAGE_AXIS));
		selected = hero;
		selectedCP = new CardPane(hero);
		JLabel healthBar = new JLabel(selected.name+"'s Health: "+selected.health);
		selectPane.add(healthBar).setFont(new Font("Serif", Font.BOLD, 15));
		selectPane.add(selectedCP);
		unselectBtn = new JButton("Unselect Card");
		unselectBtn.setVisible(false);
		selectPane.add(selectedCP);
		selectPane.add(unselectBtn);
		this.add(selectPane);
		
		//Makes the hand panel
		handPane = new JPanel();
		handPane.setLayout(new GridLayout(1,7));
		bottomPane.add(handPane, BorderLayout.WEST);
		
		//Makes the button Panel
		JPanel buttonPane = new JPanel();
		bottomPane.add(buttonPane);
		
		drawBtn = new JButton("Draw Card");
		drawBtn.addActionListener(new drawClick());
		for(int i = 0; i < 5; i++)
			drawCard();
		actionPts = 8;
		updateHeader();
//		checkDraw();
		
		endTurnBtn = new JButton("End Turn");
		endTurnBtn.addActionListener(new EndTurnClick());
		
		buttonPane.add(drawBtn);
		buttonPane.add(endTurnBtn);
		drawBtn.setEnabled(false);
		endTurnBtn.setEnabled(false);
		
		new gameListener().start();
	}
	
	public void updateHeader(){
		headerLabel.setText(playerName+": Action Pts "+actionPts);
	}
	
	public void checkDraw(){
		boolean drawEnable = (handPane.countComponents() >= 7 || deck.isEmpty() 
				|| actionPts < 5);
		drawBtn.setEnabled(!drawEnable);
	}
	
	public void drawCard(){
		Card c = deck.remove(0);
		CardPane cp = new CardPane(c);
		cp.addMouseListener(new SelectCard());
		handPane.add(cp);
		handPane.revalidate();
		actionPts -= 5;
		updateHeader();
		checkDraw();
	}
	
	public void fillGrid(){
		for(int i = 0; i < 100; i++){
			int row = 9 - i/10;
			int col = i%10;
			CardButton btn = new CardButton(col, row);
			grid[col][row] = btn;
			btn.addActionListener(new selectBtn());
			gridBoard.add(btn);
		}
	}
	
	public void clearOptions(){
		for(CardButton[] btnCol :  grid)
			for(CardButton btn : btnCol)
				btn.clearBorder();
	}
	
	public void showOptions(){		
		int maxDist = Math.min(selected.movesLeft, actionPts);
		for(CardButton[] btnCol :  grid){
			for(CardButton btn : btnCol){
				Card card = selectedBtn.getCard();
				int dist = btn.getDist(selectedBtn);

				if(dist <= maxDist && btn.getCard() == null){
					btn.moveOutline();
				}else if(dist <= card.attkRng && card.canAttk > 0 
						&& card.attkPts <= actionPts){
					btn.attkOutline(playerName);
				}
			}
		}
	}
	
	public void update(){
		clearSelected();
		checkDraw();
		updateHeader();
		if(actionPts <= 0){
			endTurn();
		}
	}
	
	public void nextTurn(){ 
		if(!deck.isEmpty() && handPane.countComponents() < 7)
			drawCard();
		actionPts = 8;
		updateHeader();
		startGame = true;
		endTurnBtn.setEnabled(true);
		checkDraw();
		for(Card card : cardsNPlay){
			card.reset();
		}
	}

	class drawClick implements ActionListener{
		public void actionPerformed(ActionEvent e){
			drawCard();
		}
	}
	
	public void showSelectPane(Card c){
		JLabel ownerTag = new JLabel(c.playerName+"'s Card\n");
		JLabel healthBar = new JLabel(c.name+"'s Health: "+c.health);
		healthBar.setFont(new Font("Serif", Font.BOLD, 15));
		selectPane.add(ownerTag);
		selectPane.add(healthBar);
		selectPane.add(new CardPane(c));
		JButton btn = new JButton("Unselect Card");
		btn.addActionListener(new UnselectClick());
		selectPane.add(btn);
		selectPane.revalidate();
		selectPane.setVisible(true);
		selected = c;
	}
	
	class UnselectClick implements ActionListener{
		public void actionPerformed(ActionEvent e){
			selectPane.setVisible(false);
			clearSelected();
		}
	}
	
	public void clearSelected(){
		clearOptions();
		selectPane.removeAll();
		selectPane.setVisible(false);
		selectPane.repaint();
		selected = null;
		selectedCP = null;
		selectedBtn = null;
	}
	
	class SelectCard extends MouseAdapter{
		public void mousePressed(MouseEvent e){
			if(startGame){
				clearSelected();
				selectedCP = (CardPane) e.getSource();
				selected = selectedCP.getCard();
				selectedBtn = null;
				showSelectPane(selected);
			}
			
//			handPane.remove(selectedPane);
//			handPane.revalidate();
		}
		
	}
	
	
	public void endTurn(){
		if(startGame){
			System.out.println("Turn Ended");
			endTurnBtn.setEnabled(false);
			drawBtn.setEnabled(false);
			startGame = false;
			player.wb.sendMove("nextTurn");
			clearOptions();
		}
	}
	
	class EndTurnClick implements ActionListener{
		public void actionPerformed(ActionEvent e){
			endTurn();
		}
	}
	
	class selectBtn implements ActionListener{
		CardButton cb;
		public void actionPerformed(ActionEvent e){
			cb = (CardButton) e.getSource();
			Card card = cb.getCard();
			boolean isSelected = (selected != null);
			boolean openGrid = (card == null);
			
			if(!startGame && cb.getFrontlineStat() && isSelected){
				String message = "start:"+cb.x+":"+cb.y+":"+playerName+":"+selected.info;
				sendMove(message);
				//Message Sent
				cb.setCard(selected, playerName);
				cardsNPlay.add(selected);
				clearSelected();
				startGame = first;
				endTurnBtn.setEnabled(startGame);
				drawBtn.setEnabled(startGame);
				
			}else if(isSelected && startGame){
				if(selectedCP != null && cb.getFrontlineStat() && actionPts >= selected.sum){
					String message = "start:"+cb.x+":"+cb.y+":"+playerName+":"+selected.info;
					sendMove(message);
					selected.reset();
					//Message Sent
					cb.setCard(selected, playerName);
					cardsNPlay.add(selected);
					handPane.remove(selectedCP);
					actionPts -= selected.sum;
					update();
				}
				//if clicked grid players card
				else if(!openGrid && card.ownCard(playerName)){
					clearSelected();
					selectedBtn = cb;
					showSelectPane(card);
					if(card.ownCard(playerName))
						showOptions();
				}
				
				//if character can attack
				else if(cb.toAttk){
					card.health -= selected.attkDam;
					int dist = selectedBtn.getDist(cb);
					if(card.attkRng >= dist){
						selected.health -= card.attkDam;
						String message = "attack:"+selectedBtn.x+":"+selectedBtn.y;
						message += ":"+card.attkDam;
						sendMove(message);
						if(selected.health <= 0){
							selectedBtn.clearBtn();
						}
					}
					if(card.health <= 0){
						cb.clearBtn();
						if(card.sum == 0){//if Hero
							endGame("Winner");
						}
					}
					selected.canAttk = 0;
					actionPts -= selected.attkPts;
					//attack:1:1:40
					String message = "attack:"+cb.x+":"+cb.y+":"+selected.attkDam;
					sendMove(message);
					update();
				}
				//if character can move
				else if(cb.toMove && selectedBtn != null){
					int dist = selectedBtn.getDist(cb);
					actionPts -= dist;
					selected.movesLeft -= dist;
					//Send message //move:0:1:1:2
					String message = "move:"+selectedBtn.x+":"+selectedBtn.y+":";
					message += cb.x+":"+cb.y;
					sendMove(message);
					cb.setCard(selected, playerName);
					selectedBtn.clearBtn();
					update();	
				}else{
					update();
					if(card != null){
						showSelectPane(card);
					}
				}
			}else if(!isSelected && startGame){
				if(!openGrid){
					selectedBtn = cb;
					showSelectPane(card);
					if(card.ownCard(playerName))
						showOptions();
				}
			}
		}
	}
	
	public void sendMove(String mess){
		player.wb.sendMove(mess);
//		synchronized(lock){
//			while(serverResponse == false){
//				try{
//					lock.wait();
//				}catch(Exception e){ }
//			}
//		}
	}
	
	class gameListener extends Thread{
		public void run(){
			session = true;
			while(session){
				BufferedReader in = null;
				try{
					in = player.wb.getReader();
					String message = in.readLine();
					if(message.equals("End Game")){
						endGame("Winner");
					}else{
						readMove(message);
//						synchronized(lock){
//							serverResponse = true;
//							readMove(message);
//							lock.notifyAll();
//						}
					}
				}catch(IOException e){
					endGame("Winner");
				}
//				serverResponse = false;
			}
		}
	}
	
	//Reading a line for move, attack, or end of turn
		//move:0:1:1:2
		//start:0:1:zwild90:King_01_02_02_3_04_Description_30
		//attack:1:1:40
		public void readMove(String line){
			Scanner scan = new Scanner(line);
			scan.useDelimiter(":");
			String move = scan.next();
			if(move.equals("move")){
				int x = 9 - scan.nextInt();
				int y = 9 - scan.nextInt();
				CardButton from = grid[x][y];
				x = 9 - scan.nextInt();
				y = 9 - scan.nextInt();
				CardButton to = grid[x][y];
				to.setCard(from.getCard(), playerName);
				from.clearBtn();
			}else if(move.equals("start")){
				int x = 9 - scan.nextInt();
				int y = 9 - scan.nextInt();
				String partnerName = scan.next();
				String info = scan.next();
				Card card = new Card(info);
				card.init(partnerName);
				CardButton to = grid[x][y];
				to.setCard(card, playerName);       
			}else if(move.equals("attack")){
				int x = 9 - scan.nextInt();
				int y = 9 - scan.nextInt();
				CardButton cb = grid[x][y];
				int attkDam = scan.nextInt();
				cb.getCard().health -= attkDam;
				if(cb.getCard().health <=0){
					if(cb.getCard().sum == 0 && cb.getCard().ownCard(playerName)){
						endGame("Loser");
						sendMove("End Game");
					}
					cb.clearBtn();
				}
					
			}else if(move.equals("nextTurn")){
				nextTurn();
			}
			
		}
	
	public void endGame(String stat){
		SearchFrame sf = new SearchFrame("Game Over: "+stat);
		sf.setVisible(true);
		session = false;
		this.setVisible(false);
	}
	
}


