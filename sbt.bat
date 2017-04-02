set SCRIPT_DIR=%~dp0
java -Xms512M -Xmx1536M -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=256M -Dsbt.ivy.home=ivy2/ -Divy.home=ivy2/ -jar "%SCRIPT_DIR%sbt-launch.jar" %*