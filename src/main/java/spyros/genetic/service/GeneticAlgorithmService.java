package spyros.genetic.service;

import spyros.genetic.comparators.ChromosomeComparator;
import spyros.genetic.exceptions.GeneticAlgorithmException;
import spyros.genetic.model.Chromosome;
import spyros.genetic.model.Gene;
import spyros.genetic.model.Population;
import spyros.genetic.utils.NumberUtils;

import java.util.*;
import java.util.stream.Collectors;

public class GeneticAlgorithmService {
    private final int POPULATION_SIZE = 30;

    private final Population population = new Population();
    private final List<Gene> availableGenes = new ArrayList<>();

    private Chromosome firstParent;
    private Chromosome secondParent;
    private Chromosome firstOffspring;
    private Chromosome secondOffspring;

    // For tests
    public void populateTest() {
        // Create random genes
        for (int i = 0; i < POPULATION_SIZE * 5; i++) {
            Gene gene = new Gene();

            gene.setId(i + 1);
            gene.setPercentAppearances(NumberUtils.getRandom(0, 100));
            gene.setPercentMistakes(NumberUtils.getRandom(0, 100));
            gene.setPercentHints(NumberUtils.getRandom(0, 100));

            availableGenes.add(gene);
        }

        createCandidateSolutions();
    }

    public void populate(List<Integer[]> data) throws GeneticAlgorithmException {
        for (Integer[] row : data) {
            if (row.length != 4)
                throw new GeneticAlgorithmException("Every row should have length equal to 4, compliant with the format: {question_id, percent_appearances, percent_mistakes, percent_hints}");

            Gene gene = new Gene();

            gene.setId(row[0]);
            gene.setPercentAppearances(row[1]);
            gene.setPercentMistakes(row[2]);
            gene.setPercentHints(row[3]);

            availableGenes.add(gene);
        }

        createCandidateSolutions();
    }

    public void run() throws GeneticAlgorithmException {
        // Validation
        checkIfPopulationsExists();

        // Execute
        select();
        crossover();
        mutate();
        replace();
    }

    public List<Integer> getBestSolution() throws GeneticAlgorithmException {
        checkIfPopulationsExists();

        List<Integer> solution = new ArrayList<>();

        Chromosome bestChromosome = population.getChromosomes().get(0);
        for (int i = 0; i < bestChromosome.getSize(); i++) {
            solution.add(bestChromosome.getGeneByPosition(i).getId());
        }

        return solution;
    }

    public int getBestSolutionFitness() throws GeneticAlgorithmException {
        checkIfPopulationsExists();

        return population.getChromosomes().get(0).calculateFitness();
    }

    // Private methods
    private void checkIfPopulationsExists() throws GeneticAlgorithmException {
        if (population.getChromosomes() == null || population.getChromosomes().size() == 0)
            throw new GeneticAlgorithmException("No candidate solutions provided.");
    }

    private void createCandidateSolutions() {
        for (int i = 0; i < POPULATION_SIZE; i++) {
            Chromosome chromosome = new Chromosome();

            for (int j = 0; j < Chromosome.GENES_SIZE; j++) {
                int number = NumberUtils.getRandom(0, availableGenes.size() - 1);
                Gene gene = availableGenes.get(number);


                if (chromosome.containsGene(gene)) {
                    j--;
                } else {
                    chromosome.addGene(gene);
                }
            }

            population.addChromosome(chromosome);
        }

        // Reverse sort chromosomes by fitness
        population.getChromosomes().sort(new ChromosomeComparator().reversed());
    }

    // Roulette wheel selection (two parents)
    private void select() {
        Map<Chromosome, Float> rouletteMap = new HashMap<>();
        int totalFitness = population.getFitness();

        if (totalFitness < 0) {
            int num1 = NumberUtils.getRandom(1, population.getChromosomes().size());
            int num2 = NumberUtils.getRandom(1, population.getChromosomes().size());

            firstParent = population.getChromosomes().get(num1);
            secondParent = population.getChromosomes().get(num2);
        } else {

            float currentPortion = 0;

            for (Chromosome chromosome : population.getChromosomes()) {
                currentPortion += (float) (100 * chromosome.calculateFitness()) / totalFitness;
                rouletteMap.put(chromosome, currentPortion);
            }

            Map<Chromosome, Float> sortedRouletteMap = rouletteMap.entrySet()
                    .stream()
                    .sorted((Map.Entry.comparingByValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

            int number1 = NumberUtils.getRandom(0, 99);
            for (Map.Entry<Chromosome, Float> entry : sortedRouletteMap.entrySet()) {
                if (number1 <= entry.getValue()) {
                    firstParent = entry.getKey();
                    break;
                }
            }

            int number2;
            do {
                number2 = NumberUtils.getRandom(0, 99);

                for (Map.Entry<Chromosome, Float> entry : sortedRouletteMap.entrySet()) {
                    if (number2 <= entry.getValue()) {
                        secondParent = entry.getKey();
                        break;
                    }
                }

            } while (secondParent == null || secondParent == firstParent);
        }
    }

    // Single-point crossover
    private void crossover() {
        int crossoverPoint = NumberUtils.getRandom(0, Chromosome.GENES_SIZE - 1);

        firstOffspring = new Chromosome();
        secondOffspring = new Chromosome();

        // Create first offspring
        for (int i = 0; i < Chromosome.GENES_SIZE; i++) {
            Gene firstParentGene = firstParent.getGeneByPosition(i);
            Gene secondParentGene = secondParent.getGeneByPosition(i);

            if (i <= crossoverPoint) {
                firstOffspring.addGene(firstParentGene);
                secondOffspring.addGene(secondParentGene);
            } else {
                firstOffspring.addGene(secondParentGene);
                secondOffspring.addGene(firstParentGene);
            }
        }

        // Fill with extra questions in case of duplicates
        Chromosome[] offsprings = {firstOffspring, secondOffspring};
        for (Chromosome offspring : offsprings) {
            int currentSize = offspring.getSize();

            if (currentSize < Chromosome.GENES_SIZE) {

                for (int i = 0; i < Chromosome.GENES_SIZE - currentSize; i++) {
                    Gene randomGene;

                    do {
                        randomGene = getRandomGene();
                    } while (offspring.containsGene(randomGene));

                    offspring.addGene(randomGene);
                }
            }
        }
    }

    private void mutate() {
        Chromosome[] offsprings = {firstOffspring, secondOffspring};

        for (Chromosome offspring : offsprings) {
            for (int i = 0; i < offspring.getSize(); i++) {
                Gene gene = offspring.getGeneByPosition(i);

                // Mutation probability: needs to be low (e.g. 5%)
                int probability = NumberUtils.getRandom(1, 100);
                if (probability <= 5) {
                    Gene randomGene;

                    do {
                        randomGene = getRandomGene();
                    } while (offspring.containsGene(randomGene));
                    offspring.replaceGene(gene, randomGene);
                }
            }
        }
    }

    private void replace() {
        population.getChromosomes().set(population.getChromosomes().size() - 1, firstOffspring);
        population.getChromosomes().set(population.getChromosomes().size() - 2, secondOffspring);

        population.getChromosomes().sort(new ChromosomeComparator().reversed());
    }

    private Gene getRandomGene() {
        return availableGenes.get(NumberUtils.getRandom(0, availableGenes.size() - 1));
    }
}
