# -*- coding: utf-8 -*-
# Generated by Django 1.9.8 on 2016-07-22 18:38
from __future__ import unicode_literals

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('accendo', '0009_auto_20160722_1835'),
    ]

    operations = [
        migrations.RenameField(
            model_name='cmisevent',
            old_name='Id',
            new_name='CMISId',
        ),
    ]
