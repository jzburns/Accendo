# -*- coding: utf-8 -*-
# Generated by Django 1.9.8 on 2016-08-04 08:25
from __future__ import unicode_literals

import datetime
from django.db import migrations, models
import django.db.models.deletion
from django.utils.timezone import utc


class Migration(migrations.Migration):

    dependencies = [
        ('accendo', '0022_attendevent'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='attendevent',
            name='EventID',
        ),
        migrations.RemoveField(
            model_name='attendevent',
            name='card_id',
        ),
        migrations.RemoveField(
            model_name='attendevent',
            name='teacher_id',
        ),
        migrations.AddField(
            model_name='attendevent',
            name='cid',
            field=models.ForeignKey(default=datetime.datetime(2016, 8, 4, 8, 25, 35, 623288, tzinfo=utc), on_delete=django.db.models.deletion.CASCADE, to='accendo.NFCUser'),
            preserve_default=False,
        ),
        migrations.AddField(
            model_name='attendevent',
            name='tid',
            field=models.ForeignKey(default=datetime.datetime(2016, 8, 4, 8, 25, 43, 871819, tzinfo=utc), on_delete=django.db.models.deletion.CASCADE, to='accendo.CMISEvent'),
            preserve_default=False,
        ),
    ]
