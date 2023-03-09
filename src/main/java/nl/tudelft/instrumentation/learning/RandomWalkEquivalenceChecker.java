package nl.tudelft.instrumentation.learning;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

/**
 * @author Bram Verboom
 */

public class RandomWalkEquivalenceChecker extends EquivalenceChecker {

    private int numberOfSamples;
    private int depth;

    private Random rng;

    public RandomWalkEquivalenceChecker(SystemUnderLearn sul, String[] alphabet, int depth, int numberOfSamples) {
        super(sul, alphabet);
        this.depth = depth;
        this.numberOfSamples = numberOfSamples;
        this.rng = new Random();
    }

    /**
     * Method for verifying a hypothesis against the SUL.
     *
     * Does up to 'numberOfSamples' queries of 'depth' random symbols to the SUL and
     * compares its result with the hypothesis. If any incosistency is found, the
     * counterexample will be returned.
     * 
     * @param hypothesis The hypothesis to verify
     * @return A counterexample or an empty option if no counterexample was found
     */
    @Override
    public Optional<String[]> verify(MealyMachine hyp) {
        String[] inputs = new String[depth];
        for (int i = 0; i < numberOfSamples; i++) {
            // Generate random input
            for (int j = 0; j < depth; j++) {
                inputs[j] = alphabet[rng.nextInt(alphabet.length)];
            }
            String[] modelOutput = hyp.getOutput(inputs);
            String[] realOutput = sul.getOutput(inputs);
            for (int j = 0; j < depth; j++) {
                // If the model output does not match the real output
                if (!modelOutput[j].equals(realOutput[j])) {
                    // Construct counterexample
                    return Optional.of(Arrays.asList(inputs).subList(0, j + 1).toArray(new String[0]));
                }
            }
        }
        // no counterexample found
        return Optional.empty();
    }

}
