public class GreenUniformStudents extends Thread {

	//	private final int paradeNeedToAttend = 3;
	//	private final int showNeedToAttend = 3;
	private int id;

	int groupNo;																		// shares same value with other 2 group member
	int paradeLine;

	boolean hasGroup;																	
	boolean cantAttend;																	// if no group, cant attend the parade
	boolean shouldWonder;																// if waiting for the next show and still waits
	boolean cantWaitAfterLastPuppetShow;
	boolean canGoToShow, canGoToParade;													// for alternating between Parade and Show


	int paradeAttended, showAttended;                                          			// how many times did the student attend

	StringBuilder paradeTimeAttended, showTimeAttended;                                 // what time did the student attend? 


	public GreenUniformStudents(int id) {
		this.id = id;
		this.setName("GreenUniformStudent_"+this.id);
		hasGroup = false;
		groupNo = 0;
		paradeLine = 0;
		cantAttend = false;
		paradeAttended = 0;
		showAttended = 0;
		paradeTimeAttended = new StringBuilder();
		showTimeAttended = new StringBuilder();
		shouldWonder = false;
		canGoToShow = false;
		canGoToParade = true;
	}

	public void msg(String m) {
		System.out.println("["+(System.currentTimeMillis()-HalloweenParadeAndPuppeShow.time)+"] "+getName()+": "+m);
	}

	public void run() {                      
		Teacher.shortNap(Teacher.randomInteger(100, 250));								//arrive at random time between starting and finish time	
		msg("Arrived at the students meeting");

		while(HalloweenParadeAndPuppeShow.paradeIsOpen){											// continue until the parade is Open

			if(canGoToParade){
				HalloweenParadeAndPuppeShow.gus.add(this);												// add itself to the list of green student
				HalloweenParadeAndPuppeShow.notifyStudentsWhoSeeksGroup();
				msg("looking for a group to march the parade");
				lookForGroup();
				if(!cantAttend){                                                                      	// if assigned to a group
					Teacher.shortNap(100);
					msg("Found group "+groupNo);	
					Teacher.shortNap(100);
					waitForOtherGroups();
					msg("Group: "+groupNo+" starting to march parade");
					inTheParade();
					msg("Parade ended going for snack break");
				}	
				else {			
					msg("will try the next parade :-(:-(");	
					while(!canGoToShow){						// if no group members =  no parade, try next parade
						if(!HalloweenParadeAndPuppeShow.paradeIsOpen)
						{
							canGoToShow = true;
						}
						else
						{
							cantAttend = false;
							noGroupWaitTillNextParade();
							HalloweenParadeAndPuppeShow.gus.add(this);											//add itself to the list of green student
							HalloweenParadeAndPuppeShow.notifyStudentsWhoSeeksGroup();
							lookForGroup();

							if(!cantAttend){
								Teacher.shortNap(100);
								msg("Found group "+groupNo);	
								Teacher.shortNap(100);
								waitForOtherGroups();
								msg("Group: "+groupNo+" starting to march parade");
								inTheParade();
								msg("Parade ended going for snack break");
							}
						}
					}
				}
			}
			if(canGoToShow && HalloweenParadeAndPuppeShow.paradeIsOpen){
				Teacher.shortNap(Teacher.randomInteger(300, 1000));              			// Snack Break 
				msg("is done with snack break");
				groupNo = 0;
				hasGroup = false;
			}
			if(!StaffMember.isGoingHome){

				msg(" gathering in front of the puppet show");
				getOnThePuppetShowLine();	

				while(!canGoToParade && !StaffMember.isGoingHome){
					msg(" gathering in front of the puppet show");
					getOnThePuppetShowLine();													// try to get in the puppet show
				}
			}
		}
		msg("have attended "+paradeAttended+" paarades");								// how many times did the student attend the parade?
		msg("Parade times attended are "+getParadeTimesAttended());				// what are the times the student attended the parade

		msg("have attended "+showAttended+" puppet show");								//how many times did the student attend the show?
		msg("Puppet show times attended are: "+getShowTimesAttended());					// what are the times the student attended the show

		msg("is going home!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");							//after parade is closed the student goes home.
	}


