# Environment Configuration

## **DO NOT USE AGAINST LIVE TARGETS!**

**I cannot stress this enough. These files are out in the open to learn from and do research on. I take no responsbility if you use these files against actual production systems. Remember, it is illegal to use these against live targets that are not your property, and it's illegal to even launch them remotely on your own VMs hosted in the cloud in some cases, as this could cause IDS / IPS systems to trip.**

## Existing Desktop or Virtual Machine?

I HIGHLY recommended that you do all of your investigation, research, debugging, and running of the following software packages and source inside of a Virtual Machine (VM). When testing exploits on target hosts, I also recommend switching them to an _INTERNAL_ virtual network switch, so that traffic can only go between Client / Target testing guests.

I'm testing the following code inside of Hyper-V with the following guest configurations:

* Windows 10 Client - Used for source exploration, debugging, and launching of exploits (only connects to an external network to download updates and any packages needed to debug...other than that, this stays on an internal network)
* Linux Mint 18 - Used for debugging and launching of exploits (only connects to an external network to download updates and any packages needed to debug...other than that, this stays on an internal network)
* Windows Server 2012 R2 - Used as an exploit target for testing (connects to external network immediately after install for updates and patching, but stays on internal network afterward)
* Windows Server 2008 R2 - Used as an exploit target for testing (connects to external network immediately after install for updates and patching, but stays on internal network afterward)

_Note: More evnironments to be added as time and investigation proceeds_

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
        * _Note: Currently this directory does not exist, but there is a java-j2se_1.6-sun in the parent_
        * For more information, see: http://stackoverflow.com/a/23217319
    * Passes any additional arguments carried forward from start_lp.py

* Start.jar - DanderSpritz Operation Center (entry point to launch DanderSpritz implementation)
  * Agruments for main(String[] args)
    * -core [debug|release]
      * Given one of the provided values, will swtich between debug and release modes <which does what exactly?>
    * -gui [debug|release]
      * Given one of the provided values, will swtich between debug and release modes <which does what exactly?>
    * -debug
    * -release
    * -local
    * -previous
    * -live
    * -replay
    * -opsdisk
    * -resource
    * -log
    * -config
    * -load

  * Reads from user.defaults and start.properties

# Configuration Files and Values

## user.defaults

* OpsDisk - This should be a directory. _Default: Current Working Directory_
* ResourceDir - Path to the /Resources/ directory
* LogDir - Path to the /Logs/ directory
* ConfigDir - Path to the /UserConfiguration/ directory
* live.DSZ_KEYWORD
* replay.DSZ_KEYWORD
* OpMode
* BuildType
* GuiType
* LocalMode
* LoadPrevious
* thread.dump
* wait.for.output

## start.properties

* res.dir = 
* java.exe
* vmargs
* vmargs.debug
* windows.start
* windows.tool.chain
* linux.tool.chain
* live.operation
* replay.operation

* show.optype
* show.debug.core
* show.debug.gui
* show.local.mode
* show.thread.dump

* label.resource
* label.log
* label.config
* label.disk
* label.browse
* label.start
* label.comms
* label.opMode
* label.live
* label.replay
* label.options
* label.loadPrevious
* label.localMode
* label.core
* label.debug
* label.release
* label.gui

* tooltip.resource
* tooltip.log
* tooltip.config
* tooltip.disk
* tooltip.resource.browse
* tooltip.log.browse
* tooltip.config.browse
* tooltip.disk.browse
* tooltip.start
* tooltip.live
* tooltip.replay
* tooltip.loadPrevious
* tooltip.localMode
* tooltip.debug
* tooltip.release

# TODO

* Figure out what the OpsDisk parameter is in user.defaults
