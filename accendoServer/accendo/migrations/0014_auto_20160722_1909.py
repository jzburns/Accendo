# -*- coding: utf-8 -*-
# Generated by Django 1.9.8 on 2016-07-22 19:09
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('accendo', '0013_auto_20160722_1905'),
    ]

    operations = [
        migrations.AlterField(
            model_name='cmisevent',
            name='Dates',
            field=models.CharField(default='NULL', max_length=150),
        ),
    ]
