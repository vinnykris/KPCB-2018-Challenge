# My KPCB 2017 Challenge Solution

## About
My name is Vinod Krishnamurthy, and this is my implementation of a fixed size hash map for KPCB's
2017 Challenge for the Engineering Fellowship. I did a lot of research prior to beginning my 
implementation, and I learned that binary search trees would be more efficient in dealing with
collisions than linked lists. I structed my implementation as an array of Nodes, each of which is
the root node of an AVL Tree. 

The runtime for basic hash map functions such as search, insertion, and deletion averages to O(1).
There are some situations in which those functions will take O(log n) time, but that will only be the
case when collisions occur. If the user sets the size of the hash map appropriately for the intended
usage, there should be few collisions, if any, and the runtime should approximate to O(1).

I used Gradle to compile my program because that is the easiest way to test and build the project. I 
have included a Gradle wrapper in the project package, so it is not necessary to have Gradle
downloaded on your system.

## How to Run the Program
1. Download the zip file that contains the submitted files, and unzip it.

2. Using the command line, navigate to the project folder. It should be named 'vinod_krishnamurthy_KPCB2017'.

3. Once in the folder, run the command './gradlew build' to easily compile and test the project.

4. Now that the program has compiled, run the command 'cd build/libs' which should take you
   to the location of the executable file.

5. To run the executable file, run the command 'java -jar FixedSizeHashMap-1.0.jar'. Follow the
   instructions from there.

