# -*- coding: utf-8 -*-
# Generated by Django 1.9.8 on 2016-07-19 19:10
from __future__ import unicode_literals

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('accendo', '0002_users_another_id'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='users',
            name='another_id',
        ),
    ]
