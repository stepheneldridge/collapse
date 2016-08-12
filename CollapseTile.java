package collapse;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

public class CollapseTile {
	private int x,y;
	private Color color;
	private static Dimension size;
	private static Color[] colors = {Color.blue,Color.red,Color.green,Color.yellow};
	private final int boarder = 2;
	private boolean checked = false;
	private int type = 0;
	public CollapseTile(int x, int y, int col) {
		this.x=x;
		this.y=y;
		color=colors[col];
		type=col;
	}
	public void move(int dx, int dy){
		x+=dx;
		y+=dy;
	}
	public void setPosition(Point p){
		this.x=p.x;
		this.y=p.y;
	}
	public boolean isChecked(){
		return checked;
	}
	public void check(){
		checked=true;
	}
	public void uncheck(){
		checked=false;
	}
	public int getType(){
		return type;
	}
	
	public void activate(){
		CollapsePanel.addToDeathList(new Point(this.x,this.y));
		check();
		CollapsePanel.activateIfSame(x+size.width, y, type);
		CollapsePanel.activateIfSame(x-size.width, y, type);
		CollapsePanel.activateIfSame(x, y+size.height, type);
		CollapsePanel.activateIfSame(x, y-size.height, type);
	}
	public void draw(Graphics g){
		g.setColor(Color.BLACK);
		g.fillRect(x, y, size.width, size.height);
		g.setColor(color);
		g.fillRect(x+boarder, y+boarder, size.width-2*boarder, size.height-2*boarder);
	}
	public static void setDimensions(Dimension d){
		size=d;
	}
}
