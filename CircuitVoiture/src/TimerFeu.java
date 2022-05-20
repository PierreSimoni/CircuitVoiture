/**
 * Thread servant a faire alterner la couleur des feux toutes les 5 s
 */
public class TimerFeu extends Thread{
    Circuit circuit;
    boolean stop;

    public TimerFeu(Circuit circuit) {
        this.circuit = circuit;
        stop = true;
    }

    public void run(){
        while (stop){
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            circuit.switchFeu();
        }
    }
}
