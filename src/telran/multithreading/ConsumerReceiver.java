package telran.multithreading;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class ConsumerReceiver extends Thread {
    private BlockingQueue<String> messageBox;
	private static AtomicLong messageCounter = new AtomicLong();
	
	public void setMessageBoxes(BlockingQueue<String> messageBox) {
		this.messageBox = messageBox;
	    }
	
	public void run() {
		boolean running = true;
		while(running) {
			try {
				String message = messageBox.take();
				processMessage(message);
			} catch (InterruptedException e) {
				running = false;
				while(!messageBox.isEmpty()) {
					processMessage(messageBox.remove());
				}
			}
		}
	}

	private void processMessage(String message) {
		System.out.printf("Thread %s - %s\n", getName(), message);
		messageCounter.getAndIncrement();	
	}
	
	public static long getMessageCounter() {
		return messageCounter.get();
	}
}
