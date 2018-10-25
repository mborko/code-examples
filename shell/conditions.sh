#!/bin/bash

#Shows different varitions of if statements

if ls nofile
then
 skip
else
  echo A: nofile does not exist

fi

if [ ! -a nofile ] 
then
  echo B: nofile does not exist.
fi

s=xz

if [ $s = "xyz" ]
then
  echo C: s = xyz
else
  echo C: s <> xyz
fi

declare -i y=42

if [ $y -eq 42 ]
then
  echo D: y enthaelt den Wert 42
else
  echo D: y enthaelt nicht den Wert 42
fi
