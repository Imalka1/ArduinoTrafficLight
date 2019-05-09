package socket_controller;

import controller.LightSensorController;
import mac_ip.NodemcuTable;

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
        for (int i = 0; i < NodemcuTable.getLightsCount(); i++) {
            try {
                lightSensorController.sendGetToLights(NodemcuTable.getIpOfLight("light" + (i + 1)), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < NodemcuTable.getSensorsCount(); i++) {
            try {
                lightSensorController.setProperties(new NodemcuTable().getSensors().get(i).getIp(), NodemcuTable.getDistance(), NodemcuTable.getDelayTime());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
