from django.conf.urls import url
from . import views
urlpatterns = [
    url(r'^$', views.index, name='index'),
    url(r'^events/$', views.CMISEventList),
    url(r'^eventdetail/(?P<pk>[0-9]+)/$', views.CMISEventDetail),
]