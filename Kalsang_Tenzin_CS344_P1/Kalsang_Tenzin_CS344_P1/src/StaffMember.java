import java.util.ArrayList;
import java.util.List;


public class StaffMember extends Thread {

	static int showFinished = 0;
	int showDuration;


	static final int maxSeat = HalloweenParadeAndPuppeShow.default_numSeat;			// total number of seats

	static int avaliableSeat = maxSeat;												// seats open in the tent  

	public static Object tentLock = new Object();									// entry gate


	public static List<Thread> watchingPuppetShow = new ArrayList<>();             	// students who are watching the show
	public static List<Thread> puppetShowWaitingList = new ArrayList<>();		   	// students who are waiting for the next show

	public static Object shouldWonderLock = new Object();

	static boolean showPlaying= false;					 // if the show is opened. 

	static boolean isGoingHome = false;
	
	public StaffMember() {
		showDuration = HalloweenParadeAndPuppeShow.showDuration;
	}

	public static void msg(String m) {
		System.out.println("["+(System.currentTimeMillis()-HalloweenParadeAndPuppeShow.time)+"] "+"StaffMember "+": "+m);
	}

	public void run() 
	{
		while(Clock.showNo<=4){
			startShow();
			finishShow();
			goSleep(200);
		}
		msg("All the Puppet show is finished");
		msg("Staff member went home!!");
	}

	public void startShow()
	{
		synchronized(Clock.lock){
			while(!showPlaying)
			{
				try {
					msg("is waiting for the clock to get notified for show to start");
					Clock.lock.wait();                                      // will wait to get notified by Clock when show start
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		msg("the entry gate is now closed. Show is Starting");

		msg("performing show!!!!!!!!!!!");

		msg("there are total of "+watchingPuppetShow.size()+" students watching the show");


		for(int i = 0; i<watchingPuppetShow.size(); i++)
		{

			Thread t = watchingPuppetShow.get(i);
			msg(t.getName()+" is watching the puppet show");

		}

		goSleep(showDuration);													// showing puppet tricks
	}

	public void finishShow()													// finish a show
	{
		msg("finishing show now!!!!!!!!!!!");
		for(int i =0 ;i<watchingPuppetShow.size(); i++)
		{
			Thread t = watchingPuppetShow.get(i);
			msg("showed "+t.getName()+" the exit++++");
		}
		
		// students who had no group who are waiting for parade to end and get on the parade line
		synchronized (Teacher.waitLock) {											
			Teacher.waitLock.notifyAll();
		}

		synchronized (watchingPuppetShow)						// notify the students who just watched the show 
		{
			watchingPuppetShow.notifyAll();
		}


		synchronized (this) 
		{
			avaliableSeat = maxSeat;
			showFinished++;
			showPlaying = false;
			msg("The room is empty now, will wait for the next puppet show");
			goSleep(200);
			msg("the size of waiting list is->>>>>>>>>>>>>>>>>>>>>>>>>>"+ puppetShowWaitingList.size()+" before notify");
			watchingPuppetShow.clear();
		
		msg("the list of waiting is");
		printList();
		
		if(!isGoingHome)
			notifyWaitingStudents();
		else
		{
			
			for(int i = 0; i<puppetShowWaitingList.size(); i++)		// students who are still waiting after waiting, should wander
			{
				if(GreenUniformStudents.class.isInstance(puppetShowWaitingList.get(i)))
				{
					GreenUniformStudents green = (GreenUniformStudents) puppetShowWaitingList.get(i);
					green.cantWaitAfterLastPuppetShow = true;
				}
				else{
					OrangeUniformStudents orange = (OrangeUniformStudents) puppetShowWaitingList.get(i);
					orange.cantWaitAfterLastPuppetShow = true;
				}
			}
			goWanderAround();										// tell the students to leave the entrance, because all the shows are finished
		}
	}
}

	public void notifyWaitingStudents()									// FCFS basis for waiting students
	{
		
		int tempSeat = maxSeat;												// notify only students 
		while(tempSeat>0)														
		{
			if(puppetShowWaitingList.size()>0){									// if not empty
				synchronized (puppetShowWaitingList.get(0)) {
					puppetShowWaitingList.get(0).notify();					// notify the student who wants to get in.
				}
				puppetShowWaitingList.remove(0);							// have the student inside the show
			}
			tempSeat--;
			goSleep(200);								// its sleeps so the notified student gets chance to get a seat in the show.
		}

		for(int i = 0; i<puppetShowWaitingList.size(); i++)						// students who are still waiting after waiting, should wander
		{
			if(GreenUniformStudents.class.isInstance(puppetShowWaitingList.get(i)))
			{
				GreenUniformStudents green = (GreenUniformStudents) puppetShowWaitingList.get(i);
				green.shouldWonder = true;
			}
			else{
				OrangeUniformStudents orange = (OrangeUniformStudents) puppetShowWaitingList.get(i);
				orange.shouldWonder = true;
			}
		}

		goWanderAround();										// students who didn't get a seat after waiting. should wander aroudnd	
		
	}

	public static void goSleep(int n)
	{
		try {
			Thread.sleep(n);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void printList()
	{
		synchronized (tentLock) {
			for(int i = 0; i<puppetShowWaitingList.size(); i++)
			{
				msg((i+1)+" "+puppetShowWaitingList.get(i).getName());
			}
		}
	}

	public static void goWanderAround()
	{

		while(puppetShowWaitingList.size()>0){
				synchronized (puppetShowWaitingList.get(0)) {
					puppetShowWaitingList.get(0).notify();					// notify the students to wonder around
				}
				
				puppetShowWaitingList.remove(0);							// remove their notification object from the list
		}
	}
	
	public static void notifyWonderer()					
	{

		synchronized (StaffMember.shouldWonderLock) {
			StaffMember.shouldWonderLock.notifyAll();
		}
	}


}
