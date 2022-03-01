public class YesGBYesS implements IMachineState{
    @Override
    public void addGumballs(GumballMachine machine, int i) {
        machine.incGumballs(i);
    }

    @Override
    public void insertQuarter(GumballMachine machine) {
        System.out.println("Quarter already in slot!");
    }

    @Override
    public void removeQuarter(GumballMachine machine) {
        machine.setSlot(false);
        machine.setState(new YesGBNoS());
    }

    @Override
    public void turnHandle(GumballMachine machine) {
        machine.incQuarters();
        machine.setSlot(false);
        machine.decGumballs();
        if(machine.getGumballs() == 0){
            machine.setState(new NoGBNoS());
        }
        else{
            machine.setState(new YesGBNoS());
        }
    }
}
