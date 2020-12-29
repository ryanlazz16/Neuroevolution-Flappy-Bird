import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter
{
	
	public void keyPressed(KeyEvent e)
	{
		int key = e.getKeyCode();
		if(key==' ' && FlappyBirdNeuroevolution.state.equals("EVOLUTION")) {
			FlappyBirdNeuroevolution.paused = !FlappyBirdNeuroevolution.paused;
		}
		if(key==' ' && FlappyBirdNeuroevolution.state.equals("GAME")) {
			FlappyBirdNeuroevolution.playingBird.jump();
			if(!FlappyBirdNeuroevolution.ready)
				FlappyBirdNeuroevolution.ready = true;
		}
	}
	
	public void keyReleased(KeyEvent e)
	{
		int key = e.getKeyCode();
		
	}
}