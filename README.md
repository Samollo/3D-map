# Diderot-map
Diderot map uses a 2D Grid and height map algorithms to generates landforms, sea and mountains with a 3D feeling.
Authors: Samy Metadjer & Mohammed Ansari

# Installation and configuration
To install, make sure you are in diderot-map/src and run:
```
javac *.java
```

# Run
And to launch it, simply:
```
java Bigbang
```

# TODO
Project isn't done yet. THere's several issues to fix or features to implement, such as:

- Add a config file allowing user to set the camera initial position and the grid's size.
- Add a command line option to set the camera initial position and the grid's size, if user do not want to use config file.
- Implement an option window to reset the map with new values.
- Refactoring of the code to make sure there's no dead code and/or performance issues with our algorithms.
- Enhance fluidity of the camera movement.
- Fix point projection issue. 
- Fix rotation issue. "S" button should move the map down. Actually it goes down with a little diagonal trajectory.
- Add sun and sunbeams.
- Put the project into a container so anyone can run it easily with Docker for example.

