package gr.spyros.genetic.model;

import java.util.ArrayList;
import java.util.List;

public class Chromosome {
    public static final int GENES_SIZE = 10;
    private final List<Gene> genes = new ArrayList<>();

    public void addGene(Gene gene) {
        if (!genes.contains(gene) && genes.size() < GENES_SIZE)
            genes.add(gene);
    }

    public Gene getGeneByPosition(int position) {
        if (position < 0 || position >= genes.size())
            return null;

        return genes.get(position);
    }

    public int getSize() {
        return genes.size();
    }

    public boolean containsGene(Gene gene) {
        return genes.contains(gene);
    }

    public void replaceGene(Gene oldGene, Gene newGene) {
        int index = genes.indexOf(oldGene);
        if (index != -1)
            genes.set(index, newGene);
    }

    @Override
    public String toString() {
        return "Chromosome{" +
                "genes=" + genes +
                ", fitness=" + calculateFitness() +
                '}';
    }

    public int calculateFitness() {
        int fitness = 0;
        float weightAppearances = 1;
        float weightMistakes = 1;
        float weightHints = 1;

        for (Gene gene : genes) {

            fitness += weightAppearances * gene.getPercentAppearances() + weightMistakes * gene.getPercentMistakes() + weightHints * gene.getPercentHints();
        }

        return fitness;
    }
}
