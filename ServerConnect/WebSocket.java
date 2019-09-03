package cs309;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * 
 * @author Zach
 *
 */
public class WebSocket {
	private Socket socket = null;
	private PrintWriter out = null;
	private BufferedReader in = null;
	public String temp_returnStr;
	
	public WebSocket(){
		try {
			socketConnect();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BufferedReader getReader(){
		return in;
	}
	
	//Creates Connection to Web Socket
	private void socketConnect() throws UnknownHostException, IOException {
		String ip = "10.65.222.239";
//        String ip = "localhost";
		int port = 8000;
		System.out.println("[Connecting to socket...]"); 
		this.socket = new Socket(ip, port);
		out = new PrintWriter(getSocket().getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(getSocket().getInputStream()));
		
	}
	//Function to login
	public boolean login(String name, String pass){
//		String func = "{\"func\":[{\"type\":\"login\",";
//		func += " \"name\":\""+name+"\", \"pass\":\""+pass+"\"}]}";
		String func = "login&"+name+"&"+pass+"&";
		
		try {
			out.println(func); 
			String returnStr = in.readLine();
			if(returnStr.equals("true")){
				return true;
			}else{
				return false;
			}

		}catch(IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	//Delete User
	public boolean del(String name){
//		String func = "{\"func\":[{\"type\":\"del\",";
//		func += " \"name\":\""+name+"\"}]}";	
		String func = "del&"+name+"&";
		try {
			out.println(func); 
			String returnStr = in.readLine();
			System.out.println(returnStr);
			if(returnStr.equals("true")){
				return true;
			}
			else if(returnStr.equals("User Does Not Exist")){
				System.out.println(returnStr);
				return false;
			}else{
				return false;
			}

		}catch(IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	//Register User
	public boolean reg(String name, String pass){
//		String func = "{\"func\":[{\"type\":\"reg\",";
//		func += " \"name\":\""+name+"\", \"pass\":\""+pass+"\"}]}";
		String func = "reg&"+name+"&"+pass+"&";
		
		try {
			out.println(func); 
			String returnStr = in.readLine();
			System.out.println(returnStr);
			temp_returnStr = returnStr;
			if(returnStr.equals("true")){
				return true;
			}
			else if(returnStr.equals("Username Already Used")){
				System.out.println(returnStr);
				return false;
			}else{
				return false;
			}

		}catch(IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	//Return CardS Library
	public int[][] getLib(String name){
		int[][] lib = new int[0][];
//		String func = "{\"func\":[{\"type\":\"getlib\", \"name\":\""+name+"\"}]}";
		String func = "getlib&"+name+"&";
		
		out.println(func);
		try {
			String s = in.readLine();
			if(s.equals("null"))
				return lib;
			int n = Integer.parseInt(s);
			lib = new int[n][2];
			for(int i = 0; i < n; i++){
				String num = in.readLine();
				String total = in.readLine();
				lib[i][0] = Integer.parseInt(num);
				lib[i][1] = Integer.parseInt(total);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lib;
	}
	
	//Return CardS Deck
	public String[] getDeck(String name){
//		String func = "{\"func\":[{\"type\":\"getdeck\", \"name\":\""+name+"\"}]}";
		String func = "getdeck&"+name+"&";
		return getCards(func);
	}
	
	
	private String[] getCards(String function){
		out.println(function);
		String[] cards = new String[0];
		try {
			String s = in.readLine();
			System.out.println(s);
			if(s.equals("null"))
				return cards;
			int n = Integer.parseInt(s);
			cards = new String[n];
			for(int i = 0; i < n; i++){
				cards[i] = in.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cards;
	}
	
	//Return CardS Info
	public String getCard(int num){
//		String func = "{\"func\":[{\"type\":\"getcard\", \"num\":\"" +num+ "\"}]}";
		String func = "getcard&"+num+"&";
		out.println(func);
		try {
			return in.readLine();
		}catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//Player joins game, returns 0 or 1 if it is their turn.
	//And -1 if the pairing was unsuccessful.
	public int joinGame(){
		int turn = -1;
//		String func = "{\"func\":[{\"type\":\"joingame\"}]}";
		String func = "joingame&";
		out.println(func);
		try {
			SearchFrame sf = new SearchFrame("Searching for Player...");
			sf.setVisible(true);
			System.out.println(in.readLine());
			turn = Integer.parseInt(in.readLine());
			sf.setVisible(false);
		}catch(IOException e) {
			e.printStackTrace();
			
		}
		return turn;
	}
	
	//Sends message back if opponent receives the message, 
	public void sendMove(String move){
//		String func = "{\"func\":[{\"type\":\"sendmove\", \"move\":\"" + move + "\"}]}";
		String func = "sendmove&"+move+"&";
		System.out.println("Sending move -> "+func);
		out.println(func);
	}
	
	//Receive move
	public String receiveMessage(){
		try {
			String message = in.readLine();
			return message;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//Add CardS to deck
	public void addtoDeck(String name, int cardNum){
//		String func = "{\"func\":[{\"type\":\"addtodeck\", \"name\":\""+name+"\", ";
//		func += "\"num\":\""+cardNum+"\"}]}";
		String func = "addtodeck&"+name+"&"+cardNum+"&";
		out.println(func);
	}
	
	//Add CardS to players library of cards
	public void addtoLib(String name, int cardNum){
//		String str = "{\"func\":[{\"type\":\"addtolib\", \"name\":\""+name+"\", ";
//		str += "\"num\":\""+cardNum+"\"}]}";
		String func = "addtolib&"+name+"&"+cardNum+"&";
		out.println(func);
	}
	
	//remove CardS to players library of cards
	public void removefromLib(String name, int cardNum){
//		String str = "{\"func\":[{\"type\":\"removefromlib\", \"name\":\""+name+"\", ";
//		str += "\"num\":\""+cardNum+"\"}]}";
		String func = "removefromlib&"+name+"&"+cardNum+"&";
		out.println(func);
	}
	
	
	//Remove card from deck
	public void removefromDeck(String name, int cardNum){
//		String str = "{\"func\":[{\"type\":\"removefromdeck\", \"name\":\""+name+"\", ";
//		str += "\"num\":\""+cardNum+"\"}]}";
		String func = "removefromdeck&"+name+"&"+cardNum+"&";
		out.println(func);
	}
	
	//Ends game
	public void endGame(){
//		String str = "{\"func\":[{\"type\":\"endgame\"}]}";
		String func = "endgame&";
		out.println(func);
	}
	
	//Reward/Spend player points  adds or subtracts points
	public void editPoints(String name, int points){
//		String str = "{\"func\":[{\"type\":\"editpoints\", \"name\":\""+name+"\", ";
//		str += "\"points\":\""+points+"\"}]}";
		String func = "editpoints&"+name+"&"+points+"&";
		out.println(func);
	}
	
	//Get Users points
	public int getPoints(String name){
		int points = 0;
//		String str = "{\"func\":[{\"type\":\"getpoints\", \"name\":\""+name+"\"}]}";
		String func = "getpoints&"+name+"&";
		out.println(func);
		try {
			String pt = in.readLine();
			points = Integer.parseInt(pt);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return points;
	}
	
	//Get array of all the card numbers
	public int[] getAllCards(){
		int[] cards = null;
//		String str = "{\"func\":[{\"type\":\"getallcards\"}]}";
		String func = "getallcards&";
		out.println(func);
		try {
			String number = in.readLine();
			int length = Integer.parseInt(number);
			cards = new int[length];
			for(int i = 0; i < length; i++){
				number = in.readLine();
				int num = Integer.parseInt(number);
				cards[i] = num;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return cards;
	}
	
	public ArrayList<Card> getPlayerDeck(String name){
//		String str = "{\"func\":[{\"type\":\"getplayerdeck\", \"name\":\""+name+"\"}]}";
		String func = "getplayerdeck&"+name+"&";
		return getFullCards(func);
	}
	
	public Card getHero(String name){
//		String str = "{\"func\":[{\"type\":\"gethero\", \"name\":\""+name+"\"}]}";
		String func = "gethero&"+name+"&";
		out.println(func);
		String card = "null";
		try {
			card = in.readLine();
		} catch (IOException e) { 
			e.printStackTrace();
			return null;
		}
		return new Card(card);
	}
	
	private ArrayList<Card> getFullCards(String function){
		out.println(function);
		ArrayList<Card> cards = new ArrayList<>();
		try {
			String s = in.readLine();
			System.out.println(s);
			if(s.equals(null))
				return null;
			int n = Integer.parseInt(s);
			for(int i = 0; i < n; i++){
				Card c = new Card(in.readLine());
				cards.add(c);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cards;
	}

	//Show users
	public String[] showUsers(){
//		String str = "{\"func\":[{\"type\":\"showusers\"}]}";
		String func = "showusers&";
		out.println(func);
		int n;
		try {
			n = Integer.parseInt(in.readLine());
			String[] users = new String[n];
			for(int i = 0; i < n; i++){
				users[i] = in.readLine();
			}
			return users;
		}catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean checkAdmin(String name){
		String func = "checkadmin&"+name+"&";
		out.println(func);
		try {
			String statement = in.readLine();
			if(statement.equals("true"))
 				return true;
 			else
 				return false;
		} catch (IOException e) {
			System.out.println("checkAdmin: Could not read response");
			e.printStackTrace();
		}
		
		return false;
	}
	
	//Register Admin
	public boolean regAdmin(String name, String pass){
//		String func = "{\"func\":[{\"type\":\"regadmin\",";
//		func += " \"name\":\""+name+"\", \"pass\":\""+pass+"\"}]}";
		String func = "regadmin&"+name+"&"+pass+"&";
		
		try {
			out.println(func); 
			String returnStr = in.readLine();
			System.out.println(returnStr);
			if(returnStr.equals("true")){
				return true;
			}
			else if(returnStr.equals("Username Already Used")){
				System.out.println(returnStr);
				return false;
			}else{
				return false;
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	//Returns the Socket
	private Socket getSocket() {
		return socket;
	}
	
	 
}

