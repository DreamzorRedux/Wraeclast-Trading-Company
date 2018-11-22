# Wraeclast Trading Company
WTC is an application made for the game Path of Exile. Its goal is to make the trading experience smoother by assisting the player in his interactions with potential buyers.

At its core, this program has **three fundamental values**: simplicity, transparency, respect of the game's *Terms of Use*.

## What does it do?
(Soon a video link here)

## How to use it?
1. Install Java 8 or later version [here](https://www.java.com/download/)
2. Download WTC [here](https://github.com/DreamzorRedux/Wraeclast-Trading-Company/releases)
3. Run WTC.jar

## How does it work?
To make it short:
1. Path of Exile writes all incoming/outgoing chat messages in a log file (Client.txt).
2. WTC reads that file realtime and detects trade requests.
3. When you click a WTC button, it sends one chat message in the game via keypress emulation (1 button click = 1 chat message = 1 server-side action).

This application does **not** modify any game file, it does **not** perform more than 1 server-side action per button click.
