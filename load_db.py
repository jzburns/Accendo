import csv
import MySQLdb
import glob

mydb = MySQLdb.connect(host='localhost',
    user='root',
    passwd='rootpasswd',
    db='accendo')

cursor = mydb.cursor()

for filename in glob.glob('ttdir/*.9'):
  with open(filename) as csvfile:
    csv_data = csv.reader(csvfile)

    for row in csv_data:
      cursor.execute('INSERT INTO accendo_cmisevent \
      (org_id, \
      teacher_id, \
      Instance, \
      EventID, \
      Day, \
      Start, \
      Finish, \
      Room, \
      Dates, \
      SubjectCode, \
      SubjectName, \
      ActivityType, \
      SubjectSubGroup, \
      Source, \
      EventType, \
      TotalOccurrences) \
      VALUES("ITTALLGHT", \
      %s, %s, %s, %s, %s, %s, %s,\
      %s, %s, %s, %s, %s, %s, %s, 0)', row)

#close the connection to the database.
cursor.close()
mydb.commit()
cursor.close()

print 'Done'

