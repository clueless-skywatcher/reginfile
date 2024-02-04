rd /s /q .\executables

jpackage ^
--input .\reginfile-core\target ^
--dest .\executables ^
--name "ReginFile" ^
--main-jar reginfile.jar ^
--main-class com.github.cluelessskywatcher.reginfile.Reginfile ^
--icon ".\reginfile-core\target\classes\reginfile.ico" ^
--type msi