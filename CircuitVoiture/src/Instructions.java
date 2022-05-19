import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.function.Supplier;

public class Instructions {
    Queue<Optional<String>> instructions;
    Supplier<Queue<Optional<String>>> getNextInstructions;

    public Instructions(Queue<Optional<String>> instructions, Supplier<Queue<Optional<String>>> getNextInstructions) {
        this.instructions = instructions;
        this.getNextInstructions = getNextInstructions;
    }

    public String get() {
        Optional<String> instruction = instructions.poll();
        if(instruction == null) {
            instructions = getNextInstructions.get();
            instruction = instructions.poll();
        }

        return instruction.get();
    }
}
