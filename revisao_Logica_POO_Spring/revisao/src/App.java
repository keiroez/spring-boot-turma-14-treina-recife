import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {
        List<Veiculo> lista = new ArrayList<>();

        Carro carro = new Carro("Opala", "FGT2444", "branco", 1965);
        Caminhao caminhao = new Caminhao("Volvo", "DFF4444", "Preto", 2020);

        lista.add(carro);
        lista.add(caminhao);

        for(Veiculo v: lista){
            v.donoVeiculo("teste");
        }

        List<Voador> voadores = new ArrayList<>();

        voadores.add(new Pato());
        voadores.add(new Aviao("F17", "31313", "cinza", 2020));

        for(Voador v: voadores){
            if(v instanceof Aviao){
                ((Aviao) v).acelerar();
            }
            if(v instanceof Pato){
                System.out.println("Qua Qua");
            }
        }

        try {
            //Tente executar algo
        } catch (Exception e) {
            //Se Der erro, trate aqui
        }
    }
}
