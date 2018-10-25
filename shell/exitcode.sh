#!/bin/bash

# demonstrates the use of exit
# call with ./exitcode param; echo $?

if [ $# -eq 0 ]
then
  exit 2
else
  if [ $1 -eq 42 ]
  then
    exit 0
  else
    exit 1
  fi
fi
