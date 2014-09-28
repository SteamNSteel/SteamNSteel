##Steam and Steel
**CURRENTLY INDEVELOPMENT**

*Note: This readme was based on [pahimar's version](https://github.com/pahimar/Equivalent-Exchange-3/blob/master/README.md).*

[Setting up the development environment](#setting-up-the-development-environment)

[Compiling Steam and Steel](#compile-this-project)

[Contributing](#contributing)

[Licensing](#licensing)

###Setting up the development environment
***
[Set Up Java](#set-up-java)

[Set Up Git](#set-up-git)

[Set Up This Project](#set-up-this-project)

[Compile This Project](#compile-this-project)

[Update Your Repository](#update-your-repository)

####Set Up Java
The Java JDK is used to compile this project.

1. Download and install the Java JDK.
	* [Windows/Mac download link](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html).  Scroll down, accept the `Oracle Binary Code License Agreement for Java SE`, and download it (if you have a 64-bit OS, please download the 64-bit version).
	* Linux: Installation methods for certain popular flavors of Linux are listed below.  If your distribution is not listed, follow the instructions specific to your package manager or install it manually [here](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html).
		* Gentoo: `emerge dev-java/oracle-jdk-bin`
		* Archlinux: `pacman -S jdk7-openjdk`
		* Ubuntu/Debian: `apt-get install openjdk-7-jdk`
		* Fedora: `yum install java-1.7.0-openjdk`
2. Windows: Set environment variables for the JDK.
    * Go to `Control Panel\System and Security\System`, and click on `Advanced System Settings` on the left-hand side.
    * Click on `Environment Variables`.
    * Under `System Variables`, click `New`.
    * For `Variable Name`, input `JAVA_HOME`.
    * For `Variable Value`, input something similar to `C:\Program Files\Java\jdk1.7.0_51` EXACTLY AS SHOWN (*or wherever your Java JDK installation is*), and click `Ok`.
    * Scroll down to a variable named `Path`, and double-click on it.
    * Append `;%JAVA_HOME%\bin` EXACTLY AS SHOWN and click `Ok`.  Make sure the location is correct; double-check just to make sure.
3. Open up your command line and run `javac`.  If it spews out a bunch of possible options and the usage, then you're good to go.

####Set Up Git
Git is used to clone this repository and update your local copy.

1. Download and install Git [here](http://git-scm.com/download/).
	* *Optional*: Download and install a Git GUI client, such as SourceTree, Github for Windows/Mac, TortoiseGit, etc.  A nice list is available [here](http://git-scm.com/downloads/guis).

####Set Up This Project
This section assumes that you're using the command-line version of Git.

1. Open up your command line.
2. Navigate to a place where you want to download this project (eg `C:\Github\Development\`) by executing `cd [folder location]`.  This location is known as `mcdev` from now on.
3. Execute `git clone https://github.com/SteamNSteel/SteamNSteel.git`.  This will download this project into a folder in `mcdev`.
4. Right now, you should have a directory that looks something like:

***
	mcdev
	\-SteamNSteel
		\-This project's files (should have `build.gradle`, etc.)
***

####Compile This Project
1. Execute `gradlew build`. If you did everything right, `BUILD SUCCESSFUL` will be displayed after it finishes.
    * If you see `BUILD FAILED`, check the error output (it should be right around `BUILD FAILED`), fix everything (if possible), and try again.
3. Navigate to `mcdev\SteamNSteel\build\libs`.
    *  You should see a `.jar` file named `[A.A.AA]steamnsteel-B.B.jar`, where A.A.AA is the `minecraft_version` value in `build.properties` and B.B is the `mod_version` value in `build.properties`. This is the mod file to be used with Minecraft.
    *  Additionally, you should see two more `.jar` files named `[A.A.AA]steamnsteel-deobf-B.B.jar` and `[A.A.AA]steamnsteel-deobf-B.B-src.jar`. These are, respectively, the development modfile and the source code. The development mod file can be used when testing mods that are under development.
4. Copy the first jar into your Minecraft mods folder, and you are done!

####Update Your Repository
In order to get the most up-to-date builds, you'll have to periodically update your local repository and recompile this project.

1. Open up your command line.
2. Navigate to `mcdev/steamnsteel` in the console.
3. Make sure you have not made any changes to the local repository, or else there might be issues with Git.
	* If you have, try reverting them to the status that they were when you last updated your repository.
4. Execute `git pull master`.  This pulls all commits from the official repository that do not yet exist on your local repository and updates it.

###Contributing
***
####Submitting A Pull Request
So you found a bug in this project's code?  Think you can make it more efficient?  Want to help in general?  Great!

1. If you haven't already, create a Github account.
2. Click the `Fork` icon located at the top-right of this page (below your username).
3. Switch to the `develop` branch.
3. Make the changes that you want to and commit them.
	* If you're making changes locally, you'll have to execute `git commit -a` and `git push` in your command line.
4. Click `Pull Request` at the right-hand side of the gray bar directly below your fork's name.
5. Click `Click to create a pull request for this comparison`, enter your pull request's title, and create a detailed description telling us what you improved.
6. Click `Send pull request`, and wait for feedback!

####Creating an Issue
this mod crashes every time?  Have a suggestion?  Found a bug?  Create an issue now!

1. Make sure your issue hasn't already been answered or fixed.  Also think about whether your issue is a valid one before submitting it.
2. Go to [the issues page](http://github.com/SteamNSteel/SteamNSteel/issues).
3. Click `New Issue` right below `Star` and `Fork`.
4. Enter your Issue's title (something that summarizes your issue), and then create a detailed description ("Hey, could you add/change xxx?" or "Hey, I found an exploit.", etc.).
5. Click `Submit new issue`, and wait for feedback!

### Licensing

- Source code Copyright &copy; 2014 Rosie Alexander and Scott Killen.

  ![GPL3](https://www.gnu.org/graphics/lgplv3-147x51.png)

  This program is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses>.

- Art assets, model objects, localizations and other non-source code are licensed under a [Creative Commons Attributions 4.0 Internationla License](http://creativecommons.org/licenses/by/4.0/).

  [![CC BY 4.0](https://i.creativecommons.org/l/by/4.0/88x31.png)](http://creativecommons.org/licenses/by/4.0/)
