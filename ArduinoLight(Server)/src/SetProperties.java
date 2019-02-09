public class SetProperties {
    private LightSensorController lightSensorController = new LightSensorController();

    public SetProperties(){
        for (int i = 0; i < 2; i++) {
            try {
                lightSensorController.sendGetToLights(MacIpTable.getIpOfLight("light" + (i + 1)), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < 3; i++) {
            try {
                lightSensorController.setProperties(MacIpTable.getIpOfSensor("sensor" + (i + 1)),30,300);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
