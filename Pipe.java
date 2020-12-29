import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Pipe {
	public int x;
	public int topY;
	public int bottomY;
	public double velocity;
	public static int width = 75;
	public boolean generate = true;
	public boolean count = true;
	Color color;
	public int pipeHole = FlappyBirdNeuroevolution.pipeHole;
	public double a = Math.random()*255;
	public double b = Math.random()*255;
	public double c = Math.random()*255;
	public double da = Math.random()+1;
	public double db = Math.random()+1;
	public double dc = Math.random()+1;
	
	public Pipe() {
		x = FlappyBirdNeuroevolution.WIDTH+10;
		topY = (int) (35+Math.random()*(FlappyBirdNeuroevolution.HEIGHT-70-pipeHole));
		bottomY = topY+pipeHole;
		velocity = -1.5;
		color = Color.GREEN;
	}
	
	public void update() {
		if(FlappyBirdNeuroevolution.ready == true) {
			x+=velocity;
		}
		
		//update color
		a+=da;
		b+=db;
		c+=dc;
		if(a>255) {
			a = 255;
			da = -(Math.random()+1);
		}
		if(b>255) {
			b = 255;
			db = -(Math.random()+1);
		}
		if(c>255) {
			c = 255;
			dc = -(Math.random()+1);
		}
		if(a<0) {
			a=0;
			da = Math.random()+1;
		}
		if(b<0) {
			b = 0;
			db = Math.random()+1;
		}
		if(c<0){
			c = 0;
			dc = Math.random()+1;
		}
	}
	
	public boolean intersects(Bird bird) {
		Rectangle topPipe = new Rectangle(x, 0, width, topY-35);
		Rectangle topPipeExtension = new Rectangle(x-10, topY-35, width+20, 35);
		Rectangle bottomPipe = new Rectangle(x, bottomY+35, width, FlappyBirdNeuroevolution.HEIGHT-(bottomY+35));
		Rectangle bottomPipeExtension = new Rectangle(x-10, bottomY, width+20, bottomY+35);
		Rectangle birdRect = new Rectangle((int)bird.x-4, (int)bird.y, bird.birdSize+8, bird.birdSize);
		Rectangle topBorder = new Rectangle(0, -1, FlappyBirdNeuroevolution.WIDTH, 1);
		Rectangle bottomBorder = new Rectangle(0, FlappyBirdNeuroevolution.HEIGHT-1, FlappyBirdNeuroevolution.WIDTH, 1);
		return birdRect.intersects(topPipe) || birdRect.intersects(topPipeExtension) 
				|| birdRect.intersects(bottomPipe) || birdRect.intersects(bottomPipeExtension)
				|| birdRect.intersects(topBorder) || birdRect.intersects(bottomBorder);
	}
	
	public void render(Graphics g) {
		g.setColor(new Color((int)a, (int)b, (int)c));
		//when bird passes through pipe
		if(x+width+2<100) {
			g.setColor(color);
		}
		
		
		g.fill3DRect(x-10, topY-35, width+20, 35, true);
		g.fill3DRect(x, 0, width, topY-34, true);
		
		g.fill3DRect(x, bottomY, width, FlappyBirdNeuroevolution.HEIGHT-bottomY, true);
		g.fill3DRect(x-10, bottomY, width+20, 35, true);
		
		//g.drawImage(FlappyBirdNeuroevolution.birdImage1, x, bottomY+35, width, FlappyBirdNeuroevolution.HEIGHT-bottomY-35, null);
		//g.drawImage(FlappyBirdNeuroevolution.birdImage2, x, 0, width, topY-35, null);
		
		g.setColor(Color.black);
	}
}
