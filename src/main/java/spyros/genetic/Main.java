package spyros.genetic;

import gr.spyros.genetic.exceptions.GeneticAlgorithmException;
import gr.spyros.genetic.service.GeneticAlgorithmService;
import gr.spyros.genetic.utils.NumberUtils;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Integer[]> rows = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Integer[] row = {i + 1, NumberUtils.getRandom(0, 100), NumberUtils.getRandom(0, 100), NumberUtils.getRandom(0, 100)};
            rows.add(row);
        }

        List<Integer> solution = null;
        int fitness = 0;

        GeneticAlgorithmService service = new GeneticAlgorithmService();

        try {
            // service.populateTest();
            service.populate(rows);

            for (int i = 1; i <= 100; i++) {
                service.run();
                solution = service.getBestSolution();
                fitness = service.getBestSolutionFitness();

                System.out.println("Generation " + i + ": " + solution + ", Fitness: " + fitness);
            }
        } catch (GeneticAlgorithmException e) {
            System.err.println(e.getMessage());
        }

        System.out.println("Final solution: " + solution + ", Fitness: " + fitness);
    }
}
