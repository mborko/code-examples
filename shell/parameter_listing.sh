#!/bin/bash

var0=0
LIMIT=10

# how much parameters are there !?
echo ${#@} arguments

while [ "$var0" -lt "$LIMIT" ]
do
echo `date +%H:%M:%S.%N` "$var0.Durchlauf"        # -n suppresses newline.
var0=`expr $var0 + 1`   # var0=$(($var0+1)) also works.
done

exit 0
