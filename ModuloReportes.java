package com.mycompany.rabbitmq;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuloReportes {

    private final static String STORAGE_FILE = "formularios.json";

    public static void main(String[] args) {
        ModuloReportes reportes = new ModuloReportes();
        reportes.generarReportes();
    }

    public void generarReportes() {
        List<String> formularios = obtenerFormularios();

        // Total de formularios
        System.out.println("Total de formularios almacenados: " + formularios.size());

        // Promedio de edad
        double promedioEdad = calcularPromedioEdad(formularios);
        System.out.println("Promedio de edad: " + promedioEdad);

        // Distribución de edades
        Map<String, Integer> distribucionEdades = calcularDistribucionEdades(formularios);
        System.out.println("Distribución de edades: " + distribucionEdades);

        // Conteo por dirección
        Map<String, Integer> conteoPorDireccion = contarPorDireccion(formularios);
        System.out.println("Conteo de formularios por dirección: " + conteoPorDireccion);
    }

    private List<String> obtenerFormularios() {
        List<String> formularios = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(STORAGE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                formularios.add(line);
                System.out.println("Formulario leído: " + line); // Imprimir cada formulario leído
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return formularios;
    }

    private double calcularPromedioEdad(List<String> formularios) {
        int totalEdad = 0;
        int count = 0;

        for (String formulario : formularios) {
            String[] campos = formulario.replace("{", "").replace("}", "").split(", ");
            for (String campo : campos) {
                if (campo.contains("edad")) {
                    String[] partes = campo.split(": ");
                    totalEdad += Integer.parseInt(partes[1].trim());
                    count++;
                }
            }
        }
        return count > 0 ? (double) totalEdad / count : 0.0;
    }

    private Map<String, Integer> calcularDistribucionEdades(List<String> formularios) {
        Map<String, Integer> distribucion = new HashMap<>();

        for (String formulario : formularios) {
            String[] campos = formulario.replace("{", "").replace("}", "").split(", ");
            for (String campo : campos) {
                if (campo.contains("edad")) {
                    String[] partes = campo.split(": ");
                    String edad = partes[1].trim();
                    distribucion.put(edad, distribucion.getOrDefault(edad, 0) + 1);
                }
            }
        }
        return distribucion;
    }

    private Map<String, Integer> contarPorDireccion(List<String> formularios) {
        Map<String, Integer> conteo = new HashMap<>();

        for (String formulario : formularios) {
            String[] campos = formulario.replace("{", "").replace("}", "").split(", ");
            for (String campo : campos) {
                if (campo.contains("direccion")) {
                    String[] partes = campo.split(": ");
                    String direccion = partes[1].trim();
                    conteo.put(direccion, conteo.getOrDefault(direccion, 0) + 1);
                }
            }
        }
        return conteo;
    }
}

    

