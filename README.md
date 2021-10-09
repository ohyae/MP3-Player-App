# MP3-Player-App
A MP3 streaming application for Android designed to play songs from a remote server without having to download the songs.

The app contacts the CTower (the remote Web server) by using a token. The application will request from CTower to send information about a song. In case CTower identifies the user from his/her token, it will randomly select one song from its database and will send its information (title, artist, and URL) to the application via an XML response. Then, the application parses this response and play this song for the user.

This code includes:

1. Jsoup for parsing an XML file on a new thread
2. android.media.MediaPlayer
3. Progress bar while the music is playing
4. Play, Pause, and Next Buttons

Note 1: All songs returned by CTower are compressed according to the MP3 standard.

Note 2: The song must be played from its remote location (i.e., from the value that is specified in the <url> tag).

Note 3: This app is a coursework assignment. It queries a server that is not personally maintained by me and thus, may not work anymore if the webmaster decides to deactivate this service.

![mp3](https://user-images.githubusercontent.com/66119148/136571762-81cc646f-72c7-400a-9485-279d22da27b9.JPG)
