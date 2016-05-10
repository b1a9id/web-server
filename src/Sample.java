import java.util.Random;

/**
 * Created by uchitate on 2016/05/10.
 */
public class Sample {
	static TimeObj obj[] = new TimeObj[3];

	public static void main(String[] args) {
		for (int i = 0; i < 3; i++) {
			obj[i] = new TimeObj(i);
		}
	}
}

class TimeObj implements Runnable {
	private int id;
	int num = 0;
	int end = 5;
	int delay = 10000;
	boolean kiss_of_death = false;


	TimeObj(int id) {
		this.id = id;
		Random random = new Random();
		delay = random.nextInt(700) + 300;
		new Thread(this).start();
		System.out.println("*id : " + id + " start.*");
 	}

	@Override
	public void run() {
		while (!kiss_of_death) {
			System.out.println(id + ": count " + ++num);
			if (num == end) {
				kiss_of_death = true;
				try {
					Thread.sleep(delay);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("*" + id + " : end*");
	}
}
