public class Parking extends Case {

    Case left;
    Case straightOrRight;

    public Parking(Case left, Case straightOrRight) {
        this.left = left;
        this.straightOrRight = straightOrRight;
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
