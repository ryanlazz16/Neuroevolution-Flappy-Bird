import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseInput implements MouseListener, MouseMotionListener{
	
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		if(x>150 && x<450 && y>200 && y<275 && FlappyBirdNeuroevolution.state.equals("HOME")) {
			FlappyBirdNeuroevolution.state = "GAME";
			FlappyBirdNeuroevolution.highScore = 0;
			FlappyBirdNeuroevolution.generations = 1;
			FlappyBirdNeuroevolution.playingBird = new Bird();
			FlappyBirdNeuroevolution.playingBird.color = Color.ORANGE;
			FlappyBirdNeuroevolution.playingBird.score = 0;
		}
		if(x>150 && x<450 && y>300 && y<375 && FlappyBirdNeuroevolution.state.equals("HOME")) {
			FlappyBirdNeuroevolution.state = "EVOLUTION";
			FlappyBirdNeuroevolution.highScore = 0;
			FlappyBirdNeuroevolution.generations = 1;
			FlappyBirdNeuroevolution.bestBird = new Bird();
			FlappyBirdNeuroevolution.playingBirds.add(FlappyBirdNeuroevolution.bestBird);
		}
		if(x>FlappyBirdNeuroevolution.WIDTH-150 && x<FlappyBirdNeuroevolution.WIDTH-50 && y>FlappyBirdNeuroevolution.HEIGHT+15 && y<FlappyBirdNeuroevolution.HEIGHT+65 && (FlappyBirdNeuroevolution.state.equals("EVOLUTION") || FlappyBirdNeuroevolution.state.equals("GAME"))) {
			FlappyBirdNeuroevolution.state = "HOME";
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	//Hover
	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
	}
}
