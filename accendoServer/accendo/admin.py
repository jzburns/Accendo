from django.contrib import admin

# Register your models here.
from django.contrib import admin
from .models import NFCUser
from .models import CMISEvent

class NFCUserAdmin(admin.ModelAdmin):
    fieldsets = [
        (None,               {'fields': ['org_id', 'card_id', 'user_id', 'role']}),
        ('Date added', {'fields': ['date_added'], 'classes': ['collapse']}),
    ]
admin.site.register(NFCUser, NFCUserAdmin)

class CMISEventAdmin(admin.ModelAdmin):
    search_fields = ('teacher_id', 'Day', 'Room','SubjectName',)
    fieldsets = [
        (None,      {'fields': ['org_id',
                    'event_id',
                    'teacher_id',
                    'Instance',
                    'EventID',
                    'Day',
                    'Start',
                    'Finish',
                    'Room',
                    'Dates',
                    'SubjectCode',
                    'SubjectName',
                    'ActivityType',
                    'SubjectSubGroup',
                    'Source',
                    'EventType']}),
    ]
admin.site.register(CMISEvent, CMISEventAdmin)

