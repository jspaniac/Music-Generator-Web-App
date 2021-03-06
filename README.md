# Music-Generator-Web-App
A [front end application](https://arxiv.org/abs/2010.04671) that uses a BNF music grammar to randomly generate and play music for users 
(note that since this is an active assignment being used in the 14X series that GrammarSolver is provided in a pre-compiled form; additionally, this application was developed to work on an a platform being used in the 14X series rather than locally)
# Spoofy
Spoofy uses the CSE 143 [GrammarSolver](https://courses.cs.washington.edu/courses/cse143/21su/handouts/a4.pdf)
assignment with the music.txt grammar to play randomly generated music right in your browser!

## Usage
To see the webpage, run the server in terminal/console:
```bash
java MusicGeneratorServer.java
```

Once you load up the webpage, there are four buttons to choose from. You can hover your mouse over each to see what function they provide!
After you've jammed out, head back to the ed workspace and in the console hit Ctrl-C to quit the app!

## How it works
Hopefully at this point you're already familiar with the GrammarSolver assignment.
To get this web app working, there are only two more Java files being used: one that uses GrammarSolver to generate midi files using the music.txt grammar, and one that parses and responds to client requests in the form of a server.

### MusicGenerator.java
This class uses the external JFugue / Staccato libraries to save the sounds generated by our grammar in a file with a .midi extension.

### MusicGeneratorServer.java
This class handles client requests from the actual webpage and does the corresponding action. You'll notice that there are only
three requests in the main method: '/', '/play', '/generate'. The first will simply serve the client the webpage, which can be
found under index.html. The second will simply send the current .midi file to the webpage so that it can be played for the client.
The third option simply generates a new music file, overwriting the previous one under midi/saved.mid. If you want to try to understand
the code a bit better, see if you cant get the program to serve one of the other premade midi files!

[Here's the link for the SIGCSE submission demonstrating the simplicity of developing similar web applications for other introductory courses](https://arxiv.org/abs/2010.04671)
