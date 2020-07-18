import java.util.Timer;
import java.util.TimerTask;


class Clock{


	static String paradeTime;
	static int paradeNo = 1;
	static String showTime;
	static int showNo = 1;

	private int value = 1;

	private Timer paradeAndShowTimer = new Timer();

	static Object lock = new Object();														// for notifying the StaffMember

	TimerTask paradeTask = new TimerTask(){

		@Override
		public void run() {
			
			switch(value){
			
			case 1:{
				HalloweenParadeAndPuppeShow.aParadeIsPlaying = true;
				HalloweenParadeAndPuppeShow.notifyStudentWhoGotNoGroup();
				paradeTime = "11:00 AM";
				msg("It is "+paradeTime+" Parade "+(paradeNo++)+" is starting now");
				Teacher.SetGroupNo = 0;
				Teacher.msg("notifying the group students now");
				HalloweenParadeAndPuppeShow.wakeUpTheStudentsForParade();
				HalloweenParadeAndPuppeShow.aParadeIsPlaying = false;
				value++;
				break;
			}
			case 2:
			{
				showTime = "11:15 AM";
				msg("It is "+showTime +" Puppet Show "+(showNo++)+" is starting now");
				wakeUpStaffMember();
				StaffMember.showPlaying = true;
				value++;
				break;


			}
			case 3: {
				HalloweenParadeAndPuppeShow.aParadeIsPlaying = true;
				HalloweenParadeAndPuppeShow.notifyStudentWhoGotNoGroup();
				paradeTime = "12:00 PM";
				msg("It is "+paradeTime+" Parade "+(paradeNo++)+" is starting now");
				Teacher.msg("notifying the group students now");
				Teacher.SetGroupNo = 0;
				HalloweenParadeAndPuppeShow.wakeUpTheStudentsForParade();
				Teacher.shortNap(200);
				HalloweenParadeAndPuppeShow.aParadeIsPlaying = false;
				value++;
				break;
			}
			case 4:
			{
				showTime = "12:45 PM";
				msg("It is "+showTime +" Puppet Show "+(showNo++)+" is starting now");
				wakeUpStaffMember();
				StaffMember.showPlaying = true;
				value++;
				break;

			}

			case 5: {
				HalloweenParadeAndPuppeShow.aParadeIsPlaying = true;
				HalloweenParadeAndPuppeShow.notifyStudentWhoGotNoGroup();
				paradeTime = "1:00 PM";
				msg("It is "+paradeTime+" Parade "+(paradeNo++)+" is starting now");
				Teacher.SetGroupNo = 0;
				Teacher.msg("notifying the group students now");
				HalloweenParadeAndPuppeShow.wakeUpTheStudentsForParade();
				Teacher.shortNap(200);
				HalloweenParadeAndPuppeShow.aParadeIsPlaying = false;
				value++;
				break;
			}
			case 6: {
				HalloweenParadeAndPuppeShow.aParadeIsPlaying = true;
				HalloweenParadeAndPuppeShow.notifyStudentWhoGotNoGroup();
				paradeTime = "2:00 PM";
				msg("It is "+paradeTime+" Parade "+(paradeNo++)+" is starting now");
				Teacher.SetGroupNo =0;
				Teacher.msg("notifying the group students now");
				HalloweenParadeAndPuppeShow.wakeUpTheStudentsForParade();
				Teacher.shortNap(200);
				HalloweenParadeAndPuppeShow.aParadeIsPlaying = false;
				value++;
				break;
			}
			case 7:
			{
				showTime = "2:15 PM";
				msg("It is "+showTime +" Puppet Show "+(showNo++)+" is starting now");
				wakeUpStaffMember();
				StaffMember.showPlaying = true;
				value++;
				break;	
			}
			case 8: {
				HalloweenParadeAndPuppeShow.aParadeIsPlaying = true;
				HalloweenParadeAndPuppeShow.notifyStudentWhoGotNoGroup();
				paradeTime = "3:00 PM";
				msg("It is "+paradeTime+" Parade "+(paradeNo++)+" is starting now");
				Teacher.SetGroupNo = 0;
				Teacher.msg("notifying the group students now");
				HalloweenParadeAndPuppeShow.wakeUpTheStudentsForParade();
				Teacher.shortNap(200);
				HalloweenParadeAndPuppeShow.aParadeIsPlaying = false;
				value++;
				break;
			}
			case 9:
			{
				showTime = "3:45 PM";
				msg("It is "+showTime +" Puppet Show "+(showNo++)+" is starting now");
				StaffMember.showPlaying = true;
				StaffMember.isGoingHome = true;
				wakeUpStaffMember();
				value++;
				StaffMember.notifyWonderer();
				break;

			}
			case 10: {
				HalloweenParadeAndPuppeShow.paradeIsOpen = false;
				HalloweenParadeAndPuppeShow.aParadeIsPlaying = true;
				HalloweenParadeAndPuppeShow.notifyStudentWhoGotNoGroup();
				paradeTime = "4:00 PM";
				msg("It is "+paradeTime+" Parade "+(paradeNo++)+" is starting now");
				Teacher.SetGroupNo = 0;
				Teacher.msg("notifying the group students now");
				HalloweenParadeAndPuppeShow.wakeUpTheStudentsForParade();
				
				Teacher.shortNap(200);
				HalloweenParadeAndPuppeShow.aParadeIsPlaying = false;
				value++;
				break;
			}
			default: {
				StaffMember.notifyWonderer();
				
				msg("All Halloween Parade are now closed. Please visit next year :-)");
				paradeAndShowTimer.cancel();
			}
			}
		}
	};


	public void start()
	{
		paradeAndShowTimer.schedule(paradeTask, 1500, 7000);
		

	}

	public static long time = System.currentTimeMillis();

	public static void msg(String m) {
		System.out.println("["+(System.currentTimeMillis()-time)+"] Clock: "+m);
	}

	public static void wakeUpStaffMember()
	{
		synchronized(lock)
		{
			lock.notify();	
		}
	}
}





