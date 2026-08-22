public class Carro extends Veiculo {

    

    @Override
    public void andar() {
        System.out.println("Carro está andando");
    }

    public Carro(String nome, String placa, String cor, int ano) {
        super(nome, placa, cor, ano);
    }

    @Override
    String getPlacaMaisAno() {
        return "Carro: " + getPlaca() + getAno();
    }

    

}
