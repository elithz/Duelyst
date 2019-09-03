package cs309;

import java.awt.event.ActionEvent;
import java.io.BufferedReader; 
import java.io.IOException; 
import java.io.InputStreamReader; 
import java.io.PrintWriter; 
import java.net.Socket; 
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	
	public static void main(String[] args){ 
		
		Player player2 = new Player("zwild90", new WebSocket());
		int two = player2.wb.joinGame();
		System.out.println("Player 2 turn: "+two);
		Board brd2 = new Board(player2, two);
		brd2.setVisible(true);
	
	}	
}