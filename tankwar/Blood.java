package tankwar;
import java.awt.*;

/**
 * 血类
 * @author xh_huang
 *
 */
public class Blood {


	//血块的位置，大小
	int x,y,w,h;
	int step = 0;
	
	private boolean live = true; 
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}

	TankClient tc;
	
	//移动的位置
	private int[][] pos = {{350,310},{355,270}, {360,290}, {370,300}, {380,320}, {380,330},{370,330},{360,330},{370,340},{380,340},{390,340}};
	
	/**
	 * 设置血块初始位置
	 */
	public Blood() {
		x = pos[0][0];
		y = pos[0][1];
		w = h = 15;
		
	}
	
	public void draw(Graphics g) {
		if(!live) return;
		Color c = g.getColor();
		g.setColor(Color.WHITE);
		g.fill3DRect(x, y, w, h, true);
		g.setColor(c);
		
		move();
	}
	
	/**
	 * 设置血块的移动路径
	 */
	private void move() {
		step++;
		if(step == pos.length) {
			step = 0;
		}
		
		x = pos[step][0];
		y = pos[step][1];
	}
	
	public Rectangle getRect() {
		return new Rectangle(x,y,w,h);
	}
}
