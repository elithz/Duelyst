package cs309;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cs309.Store.buyClick;
/**
 * 
 * @author Zach
 *
 */
public class CardPane extends JPanel{
	Color gold = new Color(200,200,150);
	Color black = new Color(200,200,200);
	Color maroon = new Color(100,40,50);
	Card card;
	
	
	public CardPane(Card card){
		this.card = card;
		int summon = card.sum;
		String name = card.name;
		JLabel cardName = new JLabel(name+" : "+summon);
		this.add(cardName);
		this.setBackground(maroon);
		JLabel pic = new JLabel("Picture Not Found");
		try{
			String path = "./Pics/"+name+".jpg";
			Image img =  ImageIO.read(new File(path));
			img = img.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
			ImageIcon icon = new ImageIcon(img);
			pic = new JLabel(icon);
		}catch(IOException e){
			e.printStackTrace();
		}
		this.add(pic, 0.5);
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		JLabel hpStat = new JLabel("Hp: "+card.hp);
		JLabel moveStat = new JLabel("Move: "+card.move);
		JLabel attkStat = new JLabel("Attk: "+card.attkDam);
		JLabel attkPtsStat = new JLabel("Attk Pts: "+card.attkPts);
		JLabel rangeStat = new JLabel("Attk Range: "+card.attkRng);
		
		cardName.setForeground(gold);
		hpStat.setForeground(gold);
		moveStat.setForeground(gold);
		attkStat.setForeground(gold);
		attkPtsStat.setForeground(gold);
		rangeStat.setForeground(gold);
		
		this.add(hpStat);
		this.add(moveStat);
		this.add(attkStat);
		this.add(attkPtsStat);
		this.add(rangeStat);
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	}
	
	public Card getCard(){
		return card;
	}
}
