#!/bin/sh
cp -r "$1" "work_$1" && cd "work_$1" && gradle $2