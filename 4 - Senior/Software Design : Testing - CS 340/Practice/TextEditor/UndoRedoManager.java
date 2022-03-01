import java.util.Stack;

public class UndoRedoManager {
    Stack<ICommand> done, undone;

    public UndoRedoManager(){
        done = new Stack<>();
        undone = new Stack<>();
    }

    void excecute(ICommand command){
        command.excecute();
        done.push(command);
        undone.clear();
    }

    void undo(){
        if(done.empty()){
            System.out.println("Nothing to undo!");
        }
        else {
            ICommand command = done.pop();
            command.undo();
            undone.push(command);
        }
    }

    void redo(){
        if(undone.empty()){
            System.out.println("Nothing to redo!");
        }
        else{
            ICommand command = undone.pop();
            command.excecute();
            done.push(command);
        }
    }
}
