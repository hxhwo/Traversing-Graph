package tankwar;
import java.awt.*;

/**
 * 爆炸类
 * @author xh_huang
 *
 */
public class Explode {

	
	int x,y;
	private boolean live = true;
	
	//爆炸的圆的直径数组
	int[] diameter = {4,15,17,37,15};
	
	int step = 0;
	
	private TankClient tc;
	
	/**
	 * 初始化explode
	 * @param x 横坐标
	 * @param y 竖坐标
	 * @param tc TankClient的引用
	 */
	public Explode(int x,int y,TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;	
	}
	
	/**
	 * 画出爆炸的效果
	 * @param g Graphics类
	 */
	public void draw(Graphics g) {
		if(!live) {
			tc.explodes.remove(this);
			return;
		}
	
		if(step== diameter.length) {
			live = false;
			step = 0;
			return;
		}
		Color c = g.getColor();
		g.setColor(Color.YELLOW);
		g.fillOval(x, y, diameter[step], diameter[step]);
		g.setColor(c);
		
		step++;
	}
}
