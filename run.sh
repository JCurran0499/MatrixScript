#!/bin/bash

if [ -z $1 ]
then
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
  echo "6: Run full test suite"
  echo "7: Run specific JUnit test"
  echo " "

  read -p "Select: " CHOICE
  echo " "

else
  CHOICE="$1"
fi

case $CHOICE in
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
  6)
    mvn surefire:test
    ;;
  7)
    read -p "Test class name: " TEST_CLASS
    mvn surefire:test -Dtest="$TEST_CLASS"
    ;;
  *)
    echo "Invalid selection!"
    ;;
esac
