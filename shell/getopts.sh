#!/bin/bash
 
while getopts ":a" opt; do
  case $opt in
    a)
      echo "-a was triggered!" >&2
      ;;
    \?)
      echo "Invalid option: -$OPTARG" >&2
      exit 1
      ;;
    *)
      echo "tzz" >&2
      exit 1
      ;;
  esac
done
