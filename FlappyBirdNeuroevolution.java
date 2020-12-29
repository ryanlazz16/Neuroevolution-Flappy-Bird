import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JSlider;

public class FlappyBirdNeuroevolution extends Canvas implements Runnable{
	
	private static final long serialVersionUID = 1L;
	
	private Thread thread;
	private boolean running = false;
	public static JFrame frame;
	
	public static int WIDTH = 600;
	public static int HEIGHT = 600;
	public static int sidebarHeight = 100;
	
	public static ArrayList<Pipe> pipes = new ArrayList<Pipe>();
	public static int highScore = 0;
	
	public static ArrayList<Bird> oldBirds = new ArrayList<Bird>();
	public static ArrayList<Bird> playingBirds = new ArrayList<Bird>();
	public static ArrayList<Bird> deadBirds = new ArrayList<Bird>();
	
	public static Bird playingBird;
	
	public static int populationSize = 1200;//16*(int)(Math.random()*150);//map between 16 and 2400 by 16's
	public static int pipeFrequency = 300;//map between 0 and 400
	public static int pipeHole = 150;//map  between 125 and 250
	public static double mutationRate = 0.05;//map between 0 and .1
	//bird brain complexity as number of hidden nodes between 1 and 16 perhaps
	public static boolean ready = true;
	public static boolean paused = false;
	public static int generations = 1;
	
	public int fitnessCounter = 0;
	
	public static Bird bestBird = new Bird();
	
	public static String state = "HOME";//HOME, GAME, NEUROEVOLUTION
	public static boolean initialized = false;
	
	public static ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
	
