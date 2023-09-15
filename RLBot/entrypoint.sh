#!/bin/bash

export DISPLAY=:99
Xvfb $DISPLAY -screen 0 765x523x16 &
sleep 5
X0tigervnc -display $DISPLAY -desktop 'Desktop' -rfbport 5050 -PasswordFile ~/.vnc/passwd -SecurityTypes VncAuth &
websockify 5051 localhost:5050 &
exec java -Drunelite.launcher.reflect=true -Xmx512m -Xss2m -XX:CompileThreshold=1500 -cp "client.jar:lib/*" net.rlbot.client.Program "$@"