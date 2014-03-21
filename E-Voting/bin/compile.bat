<<<<<<< HEAD
if not exist "F:\Documents\Git\E-Voting\E-Voting\bin\org\evoting\client\lib" mkdir "F:\Documents\Git\E-Voting\E-Voting\bin\org\evoting\client\lib"

javac -cp ..\lib\jolie.jar -d ..\bin org\evoting\client\ConsoleIO.java org\evoting\client\Controller.java org\evoting\client\UserInputData.java org\evoting\client\Model.java org\evoting\client\Ballot.java

jar cvf ..\bin\org\evoting\client\lib\ConsoleIO.jar  -C ..\bin org\evoting\client\ConsoleIO.class -C ..\bin org\evoting\client\Controller.class -C ..\bin org\evoting\client\UserInputData.class -C ..\bin org\evoting\client\Model.class -C ..\bin org\evoting\client\Ballot.class
=======
if not exist "org\evoting\client\jolie\lib" mkdir "org\evoting\client\jolie\lib"

javac -cp ..\lib\jolie.jar -d ..\bin org\evoting\client\ConsoleIO.java org\evoting\client\Controller.java org\evoting\client\UserInputData.java org\evoting\client\Model.java org\evoting\client\Ballot.java org\evoting\client\Security.java org\evoting\client\exceptions\NoCandidateListException.java 
>>>>>>> d3a4318deb93985f479a6dc96b18b7c5b87b189e

jar cvf org\evoting\client\jolie\lib\ConsoleIO.jar  -C ..\bin org\evoting\client\ConsoleIO.class -C ..\bin org\evoting\client\Controller.class -C ..\bin org\evoting\client\UserInputData.class -C ..\bin org\evoting\client\Model.class -C ..\bin org\evoting\client\Ballot.class -C ..\bin org\evoting\client\Security.class -C ..\bin org\evoting\client\exceptions\NoCandidateListException.class
