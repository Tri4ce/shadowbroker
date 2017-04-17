# Environment Configuration

## OS-neutral Steps

1. Install Python
2. Install Java
3. Install Git
4. Install Archiving utility that understands .jar files and other types (useful for digging into things)
5. Put Python, Git, and JDK in your PATH (if the installers didn't do so already)
6. git clone https://github.com/Tri4ce/shadowbroker.git

_Note: Before cloning, make sure there are NO SPACES IN YOUR DIRECTORY STRUCTURE! Some of the scripts check for spaces and will exit with an error if your directories contain any._

_Note: If you need to decompile any JAR files, http://www.javadecompilers.com works like a charm_

## Windows

1. Modify ...

## Linux

1. Modify ...

## MacOS

TODO: I don't currently own anything that can run MacOS, so please help fill this in if possible!

# Source Traversal thus-far

I have been tracing the source code paths and what links to what throughout the Python and Java files, here are my findings thus-far:

* start_lp.py -> configure_lp.py
  * Calls configure_lp.py, passing in the absolute path of the current script's directory, and then any additional arguments passed from the caller

* configure_lp.py -> Start.jar
  * Launches Start.jar in a Java VM with the following properties:
    * Enables JMX RMI connections using a random port between 25000 and 55000
    * Disables JMX RMI SSL
    * Disables JMX RMI Authenticaion
    * Starts with 20MB of memory, allowed up to 1GB
    * Tells the JVM to override endorsed java libs with those located in Resources/Dsz/Gui/Lib/endorsed
        * _Note: Currently this directory has no contents, but contains a sub-directory called java-j2se_1.6-sun which does...Need to do some more reading on com.endorsed.dirs to understand if it will read from said sub-directory_
        * For more information, see: http://stackoverflow.com/a/23217319
    * Passes any additional arguments carried forward from start_lp.py

* Start.jar
  * Reads from user.defaults and start.properties
  * 

