from django.conf.urls import url
from . import views
urlpatterns = [
    url(r'^$', views.index, name='index'),
    url(r'^events/$', views.CMISEventList),
    url(r'^eventdetail/(?P<pk>[0-9]+)/$', views.CMISEventDetail),
    url(r'^validateuser/(?P<card_id>(([0-9])|([A-F]))+)/(?P<pin>(([0-9])|([A-F]))+)/$', views.AccendoValidateUser),
    url(r'^syncsession/(?P<card_id>(([0-9])|([A-F]))+)/$', views.SyncSession),
]