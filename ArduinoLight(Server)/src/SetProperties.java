public class SetProperties {
    private LightSensorController lightSensorController = new LightSensorController();

    public SetProperties() {
        for (int i = 0; i < MacIpTable.getLightsCount(); i++) {
            try {
                lightSensorController.sendGetToLights(MacIpTable.getIpOfLight("light" + (i + 1)), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < MacIpTable.getSensorsCount(); i++) {
            try {
                lightSensorController.setProperties(MacIpTable.getIpOfSensor("sensor" + (i + 1)), MacIpTable.getDistance(), MacIpTable.getDelayTime());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
