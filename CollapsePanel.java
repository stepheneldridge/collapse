package collapse;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JPanel;

public class CollapsePanel extends JPanel implements MouseListener{
	private static final long serialVersionUID = 1L;
	private final Dimension tileSize = new Dimension(50,50);
	private final int speed = tileSize.height;
	private Random rand;
	private boolean isPlaying = false;
	private static HashMap<Point,CollapseTile> board = new HashMap<Point,CollapseTile>();
	private HashMap<Point,CollapseTile> next = new HashMap<Point,CollapseTile>();
	private static ArrayList<Point> death = new ArrayList<Point>();
	private int counter = 0;
	private int counterMax= 10;
	
	public CollapsePanel(){
		CollapseTile.setDimensions(tileSize);
		setPreferredSize(CollapseGame.getDimensions());
		addMouseListener(this);
		setFocusable(true);
		setBackground(Color.lightGray);
		this.setFont(new Font(this.getFont().getFontName(),Font.PLAIN, 50));
		rand = new Random();
		rand.setSeed(System.currentTimeMillis());
		isPlaying = true;
	}

	private void endGame(){
		isPlaying = false;
	}
	
	public static boolean isTileAt(int x, int y){
		if(x<0||x>CollapseGame.getDimensions().width||y<0||y>CollapseGame.getDimensions().height)return false;
		if(board.containsKey(new Point(x,y))){
			return true;
		}else{
			return false;
		}
	}
	public static void activateIfSame(int x, int y, int type){
		if(!isTileAt(x, y))return;
		CollapseTile b = board.get(new Point(x,y));
		if(b.getType()==type&&!b.isChecked()){
			b.activate();
		}
	}
	public static void addToDeathList(Point p){
		death.add(p);
	}
	public void killTiles(){
		if(death.size()>2){
			for(Point p: death){
				board.remove(p);
			}
			fall();
		}else{
			for(Point p: death){
				board.get(p).uncheck();
			}
		}
		death.clear();
	}
	public void fall(){
		for(int i = 0; i<12; i++){
			for(int j = 14; j>=0; j--){//j=15 cant fall
				Point p = new Point(i*speed,j*speed);
				CollapseTile b = board.get(p);
				if(b==null)continue;
				if(isTileAt(p.x,p.y+speed))continue;
				board.remove(p);
				while(!isTileAt(p.x,p.y+speed)&&p.y<15*speed){
					p.translate(0, speed);
				}
				b.setPosition(p);
				board.put(p, b);
			}
		}
		int shift = 0;
		for(int i = 5; i<12; i++){
			if(shift>0){
				for(int j = 0; j<16;j++){
					Point p = new Point(i*speed,j*speed);
					CollapseTile b = board.get(p);
					if(b!=null){
						board.remove(p);
						p.translate(-shift*speed, 0);
						b.setPosition(p);
						board.put(p, b);
					}
				}
				
			}
			if(!isTileAt((i+1)*speed,15*speed)){
				shift++;
			}
		}
		shift=0;
		for(int i = 6; i>=0; i--){
			if(shift>0){
				for(int j = 0; j<16; j++){
					Point p = new Point(i*speed,j*speed);
					CollapseTile b = board.get(p);
					if(b!=null){
						board.remove(p);
						p.translate(shift*speed, 0);
						b.setPosition(p);
						board.put(p, b);
					}
				}
			}
			if(!isTileAt((i-1)*speed,15*speed)){
				shift++;
			}
		}
	}
	public void update(){
		if(isPlaying){
			counter++;
			if(counter>=counterMax){
				progress();
				counter=0;
			}
		}
		repaint();
	}
	private int progressState = 0;
	public void progress(){
		if(progressState>=12){
			for(int i = 0; i<16; i++){
				for(int j = 0;j<12; j++){
					Point key = new Point(j*speed,i*speed);
					CollapseTile b = board.get(key);
					if(b!=null){
						if(key.getY()-speed<0){
							this.endGame();
							return;
						}
						b.move(0, -speed);
						board.remove(key);
						key.translate(0, -speed);
						board.put(key, b);
					}
				}
			}
			for(int i = 0; i<12; i++){
				Point key = new Point(i*speed,17*speed);
				CollapseTile b = next.get(key);
				if(b!=null){
					b.move(0, -speed*2);
					key.translate(0, -speed*2);
					board.put(key, b);
				}
			}
			next.clear();
			progressState=0;
		}else{
			next.put(new Point(progressState*speed,17*speed), 
					new CollapseTile(progressState*speed,17*speed,rand.nextInt(3)));
			progressState++;
		}
	}
	public void paint(Graphics g){
		g.setColor(this.getBackground());
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.darkGray);
		g.fillRect(0, 16*speed, 12*speed, speed);
		for(CollapseTile a: board.values()){
			a.draw(g);
		}
		for(CollapseTile a: next.values()){
			a.draw(g);
		}
	}

	@Override
	public void mouseClicked(MouseEvent click) {
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override	
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mousePressed(MouseEvent click) {
		if(click.getButton()==MouseEvent.BUTTON1&&isPlaying){
			CollapseTile b = board.get(new Point(click.getX()-click.getX()%speed,click.getY()-click.getY()%speed));
			if(b!=null){
				b.activate();
				killTiles();
			}	
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
