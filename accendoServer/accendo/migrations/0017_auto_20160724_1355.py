# -*- coding: utf-8 -*-
# Generated by Django 1.9.8 on 2016-07-24 13:55
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('accendo', '0016_auto_20160722_1939'),
    ]

    operations = [
        migrations.AddField(
            model_name='nfcuser',
            name='pin',
            field=models.CharField(default='NULL', max_length=200),
        ),
        migrations.AlterField(
            model_name='nfcuser',
            name='card_id',
            field=models.CharField(default='NULL', max_length=20),
        ),
        migrations.AlterField(
            model_name='nfcuser',
            name='org_id',
            field=models.CharField(default='NULL', max_length=20),
        ),
        migrations.AlterField(
            model_name='nfcuser',
            name='role',
            field=models.CharField(default='NULL', max_length=20),
        ),
        migrations.AlterField(
            model_name='nfcuser',
            name='user_id',
            field=models.CharField(default='NULL', max_length=20),
        ),
    ]