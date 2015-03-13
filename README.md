# Dynamic Media BT Router
A simple background service which detects any playback on the global audiomix and then opens the BluetoothSco channel so the audio is played on your mono Bluetooth device (hands free profile).

### Idea
Enable the _dynamic_ playback of any media sound on the Bluetooth device. Especially for the direction guide of your navigation software.
The Bluetooth channel should be opened when a playback is started and closed after the playback is finished.

### Why another app? There a several who do the same!
No, not exactly! All other apps I've seen are not dynamic or (I suppose) use the isMusicPlaying() function which in fact isn't reliable. 
Therefore a use the Visualizer API to grab anonymized info from the global audio mix and can for sure determine if music is played.

## What’s left to do?
Nearly EVERYTHING! :(
- A fancy UI
- An intent listener (as already written in the AndroidManifest)
- Adding a delay/pause -- currently audio is lost while the BluetoothSco is open
