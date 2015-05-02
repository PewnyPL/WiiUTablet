# WiiUTablet

A HTML5 + Java app that allows the Wii U Gamepad to become a PC drawing tablet.
The Java part uses http://java-websocket.org implementation of websocket protocol.

To test this project you must change a few things in the source:
* **host** variable inside WiiUTablet.js of the HTML5 part, so the IP address points to the PC the Java server runs on
* **static int resX** and **static int resY** variables in Main.java of the Java part, so they correspond to your screen resolution