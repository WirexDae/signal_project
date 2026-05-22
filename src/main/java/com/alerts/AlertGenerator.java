package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.*;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {

    private DataStorage dataStorage;

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method should define the specific conditions under which an
     * alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {

        if (patient == null) return;

        List<PatientRecord> records =
                patient.getRecords(0, Long.MAX_VALUE);

        checkBloodPressure(patient, records);
        checkSpO2(patient, records);
        checkECG(patient, records);
        checkCombined(patient, records);
    }

    /**
     * Checks blood pressure for: the critical thresholds and
     * trend patterns (3 consecutive changes bigger than 10 mmHg)
     *
     * @param patient the patient whos data will be analyzed
     * @param records the data record of the patient
     */
    private void checkBloodPressure(Patient patient, List<PatientRecord> records) {

        List<Double> bpValues = new ArrayList<>();
        List<Long> timestamps = new ArrayList<>();

        for (PatientRecord r : records) {
            if (r.getRecordType().equalsIgnoreCase("BloodPressure")) {
                bpValues.add(r.getMeasurementValue());
                timestamps.add(r.getTimestamp());

                double value = r.getMeasurementValue();

                // Critical thresholds
                if (value > 180 || value < 90) {
                    triggerAlert(patient, "Critical Blood Pressure", r.getTimestamp());
                }
            }
        }

        // Trend alert (3 consecutive bigger than 10)
        // checks the differences between 3 consecutive records and if they are both bigger than 10,
        // and same direction, alarm is triggered
        for (int i = 2; i < bpValues.size(); i++) {

            double diff1 = bpValues.get(i) - bpValues.get(i - 1);
            double diff2 = bpValues.get(i - 1) - bpValues.get(i - 2);

            if (Math.abs(diff1) > 10 && Math.abs(diff2) > 10) {

                boolean increasing = diff1 > 0 && diff2 > 0;
                boolean decreasing = diff1 < 0 && diff2 < 0;

                if (increasing || decreasing) {
                    triggerAlert(patient, "Blood Pressure Trend Alert", timestamps.get(i));
                }
            }
        }
    }

    /**
     * Checks SpO2 for low oxygen saturation and rapid drop
     *
     * @param patient the patient whos data will be analyzed
     * @param records the data record of the patient
     */
    private void checkSpO2(Patient patient, List<PatientRecord> records) {

        List<Double> spO2Values = new ArrayList<>();

        for (PatientRecord r : records) {

            if (r.getRecordType().equalsIgnoreCase("SpO2")) {

                double value = r.getMeasurementValue();
                spO2Values.add(value);

                // Low saturation
                if (value < 92) {
                    triggerAlert(patient, "Low Oxygen Saturation", r.getTimestamp());
                }

                // Rapid drop (if the consecutive drops are bigger than 5%)
                if (spO2Values.size() >= 2) {
                    double prev = spO2Values.get(spO2Values.size() - 2);

                    if (prev - value >= 5) {
                        triggerAlert(patient, "Rapid SpO2 Drop", r.getTimestamp());
                    }
                }
            }
        }
    }

    /**
     * Checks for the combined alert (hypotensive hypoxemia alert)
     * if both conditions are triggered, the alert is triggered
     *
     * @param patient the patient whos data will be analyzed
     * @param records the data record of the patient
     */
    private void checkCombined(Patient patient, List<PatientRecord> records) {

        boolean lowBP = false;
        boolean lowOxygen = false;

        for (PatientRecord r : records) {

            // checks both conditions one by one
            if (r.getRecordType().equalsIgnoreCase("BloodPressure")
                    && r.getMeasurementValue() < 90) {
                lowBP = true;
            }

            if (r.getRecordType().equalsIgnoreCase("SpO2")
                    && r.getMeasurementValue() < 92) {
                lowOxygen = true;
            }
        }

        if (lowBP && lowOxygen) {
            triggerAlert(patient, "Hypotensive Hypoxemia", System.currentTimeMillis());
        }
    }

    /**
     * Checks for abnormal ECG activity using the sliding window average.
     * We look at the last 5 ECG values.
     * If the list becomes bigger than 5 after new values, remove the oldes ones.
     * so the window constantly slides forward over time.
     * if the last value is much higher than last 5 average, the alarm is triggered.
     *
     * @param patient the patient whos data will be analyzed
     * @param records the data record of the patient
     */
    private void checkECG(Patient patient, List<PatientRecord> records) {

        List<Double> window = new ArrayList<>();
        int WINDOW_SIZE = 5;

        for (PatientRecord r : records) {

            if (r.getRecordType().equalsIgnoreCase("ECG")) {

                double value = r.getMeasurementValue();
                window.add(value);

                if (window.size() > WINDOW_SIZE) {
                    window.remove(0);
                }

                if (window.size() == WINDOW_SIZE) {

                    double avg = 0;
                    for (double v : window) avg += v;
                    avg /= WINDOW_SIZE;

                    if (value > avg * 1.5) {
                        triggerAlert(patient, "ECG Abnormal Spike", r.getTimestamp());
                    }
                }
            }
        }
    }

    /**
     * Creates and outputs an alert.
     *
     * @param patient patient whos alert is triggered
     * @param condition type of medical condition detected
     * @param timestamp time of detection
     */
    private void triggerAlert(Patient patient, String condition, long timestamp) {

        Alert alert = new Alert(
                String.valueOf(patient.getPatientId()),
                condition,
                timestamp
        );

        System.out.println(
                "[ALERT] " + alert.getPatientId() +
                        " | " + alert.getCondition() +
                        " | " + alert.getTimestamp()
        );
    }
}