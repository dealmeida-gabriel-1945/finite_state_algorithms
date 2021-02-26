package testes;

import data_shape.Automato;
import util.AutomatoUtil;

public class TratamentoArquivo02 {
    public static void main(String[] args) {
        String path = "/home/gabriel_guimaraes/Documents/Gabriel/IFMG/p_4/LFA/trabalho_automatos/exemplos/exemplo_01.jff";
        String pathToWrite = "/home/gabriel_guimaraes/Documents/Gabriel/IFMG/p_4/LFA/trabalho_automatos/exemplos/exemplo_01_write.jff";
        try{
            Automato aut = AutomatoUtil.READ_FILE(path);
            AutomatoUtil.WRITE_FILE(aut, pathToWrite);
        }catch (Exception ex){
            System.out.println("Erro");
        }
    }
}
