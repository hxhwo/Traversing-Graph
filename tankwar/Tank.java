package tankwar;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

/**
 * 坦克类
 * @author xh_huang
 *
 */
public class Tank {
	
	 
	/**
	 * 坦克每次横向移动的距离
	 */
	public static final int XSPEED = 5;
	public static final int YSPEED = 5;
	
	/**
	 * 坦克的宽度
	 */
	public static final int WIDTH = 30;
	
	/**
	 * 坦克的高度
	 */
	public static final int HEIGHT = 30;
	
	TankClient tc;
	
	private boolean bL=false, bU=false, bR=false, bD = false;
	
	//坦克默认移动方向
	private Direction dir = Direction.STOP;
	
	//坦克炮筒的方向，默认向上
	private Direction ptDir = Direction.U;
	

	private boolean live = true;
	
	private BloodBar bb = new BloodBar();
	
	//坦克的生命值
	private int life = 100;

	private Random r = new Random();
	private int step = r.nextInt(12) + 3;
	
	private int x, y;
	private int oldX,oldY;
	
	private boolean good;
	public boolean isGood() {
		return good;
	}

	public void setGood(boolean good) {
		this.good = good;
	}
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public Tank(int x, int y,boolean good) {
		this.x = x;
		this.y = y;
		this.oldY = y;
		this.oldX = x;
		this.good = good;
	}
	
	public Tank(int x, int y, boolean good,Direction dir,TankClient tc) {
		
		this(x, y,good);
		this.dir = dir;
		this.tc = tc;
	}
	

