package Simulator;

import javafx.scene.shape.Mesh;

import java.util.ArrayList;
import java.util.List;

public class BufferPollution implements Buffer {
    ArrayList<Measurement> measurements;
    volatile ArrayList<Measurement> averages; // Ensure visibility, not mutual exclusion
    float overlapPerc = .5f;

    public BufferPollution(){
        measurements = new ArrayList<Measurement>();
        averages = new ArrayList<Measurement>();
    }

    @Override
    public void addMeasurement(Measurement m) {        
        measurements.add(m);

        if(measurements.size() == 8)
            ComputeAverage();
    }

    // Return the list of averages
    @Override
    public List<Measurement> readAllAndClean() {
        // List to be sent, copy the values not the ref
        List listCopy = new ArrayList<>();
        for(Measurement avg: averages){
            listCopy.add(new Measurement(
                    avg.getId(),
                    avg.getType(),
                    avg.getValue(),
                    avg.getTimestamp()
            ));
        }

        // Clear the list of averages
        averages.clear();

        return listCopy;
    }

    private void ComputeAverage(){
        double avg = 0;

        // Compute average
        for (Measurement m: measurements){
            avg += m.getValue();
        }

        averages.add(new Measurement(
                measurements.get(measurements.size()-1).getId(),
                measurements.get(measurements.size()-1).getType(),
                avg,
                measurements.get(measurements.size()-1).getTimestamp()
        ));

        FreeSpace();
    }

    // Free space based on sliding window of 8 mes and 50%
    private synchronized void FreeSpace(){
        for(int i = 0; i < measurements.size() * overlapPerc; i++){
            measurements.remove(0);
        }
    }
}
