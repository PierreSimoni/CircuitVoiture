import java.util.*;
import java.util.function.Supplier;

public class Circuit {

    static final String PARK_INSTRUCTION_REGEX = "park\\s*\\d+";

    HashMap<Voiture, Case> positions = new HashMap<>();
    HashMap<Voiture, Long> timesToPark = new HashMap<>();

    List<Feu> listeFeux = new ArrayList<Feu>();


    public Parking getParking() {
        return parking;
    }

    public void setParking(Parking parking) {
        this.parking = parking;
    }

    Parking parking;

    public synchronized boolean passInstruction(Voiture voiture, String instruction) {
        Case positionActuelle = positions.get(voiture);

        if(timesToPark.getOrDefault(voiture, 0l) > 0){
            tryToPark(voiture);
            return false;
        }

        switch(instruction) {
            case "END_OF_STREAM":
                System.out.println(voiture + " END_OF_STREAM : plus d'instructions.");
                return false;
            case "left":
                try {
                    return handleLeft(voiture);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            case "right":
                return handleRight(voiture);
            case "straight-on":
                return handleStraightOn(voiture);
            default :
                if(instruction.matches(PARK_INSTRUCTION_REGEX)) return handleParkInstruction(voiture, instruction);
                return false;
        }
    }

    public synchronized long getTimeToSleep(Voiture voiture) {
        Case positionVoiture = positions.get(voiture);

        if(voiture.isParked()) {
            return timesToPark.getOrDefault(voiture, 0l);
        }

        if(positionVoiture instanceof Troncon)
            return ((Troncon)positionVoiture).getDureeDeTraversee();

        return 0;
    }

    public synchronized void updateParkTime(Voiture voiture, long timeSpendParked) {
        Long oldTimeToPark = timesToPark.getOrDefault(voiture, 0l);
        System.out.println(voiture + " : oldTimeToPark " + oldTimeToPark);
        System.out.println(voiture + " : timeSpendParked " + timeSpendParked);
        timesToPark.put(voiture, oldTimeToPark - timeSpendParked);
        System.out.println(voiture + " voiture updateParkTime");

        if(timesToPark.get(voiture) <= 0) {
            System.out.println(voiture + " voiture n'est plus garée (mais est toujours sur le parking)");
            voiture.setParked(false);
            // synchronized NbPlace ??
            parking.incrNbPlace();
        }
    }

    private boolean handleParkInstruction(Voiture voiture, String instruction) {
        if (voiture.isParked()) return false;
        instruction.trim();
        int timeToPark = Integer.valueOf(instruction.substring(5));
        Long oldTimeToPark = timesToPark.getOrDefault(voiture, 0l);

        if (tryToPark(voiture)){
            // synchronizied timesToPark ??
            /*synchronized (timesToPark) {
                timesToPark.put(voiture, oldTimeToPark + timeToPark);
            }*/
            timesToPark.put(voiture, oldTimeToPark + timeToPark);
        }
        return true;
    }

    private boolean tryToPark(Voiture voiture) {

        Case positionVoiture = positions.get(voiture);
        System.out.println(voiture + " essaie de se garer");
        if (!(positionVoiture instanceof Troncon)){
            return false;
        }

        if (parking.getNbPlace() > 0 && ((Troncon) positionVoiture).isAccesParking()){
            //synchronized NbPlace ??
            parking.decrNbPlace();
            //TODO trouver un moyen propre de mettre à jour voiture.isParked
            //TODO trouver un moyen de repasser isParked à false quand la voiture n'est plus garée
            voiture.setParked(true);
            //synchronized positions ??
            positions.put(voiture, parking);
            System.out.println(voiture + " : voiture garée");
            System.out.println(voiture + " est sur la case " + parking);
            return  true;
        }
        return  false;
    }

    private boolean handleLeft(Voiture voiture) throws InterruptedException {
        if(voiture.isParked()) return false;

        Case positionVoiture = positions.get(voiture);

        System.out.println(voiture + " essaie de tourner à gauche ...");

        // on ne peut tourner que si l'on est sur un feu ou un parking
        if(positionVoiture instanceof Troncon) {
            //deplacer la voiture sur la position suivante
            Case positionSuivante = ((Troncon)positionVoiture).getNext();
            positions.put(voiture, positionSuivante);
            System.out.println(voiture + " ne peux pas tourner et continue tout droit");
            System.out.println(voiture + " est maintenant sur la case " + positionSuivante);
            return false;
        }

        if(positionVoiture instanceof Feu) {
            Feu feu = (Feu)positionVoiture;
            while(!feu.green) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    //System.out.println(e);
                }
            }

            if (positions.containsValue(feu.oposite)){
                notify();
                wait();
            }

            positions.put(voiture, feu.getLeft());
            notify();
            System.out.println(voiture + " tourne à gauche");
            System.out.println(voiture + " est maintenant sur la case " + feu.getLeft());
            return true;
        }

