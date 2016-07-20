from __future__ import unicode_literals

from django.db import models

# the following lines added:
import datetime
from django.utils import timezone


class NFCUser(models.Model):
    org_id = models.CharField(max_length=20)
    card_id = models.CharField(max_length=20)
    user_id = models.CharField(max_length=20)
    role = models.CharField(max_length=20)
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
    event_id = models.CharField(max_length=20)
    teacher_id = models.CharField(max_length=20)
    # atributes
    room = models.CharField(max_length=20)
    day = models.CharField(max_length=20)
    subject = models.CharField(max_length=20)
    start_time = models.CharField(max_length=20)
    finish_time = models.CharField(max_length=20)

    # a collection of dates such as:
    # {14-09-2015, 21-09-2015, 28-09-2015, 05-10-2015, 12-10-2015, 19-10-2015}
    active_dates = models.CharField(max_length=400)

    site = models.CharField(max_length=20)
    date_from = models.CharField(max_length=20)
    date_to = models.CharField(max_length=20)
    subject = models.CharField(max_length=20)
    course = models.CharField(max_length=20)
    department = models.CharField(max_length=20)
