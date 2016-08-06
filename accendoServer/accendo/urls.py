from django.conf.urls import url
from . import views
urlpatterns = [
    url(r'^$', views.index, name='index'),
    url(r'^events/$', views.CMISEventList),
    url(r'^eventdetail/(?P<pk>[0-9]+)/$', views.CMISEventDetail),
    url(r'^validateuser/(?P<cardid>(([0-9])|([a-f]))+)/(?P<pin>(([0-9])|([a-f]))+)/$', views.AccendoValidateUser),
    url(r'^initsession/(?P<sessionid>(([0-9])|([a-f])|(-))+)/$', views.InitSession),
    url(r'^attend/(?P<sessionid>(([0-9])|([a-f])|(-))+)/(?P<cardid>(([0-9])|([a-f]))+)/$', views.Attend),
]