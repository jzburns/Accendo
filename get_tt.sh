#!/bin/bash

ttdir=$HOME/accendo/ttdir

for i in {500..550}
do
  wget -O /dev/null --save-cookies cookies.txt  --keep-session-cookies \
  "http://cmis.it-tallaght.ie/eportal/PortalServ?reqtype=login&username=$i"

  wget -O $ttdir/user"$i".html --load-cookies cookies.txt \
  "http://cmis.it-tallaght.ie/eportal/PortalServ?reqtype=timetable&username=$i&action=getgrid&sKey=2015-2016%7C$i&sWeeks=&sType=lecturer&sYear=&sFromDate=&sToDate=&sEventType=&sModOccur=&sSource=&instCode=-2&instName=&useBooking=null&contactkey=&isBooking=null&subTitle="

rm cookies.txt

sed -e 's/<[^>]*>//g' $ttdir/user"$i".html > $ttdir/user"$i".txt
rm $ttdir/user"$i".html

done
