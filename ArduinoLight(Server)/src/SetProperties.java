import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/setProperties")
public class SetProperties extends HttpServlet {
    private LightSensorController lightSensorController = new LightSensorController();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setProperties();
    }

    public void setProperties() {
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