	public void draw(Graphics g) {
		
		//若敌军被被打，消失
		if(!live) {
			if(!good){
				tc.tanks.remove(this);
			}
			return;
		}
	
		Color c = g.getColor();
		if(good) {
			g.setColor(Color.RED);
		}else 
			g.setColor(Color.YELLOW);
		
		g.fillRect(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		
		
		if(good) {
		bb.draw(g);
		}
		switch(ptDir) {
		case L:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y + Tank.HEIGHT/2);
			break;
		case LU:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y);
			break;
		case U:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH/2, y);
			break;
		case RU:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y);
			break;
		case R:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y + Tank.HEIGHT/2);
			break;
		case RD:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y + Tank.HEIGHT);
			break;
		case D:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH/2, y + Tank.HEIGHT);
			break;
		case LD:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y + Tank.HEIGHT);
			break;
		}
		
		move();
	}
	
	
	
	/**
	 * 根据游戏用户的按键方向移动
	 */
	void move() {
		
		this.oldX = x;
		this.oldY = y;
		
		switch(dir) {
		case L:
			x -= XSPEED;
			break;
		case LU:
			x -= XSPEED;
			y -= YSPEED;
			break;
		case U:
			y -= YSPEED;
			break;
		case RU:
			x += XSPEED;
			y -= YSPEED;
			break;
		case R:
			x += XSPEED;
			break;
		case RD:
			x += XSPEED;
			y += YSPEED;
			break;
		case D:
			y += YSPEED;
			break;
		case LD:
			x -= XSPEED;
			y += YSPEED;
			break;
		case STOP:
			break;
		}
		
		if(this.dir != Direction.STOP) {
			this.ptDir = this.dir;
		}
		
		/**
		 * 坦克只允许在固定框架内活动
		 */
		if(x < 0) x = 0;
		if(y < 30) y = 30;
		if(x + Tank.WIDTH > TankClient.GAME_WIDTH)
			x = TankClient.GAME_WIDTH - Tank.WIDTH;
		if(y + Tank.HEIGHT > TankClient.GAME_HEIGHT) 
			y = TankClient.GAME_HEIGHT - Tank.HEIGHT;
	
		/**
		 * 敌军坦克随机的移动
		 */
		if(!good) {
			Direction[] dirs = Direction.values();
			
			if(step == 0) {
				step = r.nextInt(12) + 3;
				int rn = r.nextInt(dirs.length);
				dir = dirs[rn];
			}
			step--;
			//控制敌军发射子弹的频率
			if(r.nextInt(40) > 38)
			this.fire();
		}
	}
	
	private void stay() {
		x = oldX;
		y = oldY;
	}
	
	/**
	 * 对按键的监听
	 * @param e 键盘事件
	 */
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_F2:
			if(!this.live) {
				this.live = true;
				this.life = 100;
			}
		case KeyEvent.VK_LEFT :
			bL = true;
			break;
		case KeyEvent.VK_UP :
			bU = true;
			break;
		case KeyEvent.VK_RIGHT :
			bR = true;
			break;
		case KeyEvent.VK_DOWN :
			bD = true;
			break;
		}
		locateDirection();
	}
	
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		
		case KeyEvent.VK_CONTROL:
			fire();
			break;
		case KeyEvent.VK_LEFT :
			bL = false;
			break;
		case KeyEvent.VK_UP :
			bU = false;
			break;
		case KeyEvent.VK_RIGHT :
			bR = false;
			break;
		case KeyEvent.VK_DOWN :
			bD = false;
			break;
		case KeyEvent.VK_A: 
			superFire();
			break;
		}
		locateDirection();		
	}
	
	/**
	 * 重新定位
	 */
	void locateDirection() {
		if(bL && !bU && !bR && !bD) dir = Direction.L;
		else if(bL && bU && !bR && !bD) dir = Direction.LU;
		else if(!bL && bU && !bR && !bD) dir = Direction.U;
		else if(!bL && bU && bR && !bD) dir = Direction.RU;
		else if(!bL && !bU && bR && !bD) dir = Direction.R;
		else if(!bL && !bU && bR && bD) dir = Direction.RD;
		else if(!bL && !bU && !bR && bD) dir = Direction.D;
		else if(bL && !bU && !bR && bD) dir = Direction.LD;
		else if(!bL && !bU && !bR && !bD) dir = Direction.STOP;
	}

	/**
	 * 射击敌军
	 * @return 每射击一次，弹出一颗子弹
	 */
	public Missile fire() {
		if(!live) return null;
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x,y,good,ptDir,this.tc);
		tc.missiles.add(m);
		return m;
	}
	
	/**
	 * 按指定的方向射出子弹
	 * @param dir 指定的方向
	 * @return 发出一个子弹
	 */
	public Missile fire(Direction dir) {
		if(!live) return null;
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x, y, good,dir,this.tc);
		tc.missiles.add(m);
		return m;
	}
	
	/**
	 *在指定的 每个方向都射出一发子弹
	 */
	private void superFire() {
		
		Direction[] dirs = Direction.values();
		for(int i = 0;i<8;i++) {
			fire(dirs[i]);
		}
	}
	public Rectangle getRect() {
		return new Rectangle(x,y,WIDTH,HEIGHT);
	}
	
	
	/**
	 * 撞墙
	 * @param w 被撞的墙
	 * @return 撞上返回true，否则false
	 */
	public boolean collidesWithWall(Wall w) {
		if( this.live && this.getRect().intersects(w.getRect())) {
			this.stay();
			return true;
	}
	return false;
	}
	
	/**
	 * 坦克互撞
	 * @param tanks 坦克类
	 * @return 若撞到坦克则返回true，否则false
	 */
	public boolean collidesWithTanks( java.util.List<Tank> tanks) {
		for(int i=0;i<tanks.size();i++) {
			Tank t = tanks.get(i);
			if(this != t){
			if( this.live && this.getRect().intersects(t.getRect())) {
				this.stay();
				t.stay();
				return true;
			}
		}
	}
	return false;
	}

	
	//血块类。表现坦克的生命值
	private class BloodBar {
		public void draw(Graphics g) {
			Color c = g.getColor();
			g.setColor(Color.RED);
			g.drawRect(x,y-10,WIDTH,6);
			int w = WIDTH * life/100;
			g.fillRect(x,y-10, w, 6);
			g.setColor(c);
		}
	}
	

	/**
	 * 此方法是坦克吃血块，若吃血块一次则生命值增值为100
	 * @param b 血块
	 * @return 若吃到血块返回true，否则false
	 */
	public boolean eat(Blood b) {
		if( this.live && b.isLive() && this.getRect().intersects(b.getRect())) {
			this.life = 100;
			b.setLive(false);
			return true;
		}
		return false;
	}
	
	
}