# Dynamic Media BT Router
A simple background service which detects any playback on the global audiomix and then opens the BluetoothSco channel so the audio is played on your mono Bluetooth device (hands free profile).

### Idea
Enable the _dynamic_ playback of any media sound on the Bluetooth device. Especially for the direction guide of your navigation software.
The Bluetooth channel should be opened when a playback is started and closed after the playback is finished.

### Why another app? There a several that do the same!
No, not exactly! All other apps I've seen are not dynamic or (I suppose) use the isMusicActive() function which in fact isn't reliable.
Therefore a use the Visualizer API to grab anonymized info from the global audiomix and can for sure determine if audio is played.
The visualizer API function I use is only available since API Level 19 (KITKAT). For API Levels below I use the isMusicActive() function as well...

### State based redirection
The service is only startable if the bluetooth adapter is turned on, everything else wouldn't make sense would it?
The redirection only starts if a bluetooth device is connected to the handsfree bluetooth profile. If the bluetooth profile isn't available anymore the redirection stops.

### Intent to start/stop the service
It's really really simple!
Send of the following intents
- net.philipp_koch.dynamicmediabtrouter.ON | to start the service
- net.philipp_koch.dynamicmediabtrouter.OFF | to stop the service

## What’s left to do?
Nearly EVERYTHING! :(
- A fancy UI with current status of the service
- start on boot / on bluetooth device connected ...
- Adding a delay/pause while opening the BluethootSco -- currently audio is lost
