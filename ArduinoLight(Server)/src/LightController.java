import java.net.HttpURLConnection;
import java.net.URL;

public class LightController {
    public void sendGetToLights(String ip, int status) throws Exception {
        String lightUrl = "";
        if (status == 1 && ip != null) {
            lightUrl = "http://" + ip + "/ledBody?led=led_on";
        } else if (status == 0 && ip != null) {
            lightUrl = "http://" + ip + "/ledBody?led=led_off";
        }

        URL obj = new URL(lightUrl);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        try {
            con.getResponseCode();
        } catch (Exception ex) {
            System.out.println("Something went wrong");
        }
    }

    public String getIpViaLight(String light) {
        switch (light) {
            case "light1":
                return "192.168.9.7";//Galle 1
            case "light2":
                return "192.168.9.8";//Galle 2
            default:
                return null;
        }
    }
}
