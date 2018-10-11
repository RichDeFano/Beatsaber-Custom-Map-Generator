# Beatsaber-Custom-Map-Generator
A work in progress Machine Learning project for HackIowa 10/7/18. Intended to allow an AI to create maps for the game beatsaber based on the existing maps created by the community. So far the program is able to take in a source folder (should be the customSongs folder used in beatsaber, placed in this programs directory.) and is able to sort the song into two different data structures. It will also read data from the info.json file, which will be used in the creation

The input structure, which is the Root Means Squared of the amplitude of the sounds wave. This average is broken up at every .125 seconds.

The output structure, which is the .json file labeled "Expert.json". This will arrage the notes in ascending order, by the time they appear. The note is built up of 4 data points, Line Index, Line Layer, Type, and Cut Direction.

The program is not yet functioning with a neural network, although the process of collecting and entering the data is mainly complete.
