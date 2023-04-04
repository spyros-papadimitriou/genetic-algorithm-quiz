# genetic-algorithm-quiz
A genetic algorithm for creating personalized quizzes.
This is a Java Maven web project.

Each chromosome contains ten genes. Each gene corresponds to a quiz question.

The genetic algorithm takes as input a list of arrays of integers. Each array (list entry) contains the following four values:

The id of the question (can be derived from a database).
The percentage of incorrect answers to the question.
The percentage that the user asked for help on a question.

The above percentages can be overall or at the user level.
The question with id = 83 appeared eight times in the 40 quizzes the user took. Of the eight times, the user answered incorrectly four times. In addition, of the eight times, they asked for help two times. Therefore, the record with the id of the question and the corresponding percentages would be as follows:
{83, 20, 50, 25};

So, after supplying the algorithm with the list of similar records, it is executed and calculates the ten most appropriate questions that will form a new personalized quiz. Then, the suitability (fitness) of each question is calculated by the following function:

weightAppearances * gene.getPercentAppearances() + weightMistakes * gene.getPercentMistakes() + weightHints * gene.getPercentHints();

This function is executed for each chromosome's gene.
Therefore, the fitness of a quiz (chromosome) is calculated by the sum of the fitness of each question (gene) it contains.

Notice from the function that one can define the weights for each percentage. Hence, one can emphasize a different criterion, e.g. the percentage of wrong answers. 