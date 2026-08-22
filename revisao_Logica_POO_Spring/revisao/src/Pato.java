public class Pato implements Nadador, Voador, Andador {

    @Override
    public void nadar() {
        System.out.println("Pato está nadando");
    }

    @Override
    public void voar() {
        System.out.println("Pato está voando");
    }

    @Override
    public void andar() {
        System.out.println("Pato está andando");
    }
    

}
