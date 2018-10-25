#!/bin/bash

# Demonstriert $# 

echo 'All Arguments'
echo $*
echo "Argument Count: $#"

echo "Name of command $0"

if [ $# -gt 0 ]
then
  echo "First argument: $1"
  
  if [ $# -gt 1 ]
  then
    shift
    echo "First argument after shift: $1"
  fi
else
  echo "No arguments"
fi
