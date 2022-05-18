public class Troncon extends Case {
    int id;
    boolean accesParking;
    Case next;
    long dureeDeTraversee;

    public Troncon(int id, Case next, boolean accesParking, long dureeDeTraversee) {
        this.id = id;
        this.next = next;
        this.accesParking = accesParking;
        this.dureeDeTraversee = dureeDeTraversee;
    }

    public void setNext(Case next) {
        this.next = next;
    }

    public Case getNext() {
        return next;
    }

    @Override
    public String toString() {
        return "Troncon" + id;
    }
}