        if(positionVoiture instanceof Parking) {
            //...
            if(positions.values().stream().filter(c -> c instanceof Troncon).map(c -> (Troncon)c).anyMatch(troncon->troncon.accesParking)) {
                System.out.println(voiture + " laisse la priorité aux voitures avant de sortir du parking.");
                return false;
            }
            Case positionSuivante = ((Parking)positionVoiture).getLeft();
            positions.put(voiture, ((Parking)positionVoiture).getLeft());
            System.out.println(voiture + " sort du parking par la gauche");
            System.out.println(voiture + " est maintenant sur la case " + positionSuivante);
            return true;
        }

        return false;
    }

    private boolean handleRight(Voiture voiture) {
        if(voiture.isParked()) return false;

        Case positionVoiture = positions.get(voiture);

        System.out.println(voiture + " essaie de tourner à droite ...");

        // on ne peut tourner que si l'on est sur un feu ou un parking
        if(positionVoiture instanceof Troncon) {
            //deplacer la voiture sur la position suivante
            Case positionSuivante = ((Troncon)positionVoiture).getNext();
            positions.put(voiture, positionSuivante);
            System.out.println(voiture + " ne peut pas tourner et continue tout droit");
            System.out.println(voiture + " est maintenant sur la case " + positionSuivante);
            return false;
        }

        if(positionVoiture instanceof Feu) {
            Feu feu = (Feu)positionVoiture;
            while(!feu.green) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    //System.out.println(e);
                }
            }
            positions.put(voiture, feu.getRight());
            System.out.println(voiture + " tourne à droite");
            System.out.println(voiture + " est maintenant sur la case " + feu.getRight());

            if (positions.values().stream().filter(p -> p instanceof Feu).map(p -> (Feu)p).filter(f->f.equals(feu)).count()==0){
                notify();
            }
            return true;
        }

        if(positionVoiture instanceof Parking) {
            if(positions.values().stream().filter(c -> c instanceof Troncon).map(c -> (Troncon)c).anyMatch(troncon->troncon.accesParking)) {
                System.out.println(voiture + " laisse la priorité aux voitures avant de sortir du parking.");
                return false;
            }
            Case positionSuivante = ((Parking)positionVoiture).getStraightOrRight();
            positions.put(voiture, positionSuivante);
            System.out.println(voiture + " sort du parking par la droite");
            System.out.println(voiture + " est maintenant sur la case " + positionSuivante);
            return true;
        }

        return false;
    }

    private boolean handleStraightOn(Voiture voiture) {
        if(voiture.isParked()) return false;

        Case positionVoiture = positions.get(voiture);

        System.out.println(voiture + " essaie d'aller tout droit");

        if(positionVoiture instanceof Troncon) {
            //deplacer la voiture sur la position suivante
            Case positionSuivante = ((Troncon)positionVoiture).getNext();
            positions.put(voiture, positionSuivante);
            System.out.println(voiture + " va tout droit");
            System.out.println(voiture + " est maintenant sur la case " + positionSuivante);
            return true;
        }

        if(positionVoiture instanceof Feu) {
            Feu feu = (Feu)positionVoiture;
            while(!feu.green) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    //System.out.println(e);
                }
            }
            positions.put(voiture, feu.getStraightOn());
            System.out.println(voiture + " va tout droit");
            System.out.println(voiture + " est maintenant sur la case " + feu.getStraightOn());
            if (positions.values().stream().filter(p -> p instanceof Feu).map(p -> (Feu)p).filter(f->f.equals(feu)).count()==0){
                notify();
            }
            return true;
        }

        if(positionVoiture instanceof Parking) {
            //...
            if(positions.values().stream().filter(c -> c instanceof Troncon).map(c -> (Troncon)c).anyMatch(troncon->troncon.accesParking)) {
                System.out.println(voiture + " laisse la priorité aux voitures avant de sortir du parking.");
                return false;
            }
            Case positionSuivante = ((Parking)positionVoiture).getStraightOrRight();
            positions.put(voiture, positionSuivante);
            System.out.println(voiture + " sort du parking par la droite");
            System.out.println(voiture + " est maintenant sur la case " + positionSuivante);
            return true;
        }

        return false;
    }

    public synchronized void switchFeu(){
        for (Feu feu: listeFeux) {
            feu.setGreen(!feu.isGreen());
            System.out.println(feu + " est " + (feu.isGreen()? "vert" : "rouge"));
        }
        notifyAll();
    }

    public void ajoutVoiture(Voiture voiture, Case position) {
        positions.put(voiture, position);
    }

    public void ajoutFeu(Feu feu) {
        this.listeFeux.add(feu);
    }
}
