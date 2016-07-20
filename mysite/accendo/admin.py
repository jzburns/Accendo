from django.contrib import admin

# Register your models here.
from django.contrib import admin
from .models import NFCUser

class NFCUserAdmin(admin.ModelAdmin):
    fieldsets = [
        (None,               {'fields': ['org_id', 'card_id', 'user_id', 'role']}),
        ('Date added', {'fields': ['date_added'], 'classes': ['collapse']}),
    ]
admin.site.register(NFCUser, NFCUserAdmin)