package testes;

import data_shape.Automato;
import util.AutomatoUtil;
import util.ExemploUtil;

public class TesteTransformaAfnEmAfd01_UT18 {
    public static void main() {
        try{
            Automato automato = AutomatoUtil.READ_FILE(ExemploUtil.EXEMPLO_06);
            Automato semMov = new Automato(automato);
            Automato afd = new Automato(automato);

            semMov.remove_movimentos_vazios();
            afd.to_afd();

            AutomatoUtil.WRITE_FILE(semMov, ExemploUtil.EXEMPLO_08_SEM_MOVIMENTOS_VAZIOS);
            AutomatoUtil.WRITE_FILE(afd, ExemploUtil.EXEMPLO_08_AFD);

            System.out.println(ExemploUtil.VERDE + "TesteTransformaAfnEmAfd01_UT18 => SUCESSO" + ExemploUtil.RESET);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
