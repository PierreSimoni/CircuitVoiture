/**
 * Simule le fonctionement d'un feu bicolore rouge/vert
 */
public class Feu extends Case {

    int id;

    /**
     * Case ateignable a gauche, droite et en face  du feu
     */

    Case left;
    Case right;
    Case straightOn;


    /**
     * Feu opposé a l'objet actuel sur le circuit
     */
    Case oposite;

    public Case getOposite() {
        return oposite;
    }

    public void setOposite(Case oposite) {
        this.oposite = oposite;
    }

    public boolean green;

    public Feu(int id, Case left, Case right, Case straightOn, boolean green) {
        this.id = id;
        this.left = left;
        this.right = right;
        this.straightOn = straightOn;
        this.green = green;
    }

    public Case getLeft() {
        return left;
    }

    public void setLeft(Case left) {
        this.left = left;
    }

    public Case getRight() {
        return right;
    }

    public void setRight(Case right) {
        this.right = right;
    }

    public Case getStraightOn() {
        return straightOn;
    }

    public void setStraightOn(Case straightOn) {
        this.straightOn = straightOn;
    }

    public boolean isGreen() {
        return green;
    }

    public void setGreen(boolean green) {
        this.green = green;
    }

    @Override
    public String toString() {
        return "Feu" + id;
    }
}
