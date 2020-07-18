import java.util.ArrayList;
import java.util.List;

public class HalloweenParadeAndPuppeShow {


	static List<OrangeUniformStudents> ous = new ArrayList<>();                  // list of Orange Uniformed looking for groups
	static List<GreenUniformStudents> gus = new ArrayList<>();					 // list of Green  Uniformed looking for groups

	static int paradeDuration = Teacher.randomInteger(1500,2000);
	static int showDuration = Teacher.randomInteger(1000,1500);

	public static long time = System.currentTimeMillis();

	static int default_numGreen = 14;      // 14
	static int default_numOrange =7;	   // 7
	static int default_numSeat = 6;		//6

	static List<Thread> groups = new ArrayList<>();						// all the groups for parade
//	static int paradeIndex = 0;													// for notifying

	static boolean paradeIsOpen= true;

	static boolean aParadeIsPlaying = false;

	static int numOfTotalGroups = 0;

	static boolean isSnackTime = false;

	static boolean readyToStart = false;


	public static void main(String[] args) {

		if (args.length == 2) {                                    	// if arguments passed then will use those arguments that are passed
			default_numGreen = Integer.parseInt(args[0]);   
			default_numOrange= Integer.parseInt(args[1]);           
		}
		else if(args.length == 3){									//if 3 arguments passed use it, overwrite on the default values
			default_numGreen = Integer.parseInt(args[0]);   
			default_numOrange= Integer.parseInt(args[1]); 
			default_numSeat = Integer.parseInt(args[2]);
		}
		else      
			System.out.println("No arguments passed will use Default values");  // if no arguments then default values


		{
			int green  = default_numGreen;
			int orange = default_numOrange;
			while(green>=2&&orange>=1)  {                         	// find the total numberofGroups;      
				green-= 2;
				orange-= 1;
				numOfTotalGroups++;								 	// 2 Green and 1 Orange = 1 Group
			}
		}

		System.out.println("\nWelcome to Halloween Parade and Puppet Show"
				+ "\n\tThe time is: "		+time
				+ "\n\tnumGreen: "          + default_numGreen									 // total students in Green Uniform
				+ "\n\tnumOrange: "     	+ default_numOrange									 // total students of Orange Uniform
				+ "\n\tnumSeat:   "   		+ default_numSeat                                    // total seat available for puppet show
				+ "\n\tTotal Groups: "  	+ numOfTotalGroups                          		 // Total group possible assembled
				+ "\n\tParade duration: "   + paradeDuration + " milliseconds"                   // Each Parade Duration
				+ "\n\tShow Duration: "   	+ showDuration + " milliseconds"					 // Each show Duration
				+ "\n\n");


		Clock clock = new Clock();
		
		clock.start();

				// for notifying when all the groups are assembled.

		StaffMember sm = new StaffMember();
		sm.start();




		for(int i = 0; i<default_numOrange; i++)
		{
			OrangeUniformStudents o = new OrangeUniformStudents(i+1);
			o.start();	
		}

		for(int i = 0; i<default_numGreen; i++)
		{
			GreenUniformStudents g = new GreenUniformStudents(i+1);
			g.start();
		}


	};

	public static void wakeUpTheStudentsForParade()				// for students waiting for parade to start
	{
		notifyingGroups();
		
		
		paradeEnded();
		goSleep(100);
		
		HalloweenParadeAndPuppeShow.gus.clear();
		HalloweenParadeAndPuppeShow.ous.clear();
		groups.clear();
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
	
	public static void notifyStudentsWhoSeeksGroup()			// when parade has not started, send notify
	{
			
		if(gus.size()>=2 && ous.size()>=1)
		{	
			synchronized (Teacher.meetingLock) {
			Teacher.msg("notify students a group can be formed");
			Teacher.meetingLock.notifyAll();
		}
		}
	}
	
	
	public static void notifyStudentWhoGotNoGroup()				// when parade starts, notify students who has no groups
	{
		
		synchronized (Teacher.meetingLock) {
			Teacher.meetingLock.notifyAll();
		}
		
	}
		
		public static void notifyingGroups()					// when parade starts notify the groups 
		{
			while(groups.size()>0)
			{
				synchronized (groups.get(0)) {
					groups.get(0).notify();
				}
				groups.remove(0);
				goSleep(30);
			}
		}
			
		public static void paradeEnded()						// parade ended.
		{
			goSleep(HalloweenParadeAndPuppeShow.paradeDuration);
			Clock.msg("Parade ended");
			synchronized (groups) {
				groups.notifyAll();
			}
		
			
			goSleep((Teacher.randomInteger(300, 1000)));			//snack break duration so the wandering dont get priority
			
			
			StaffMember.notifyWonderer(); // those wandering are notified to and try the next show
			
		}
		



	


}
