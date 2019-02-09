public class MacIpTable {
    public static String getIpOfLight(String light) {
        switch (light) {
            case "light1":
                return "192.168.9.7";
            case "light2":
                return "192.168.9.8";
            default:
                return null;
        }
    }

    public static String getIpOfSensor(String sensor) {
        switch (sensor) {
            case "sensor1":
                return "192.168.9.21";
            case "sensor2":
                return "192.168.9.22";
            case "sensor3":
                return "192.168.9.23";
            default:
                return null;
        }
    }

    public static String getSensorViaMac(String mac) {
        switch (mac) {
            case "80:7D:3A:3E:18:AB":
                return "sensor1";
            case "3C:71:BF:20:08:9D":
                return "sensor2";
            case "80:7D:3A:3E:00:FF":
                return "sensor3";
            default:
                return "";
        }
    }
}
