package cs309;

import java.awt.*;  
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.swing.*;  
  
public class Test {
	public static void main (String[] args){
		
//		WebSocket wb = new WebSocket();
//		wb.reg("name", "password"); // Once you register, you can comment this out
		Player player1 = new Player("ztwild", new WebSocket());
		int one = player1.wb.joinGame();
		System.out.println("Player 1 turn: "+one);
		Board brd1 = new Board(player1, one);
		brd1.setVisible(true);
		
//		WebSocket wb = new WebSocket();
//		
//		System.out.println(wb.checkAdmin("ztwild"));
//		System.out.println(wb.checkAdmin("admin"));
		
		
	}
}