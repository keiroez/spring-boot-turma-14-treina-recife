public class Aviao extends Veiculo implements Voador {

    @Override
    public void voar() {
        System.out.println("Avião está voando");
    }

    public Aviao(String nome, String placa, String cor, int ano) {
        super(nome, placa, cor, ano);
    }

    @Override
    String getPlacaMaisAno() {
        // TODO Auto-generated method stub
        return null;
    }


}
