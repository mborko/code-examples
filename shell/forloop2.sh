#!/bin/bash

# counts all words in etc
# Verwendet Command Substitution mit $()

for i in $(ls /etc)
do
  wc "/etc/$i"
done
