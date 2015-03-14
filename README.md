# Dynamic Media BT Router
A simple background service which detects any playback on the global audiomix and then opens the BluetoothSco channel so the audio is played on your mono Bluetooth device (hands free profile).

### Idea
Enable the _dynamic_ playback of any media sound on the Bluetooth device. Especially for the direction guide of your navigation software.
The Bluetooth channel should be opened when a playback is started and closed after the playback is finished.

### Why another app? There a several that do the same!
No, not exactly! All other apps I've seen are not dynamic or (I suppose) use the isMusicActive() function which in fact isn't reliable.
Therefore a use the Visualizer API to grab anonymized info from the global audiomix and can for sure determine if audio is played.
The visualizer API function I use is only available since API Level 19 (Kitkat - Android 4.4). For API Levels below I use the isMusicActive() function as well...

### State based redirection
The service is only startable if the bluetooth adapter is turned on, everything else wouldn't make sense would it?
The redirection only starts if a bluetooth device is connected to the hands free bluetooth profile. If the hands free connection isn't available anymore the redirection stops.

### Intent to start/stop the service
It's really really simple!
Send of the following intents
- net.philipp_koch.dynamicmediabtrouter.ON
- net.philipp_koch.dynamicmediabtrouter.OFF

### Requirements
- An Android device with the minimum API Level 11 (Honeycomb - Android 3.0)
- A bluetooth device which supports the hands free profile (usually headsets or car speakerphones)
- No task managers! They're crap and nothing else! They kill the background service so don't complain!

#### Working as designed
If you end the "call" the redirection doesn't stop and audio is routed to nowhere.
Because the app was intended for navigation apps this is a useful design:

If redirection starts and you are already aware of the information presented you could end the "call" as soon it starts.
After the information is played the service will close the channel as usual and open it again as soon as audio is detected.

If you use the app to redirect sound the whole time and you end the "call" accidentally you have to pause the music for at least a few seconds. The service will stop the redirection and start it again as you press play.

##### "_The sound is shitty fix that!!1!_"
I can't! The hands free profile isn't intended for high quality. Use A2DP instead!

## What’s left to do?
Nearly EVERYTHING! :(
- A fancy UI with current status of the service
- start on boot / on bluetooth device connected ...
- Adding a delay/pause while opening the bluetooth channel -- currently audio is lost