	private void lookForGroup()
	{
		// acts as monitor lock
		synchronized(Teacher.meetingLock){
			while(!hasGroup)
			{
				if(Teacher.SetGroupNo==HalloweenParadeAndPuppeShow.numOfTotalGroups|| HalloweenParadeAndPuppeShow.aParadeIsPlaying 		
						|| !HalloweenParadeAndPuppeShow.paradeIsOpen)
				{ 															// if max group or parade is starting or all parade is closed
					msg("couldnt find group memeber");
					cantAttend = true;
					break;
				}
				msg("searching for groups members!!!!");
				printTotalStudents();

				if(HalloweenParadeAndPuppeShow.gus.size()>=2&& HalloweenParadeAndPuppeShow.ous.size()>=1 && !hasGroup)
				{	
					msg("in Meeting Critical Section....");
					this.groupNo = ++Teacher.SetGroupNo;
					this.hasGroup = true;


					HalloweenParadeAndPuppeShow.groups.add(this);

					for(int i= 0 ; i<HalloweenParadeAndPuppeShow.gus.size(); i++)
					{
						if(this==HalloweenParadeAndPuppeShow.gus.get(i))
						{
							HalloweenParadeAndPuppeShow.gus.remove(i);
							msg("deleted myself from list");
							break;
						}
					}

					HalloweenParadeAndPuppeShow.gus.get(0).groupNo =  Teacher.SetGroupNo;                      

					HalloweenParadeAndPuppeShow.gus.get(0).hasGroup= true;
					HalloweenParadeAndPuppeShow.groups.add(HalloweenParadeAndPuppeShow.gus.remove(0)); // has chosen a group member

					HalloweenParadeAndPuppeShow.ous.get(0).groupNo = Teacher.SetGroupNo;
					HalloweenParadeAndPuppeShow.ous.get(0).hasGroup = true;

					HalloweenParadeAndPuppeShow.groups.add(HalloweenParadeAndPuppeShow.ous.remove(0)); //has chosen another group member
					msg(" Ending critical section of meeting because I chose to two group memebers");

				} else{
					try {
						msg("gonna wait for more green students or orange students");
						Teacher.meetingLock.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}	
		}
	}
	private void noGroupWaitTillNextParade() {
		// TODO Auto-generated method stub
		synchronized (Teacher.waitLock) {
			try {
				if(HalloweenParadeAndPuppeShow.paradeIsOpen){
					msg("waiting for parade to end and show to end");
					Teacher.waitLock.wait();
					msg("done waiting going to student meeting if theres a next parade");
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void waitForOtherGroups()
	{
		if(!HalloweenParadeAndPuppeShow.aParadeIsPlaying){
			if(HalloweenParadeAndPuppeShow.groups.size()>0)
			{
				synchronized (this) {
					try {
						msg("waiting for Halloween Parde to start");
						this.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	private void getOnThePuppetShowLine()
	{
		boolean inShow = false;
		synchronized(StaffMember.tentLock){										// shares the lock with all the Orange/Green Students
			if(StaffMember.avaliableSeat>0 && !StaffMember.showPlaying){
				msg("there is currently "+StaffMember.avaliableSeat+ " seat avaible");
				StaffMember.avaliableSeat--;
				msg("took a seat now, it has currently "+StaffMember.avaliableSeat+ " seat avaible");
				inShow = true;
				StaffMember.watchingPuppetShow.add(this);
			}
		}
		if(inShow)															// inside the tent/show
			waitForShowToStart();
		else
			waitForNextShow();												//outside the tent/show
	}

	private void waitForShowToStart() {
		boolean doneWatching = false;
		msg(" got a seat in puppet Show!!!! waiting for the show to start");
		synchronized (StaffMember.watchingPuppetShow) {
			try {
				StaffMember.watchingPuppetShow.wait();
				doneWatching = true;
				canGoToParade = true;
				canGoToShow = false;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if(doneWatching)					  		// if interrupted, showAttended wont count
			{               
				String timeP = Clock.showTime;
				showTimeAttended.append(timeP+" , ");
				showAttended++;				
				msg("enjoyed the puppet show!!");
			}
		}	

	}

	private void printTotalStudents(){								// print how many students are looking for groups
		int size, size2;
		size = HalloweenParadeAndPuppeShow.gus.size();
		msg("total Green student looking for group:"+size);
		size2 = HalloweenParadeAndPuppeShow.ous.size();
		msg("total Orange student looking for group:"+size2);
	}


	private void waitForNextShow()										// waiting Students at the puppetShow, waiting for the next show
	{
		synchronized(this)
		{
			msg("the puppet show is playing or is full, will wait for the next puppet show");
			try {
				StaffMember.puppetShowWaitingList.add(this);
				this.wait();

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		if(!shouldWonder && !StaffMember.isGoingHome){
			getOnThePuppetShowLine();
		}
		else if(shouldWonder)
		{
			synchronized (StaffMember.shouldWonderLock) {
				try {
					canGoToParade = false;
					msg("is gonna wonder around the park and try the next show");
					StaffMember.shouldWonderLock.wait();
					msg("is done wandering around");
					shouldWonder = false;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else if(cantWaitAfterLastPuppetShow)						// not allowed to wait after the last puppet show is playing
		{
			msg("leaving the entrance of tent because all the puppet show is finished");
			canGoToParade = false;
		}
	}

	private String getShowTimesAttended()						// what are the times did the student attend puppet show!
	{
		if(showTimeAttended.length()>0)
		{
			showTimeAttended = showTimeAttended.replace(showTimeAttended.length()-2,showTimeAttended.length()-1," ");
			return showTimeAttended.toString();
		}
		else
			return "null";
	}

	private String getParadeTimesAttended()							// what are the times did the student attend Parade!
	{

		if(paradeTimeAttended.length()>0)
		{
			paradeTimeAttended = paradeTimeAttended.replace(paradeTimeAttended.length()-2,paradeTimeAttended.length()-1," ");
			return paradeTimeAttended.toString();
		}
		else
			return "null";

	}

	private void inTheParade()										// in the parade march
	{
		synchronized (HalloweenParadeAndPuppeShow.groups) {
			try {
				HalloweenParadeAndPuppeShow.groups.wait();
				canGoToShow = true;								    // is allowed to go to the show	
				canGoToParade = false;								// not allowed to go to show until went to show 
				paradeAttended++;
				paradeTimeAttended.append(Clock.paradeTime+" , ");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
