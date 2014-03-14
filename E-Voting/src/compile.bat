if not exist "F:\Documents\Git\E-Voting\E-Voting\bin\org\evoting\client\lib" mkdir "F:\Documents\Git\E-Voting\E-Voting\bin\org\evoting\client\lib"

javac -cp ..\lib\jolie.jar -d ..\bin org\evoting\client\ConsoleIO.java org\evoting\client\Controller.java org\evoting\client\UserInputData.java org\evoting\client\Model.java org\evoting\client\Ballot.java

jar cvf ..\bin\org\evoting\client\lib\ConsoleIO.jar  -C ..\bin org\evoting\client\ConsoleIO.class -C ..\bin org\evoting\client\Controller.class -C ..\bin org\evoting\client\UserInputData.class -C ..\bin org\evoting\client\Model.class -C ..\bin org\evoting\client\Ballot.class