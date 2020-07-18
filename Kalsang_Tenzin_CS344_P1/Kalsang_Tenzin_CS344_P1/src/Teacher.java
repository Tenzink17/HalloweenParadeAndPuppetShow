import java.util.Random;

public class Teacher {

	static Object meetingLock = new Object();

	static int snackBreakDuration = randomInteger(1500, 3000);

	static int SetGroupNo = 0;

	public static Object waitLock = new Object();

	public static void shortNap(int n)
	{
		try {
			Thread.sleep(n);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static int randomInteger(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}

	public static void msg(String m) {
		System.out.println("["+(System.currentTimeMillis()-HalloweenParadeAndPuppeShow.time)+"] Teacher: "+m);
	}

}
