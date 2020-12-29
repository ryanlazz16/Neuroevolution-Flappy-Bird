import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Bird {
	public double x;
	public double y;
	public double gravity;
	public double velocity;
	public int score = 0;
	public NeuralNetwork brain;
	public double fitness = 0;
	public Color color;

	public static int birdSize = 40;
	public double dampingForce = 0.95;
	public int random;
	
	public Bird() {
		x = 125;
		y = FlappyBirdNeuroevolution.HEIGHT/2;
		gravity = 0.6;
		velocity = 0;
		brain = new NeuralNetwork(5, 10, 1);
		color = new Color((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255));
		try {
			random = (int) (Math.random()*FlappyBirdNeuroevolution.images.size());
		} catch(Exception e) {
			random = 0;
		}
		
	}
	
	public void render(Graphics g) {
		
		//wings
		g.setColor(new Color(200, 0, 0));
		g.fillOval((int)x-6, (int)y+12, 14, 20);
		g.fillOval((int)x+32, (int)y+12, 14, 20);
		g.setColor(Color.BLACK);
		g.drawOval((int)x-6, (int)y+12, 14, 20);
		g.drawOval((int)x+32, (int)y+12, 14, 20);
		
		//main body
		g.setColor(Color.BLACK);
		g.drawOval((int)x, (int)y, birdSize, birdSize);
		g.setColor(new Color((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255)));
		g.setColor(color);
		g.fillOval((int)x, (int)y, birdSize, birdSize);
		
		//eyes
		g.setColor(Color.WHITE);
		g.fillOval((int)x+2,(int)y+2, 18, 18);
		g.fillOval((int)x+20, (int)y+2, 18, 18);
		g.setColor(Color.BLACK);
		g.drawOval((int)x+2,(int)y+2, 18, 18);
		g.drawOval((int)x+20, (int)y+2, 18, 18);
		g.fillOval((int)x+8, (int)y+6, 8, 8);
		g.fillOval((int)x+24, (int)y+6, 8, 8);
		
		//beak
		int[] beakx = {(int)x+12, (int)x+28, (int)x+20};
		int[] beaky = {(int)y+20, (int)y+20, (int)y+33};
		g.setColor(Color.ORANGE);
		g.fillPolygon(beakx, beaky, 3);
		g.setColor(Color.BLACK);
		g.drawPolygon(beakx, beaky, 3);
		
		//feet
		g.setColor(Color.YELLOW);
		int[] leftFootx = {(int) (x+10), (int) (x+4), (int) (x+16)};
		int[] rightFootx = {(int) (x+30), (int) (x+24), (int) (x+36)};
		int[] feety = {(int) (y+33), (int) (y+40), (int) (y+40)};
		g.fillPolygon(leftFootx, feety, 3);
		g.fillPolygon(rightFootx, feety, 3);
		g.setColor(Color.BLACK);
		g.drawPolygon(leftFootx, feety, 3);
		g.drawPolygon(rightFootx, feety, 3);
		
		/*
		for(int i = 0; i<FlappyBirdNeuroevolution.images.size(); i++) {
			if(i==random) {
				g.drawImage(FlappyBirdNeuroevolution.images.get(i), (int)x, (int)y, birdSize, birdSize, null);
			}
		}
		*/
	}
	
	public void update() {
		if(y>=FlappyBirdNeuroevolution.HEIGHT-birdSize && velocity>0) {
			y = FlappyBirdNeuroevolution.HEIGHT-birdSize;
			velocity = 0;
		}
		else if(y<=0 && velocity<0) {
			y = -1;
			velocity = 0;
		}
		else {
			velocity+=gravity;
			velocity*=dampingForce;
			y+=velocity;
		}
	}
	
	public void jump() {
		velocity -= gravity*25;
	}
}
