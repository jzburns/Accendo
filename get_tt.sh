#!/bin/bash

ttdir=$HOME/accendo/ttdir
rm $ttdir/*

for i in {350..650}
do
   wget -O /dev/null --save-cookies cookies.txt  --keep-session-cookies \
    "http://cmis.it-tallaght.ie/eportal/PortalServ?reqtype=login&username=$i"

   wget -O $ttdir/"$i".html --load-cookies cookies.txt \
    "http://cmis.it-tallaght.ie/eportal/PortalServ?reqtype=timetable&username=$i&action=getgrid&sKey=2015-2016%7C$i&sWeeks=&sType=lecturer&sYear=&sFromDate=&sToDate=&sEventType=&sModOccur=&sSource=&instCode=-2&instName=&useBooking=null&contactkey=&isBooking=null&subTitle="

  # convert html to text
  sed -e 's/<[^>]*>//g' $ttdir/"$i".html > $ttdir/"$i".txt

  # remove the html don't need it now
  rm $ttdir/"$i".html

  # delete the first 289 lines
  sed -i -e 1,288d $ttdir/"$i".txt

  # lines starting with are NULL values
  sed -i -e 's/^-$/NULL/g' $ttdir/"$i".txt 

  # remove lines starting with:
  sed -i -e '/^Instance/ d' $ttdir/"$i".txt 
  sed -i -e '/^Event/ d' $ttdir/"$i".txt 
  sed -i -e '/^Day/ d' $ttdir/"$i".txt 
  sed -i -e '/^Start/ d' $ttdir/"$i".txt 
  sed -i -e '/^Finish/ d' $ttdir/"$i".txt 
  sed -i -e '/^Room/ d' $ttdir/"$i".txt 
  sed -i -e '/^Dates/ d' $ttdir/"$i".txt 
  sed -i -e '/^Subject/ d' $ttdir/"$i".txt 
  sed -i -e '/^Activity/ d' $ttdir/"$i".txt 
  sed -i -e '/^Source/ d' $ttdir/"$i".txt 
  sed -i -e '/^Event/ d' $ttdir/"$i".txt 

  # delete trailing lines
  head -n -43 $ttdir/"$i".txt > $ttdir/"$i"

  # clean up
  rm $ttdir/"$i".txt

  # remove the cookies file don't want that for the next login
  rm cookies.txt

done

 find $ttdir -size 0 -delete
 source ./clean_up.sh

