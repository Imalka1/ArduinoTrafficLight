public class SetLightsOff {
    private LightController lightController = new LightController();

    public SetLightsOff() {
        for (int i = 0; i < 2; i++) {
            try {
//                lightController.sendGetToLights(lightController.getIpViaLight("light" + (i + 1)), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
