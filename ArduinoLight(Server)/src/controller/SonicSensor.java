package controller;

import components.Sensor;
import mac_ip.NodemcuTable;
import service.SonicSensorOperator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet(urlPatterns = "/sonicDistance")
public class SonicSensor extends HttpServlet {

    private SonicSensorOperator sonicSensorOperator;
    private NodemcuTable nodemcuTable;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("req " + req.getParameter("wifiStatus"));
        sonicSensorOperator = new SonicSensorOperator();
        nodemcuTable = new NodemcuTable();
        if (req.getParameter("wifiStatus") != null) {
            Sensor sensor = nodemcuTable.getSensorViaMac(req.getParameter("mac"));
            sensor.setError("Not found(OK)");
            String message = "{\"sensor\":\"" + sensor.getName().substring(6) + "\",\"segment\":\"" + sensor.getSegment().substring(3) + "\",\"errorFound\":\"" + sensor.getError() + "\"}";
            sonicSensorOperator.broadcast(message);
        } else {
            String distance = req.getParameter("distance");
            Sensor sensor = nodemcuTable.getSensorViaMac(req.getParameter("mac"));
            switch (sensor.getName()) {
                case "sensor1":
                    sonicSensorOperator.switchLights(NodemcuTable.getIpOfLight("light0"), NodemcuTable.getIpOfLight("light1"), distance, 0);
                    break;
                case "sensor2":
                    sonicSensorOperator.switchLights(NodemcuTable.getIpOfLight("light1"), NodemcuTable.getIpOfLight("light2"), distance, 1);
                    break;
                case "sensor3":
                    sonicSensorOperator.switchLights(NodemcuTable.getIpOfLight("light2"), NodemcuTable.getIpOfLight("light3"), distance, 2);
                    break;
            }
        }
    }
}
