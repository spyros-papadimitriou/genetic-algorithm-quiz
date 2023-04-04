package gr.spyros.genetic.comparators;

import gr.spyros.genetic.model.Chromosome;

import java.util.Comparator;

public class ChromosomeComparator implements Comparator<Chromosome> {
    @Override
    public int compare(Chromosome c1, Chromosome c2) {
        return c1.calculateFitness() - c2.calculateFitness();
    }
}
