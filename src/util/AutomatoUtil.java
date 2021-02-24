package util;

import data_shape.Automato;
import data_shape.Estado;
import data_shape.Transicao;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class AutomatoUtil {
    public static List<Transicao> MULTIPLICA_TRANSICOES(Automato automato1, Automato automato2, Automato resultante) {
        List<Transicao> resultado = new ArrayList<>();
        automato1.inputs_possiveis.forEach(
            input -> automato1.estados.forEach(
                estado1 -> {
                    automato2.estados.forEach(
                        estado2 ->{
                            Optional<Transicao> opt_trans1 = automato1.transicoes.stream().filter(transicao -> Objects.equals(transicao.origem, estado1) && Objects.equals(transicao.valor, input)).findFirst();
                            Optional<Transicao> opt_trans2 = automato2.transicoes.stream().filter(transicao -> Objects.equals(transicao.origem, estado2) && Objects.equals(transicao.valor, input)).findFirst();
                            if(opt_trans1.isPresent() && opt_trans2.isPresent()){
                                Optional<Estado> opt_estado_origem = resultante.estados.stream().filter(estado -> Objects.equals(estado.nome, String.join("_", opt_trans1.get().origem.nome, opt_trans2.get().origem.nome))).findFirst();
                                Optional<Estado> opt_estado_destino = resultante.estados.stream().filter(estado -> Objects.equals(estado.nome, String.join("_", opt_trans1.get().destino.nome, opt_trans2.get().destino.nome))).findFirst();
                                if(!opt_estado_origem.isPresent() || !opt_estado_destino.isPresent()) return;
                                Transicao trans = new Transicao();
                                trans.valor = input;
                                trans.origem = opt_estado_origem.get();
                                trans.destino = opt_estado_destino.get();
                                resultado.add(trans);
                            }
                        }
                    );
                }
            )
        );
        return resultado;
    }

    public static Automato READ_FILE(String path) throws Exception {
        Automato toReturn = new Automato();

        File fXmlFile = new File(path);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);

        //optional, but recommended
        //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
        doc.getDocumentElement().normalize();

        toReturn.estados = AutomatoUtil.GET_ESTADOS(doc.getElementsByTagName("state"));
        toReturn.estados.stream()
            .filter(estado -> estado.inicial)
                .findFirst().ifPresent(estado -> toReturn.estado_inicial = estado);
        toReturn.estados_de_aceitacao = toReturn.estados.stream().filter(estado -> estado.de_aceitacao).collect(Collectors.toList());

        toReturn.transicoes = AutomatoUtil.GET_TRANSICOES(doc.getElementsByTagName("transition"), toReturn.estados);

        toReturn.inputs_possiveis = AutomatoUtil.GET_ALFABETO(toReturn.transicoes);

        return toReturn;
    }

    public static List<Estado> GET_ESTADOS(NodeList nList) throws Exception {
        List<Estado> toReturn = new ArrayList<>();
        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                toReturn.add(new Estado(
                    eElement.getAttribute("id"),
                    Objects.nonNull(eElement.getElementsByTagName("final").item(0)),
                    Objects.nonNull(eElement.getElementsByTagName("initial").item(0))
                ));
            }
        }
        return toReturn;
    }

    public static List<Transicao> GET_TRANSICOES(NodeList nList, List<Estado> estados) throws Exception {
        List<Transicao> toReturn = new ArrayList<>();
        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                Optional<Estado> optEstadoOrigem = estados.stream().filter(item -> Objects.equals(eElement.getElementsByTagName("from").item(0).getTextContent(), item.nome)).findFirst();
                Optional<Estado> optEstadoDestino = estados.stream().filter(item -> Objects.equals(eElement.getElementsByTagName("to").item(0).getTextContent(), item.nome)).findFirst();

                if(!optEstadoOrigem.isPresent() || !optEstadoDestino.isPresent()) throw new Exception("Arquivo mal gerado");

                toReturn.add(new Transicao(
                    optEstadoOrigem.get(),
                    optEstadoDestino.get(),
                    eElement.getElementsByTagName("read").item(0).getTextContent()
                ));
            }
        }
        return toReturn;
    }

    public static List<String> GET_ALFABETO(List<Transicao> transicoes) {
        List<String> toReturn = new ArrayList<>();
        transicoes.stream().map(item -> item.valor)
            .forEach(input -> {
                if(!toReturn.contains(input)) toReturn.add(input);
            });
        return toReturn;
    }

    /*
    *
    NodeList nList = doc.getElementsByTagName("state");

        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) nNode;
                Estado estado = new Estado();
                estado.nome = eElement.getAttribute("id");
                estado.inicial = Objects.nonNull(eElement.getElementsByTagName("initial").item(0));
                estado.de_aceitacao = Objects.nonNull(eElement.getElementsByTagName("final").item(0));

                System.out.println("id : " + eElement.getAttribute("id"));
                System.out.println("name : " + eElement.getAttribute("name"));
                System.out.println("x : " + eElement.getElementsByTagName("x").item(0).getTextContent());
                System.out.println("y : " + eElement.getElementsByTagName("y").item(0).getTextContent());
                System.out.println("inicial : " + Objects.nonNull(eElement.getElementsByTagName("initial").item(0)));
                System.out.println("final : " + Objects.nonNull(eElement.getElementsByTagName("final").item(0)));

            }
        }
    * */
}
