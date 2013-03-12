This Android application allows an Android phone or tablet to function as the brain of your robot and connect to the Ardrobot Server and Ardrobot Arduino components of the system.

## Ardrobot Android Device Setup

1. Setup OpenVPN for Android 
    - Install [OpenVPN for Android](https://play.google.com/store/apps/details?id=de.blinkt.openvpn&hl=en) on your Android device
    - Download the [Ardrobot OpenVPN development client](https://raw.github.com/ardrobot/ArdrobotServer/master/ardrobot.ovpn) configuration file to your Android device -- or download to your computer, email it to yourself, and save the attachment on your Android device.  Only use these keys for local development.
    - Open OpenVPN for Android on your Android device and click the "Import" button in the upper right corner
    - Select the ardrobot.ovpn file, click "Select" and then click "Import"
    - Click on the settings icon to the right of the newly imported ardrobot profile.
    - Click "Basic", click on Server Address, and replace the IP address with the address of your Ardrobot Server.  (If you don't have a server setup yet, you can find the [Ardrobot Server setup instructions here](https://github.com/ardrobot/ArdrobotServer#ardrobot-server-setup).)
    - Click the back arrow two times
2. Start OpenVPN for Android
    - Click the "ardrobot" link in OpenVPN for Android to start the connection.
    - You should see a number of connection handshake messages followed by a key in the upper left of your screen.
    - If that key disappears, reopen the OpenVPN for Android app and repeat step 2.
3. Install the latest [Ardrobot Android app](https://www.dropbox.com/s/32a1cgt4a677dxo/ArdrobotAndroid.apk) on your Android device
4. The Ardrobot Android app should now automatically start when you connect an Android device running the Ardrobot Arduino client.  When the app launches, it will automatically connect to both the Arduino and the Ardrobot Server via the OpenVPN connection you established above.
5. See the [Ardrobot Server instructions on using the Pilot Interface](https://github.com/ardrobot/ArdrobotServer#ardrobot-server-pilot-interface-vnc) over VNC to view the video feed from your Android device and pass along instructions to the Arduino.

(**NOTE**: *Due to the requirements for OpenVPN, the Android device you use must be capable of running Android verion 4.0.3 or higher.  We are working on integrating OpenVPN directly into the Ardrobot Android app and will do our best to make it work on older devices.*)


## Ardrobot Android Development Setup

*Coming soon*


## TODO Items

Visit issues for this project...


## LICENSE
This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
