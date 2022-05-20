/**
 * Classe simulant le comportement d'une voiture sur le circuit
 */
public class Voiture extends Thread {

    int id;
    /**
     * Instructions suivi par la voiture
     */
    Instructions listeInstructions;
    String instruction;
    Circuit circuit;
    boolean isParked = false;

    public Voiture(int id, Instructions listeInstructions, Circuit circuit) {
        this.id = id;
        this.listeInstructions = listeInstructions;
        this.circuit = circuit;
    }
    /**
     * Simulation du comportment d'une voiture , intéraction avec le circuit
     */
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
            if(isParked) circuit.updateParkTime(this, timeToSleep);
        }
    }

    public boolean isParked() {
        return isParked;
    }

    public void setParked(boolean parked) {
        isParked = parked;
    }

    @Override
    public String toString() {
        return "Voiture" + id;
    }
}
