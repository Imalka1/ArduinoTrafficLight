package mac_ip;

import components.Sensor;

import java.util.ArrayList;

public class NodemcuTable {
    private static final int SENSORS_COUNT = 3;
    private static ArrayList<Sensor> sensors = new ArrayList<>();

    public NodemcuTable() {
        setSensor("sensor1", "seg1", "192.168.1.21", "errorFound");
        setSensor("sensor2", "seg1", "192.168.1.22", "errorFound");
        setSensor("sensor3", "seg1", "192.168.1.23", "errorFound");
    }

    private void setSensor(String name, String segment, String ip, String error) {
        Sensor sensor = new Sensor();
        sensor.setName(name);
        sensor.setSegment(segment);
        sensor.setIp(ip);
        sensor.setError(error);
        sensors.add(sensor);
    }

    public static String getIpOfLight(String light) {
        switch (light) {
            case "light1":
                return "192.168.1.7";
            case "light2":
                return "192.168.1.8";
            default:
                return null;
        }
    }

    public static ArrayList<Sensor> getSensors() {
        return sensors;
    }
    //    public static String getIpOfSensor(String sensor) {
//        switch (sensor) {
//            case "sensor1":
//                return "192.168.9.21";
//            case "sensor2":
//                return "192.168.9.22";
//            case "sensor3":
//                return "192.168.9.23";
//            default:
//                return null;
//        }
//    }

    public static Sensor getSensorViaMac(String mac) {
        switch (mac) {
            case "80:7D:3A:3E:18:AB":
                return sensors.get(0);
            case "3C:71:BF:20:08:9D":
                return sensors.get(1);
            case "80:7D:3A:3E:00:FF":
                return sensors.get(2);
            default:
                return null;
        }
    }

    public static int getSensorsCount() {
        return SENSORS_COUNT;
    }

    public static int getLightsCount() {
        return 2;
    }

    public static int getDistance() {
        return 10;
    }

    public static int getDelayTime() {
        return 250;
    }

    public static int[] getSegmentSensorsCount() {
        return new int[]{3};
    }
}
