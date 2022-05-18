public class Voiture extends Thread {

    int id;
    Instructions listeInstructions;
    String instruction;
    Circuit circuit;
    public Voiture(int id, Instructions listeInstructions, Circuit circuit) {
        this.id = id;
        this.listeInstructions = listeInstructions;
        this.circuit = circuit;
    }

    public void run() {
        while(true) {
            //recupérer l'instruction
            if(instruction == null) instruction = listeInstructions.get();

            boolean instructionExecuted = circuit.passInstruction(this, instruction);
            if(instructionExecuted) instruction = null;

            long timeToSleep = circuit.getTimeToSleep(this);

            try {
                sleep(timeToSleep * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    @Override
    public String toString() {
        return "Voiture" + id;
    }
}
