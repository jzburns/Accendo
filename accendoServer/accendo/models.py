from __future__ import unicode_literals

from django.db import models

# the following lines added:
import datetime
from django.utils import timezone

class NFCUser(models.Model):
    org_id = models.CharField(max_length=20, default='NULL')
    card_id = models.CharField(max_length=20, default='NULL')
    user_id = models.CharField(max_length=20, default='NULL')
    role = models.CharField(max_length=20, default='NULL')
    pin = models.CharField(max_length=200, default='0000')
    wrong_pin_count = models.IntegerField(default=0)
    status = models.CharField(max_length=200, default='OK')
    date_added = models.DateTimeField('date added')

    def __str__(self):
        return self.org_id

    def was_added_recently(self):
        now = timezone.now()
        return now - datetime.timedelta(days=1) <= self.pub_date <= now


# CMIS timetable model
class CMISEvent(models.Model):
    # FKs
    org_id = models.CharField(max_length=20)
    teacher_id = models.CharField(max_length=20)

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

    def __str__(self):
        return '%s\t%s\t%s\t%s\t%s' % (self.teacher_id, self.Day, self.Start, self.Finish, self.SubjectName)

class AttendEvent(models.Model):
    # FKs
    # event ID
    event = models.ForeignKey(CMISEvent)
    # student ID
    student = models.ForeignKey(NFCUser)
    date_attended = models.DateTimeField(default=datetime.datetime.now)
