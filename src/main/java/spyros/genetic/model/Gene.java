package gr.spyros.genetic.model;

public class Gene {
    private int id;
    private int percentAppearances;
    private int percentMistakes;
    private int percentHints;

    public Gene() {
    }

    @Override
    public String toString() {
        return "Gene{" +
                "id=" + id +
                '}';
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPercentAppearances() {
        return percentAppearances;
    }

    public void setPercentAppearances(int percentAppearances) {
        if (percentAppearances < 0)
            percentAppearances = 0;
        else if (percentAppearances > 100)
            percentAppearances = 100;

        this.percentAppearances = percentAppearances;
    }

    public int getPercentMistakes() {
        return percentMistakes;
    }

    public void setPercentMistakes(int percentMistakes) {
        if (percentMistakes < 0)
            percentMistakes = 0;
        else if (percentMistakes > 100)
            percentMistakes = 100;

        this.percentMistakes = percentMistakes;
    }

    public int getPercentHints() {
        return percentHints;
    }

    public void setPercentHints(int percentHints) {
        if (percentHints < 0)
            percentHints = 0;
        else if (percentHints > 100)
            percentHints = 100;

        this.percentHints = percentHints;
    }
}
