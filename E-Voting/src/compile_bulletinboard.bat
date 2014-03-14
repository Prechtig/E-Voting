if not exist "org\evoting\bulletinboard\jolie\lib" mkdir "org\evoting\bulletinboard\jolie\lib"

javac -cp ..\lib\jolie.jar -d ..\bin org\evoting\bulletinboard\Controller.java

jar cvf org\evoting\bulletinboard\jolie\lib\BulletinBoard.jar  -C ..\bin org\evoting\bulletinboard\Controller.class