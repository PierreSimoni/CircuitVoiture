import java.util.*;
import java.util.function.Supplier;

public class Main {

    Troncon t1;
    Troncon t2;
    Troncon t3;
    Troncon t4;
    Troncon t5;
    Troncon t6;

    Feu f1;
    Feu f2;
    Feu f3;
    Feu f4;

    Parking parking;

    List<Voiture> voitures = new ArrayList<>();
    Circuit circuit;
    TimerFeu timerFeu;

    public void init() {

        //Creation du circuit
        circuit = new Circuit();


        //Définition des tronçons
        Troncon t1 = new Troncon(1, null, false, 4);
        Troncon t2 = new Troncon(2, null, false, 3);
        Troncon t3 = new Troncon(3, null, true, 6);
        Troncon t4 = new Troncon(4, null, false, 5);
        Troncon t5 = new Troncon(5, null, true, 6);
        Troncon t6 = new Troncon(6, null, false, 7);
        //Définition des feu
        Feu f1 = new Feu(1,t5, t1, t3, true);
        Feu f2 = new Feu(2,t2, t3, t5, false);
        Feu f3 = new Feu(3,t1, t5, t2, true);
        Feu f4 = new Feu(4,t3, t2, t1, false);

        f1.setOposite(f3);
        f3.setOposite(f1);
        f2.setOposite(f4);
        f4.setOposite(f2);


        t1.setNext(f1);
        t2.setNext(f2);
        t3.setNext(t6);
        t4.setNext(f3);
        t5.setNext(t4);
        t6.setNext(f4);
        //Définition su Parking
        Parking park =  new Parking(t6,t4);

        park.setNbPlace(0);
        circuit.setParking(park);


        //Création des streams d'instructions
        Queue<Optional<String>> queue1 = new LinkedList<>(Arrays.asList(Optional.of("left"), Optional.of("right"), Optional.of("park 3"), Optional.of("straight-on")));
        Instructions instructions1 = new Instructions(queue1, new Supplier<Queue<Optional<String>>>() {
            @Override
            public Queue<Optional<String>> get() {
                return new LinkedList<>(Arrays.asList(Optional.of("left"), Optional.of("right"), Optional.of("park 3"), Optional.of("straight-on")));
            }
        });

      /*  Queue<String> queue2 = new LinkedList<>(Arrays.asList("right", "left", "straight-on", "left"));
        Instructions instructions2 = new Instructions(queue2, new Supplier<Queue<String>>() {
            @Override
            public Queue<String> get() {
                return new LinkedList<String>(Arrays.asList("END_OF_STREAM"));
            }
        });*/
        Queue<Optional<String>> queue2 = new LinkedList<>(Arrays.asList(Optional.of("straight-on"), Optional.of("left"), Optional.of("park 3"), Optional.of("right")));
        Instructions instructions2 = new Instructions(queue2, new Supplier<Queue<Optional<String>>>() {
            @Override
            public Queue<Optional<String>> get() {
                return new LinkedList<>(Arrays.asList(Optional.of("straight-on"), Optional.of("left"), Optional.of("park 3"), Optional.of("right")));
            }
        });
        Queue<Optional<String>> queue3 = new LinkedList<>(Arrays.asList(Optional.of("right"), Optional.of("right"), Optional.of("park 3"), Optional.of("straight-on")));
        Instructions instructions3 = new Instructions(queue3, new Supplier<Queue<Optional<String>>>() {
            @Override
            public Queue<Optional<String>> get() {
                return new LinkedList<>(Arrays.asList(Optional.of("right"), Optional.of("right"), Optional.of("park 3"), Optional.of("straight-on")));
            }
        });

        //Définition des voitures
        Voiture v1 = new Voiture(1,instructions1 , circuit);
        Voiture v3 = new Voiture(3, instructions3, circuit);
        Voiture v4 = new Voiture(4, instructions2, circuit);
        //On place les voitures sur le circuit
        circuit.ajoutVoiture(v1, park);
        circuit.ajoutVoiture(v3, park);
        circuit.ajoutVoiture(v4, park);
        
        voitures.add(v1);
        voitures.add(v3);

        voitures.add(v4);

        circuit.ajoutFeu(f1);
        circuit.ajoutFeu(f2);
        circuit.ajoutFeu(f3);
        circuit.ajoutFeu(f4);

        timerFeu = new TimerFeu(circuit);
    }

    public void start() {
        timerFeu.start();

        for(Voiture voiture : voitures) {
            voiture.start();
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.init();
        main.start();
    }
}
