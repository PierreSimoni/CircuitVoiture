import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Supplier;

public class Instructions {
    Queue<String> instructions = new LinkedList<>();
    Supplier<Queue<String>> getNextInstructions;

    public Instructions(Queue<String> instructions, Supplier<Queue<String>> getNextInstructions) {
        this.instructions = instructions;
        this.getNextInstructions = getNextInstructions;
    }

    public String get() {
        String instruction = instructions.poll();
        if(instruction == null) {
            instructions = getNextInstructions.get();
            instruction = instructions.poll();
        }

        return instruction;
    }
}
