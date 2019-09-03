package cs309;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class SearchFrame extends JFrame {
	
	public SearchFrame(String line){
		this.setBounds(1300, 500, 500, 200);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel message = new JLabel(line);
		message.setFont(new Font("Serif", Font.ITALIC | Font.BOLD, 40));
		this.add(message);

	}
}
