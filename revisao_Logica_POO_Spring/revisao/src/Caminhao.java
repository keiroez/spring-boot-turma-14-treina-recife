public class Caminhao extends Veiculo {

    private int carga;

    @Override
    public void andar() {
        System.out.println("Caminhao está andando");
    }

    public Caminhao(String nome, String placa, String cor, int ano) {
        super(nome, placa, cor, ano);
    }

    public void trocarOleo(){

    }

    public void trocarOleo(String oleo){

    }

    @Override
    public void acelerar() {
        this.velocidade = this.velocidade + 5;  
    }

    @Override
    String getPlacaMaisAno() {
        return "Caminhao: " + getPlaca() + getAno();
    }

    

}
