public class Sleeper implements SleeperInterface {
    @Override
    public void takeNap() {
        System.out.println("Imma take a quick nap");
    }

    @Override
    public void goToSleep() {
        System.out.println("Imma go to bed");
    }
}
