package cs309;

import java.util.Scanner;

/**
 * @author Zach
 */
public class Card {
	public String name, des;
	public int move, sum, num;
	public int attkPts, attkDam, attkRng;
	public int hp, price, health;
	
	public int movesLeft, canAttk;
	
	public String playerName, info;
	
	
	public Card(String line){
		Scanner scan = new Scanner(line);
		scan.useDelimiter("_");
		info = line;
		name = scan.next();
		num = scan.nextInt();
		sum = scan.nextInt();
		attkDam = scan.nextInt();
		move = scan.nextInt();
		attkRng = scan.nextInt();
		hp = scan.nextInt();
		attkPts = scan.nextInt();
		des = scan.next();
		price = scan.nextInt();
	}
	
	//Initializes owner of card and the health of card
	public void init(String plyrNm){
		playerName = plyrNm;
		health = hp;
		canAttk = attkRng;
		reset();
	}
	
	public boolean ownCard(String name){
		return playerName.equals(name);
	}
	
	public void reset(){
		movesLeft = move;
		canAttk = attkRng;
	}
	
}