package testes;

import data_shape.Automato;
import data_shape.Estado;
import data_shape.Transicao;
import util.AutomatoUtil;

import java.util.Arrays;
import java.util.List;

public class TratamentoArquivo01 {
    public static void main(String[] args) {
        String path = "/home/gabriel_guimaraes/Documents/Gabriel/IFMG/p_4/LFA/trabalho_automatos/exemplos/exemplo_01.jff";
        try{
            AutomatoUtil.READ_FILE(path).show();

        }catch (Exception ex){
            System.out.println("Erro");
        }
    }
}
