from __future__ import unicode_literals

from decimal import Decimal
from django.db import models

# the following lines added:
import datetime
from django.utils import timezone

class NFCUser(models.Model):
    org_id = models.CharField(max_length=20, default='NULL')
    card_id = models.CharField(max_length=20, default='NULL')
    user_id = models.CharField(max_length=20, default='NULL')
    lname = models.CharField(max_length=20, default='NULL')
    fname = models.CharField(max_length=20, default='NULL')
    role = models.CharField(max_length=20, default='NULL')
    pin = models.CharField(max_length=200, default='0000')
    wrong_pin_count = models.IntegerField(default=0)
    status = models.CharField(max_length=200, default='OK')
    date_added = models.DateTimeField('date added')

    def __str__(self):
        return '%s %s %s %s %s %s' % (self.lname, self.fname, self.org_id, self.card_id, self.user_id, self.role)

    def was_added_recently(self):
        now = timezone.now()
        return now - datetime.timedelta(days=1) <= self.pub_date <= now


# CMIS timetable model
class CMISEvent(models.Model):
    # FKs
    org_id = models.CharField(max_length=20)
    teacher_id = models.CharField(max_length=20)

    # after a data load
    # UPDATE accendo_cmisevent SET Dates = REPLACE(Dates, '|', '');
    # UPDATE accendo_cmisevent SET Dates = REPLACE(Dates, '{', '');
    # UPDATE accendo_cmisevent SET Dates = REPLACE(Dates, '}', '');


    # UPDATE accendo_cmisevent SET SubjectName = REPLACE(SubjectName, '|', '');
    # UPDATE accendo_cmisevent SET SubjectName = REPLACE(SubjectName, '{', '');
    # UPDATE accendo_cmisevent SET SubjectName = REPLACE(SubjectName, '}', '');

    # UPDATE accendo_cmisevent SET Room = REPLACE(Room, '|', '');
    # UPDATE accendo_cmisevent SET Room = REPLACE(Room, '{', '');
    # UPDATE accendo_cmisevent SET Room = REPLACE(Room, '}', '');

    # CMIS atributes
    Instance = models.CharField(max_length=50, default='NULL')
    EventID = models.CharField(max_length=50, default='NULL')
    Day = models.CharField(max_length=50, default='NULL')
    Start = models.CharField(max_length=50, default='NULL')
    Finish = models.CharField(max_length=50, default='NULL')
    Room = models.CharField(max_length=100, default='NULL')
    Dates = models.CharField(max_length=450, default='NULL')
    SubjectCode = models.CharField(max_length=100, default='NULL')
    SubjectName = models.CharField(max_length=100, default='NULL')
    ActivityType = models.CharField(max_length=50, default='NULL')
    SubjectSubGroup = models.CharField(max_length=50, default='NULL')
    Source = models.CharField(max_length=50, default='NULL')
    EventType = models.CharField(max_length=50, default='NULL')
    # run this update to compute the TotalOccurrences
    # update accendo_cmisevent ev1, accendo_cmisevent ev2 set ev1.TotalOccurrences =
    # (select LENGTH(ev1.Dates) - LENGTH(REPLACE(ev1.Dates, '|', '')) + 1) where ev1.id = ev2.id;
    TotalOccurrences = models.IntegerField(default=0)

    def __str__(self):
        return '%s %s %s %s %s ' % (self.SubjectName, self.Room, self.Day, self.Start, self.Finish)

class AttendEvent(models.Model):
    # FKs
    # event ID
    event = models.ForeignKey(CMISEvent)
    # student ID
    student = models.ForeignKey(NFCUser)
    date_attended = models.DateTimeField(default=datetime.datetime.now)
    Session = models.CharField(max_length=50, default='NULL')

    def __str__(self):
        return '%s %s %s %s' % (self.student, self.event, self.date_attended, self.Session)


class Attendance(models.Model):
    event = models.ForeignKey(CMISEvent)
    student = models.ForeignKey(NFCUser)
    AttendancePct = models.DecimalField(max_digits=6,decimal_places=2,default=Decimal('0.00'))
    RunningTotal = models.IntegerField(default=0)

    def __str__(self):
        return '%s %s %s %s' % (self.student, self.event, self.AttendancePct, self.RunningTotal)
