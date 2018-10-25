#!/usr/bin/expect -f
spawn mount /mnt/dotTGM
expect "Username: "
sleep 0.5
send "michael.borko\r"
expect "Password: "
sleep 0.5
send "PASSWD\r"
expect "\? "
sleep 0.5
send "y\r"
wait
