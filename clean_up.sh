#!/bin/bash

cd ttdir

for f in *.small 
do
  # remove tabs
  sed 's/^	//' $f > $f.1

  # change commas to |
  sed 's/,/|/g' $f.1 > $f.2

  # remove duplicate lines
  cat -s $f.2 > $f.3

  # remove newlines and replace with commas
  sed 's/^$/EOR/g' $f.3 > $f.4

  tr '\n' ',' < $f.4 > $f.5 

  # mark the end of record
  sed 's/EOR/\n/g' $f.5 > $f.6

  # clean up trailing and starting commas
  sed 's/,$//g' $f.6 > $f.7
  sed 's/^,//g' $f.7 > $f.8

  rm *.0 *.1 *.2 *.3 *.4 *.5 *.6 *.7

done
