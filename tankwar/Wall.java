package tankwar;
import java.awt.*;
/**
 * 墙类（障碍物）
 * @author xh_huang
 *
 */

public class Wall {
	
	//墙的位置大小
	int x,y,w,h;
	TankClient tc;

	public Wall(int x,int y,int w,int h,TankClient tc) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.tc = tc;
	}
	
	public void draw(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.GRAY);
		g.fillRect(x, y, w, h);
		g.setColor(c);
		
	}
	
	
	public Rectangle getRect() {
		return new Rectangle(x,y,w,h);
	}
	
	
}
