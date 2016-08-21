from django.contrib import admin

# Register your models here.
from django.contrib import admin
from .models import NFCUser, Attendance, AttendEvent
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

class AttendEventAdmin(admin.ModelAdmin):
    search_fields = ('event', 'student', 'date_attended', 'Session')
    fieldsets = [
        (None,      {'fields': ['event',
                    'student',
                    'date_attended',
                    'Session']}),
    ]
admin.site.register(AttendEvent, AttendEventAdmin)

class AttendanceAdmin(admin.ModelAdmin):
    search_fields = ('event', 'student', 'Room')
    fieldsets = [
        (None,      {'fields': ['event',
                    'student',
                    'RunningTotal',
                    'AttendancePct']}),
    ]
admin.site.register(Attendance, AttendanceAdmin)