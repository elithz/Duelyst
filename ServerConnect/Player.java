package cs309;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
/**
 * 
 * @author Zach
 *
 */
public class Player {
	public static String name = "zwild90";
	private ArrayList<Card> deck;
	private Card hero;
	
	public WebSocket wb;
	
	
	public Player(String name, WebSocket wb){
		this.wb = wb;
		this.name = name;
	}
	
	public ArrayList<Card> shuffleDeck(){
		deck = wb.getPlayerDeck(name);
		ArrayList<Card> shuffle = new ArrayList<>(deck);
		for(Card c : deck){
			c.init(name);
		}
		Collections.shuffle(shuffle);
		return shuffle;
	}
	
	public Card getHero(){
		hero = wb.getHero(name);
		hero.init(name);
		return hero;
	}
	
	
	
//	public getMove(){
//		
//	}
}
