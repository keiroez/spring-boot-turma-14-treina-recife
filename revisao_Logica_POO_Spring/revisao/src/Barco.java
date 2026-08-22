public class Barco extends Veiculo implements Nadador {


    @Override
    public void nadar() {
        System.out.println("Barco está nadando");
    }

    public Barco(String nome, String placa, String cor, int ano) {
        super(nome, placa, cor, ano);
    }

    @Override
    String getPlacaMaisAno() {
        // TODO Auto-generated method stub
        return null;
    }

}