	public FlappyBirdNeuroevolution() {
		// can implement textures for birds by adding images to images list
		/*
		images.add(imageLoader.loadImage("/textures/baby.png"));
		images.add(imageLoader.loadImage("/textures/baby2.png"));
		images.add(imageLoader.loadImage("/textures/baby3.png"));
		*/
		
		
		this.addMouseListener(new MouseInput());
		this.addMouseMotionListener(new MouseInput());
		this.addKeyListener(new KeyInput());
		
		frame = new JFrame();
		frame.setTitle("Flappy Bird");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(this);
		frame.setVisible(true);
		frame.setSize(WIDTH, HEIGHT+sidebarHeight);
		frame.setLocationRelativeTo(null);
		this.start();
		
		this.requestFocusInWindow();
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	
	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000/amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while(running)
		{
			long now = System.nanoTime();
			delta += (now-lastTime) / ns;
			lastTime = now;
			while(delta>=1)
			{
				tick();
				delta--;
			}
			if(running)
				render();
			frames++;
			if(System.currentTimeMillis() - timer >1000) 
			{
				timer+= 1000;
				System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
		stop();
	}
	
	private void tick() {
		if(state.equals("HOME")) {
			//clears all birds from neuroevolution
			initialized = false;
			while(oldBirds.size()>0)
				oldBirds.remove(0);
			while(playingBirds.size()>0)
				playingBirds.remove(0);
			while(deadBirds.size()>0)
				deadBirds.remove(0);
			while(pipes.size()>0)
				pipes.remove(0);
		}
		if(state.equals("GAME")) {
			if(!initialized) {
				initialized = true;
				while(pipes.size()>0)
					pipes.remove(0);
				playingBird = new Bird();
				pipes.add(new Pipe());
				ready = false;
			}
			if(ready) {
				playingBird.update();
				for(Pipe x: pipes) {
					x.update();
				}
				
				for(int i = 0; i<pipes.size(); i++) {
					if(pipes.get(i).x<-pipes.get(i).width) {
						pipes.remove(i);
					}
					if(pipes.get(i).generate ==true && pipes.get(i).x<pipeFrequency) {
						pipes.get(i).generate = false;
						pipes.add(new Pipe());
					}
					if(pipes.get(i).count && pipes.get(i).x+Pipe.width<playingBird.x) {
						pipes.get(i).count = false;
						playingBird.score++;
					}
					if(pipes.get(i).intersects(playingBird)) {
						initialized = false;
						ready = false;
					}
				}
			}
			
			if(playingBird.score>highScore) {
				highScore = playingBird.score;
			}
			
		}
		if(state.equals("EVOLUTION")) {
			if(!initialized) {
				initialized = true;
				ready = true;
				paused = false;

				pipes.add(new Pipe());
				
				for(int i = 0; i<populationSize; i++) {
					oldBirds.add(new Bird());
				}
				
				for(int i = 0; i<populationSize; i++) {
					playingBirds.add(new Bird());
					playingBirds.get(i).brain = oldBirds.get(i).brain.copy();
				}
			}
			
			//Predicting whether to jump and finding closest pipe
			Pipe closestPipe = new Pipe();
			for(int i = pipes.size()-1; i>=0; i--) {
				if(pipes.get(i).x+pipes.get(i).width+10>100) {
					closestPipe = pipes.get(i);
				}
			}
			for(int j = 0; j<playingBirds.size(); j++) {
				double temp1 = (double)playingBirds.get(j).y/HEIGHT;
				double temp2 = (double)playingBirds.get(j).velocity/10;
				double temp3 = (double)closestPipe.topY/HEIGHT;
				double temp4 = (double)closestPipe.bottomY/HEIGHT;
				double temp5 = (double)closestPipe.x/WIDTH;
				double[] inputs = {temp1, temp2, temp3, temp4, temp5};
				double result = playingBirds.get(j).brain.feedForward(inputs)[0];
				
				if(result>0.5) {
					playingBirds.get(j).jump();
				}
			
				if(playingBirds.get(j).score>highScore) {
					highScore = playingBirds.get(j).score;
				}
			}
			
			//update the fitness of each bird
			fitnessCounter++;
			for(Bird x: playingBirds) {
				x.fitness=Math.pow(fitnessCounter, 3);
			}
				
			//updates locations of birds and pipes according to velocities
			if(!paused) {
				for(Bird x: playingBirds) {
					x.update();
				}
				for(Pipe x: pipes) {
					x.update();
				}
			}
			for(int i = 0; i<pipes.size(); i++) {
				//removes and generates pipes
				if(pipes.get(i).x < -pipes.get(i).width-10) {
					pipes.remove(i);
				}
				if(pipes.get(i).generate == true && pipes.get(i).x<pipeFrequency) {
					pipes.get(i).generate = false;
					pipes.add(new Pipe());
				}
				//keeps track of the score for each playing bird
				if(playingBirds.size()>0) {
					if(pipes.get(i).count && pipes.get(i).x+pipes.get(i).width<playingBirds.get(0).x) {
						pipes.get(i).count = false;
						for(Bird x: playingBirds) {
							x.score++;
						}
					}
				}
				
				//deletes birds if they collide with pipe or hit floor or ceiling
				for(int j = 0; j<playingBirds.size(); j+=0) {
					if(pipes.get(i).intersects(playingBirds.get(j))) {
						deadBirds.add(playingBirds.remove(j));
					}else {
						j++;
					}
				}
				
			}
			
			//if there are no playing birds generate a new generation and restart the game
			if(playingBirds.size()==0) {
				fitnessCounter = 0;
				resetPipes();
				for(int i = 0; i<deadBirds.size(); i+=0) {
					oldBirds.add(deadBirds.remove(0));
				}
				for(Bird x: oldBirds) {
					if(x.fitness>bestBird.fitness) {
						bestBird=new Bird();
						bestBird.brain = x.brain.copy();
						bestBird.fitness = x.fitness;
						bestBird.score = x.score;
					}
				}
				for(int i = 0; i<3*populationSize/4; i++) {
					playingBirds.add(new Bird());
					playingBirds.get(i).brain = new NeuralNetwork(chooseBirdBrain());
					playingBirds.get(i).brain.mutate(mutationRate);
				}
				for(int i = 0; i<3*populationSize/16; i++) {
					playingBirds.add(new Bird());
				}
				for(int i = 0; i<populationSize/16; i++) {
					playingBirds.add(new Bird());
					playingBirds.get(i+populationSize*15/16).brain = bestBird.brain.copy();
					playingBirds.get(i+populationSize*15/16).brain.mutate(mutationRate);
				}
				playingBirds.get(populationSize-1).brain = bestBird.brain.copy();
				playingBirds.get(populationSize-1).color = Color.BLACK;
				
				generations++;
			}
		}
	}
	
	public static void resetPipes() {
		while(pipes.size()>0) {
			pipes.remove(0);
		}
		pipes.add(new Pipe());
	}
	
	public static NeuralNetwork chooseBirdBrain() {
		int sumFitness = 0;
		for(Bird x: oldBirds) {
			sumFitness+=x.fitness;
		}
		double random = Math.random()*sumFitness;
		int whichBird = 0;
		while(random>0) {
			random-=oldBirds.get(whichBird).fitness;
			whichBird++;
		}
		whichBird--;
		if(whichBird<0) {
			return chooseBirdBrain();
		}
		return oldBirds.get(whichBird).brain.copy();
	}
	
	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null)
		{
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.setFont(new Font("arial", Font.PLAIN, 24));
		
		if(state.equals("HOME")) {
			g.setFont(new Font("arial", Font.BOLD, 48));
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, WIDTH, HEIGHT+sidebarHeight);
			g.setColor(Color.BLACK);
			drawCenteredString(g, "FLAPPY", WIDTH/2, 60);
			drawCenteredString(g, "BIRD", WIDTH/2, 140);
			g.setColor(new Color(0, 153, 51));
			g.setFont(new Font("arial", Font.PLAIN, 40));
			g.fillRoundRect(150, 200, 300, 75, 10, 10);
			g.fillRoundRect(150, 300, 300, 75, 10, 10);
			g.setColor(Color.BLACK);
			drawCenteredString(g, "Play Game", WIDTH/2, 237);
			drawCenteredString(g, "Neuroevolution", WIDTH/2, 337);
			JSlider temp = new JSlider();
			frame.getContentPane().add(temp);
		}
		if(state.equals("GAME")) {
			g.setColor(Color.WHITE);
			//g.setColor(new Color(240, 255, 240));
			g.fillRect(0, 0, WIDTH, HEIGHT);
			
			for(Pipe x: pipes) {
				x.render(g);
			}
			playingBird.render(g);
			
			if(!ready) {
				g.setColor(Color.DARK_GRAY);
				drawCenteredString(g, "PRESS SPACE", WIDTH/2, 150);
				drawCenteredString(g, "TO START", WIDTH/2, 180);
			}
			
			g.setColor(Color.GRAY);
			g.fillRect(0, HEIGHT, WIDTH, sidebarHeight);
			g.setColor(Color.WHITE);
			g.fillRoundRect(WIDTH-150, HEIGHT+15, 100, 50, 5, 5);
			g.setColor(Color.BLACK);
			g.drawString(String.format("SCORE: %d", playingBird.score), 50, 650);
			g.drawString(String.format("HIGH SCORE: %d", highScore), 200, 650);
			drawCenteredString(g, "QUIT", WIDTH-100, HEIGHT+40);
		}
		if(state.equals("EVOLUTION")) {
			g.setColor(Color.WHITE);

			g.fillRect(0, 0, WIDTH, HEIGHT);

			for(Pipe x: pipes) {
				x.render(g);
			}
			for(Bird x: playingBirds) {
				x.render(g);
			}
			
			g.setColor(Color.GRAY);
			g.fillRect(0, HEIGHT, WIDTH, sidebarHeight);
			g.setColor(Color.WHITE);
			g.fillRoundRect(WIDTH-150, HEIGHT+15, 100, 50, 5, 5);
			g.setColor(Color.BLACK);
			g.drawString(String.format("SCORE: %d", playingBirds.get(0).score), 30, 635);
			g.drawString(String.format("HIGH SCORE: %d", highScore), 30, 660);
			g.drawString(String.format("STILL ALIVE: %d", playingBirds.size()), 240, 635);
			g.drawString(String.format("GENERATION: %d", generations), 240, 660);
			drawCenteredString(g, "QUIT", WIDTH-100, HEIGHT+40);
		}

		g.dispose();
		bs.show();
	}
	
	public static void drawCenteredString(Graphics g, String text, int x, int y) {
	    // Get the FontMetrics
	    FontMetrics metrics = g.getFontMetrics();
	    // Determine the X coordinate for the text
	    int xcoord = x - metrics.stringWidth(text) / 2;
	    // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
	    int ycoord = y - metrics.getHeight() / 2 + metrics.getAscent();
	    // Draw the String
	    g.drawString(text, xcoord, ycoord);
	}
	
	public static void main(String[] args) {
		new FlappyBirdNeuroevolution();
	}
}
