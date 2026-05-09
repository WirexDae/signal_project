package com.cardio_generator.outputs;

/**
 * Defines a strategy for handling generated patient data output.
 */
public interface OutputStrategy {

    /**
     * Outputs a patient related data.
     *
     * @param patientId identifier of a patient.
     * @param timestamp the time at which the data was generated.
     * @param label a label indicating the type of data.
     * @param data the actual data value as a string.
     */
    void output(int patientId, long timestamp, String label, String data);
}