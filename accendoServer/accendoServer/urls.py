from django.conf.urls import include, url
from django.contrib import admin

urlpatterns = [
    url(r'^accendo/', include('accendo.urls')),  # this line added
    url(r'^admin/', include(admin.site.urls)),
]
