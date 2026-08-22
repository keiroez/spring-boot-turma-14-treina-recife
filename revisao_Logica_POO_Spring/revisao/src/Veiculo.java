public abstract class Veiculo implements Andador {
    private String nome;
    private String placa;
    private String cor;
    private int ano;
    protected  int velocidade;

    abstract String getPlacaMaisAno();


    public Veiculo(String nome, String placa, String cor, int ano) {
        this.nome = nome;
        this.placa = placa;
        this.cor = cor;
        this.ano = ano;
        this.velocidade = 0;
    }

    public void acelerar(){
        this.velocidade = this.velocidade + 10;
    }

    public void freiar(){
        if(velocidade > 0){
            this.velocidade = this.velocidade - 10;
        }
    }

    private String detalhes(){
        return "Nome: "+ this.nome +" - Placa: "+ this.placa;
    }

    public String donoVeiculo(String nome){
        return nome +"- "+ this.detalhes();
    }


    public String getNome() {
        return nome;
    }


    public void setNome(String nome) {
        this.nome = nome;
    }


    public String getPlaca() {
        return placa;
    }


    public void setPlaca(String placa) {
        this.placa = placa;
    }


    public String getCor() {
        return cor;
    }


    public void setCor(String cor) {
        this.cor = cor;
    }


    public int getAno() {
        return ano;
    }


    public void setAno(int ano) {
        if(ano>1990){
            this.ano = ano;
        }
    }


    public int getVelocidade() {
        return velocidade;
    }

}
