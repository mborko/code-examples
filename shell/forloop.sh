#!/bin/bash

# counts all words in all argument files

for i in $@
do
  wc $i
done
