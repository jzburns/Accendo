# -*- coding: utf-8 -*-
# Generated by Django 1.9.8 on 2016-07-26 12:53
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('accendo', '0018_auto_20160724_1357'),
    ]

    operations = [
        migrations.AddField(
            model_name='nfcuser',
            name='wrong_pin_count',
            field=models.IntegerField(default=0),
        ),
    ]
