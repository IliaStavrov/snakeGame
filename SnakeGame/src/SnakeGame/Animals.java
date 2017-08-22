package SnakeGame;

import java.awt.Image;
import java.util.Random;

import javax.swing.Timer;

public class Animals implements Runnable {

	int delay;
	Thread thread;
	boolean die;
	boolean isPaused;
	int snakeDelay = 200;
	int frogDelay = 1000;
	int lastStep_x, lastStep_y;
//	int snakeSize = 7;
	
	int[] x = new int[mainForm.playWidth];
	int[] y = new int[mainForm.playHeight];
	
	public int i, animSize;
		
	boolean inGame;
	
	Direction dir;
	animalType aType;

	public animalType getaType(){
		return this.aType;
	}
	
	Animals(animalType aType) {
		
		this.aType = aType;
		
		if (aType == animalType.Snake) {
			animSize = mainForm.snakeSize;
			delay = snakeDelay;
			int j = 0;
			for (i = animSize; i > 0; i--) {
				x[j] = i * mainForm.block;
				y[j] = 0;
				j++;
			}
		} else {
			animSize = 1;
			delay = frogDelay;
			Random random = new Random();
			x[0] = random.nextInt(mainForm.playWidth / mainForm.block) * mainForm.block;
			y[0] = random.nextInt(mainForm.playHeight / mainForm.block) * mainForm.block;
		}
		
		dir = Direction.Right;
		
		thread = new Thread(this);
		thread.start(); 
		
//		b.start();
		 
        
		
	}

	public void stopAnim(){
		thread.interrupt();
	}
	
	public void pauseAnim() {
	
		isPaused = true;
		
	}
	
	synchronized public void unPauseAnim() {
		isPaused = false;
		notify();
//		System.out.println("Waiting for b to complete..." + thread.getId() + "  " + isPaused);
		
	}
	
	public void run() {
		
		try {
			
			while (!thread.isInterrupted()) {

				move();
				Thread.sleep(delay);

				synchronized (this) {
					while (isPaused) {
//						System.out.println("Waiting for b to complete..." + thread.getId() + "  " + isPaused);
						wait();
//						System.out.println("after Waiting for b to complete..." + thread.getId() + "  " + isPaused);
					}
				}
			}

		} catch (InterruptedException e) {
//			System.out.println("G���� stopped");
		}
	}
	
	public void move() {

		if (aType == animalType.Snake) {
			for (i = animSize; i > 0; i--) {
				x[i] = x[i - 1];
				y[i] = y[i - 1];

//				if (i > 3 && (x[i] == x[0]) && (y[i] == y[0])) {
//					// Stop();
//					return;
//				}
			}

			if (dir == Direction.Right)
				x[0] = x[0] + mainForm.block;
			if (dir == Direction.Left)
				x[0] = x[0] - mainForm.block;
			if (dir == Direction.Up)
				y[0] = y[0] - mainForm.block;
			if (dir == Direction.Down)
				y[0] = y[0] + mainForm.block;

			
		} else {
		
			Random random = new Random();
			lastStep_x = 1 - random.nextInt(3);
			lastStep_y = 1 - random.nextInt(3);
			
			x[0] = x[0] + lastStep_x*mainForm.block;
			y[0] = y[0] + lastStep_y*mainForm.block;
		}
		
		if (x[0] > mainForm.playWidth-mainForm.block)
			x[0] = 0;
		if (y[0] > mainForm.playHeight-mainForm.block)
			y[0] = 0;
		if (x[0] < 0)
			x[0] = mainForm.playWidth-mainForm.block;
		if (y[0] < 0)
			y[0] = mainForm.playHeight-mainForm.block;
		
//		if (x[0] == xApple && y[0] == yApple) {
//			snakeSize++;
////			splaceFrogg();
//		}
	}
}

enum Direction {
	Up, Down, Left, Right
};
enum animalType {
	Snake, Frogg, RedFrogg, BlueFrogg
}