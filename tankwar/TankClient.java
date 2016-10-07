package tankwar;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * 这个类是坦克大战游戏的主窗口
 * @author xh_huang
 *
 */

public class TankClient extends Frame {
	
	/**
	 * 坦克游戏的宽度
	 */
	public static final int GAME_WIDTH = 600;
	public static final int GAME_HEIGHT = 600;
	
	Tank myTank = new Tank(300, 450,true,Direction.STOP,this);


	Wall w1 = new Wall(100,380,20,100,this);
	Wall w2 = new Wall(250,100,200,20,this);

	
	Blood blood = new Blood();
	
	List<Missile> missiles = new ArrayList<Missile>();
	List<Explode> explodes = new ArrayList<Explode>();
	List<Tank> tanks = new ArrayList<Tank>();
	
	Image offScreenImage = null;
	
	public void paint(Graphics g) {
		
		/**
		 * 指明子弹、爆炸、坦克的数量
		 * 及坦克的生命值
		 */
		g.drawString("missiles count:" + missiles.size(), 10, 40);
		g.drawString("explodes count:" + explodes.size(), 10, 60);
		g.drawString("enemytanks count:" + tanks.size(), 10, 80);
		g.drawString("Life values:" + myTank.getLife(), 10, 100);
		
		if(tanks.size() <= 0) {
			for(int i=0;i<5;i++) {
				tanks.add(new Tank(100 + 30*(i + 1),250,false,Direction.L,this));
			}
			
		}
		
	
		for(int i=0; i<missiles.size(); i++) {
			Missile m = missiles.get(i);
			m.hitTanks(tanks);
			m.hitTank(myTank);
			m.hitWall(w1);
			m.hitWall(w2);
			m.draw(g);
		}
		for(int j=0;j < tanks.size();j++) {
			Tank t = tanks.get(j);
			t.collidesWithWall(w1);
			t.collidesWithWall(w2);
			t.collidesWithTanks(tanks);
			t.draw(g);
		}
		for(int i=0;i < explodes.size();i++) {
			Explode e = explodes.get(i);
			e.draw(g);
		}
		blood.draw(g);
		
		//enemyTank.draw(g);
		w1.draw(g);
		w2.draw(g);
		myTank.eat(blood);
		myTank.collidesWithWall(w1);
		myTank.collidesWithWall(w2);
		myTank.collidesWithTanks(tanks);
		myTank.draw(g);
	}


	
	public void update(Graphics g) {
		if(offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.PINK);
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}
	/**
	 * 本方法显示游戏的主窗口
	 */
	public void lauchFrame() {
		
		for(int i=0;i<10;i++) {
			tanks.add(new Tank(100 + 30*(i + 1),250,false,Direction.L,this));
		}
		
		//this.setLocation(400, 300);
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setTitle("TankWar");
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setResizable(false);
		this.setBackground(Color.PINK);
		
		this.addKeyListener(new KeyMonitor());
		
		setVisible(true);
		
		new Thread(new PaintThread()).start();
	}

	/*
	 * main方法，启动游戏
	 */
	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.lauchFrame();
	}
	
	private class PaintThread implements Runnable {

		public void run() {
			while(true) {
				repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private class KeyMonitor extends KeyAdapter {

		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}

		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
		}
		
	}
}













