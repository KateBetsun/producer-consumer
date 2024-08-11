package telran.multithreading;

import java.util.concurrent.BlockingQueue;
import java.util.stream.IntStream;

public class ProducerSender extends Thread {
	
	private BlockingQueue<String> messageEvenBox;
	private BlockingQueue<String> messageOddBox;
	private int nMassages;
	
	public ProducerSender(BlockingQueue<String> messageEvenBox, 
			BlockingQueue<String> messageOddBox,int nMassages) {
		this.messageEvenBox = messageEvenBox;
		this.messageOddBox = messageOddBox;
		this.nMassages = nMassages;
	}
	
	public void run() {
	    IntStream.rangeClosed(1, nMassages)
	        .forEach(i -> {
	        	String message = "message" + i;
	            try {
	                if (i % 2 == 0) {
	                    messageEvenBox.put(message);
//	                    System.out.println(i + " added to messageEvenBox");
	                } else {
	                    messageOddBox.put(message);
//	                    System.out.println(i + " added to messageOddBox");
	                }
	            } catch (InterruptedException e) {
	                //no interrupt logics
	            }
	        });
	}
}
