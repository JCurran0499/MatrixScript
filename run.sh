#!/bin/bash

echo "================================================================================"
echo "|                                                                              |"
echo "|  |\   /|  / \ |_   _||  _|  | | \ \/ / / __|  / _| |  _|  | | | -  ||_   _|  |"
echo "|  | \ / | / _ \  | |  |   \  | |  |  |  \__ \ | |_  |   \  | | |  _/   | |    |"
echo "|  |_| |_|/_/ \_\ |_|  |_||_| |_| /_/\_\ |___/  \__| |_||_| |_| |_|     | |    |"
echo "|                                                                              |"
echo "================================================================================"
echo " "
echo " "
echo "1: Build app (maven)"
echo "2: Build app and run API locally"
echo "3: Run API locally (without a build)"
echo "4: Build app and run command line"
echo "5: Run command line (without a build)"
echo " "

read -p "Select: " choice
echo " "

case $choice in
  1)
    mvn clean package
    ;;
  2)
    mvn clean package
    java -jar target/MatrixScript-1.0-jar-with-dependencies.jar
    ;;
  3)
    java -jar target/MatrixScript-1.0-jar-with-dependencies.jar
    ;;
  4)
    mvn clean package
    java -jar target/MatrixScript-1.0-jar-with-dependencies.jar run
    ;;
  5)
    java -jar target/MatrixScript-1.0-jar-with-dependencies.jar run
    ;;
  *)
    echo "Invalid selection!"
    ;;
esac