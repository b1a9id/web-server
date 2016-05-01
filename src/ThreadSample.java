//import java.time.LocalDate;
//
///**
// * Created by uchitate on 2016/04/30.
// */
//public class ThreadSample {
//
//	public static void main(String[] args) {
//		Thread thread = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(5000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				System.out.println("run");
//			}
//		});
//		thread.start();
//
//		System.out.println("success");
//	}
//}
