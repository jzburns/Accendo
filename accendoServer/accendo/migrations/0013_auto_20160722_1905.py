# -*- coding: utf-8 -*-
# Generated by Django 1.9.8 on 2016-07-22 19:05
from __future__ import unicode_literals

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('accendo', '0012_cmisevent'),
    ]

    operations = [
        migrations.RenameField(
            model_name='cmisevent',
            old_name='CMISId',
            new_name='EventID',
        ),
        migrations.RemoveField(
            model_name='cmisevent',
            name='Event',
        ),
    ]
