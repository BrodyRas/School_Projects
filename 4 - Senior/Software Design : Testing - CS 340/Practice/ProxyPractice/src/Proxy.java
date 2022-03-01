import java.util.ArrayList;

public class Proxy {
    private static final String TYPE_NAP = "NAP";
    private static final String TYPE_SLEEP = "SLEEP";
    private Sleeper mySleeper;
    private ArrayList<String> allowedDays = new ArrayList<>();
    private int earliest, latest;

    public Proxy(){
        mySleeper = new Sleeper();
        allowedDays.add("M");
        allowedDays.add("T");
        allowedDays.add("W");
        allowedDays.add("Th");
        allowedDays.add("F");

        earliest = 800;
        latest = 1700;
    }

    public void attemptOperation(String type, String day, int time){
        if(appropriateTime(day,time)){
            switch (type){
                case TYPE_NAP:
                    mySleeper.takeNap();
                    break;
                case TYPE_SLEEP:
                    mySleeper.goToSleep();
                    break;
            }
        }
        else{
            System.out.println("Inappropriate time passed");
        }
    }

    private boolean appropriateTime(String day, int time){
        return allowedDays.contains(day) && (earliest <= time) && (time <= latest);
    }
}
