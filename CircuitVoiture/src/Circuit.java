import java.util.*;
import java.util.function.Supplier;

public class Circuit {

    static final String PARK_INSTRUCTION_REGEX = "park\\s*\\d+";

    HashMap<Voiture, Case> positions = new HashMap<>();
    //HashMap<Voiture, Long> timesToPark = new HashMap<>();

    List<Feu> listeFeux = new ArrayList<Feu>();

    public synchronized boolean passInstruction(Voiture voiture, String instruction) {
        Case positionActuelle = positions.get(voiture);

        switch(instruction) {
            case "END_OF_STREAM":
                return false;
            case "left":
                return handleLeft(voiture);
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

        if(positionVoiture instanceof Troncon)
            return ((Troncon)positionVoiture).dureeDeTraversee;

        return 0;
    }

    private boolean handleParkInstruction(Voiture voiture, String instruction) {
        instruction.trim();
        int timeToPark = Integer.valueOf(instruction.substring(5));
        //...
        return true;
    }

    private boolean handleLeft(Voiture voiture) {
        Case positionVoiture = positions.get(voiture);

        System.out.println(voiture.toString() + " essaie de tourner à gauche ...");

        // on ne peut tourner que si l'on est sur un feu ou un parking
        if(positionVoiture instanceof Troncon) {
            //deplacer la voiture sur la position suivante
            Case positionSuivante = ((Troncon)positionVoiture).getNext();
            positions.put(voiture, positionSuivante);
            System.out.println(voiture.toString() + " ne peux pas tourner et continue tout droit");
            System.out.println(voiture.toString() + " est maintenant sur la case " + positionSuivante.toString());
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
            positions.put(voiture, feu.getLeft());
            System.out.println(voiture.toString() + " tourne à gauche");
            System.out.println(voiture.toString() + " est maintenant sur la case " + feu.getLeft().toString());
            return true;
        }

        if(positionVoiture instanceof Parking) {
            //...
            Case positionSuivante = ((Parking)positionVoiture).getLeft();
            positions.put(voiture, ((Parking)positionVoiture).getLeft());
            System.out.println(voiture.toString() + " sors du parking par la gauche");
            System.out.println(voiture.toString() + " est maintenant sur la case " + positionSuivante.toString());
            return true;
        }

        return false;
    }

    private boolean handleRight(Voiture voiture) {
        Case positionVoiture = positions.get(voiture);

        System.out.println(voiture.toString() + " essaie de tourner à droite ...");

        // on ne peut tourner que si l'on est sur un feu ou un parking
        if(positionVoiture instanceof Troncon) {
            //deplacer la voiture sur la position suivante
            Case positionSuivante = ((Troncon)positionVoiture).getNext();
            positions.put(voiture, positionSuivante);
            System.out.println(voiture.toString() + " ne peux pas tourner et continue tout droit");
            System.out.println(voiture.toString() + " est maintenant sur la case " + positionSuivante.toString());
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
            System.out.println(voiture.toString() + " tourne à droite");
            System.out.println(voiture.toString() + " est maintenant sur la case " + feu.getRight().toString());
            return true;
        }

        if(positionVoiture instanceof Parking) {
            //...
            Case positionSuivante = ((Parking)positionVoiture).getStraightOrRight();
            positions.put(voiture, positionSuivante);
            System.out.println(voiture.toString() + " sors du parking par la droite");
            System.out.println(voiture.toString() + " est maintenant sur la case " + positionSuivante.toString());
            return true;
        }

        return false;
    }

    private boolean handleStraightOn(Voiture voiture) {
        Case positionVoiture = positions.get(voiture);

        System.out.println(voiture.toString() + " essaie d'aller tout droit");

        if(positionVoiture instanceof Troncon) {
            //deplacer la voiture sur la position suivante
            Case positionSuivante = ((Troncon)positionVoiture).getNext();
            positions.put(voiture, positionSuivante);
            System.out.println(voiture.toString() + " va tout droit");
            System.out.println(voiture.toString() + " est maintenant sur la case " + positionSuivante.toString());
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
            System.out.println(voiture.toString() + " va tout droit");
            System.out.println(voiture.toString() + " est maintenant sur la case " + feu.getStraightOn().toString());
            return true;
        }

        if(positionVoiture instanceof Parking) {
            //...
            Case positionSuivante = ((Parking)positionVoiture).getStraightOrRight();
            positions.put(voiture, positionSuivante);
            System.out.println(voiture.toString() + " sors du parking par la droite");
            System.out.println(voiture.toString() + " est maintenant sur la case " + positionSuivante.toString());
            return true;
        }

        return false;
    }

    public synchronized void switchFeu(){
        for (Feu feu: listeFeux) {
            feu.setGreen(!feu.isGreen());
            System.out.println(feu.toString() + " est " + (feu.isGreen()? "vert" : "rouge"));
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
