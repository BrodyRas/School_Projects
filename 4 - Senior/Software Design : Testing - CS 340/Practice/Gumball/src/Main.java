public class Main {
    public static void main(String[] args){
        GumballMachine machine = new GumballMachine();
        machine.setState(new NoGBNoS());
        machine.turnHandle();
        machine.addGumballs(1);
        machine.turnHandle();
        machine.insertQuarter();
        machine.turnHandle();
        machine.insertQuarter();
        machine.turnHandle();
    }
}
