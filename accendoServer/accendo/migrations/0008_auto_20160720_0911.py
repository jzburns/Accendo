# -*- coding: utf-8 -*-
# Generated by Django 1.9.8 on 2016-07-20 09:11
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('accendo', '0007_event'),
    ]

    operations = [
        migrations.CreateModel(
            name='CMISEvent',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('org_id', models.CharField(max_length=20)),
                ('event_id', models.CharField(max_length=20)),
                ('teacher_id', models.CharField(max_length=20)),
                ('room', models.CharField(max_length=20)),
                ('day', models.CharField(max_length=20)),
                ('start_time', models.CharField(max_length=20)),
                ('finish_time', models.CharField(max_length=20)),
                ('active_dates', models.CharField(max_length=400)),
                ('site', models.CharField(max_length=20)),
                ('date_from', models.CharField(max_length=20)),
                ('date_to', models.CharField(max_length=20)),
                ('subject', models.CharField(max_length=20)),
                ('course', models.CharField(max_length=20)),
                ('department', models.CharField(max_length=20)),
            ],
        ),
        migrations.DeleteModel(
            name='Event',
        ),
    ]
