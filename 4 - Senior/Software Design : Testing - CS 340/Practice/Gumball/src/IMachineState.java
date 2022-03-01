public interface IMachineState {
    void addGumballs(GumballMachine machine, int i);
    void insertQuarter(GumballMachine machine);
    void removeQuarter(GumballMachine machine);
    void turnHandle(GumballMachine machine);
}
