/**
 * Created by uchitate on 2016/05/01.
 */
public class ServerThread {

	public static Thread generateThread() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
//						IntStream.rangeClosed(0, 10000).forEach(System.out::println);
			}
		});
		System.out.println(thread.getName());
		return thread;
	}
}
