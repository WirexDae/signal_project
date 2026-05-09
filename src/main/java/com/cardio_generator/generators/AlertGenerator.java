package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * A {@link PatientDataGenerator} implementation that simulates alerts for patients.
 * Each patient has an alert state.
 * If {@code alertStates} is true for a patient, the patient has an active alert.
 * Else alert is resolved.
 */
public class AlertGenerator implements PatientDataGenerator {

    // Changed constant name to UPPER_SNAKE_CASE
    public static final Random RANDOM_GENERATOR = new Random();
    // Replaced constant numbers with named constants for clarity and maintainability
    // 90% chance to resolve
    private static final double RESOLVE_PROBABILITY = 0.9;
    // Average rate (alerts per period), adjust based on desired frequency
    private static final double ALERT_RATE = 0.1;


    // Changed variable name to lowerCamelCase
    private boolean[] alertStates; // false = resolved, true = pressed

    /**
     * Constructs a new {@code AlertGenerator}.
     *
     * @param patientCount the total number of patients.
     */
    // Added comment to clarify why +1 is used
    // +1 is used because patient IDs are assumed to start from 1
    public AlertGenerator(int patientCount) {
        alertStates = new boolean[patientCount + 1];
    }

    /**
     * Generates alert state changes for a given patient and outputs them
     * using the provided {@link OutputStrategy}.
     * If an alert is active, it can be resolved with the probability of 0.9.
     * If an alert is inactive, it can be triggered with the probability of 0.1.
     *
     * @param patientId      the ID of the patient
     * @param outputStrategy the strategy used to output generated data
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {

        try {

            if (alertStates[patientId]) {

                if (RANDOM_GENERATOR.nextDouble() < RESOLVE_PROBABILITY) {
                    alertStates[patientId] = false;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }

            } else {
                // Lambda variable since ALERT_RATE provides the number
                // Renamed variable from "p" to "probability" to avoid confusion
                double probability = -Math.expm1(-ALERT_RATE); // Probability of at least one alert in the period
                boolean alertTriggered = RANDOM_GENERATOR.nextDouble() < probability;

                if (alertTriggered) {
                    alertStates[patientId] = true;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }

        } catch (Exception e) {
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            // Deleted printStackTrace() since it is not needed
        }
    }
}
