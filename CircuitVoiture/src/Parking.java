public class Parking extends Case {

    Case left;
    Case straightOrRight;
    int nbPlace;
    public Parking(Case left, Case straightOrRight) {
        this.left = left;
        this.straightOrRight = straightOrRight;
    }

    public void incrNbPlace() {
        nbPlace++;
    }

    public void decrNbPlace() {
        nbPlace--;
    }

    public int getNbPlace() {
        return nbPlace;
    }

    public void setNbPlace(int nbPlace) {
        this.nbPlace = nbPlace;
    }

    public Parking(Case left, Case straightOrRight, int nbPlace) {
        this.left = left;
        this.straightOrRight = straightOrRight;
        this.nbPlace = nbPlace;
    }

    public Case getLeft() {
        return left;
    }

    public void setLeft(Case left) {
        this.left = left;
    }

    public Case getStraightOrRight() {
        return straightOrRight;
    }

    public void setStraightOrRight(Case straightOrRight) {
        this.straightOrRight = straightOrRight;
    }

    @Override
    public String toString() {
        return "Parking";
    }
}
