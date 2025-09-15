package yılan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class Screen extends JPanel implements ActionListener, KeyListener {
	private class Tile {
		int x, y;
		Tile(int x, int y){
			this.x=x;
			this.y=y;
		}
	}
	
	int en, boy;
	int tileSize=25;
	Tile snakeHead;
	ArrayList<Tile> snakeBody;
	Tile elma;
	Random random;
	Timer dongu;
	int hizX, hizY;
	boolean gameOver=false;
	int skor=0;
	
	Screen(int en, int boy){
		this.en=en;
		this.boy=boy;
		setPreferredSize(new Dimension(this.en, this.boy));
		setBackground(Color.black);
		addKeyListener(this);
		setFocusable(true);
		
		snakeHead=new Tile(5, 5);
		snakeBody=new ArrayList<Tile>();
		
		elma=new Tile(10, 10);
		random=new Random();
		elmaYerlestir();
		
		hizX=0;
		hizY=0;
		
		dongu=new Timer(300, this);
		dongu.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		//ızgara çizimi
		for(int i=0; i<en/tileSize; i++) {
			g.drawLine(i*tileSize, 0, i*tileSize, boy);
			g.drawLine(0, i*tileSize, en, i*tileSize);
		}
		
		//duvarlar
		g.setColor(Color.blue);
		g.fillRect(0, 0, en, tileSize);
		g.fillRect(0, 0, tileSize, boy);
		g.fillRect(0, 575, en, tileSize);
		g.fillRect(575, 0, tileSize, boy);

		//skor
		g.setColor(Color.white);
		g.setFont(new Font("serif", Font.BOLD, 21));
		g.drawString("Skor: "+skor, 268, 594);
		
		//elma
		g.setColor(Color.red);
		g.fillOval(elma.x*tileSize, elma.y*tileSize, tileSize, tileSize);
		
		//yılanın başı
		g.setColor(Color.green);
		g.fill3DRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize, true);
		
		//yılanın gövdesi
		for(int i=0; i<snakeBody.size(); i++) {
			Tile snakePart=snakeBody.get(i);
			g.fill3DRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize, true);
		}
		
		//oyun bitince
		if(gameOver) {
			g.setColor(Color.white);
			g.setFont(new Font("serif", Font.BOLD, 31));
			g.drawString("Press Space to Restart!", 150, 309);
		}
	}
	
	public void elmaYerlestir() {
		elma.x=random.nextInt((en/tileSize)-2)+1;
		elma.y=random.nextInt((boy/tileSize)-2)+1;
	}
	
	public boolean carpisma(Tile tile1, Tile tile2) {
		return (tile1.x==tile2.x && tile1.y==tile2.y);
	}

	public void hareket() {
		if(carpisma(snakeHead, elma)) {
			snakeBody.add(new Tile(snakeHead.x, snakeHead.y));
			skor+=25;
			elmaYerlestir();
		}
		
		for(int i=snakeBody.size()-1; i>=0; i--) {
			Tile snakePart=snakeBody.get(i);
			if(i==0) {
				snakePart.x=snakeHead.x;
				snakePart.y=snakeHead.y;
			}else {
				Tile previousSnakePart=snakeBody.get(i-1);
				snakePart.x=previousSnakePart.x;
				snakePart.y=previousSnakePart.y;
			}
		}
		
		snakeHead.x+=hizX;
		snakeHead.y+=hizY;
		
		for(int i=0; i<snakeBody.size(); i++) {
			Tile snakePart=snakeBody.get(i);
			if(carpisma(snakeHead, snakePart)) {
				gameOver=true;
			}
		}
		
		Rectangle yilan=new Rectangle(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize);
		Rectangle duvarUst=new Rectangle(0, 0, en, tileSize);
		Rectangle duvarSag=new Rectangle(576, 0, tileSize, boy);
		Rectangle duvarSol=new Rectangle(0, 0, tileSize, boy);
		Rectangle duvarAlt=new Rectangle(0, 576, en, tileSize);
		if(yilan.intersects(duvarUst) || yilan.intersects(duvarSag) || yilan.intersects(duvarSol) || yilan.intersects(duvarAlt)) {
			gameOver=true;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		hareket();
		repaint();
		if(gameOver) {
			dongu.stop();
		}
	}
	
	@Override
	public void keyPressed(KeyEvent ke) {
		if((ke.getKeyCode()==KeyEvent.VK_UP || ke.getKeyCode()==KeyEvent.VK_W) && hizY!=1) {
			hizX=0;
			hizY=-1;
		}else if((ke.getKeyCode()==KeyEvent.VK_DOWN || ke.getKeyCode()==KeyEvent.VK_S) && hizY!=-1) {
			hizX=0;
			hizY=1;
		}else if((ke.getKeyCode()==KeyEvent.VK_RIGHT || ke.getKeyCode()==KeyEvent.VK_D) && hizX!=-1) {
			hizX=1;
			hizY=0;
		}else if((ke.getKeyCode()==KeyEvent.VK_LEFT || ke.getKeyCode()==KeyEvent.VK_A) && hizX!=1) {
			hizX=-1;
			hizY=0;
		}
		
		if(ke.getKeyCode()==KeyEvent.VK_SPACE) {
			if(gameOver) {
				skor=0;
				gameOver=false;
				
				snakeHead=new Tile(5, 5);
				snakeBody=new ArrayList<Tile>();
				
				elma=new Tile(10, 10);
				random=new Random();
				elmaYerlestir();
				
				hizX=0;
				hizY=0;
				
				dongu=new Timer(300, this);
				dongu.start();
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent ke) {}

	@Override
	public void keyReleased(KeyEvent ke) {}
}
