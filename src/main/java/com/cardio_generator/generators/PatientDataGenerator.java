package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Defines a contract for generating patient data.
 */
public interface PatientDataGenerator {

    /**
     * Generates data for a given patient.
     *
     * @param patientId identifier of a patient.
     * @param outputStrategy the strategy used to handle generated data.
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
