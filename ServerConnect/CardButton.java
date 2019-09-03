package cs309;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
/**
 * 
 * @author Zach
 *
 */
public class CardButton extends JButton {
	private Card card = null;
	private Color color;
	public int x, y;
	public boolean frontline, toAttk, toMove;
	
	private Border moveBorder = new LineBorder(Color.GREEN, 5);
	private Border attkBorder = new LineBorder(Color.RED, 5);
	private Border myBorder = null;
	
	public CardButton(int x, int y){
		this.x = x;
		this.y = y;
		frontline = (y == 0);
		color = ((x+y)%2 == 0 ? Color.WHITE : Color.GRAY);
		this.setBackground(color);
		this.setPreferredSize(new Dimension(100, 100));
		
	}
	
	public void setCard(Card c, String pName){
		card = c;
		ImageIcon icon = null;
		try {
			String path = "./Pics/"+c.name+".jpg";
			Image img =  ImageIO.read(new File(path));
			img = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
			icon = new ImageIcon(img);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setIcon(icon);
		if(card != null && !card.ownCard(pName)){
			myBorder = new LineBorder(Color.BLUE, 5);
			this.setBorder(myBorder);
		}
			
	}
	
	public void clearBtn(){
		card = null;
		this.setIcon(null);
		myBorder = null;
		this.setBorder(null);
	}
	
	public void moveOutline(){
		if(card == null){
			this.setBorder(moveBorder);
			toMove = true;
		}
	}
	
	public void attkOutline(String name){
		if(card != null && !card.ownCard(name)){
			this.setBorder(attkBorder);
			toAttk = true;
		}
	}
	
	public void clearBorder(){
		this.setBorder(myBorder);
		toMove = toAttk = false;
		
	}
	
	public Card getCard(){
		return card;
	}
	
	public boolean getFrontlineStat(){
		return frontline && card == null;
	}
	public int getDist(CardButton cb){
		int deltaX = Math.abs(x - cb.x);
		int deltaY = Math.abs(y - cb.y);
		return deltaX + deltaY;
	}
}
