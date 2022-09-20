import java.util.Iterator;
import java.util.Objects;

public class Hospital1 extends HospitalBase {

    /** Array of all available timeslots */
    private PatientBase[] appointments;

    /** Hospital working times and break times with 20min appointment slots */
    private String startTime = "08:00";
    private String endTime = "17:40";
    private String breakStart = "11:40";
    private String breakEnd = "13:00";
    private int numPossibleTimeSlots;

    private int appointmentLength = 20;

    public Hospital1() {
        /* Add your code here! */
        this.numPossibleTimeSlots = numTimeSlots();
        this.appointments = new Patient[numPossibleTimeSlots];
    }

    @Override
    public boolean addPatient(PatientBase patient) {
        /* Add your code here! */
        // Check time given is within working hours of hospital
        if (!Patient.validTime(startTime, endTime, breakStart, breakEnd, patient.getTime())) {
            return false;
        }

        // Check time is a multiple of the appointment length
        if (!validAppointmentTime(patient.getTime())) {
            return false;
        }

        // Check if there exists a Patient already at the index for the time specified
        if (appointments[timeToIndex(patient.getTime())] != null) {
            return false;
        }
        // time slot is free
        appointments[timeToIndex(patient.getTime())] = patient;
        return true;
    }

    @Override
    public Iterator<PatientBase> iterator() {
        /* Add your code here! */

        return new Iterator<PatientBase>() {
            int currentIndex = 0;
            @Override
            public boolean hasNext() {
                for (int i = currentIndex; i < numPossibleTimeSlots; i++) {
                    if (appointments[i] != null) { // found next patient index
                        currentIndex = i;
                        return true;
                    }
                }
                // iterated through entire array and didnt find another patient
                return false;
            }

            @Override
            public PatientBase next() {
                return appointments[currentIndex++];
            }
        };
    }

    /* Add any extra functions below */


    /** Calculates the number of 20 min time slots available accounting for the lunch break. */
    public int numTimeSlots() {
        return 1 + (toMinuteOfDay(endTime) - toMinuteOfDay(startTime)) / appointmentLength;
    }

    /** Converts a time string to an index */
    public int timeToIndex(String time) {
        return (toMinuteOfDay(time) - toMinuteOfDay(startTime)) / appointmentLength;
    }

    /** Converts a time string to the minutes in a day */
    public int toMinuteOfDay(String time) {
        int hours = Integer.parseInt(time.split(":")[0]);
        int mins = Integer.parseInt(time.split(":")[1]);
        return 60 * hours + mins;
    }

    /** Check the requested time is a multiple of the appointment time */
    public boolean validAppointmentTime(String time) {
        return toMinuteOfDay(time) % appointmentLength == 0;
    }
}
