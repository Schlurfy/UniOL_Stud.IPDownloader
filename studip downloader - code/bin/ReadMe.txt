Compilieren des Programms:
Windows: javac -cp ".;libs/*" Main.java view/OverviewController.java
Unix: javac -cp ".:libs/*" Main.java view/OverviewController.java

Ausführen des Programms:
Windows: java -cp ".;libs/*" Main
Unix: java -cp ".:libs/*" Main


Jar erstellen (nach dem ausfürhen von javac):
jar -cvfm JarFile/StudIPDownloader.jar add_manifest.MF Main.class Main.java build.fxbuild GUI/ model/ service/ view/ libs/

Jar ausführen:
java -jar JarFile/StudIPDownloader.jar