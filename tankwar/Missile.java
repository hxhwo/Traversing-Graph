package tankwar;
import java.awt.*;
import java.util.List;

/**
 * 子弹类
 * @author xh_huang
 *
 */

public class Missile {
	
	/**
	 * 子弹每次横向移动的距离
	 */
	public static final int XSPEED = 10;
	
	/**
	 * 子弹每次竖向移动的距离
	 */
	public static final int YSPEED = 10;
	
	/**
	 * 子弹的宽
	 */
	public static final int WIDTH = 10;
	
	/**
	 * 子弹的高
	 */
	public static final int HEIGHT = 10;
	
	int x, y;
	Direction dir;
	private TankClient tc;
	
	//区分子弹是敌是友打出的
	private boolean good;
	
	public boolean isGood() {
		return good;
	}

	private boolean live = true;
	
	public boolean isLive() {
		return live;
	}
	
	public Missile(int x, int y, Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	
	public Missile(int x, int y,boolean good,Direction dir,TankClient tc) {
		this(x, y, dir);
		this.good = good;
		this.tc = tc;
	}
	
	/**
	 * 画出子弹
	 * @param g Graphics类
	 */
	public void draw(Graphics g) {
	try{
		if(!live ) {
			tc.missiles.remove(this);
            return;
		}
	}catch(NullPointerException e) {
		e.printStackTrace();;
	}
	
		Color c = g.getColor();
		if(!good) {
			g.setColor(Color.yellow);
		} else {
			g.setColor(Color.RED);
		}
		
		
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		
		move();
	}

	
	/**
	 * 子弹根据方向的不同进行移动
	 */
	private void move() {
		
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
		
		//如果子弹超出边界，子弹消失
		if(x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y > TankClient.GAME_HEIGHT) {
			live = false;
			
		}
		
	}

	
	public Rectangle getRect() {
		return new Rectangle(x,y,WIDTH,HEIGHT);
	}
	

	/**
	 * （碰撞检测）子弹击打坦克。若敌方坦克被击中，敌方坦克死亡，若我方被击中，我方坦克生命值减20，若生命值小于等于0则死亡
	 * @param t 坦克类
	 * @return 是否击中
	 */
	public boolean hitTank(Tank t) {
		if( this.live && this.getRect().intersects(t.getRect()) && t.isLive() && this.good != t.isGood()) {
			if(t.isGood()) {
				t.setLife(t.getLife() - 20);
				if(t.getLife() <= 0)
					t.setLive(false);
			}else {
				t.setLive(false);
			}
			
			this.live = false;
			Explode e = new Explode(x,y,tc);
			tc.explodes.add(e);
			return true;
		}
		return false;
	}
	
	
	public boolean hitTanks(List<Tank> tanks) {
		for(int i=0;i<tanks.size();i++){
			if(hitTank(tanks.get(i))) {
				return true;
			}
		}
		return false;
	}
	
	
	
	/**
	 *子弹击打墙壁。若子弹击中墙壁子弹消失 
	 * @param w 墙
	 * @return 撞到墙返回true，否则false
	 */
	public boolean hitWall(Wall w) {
		if( this.live && this.getRect().intersects(w.getRect())) {
			this.live = false;
			return true;
		}
		return false;
	}
	
}