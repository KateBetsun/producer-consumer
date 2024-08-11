package telran.multithreading;

import java.util.Arrays;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class SenderReceiverAppl {

	private static final int N_MESSAGES = 20;
	private static final int N_RECEIVERS = 10;

	public static void main(String[] args) throws InterruptedException {
	//TODO for HW #44  (ConsumerReceiver should not be updated)
		//Provide functionality of dispatching
		//Even messages must be processed by receiver threads with even id
		//Odd messages must be processed by receiver threads with odd id
		//Hints two message boxes: one for even messages and other for odd messages
		
		BlockingQueue<String> messageEvenBox = new LinkedBlockingQueue<String>();
		BlockingQueue<String> messageOddBox = new LinkedBlockingQueue<String>();
		
		ProducerSender sender = startSender(messageEvenBox, messageOddBox, N_MESSAGES);
		
		ConsumerReceiver[] receivers = startReceivers(messageEvenBox, messageOddBox, N_RECEIVERS);
		sender.join();
//		Thread.sleep(200);
		stopReceivers(receivers);
		displayResult();
		
	}

	private static void displayResult() {
		System.out.printf("counter of processed messages is %d\n", 
				ConsumerReceiver.getMessageCounter());		
	}

	private static void stopReceivers(ConsumerReceiver[] receivers) throws InterruptedException {
		for(ConsumerReceiver receiver: receivers) {
			receiver.interrupt();
			receiver.join();
		}		
	}

	private static ConsumerReceiver[] startReceivers(BlockingQueue<String> messageEvenBox,
													BlockingQueue<String> messageOddBox,int nReceivers) {
		ConsumerReceiver[] receivers = 
		IntStream.range(0, nReceivers).mapToObj(i -> {
			ConsumerReceiver receiver = new ConsumerReceiver();
			int threadNumber = (int) receiver.getId();
			BlockingQueue<String> assignedBox = (threadNumber % 2 == 0) ? messageEvenBox : messageOddBox;
            receiver.setMessageBoxes(assignedBox);
			return receiver;
		}).toArray(ConsumerReceiver[]::new);
		Arrays.stream(receivers).forEach(ConsumerReceiver::start);
		return receivers;
	}


	private static ProducerSender startSender(BlockingQueue<String> messageEvenBox, 
							BlockingQueue<String> messageOddBox, int nMessages) {
		ProducerSender sender = new ProducerSender(messageEvenBox, messageOddBox, nMessages);
		sender.start();
		return sender;
	}

}
