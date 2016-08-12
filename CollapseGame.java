package collapse;

import java.awt.Dimension;

import javax.swing.JFrame;

public class CollapseGame implements Runnable{
	private static boolean running = false;
	private CollapsePanel panel;
	private final int fps = 30;
	private static final Dimension size = new Dimension(600,900);//12x16+space for preview
	private static Integer score = 0;
	public static void main(String...args) {
		new CollapseGame();
	}
	public CollapseGame(){
		JFrame frame = new JFrame("Collapse");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel=new CollapsePanel();
		frame.setResizable(false);
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
		
		
		(new Thread(this)).start();
	}
	@Override
	public void run() {
		running=true;
		while(running){
			try{
				Thread.sleep(1000/fps);
				update();
			}catch(InterruptedException e){
				e.printStackTrace();
				stopRunning();
			}
		}
		System.gc();
		System.exit(0);
	}
	public static Dimension getDimensions(){
		return size;
	}
	public static void stopRunning(){
		running = false;
	}
	public void update(){
		panel.update();
	}
	public static void addScore(int a){
		score+=a;
	}
	public static Integer getScore(){
		return score;
	}
}
