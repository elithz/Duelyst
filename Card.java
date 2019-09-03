/**
 * 
 */
package Aadad;

import javax.swing.ImageIcon;

/**
 * @author Junhua Hu
 * Sep 30, 2016
 */
public class Card {

	private ImageIcon pic;
	private String name;
	private Integer number;
	private Integer summon;
	private Integer damage;
	private Integer health;
	private Integer attack;
	private Integer move;
	private String descrip;
	
	public Card(){
		this.pic = null;
		this.name = null;
		this.number = null;
		this.summon = null;
		this.damage = null;
		this.health = null;
		this.attack = null;
		this.move = null;
		this.descrip = null;
	}
	
	public Card(ImageIcon pic, String name, Integer number, Integer summon, Integer damage, 
			Integer health, Integer attack, Integer move, String descrip){
		this.pic = pic;
		this.name = name;
		this.number = number;
		this.summon = summon;
		this.damage = damage;
		this.health = health;
		this.attack = attack;
		this.move = move;
		this.descrip = descrip;
	}
	
	public void setPic(ImageIcon pic){
		this.pic = pic;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setNum(Integer number){
		this.number = number;
	}
	
	public void setSumm(Integer summon){
		this.summon = summon;
	}
	
	public void setDam(Integer damage){
		this.damage = damage;
	}
	
	public void setHp(Integer health){
		this.health = health;
	}
	
	public void setAtk(Integer attack){
		this.attack = attack;
	}
	
	public void setMv(Integer move){
		this.move = move;
	}
	
	public void setDes(String descrip){
		this.descrip = descrip;
	}
	
	public ImageIcon getPic(){
		return this.pic;
	}
	
	public String getName(){
		return this.name;
	}
	
	public Integer getNum(){
		return this.number;
	}
	
	public Integer getSumm(){
		return this.summon;
	}
	
	public Integer getDam(){
		return this.damage;
	}
	
	public Integer getHp(){
		return this.health;
	}
	
	public Integer getAtk(){
		return this.attack;
	}
	
	public Integer getMov(){
		return this.move;
	}
	
	public String getDes(){
		return this.descrip;
	}
}