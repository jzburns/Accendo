from rest_framework import serializers
from accendo.models import CMISEvent

class CMISEventSerializer(serializers.Serializer):

    pk = serializers.IntegerField(read_only=True)
    Day = serializers.CharField(max_length=50, default='NULL')
    Start = serializers.CharField(max_length=50, default='NULL')
    Finish = serializers.CharField(max_length=50, default='NULL')
    Room = serializers.CharField(max_length=100, default='NULL')
