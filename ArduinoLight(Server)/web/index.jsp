<%--
  Created by IntelliJ IDEA.
  User: Imalka Gunawardana
  Date: 2019-02-05
  Time: 7:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="assets/font-awesome/latest/css/font-awesome.min.css">
    <title>$Title$</title>
</head>
<body class="">
<div class="row">
    <div class="col-12" style="margin-top: 50px">
        <div id="txtId" style="text-align: center;margin: auto;font-size: 32px"></div>
    </div>
</div>
<%--<div class="row" style="margin-bottom: 10px">--%>
<%--<div class="col-12">--%>
<%--<div style="text-align: center;margin: auto;font-size: 28px">(LEDs are switched on below 30cm)</div>--%>
<%--</div>--%>
<%--</div>--%>
<%--<div class="row" style="margin-top: 100px">--%>
<%--<div class="col-12">--%>
<%--<div style="width: 60%;text-align: center;margin: auto;background-color: #f9b94e;color: #635120;padding: 50px;font-size: 28px">--%>
<%--<div id="swCol" style="width: 50%;background-color: red;padding: 10px;margin: auto"></div>--%>
<%--<div id="lightId" style="margin-top: 10px"></div>--%>
<%--</div>--%>
<%--</div>--%>
<%--<div class="col-12">--%>

<%--</div>--%>
<%--</div>--%>
<div id="screenBody"></div>
<div class="row">
    <div class="col-sm-12" id="lightsText" style="text-align: center;margin-top: 50px">
        Sensors not ready
    </div>
</div>
<div class="row">
    <div class="col-sm-12">
        <button id="startBtn" class="btn btn-warning" style="position: relative;left: 50%;transform: translateX(-50%);margin-top: 50px">Start</button>
    </div>
</div>
<div class="row">
    <div class="col-sm-12" id="systemText" style="text-align: center;margin-top: 50px;color: red">
        System is not started
    </div>
</div>
</body>
<script src="assets/js/jquery-3.2.1.min.js"></script>
<script src="sensor_js/sessionController.js"></script>
</html>
