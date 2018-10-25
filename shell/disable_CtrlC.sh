#!/bin/bash
trap '' 2

echo "This is a test. Hit [Ctrl+C] to test it..."
sleep 2

trap 2

echo "test again"
sleep 2
