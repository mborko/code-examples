#!/bin/bash

# find all files with digsig and copy them to the destination
find | grep digsig | xargs -i cp -p "{}" ~/repositories/diploma-thesis/prototypes/kernel/digsig/.
