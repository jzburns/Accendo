# Create your views here.from django.http import HttpResponse
import datetime
from django.http import HttpResponse
from rest_framework.renderers import JSONRenderer
from rest_framework.parsers import JSONParser
from accendo.models import CMISEvent
from accendo.models import NFCUser
from accendo.serializers import CMISEventSerializer
from accendo.serializers import NFCUserSerializer
from django.views.decorators.csrf import csrf_exempt
from uuid import uuid4

def index(request):
   return HttpResponse("Welcome to Accendo")


class JSONResponse(HttpResponse):
    def __init__(self, data, **kwargs):
        content = JSONRenderer().render(data)
        kwargs['content_type'] = 'application/json'
        super(JSONResponse, self).__init__(content, **kwargs)

@csrf_exempt
def CMISEventList(request):
    if request.method == 'GET':
        events = CMISEvent.objects.all()
        serializer = CMISEventSerializer(events, many=True)
        return JSONResponse(serializer.data)

@csrf_exempt
def CMISEventDetail(request, pk):

    try:
        event = CMISEvent.objects.filter(teacher_id=pk)
    except CMISEvent.DoesNotExist:
        return HttpResponse(status=404)

    if request.method == 'GET':
        serializer = CMISEventSerializer(event, many=True)
        return JSONResponse(serializer.data)

    elif request.method == 'PUT':
        data = JSONParser().parse(request)
        serializer = CMISEventSerializer(event, data=data)
        if serializer.is_valid():
            serializer.save()
            return JSONResponse(serializer.data)
        return JSONResponse(serializer.errors, status=400)

    elif request.method == 'DELETE':
        event.delete()
        return HttpResponse(status=204)

@csrf_exempt
def AccendoValidateUser(request, cardid, pin):
    try:
        nfcuser = NFCUser.objects.get(card_id=cardid)
    except NFCUser.DoesNotExist:
        return HttpResponse(status=200)

    if nfcuser.status == 'LOCKED':
        return HttpResponse(status=300)

    if nfcuser.pin != pin:
        nfcuser.wrong_pin_count += 1
        nfcuser.save();
        if nfcuser.wrong_pin_count == 3:
            nfcuser.status = 'LOCKED'
            nfcuser.save();
        return HttpResponse(status=300)
    else:
        nfcuser.wrong_pin_count = 0
        nfcuser.save();
        if request.method == 'GET':
            sessionid = str(uuid4())
            request.session['sessionid'] = sessionid
            request.session['user_id'] = nfcuser.user_id
            return JSONResponse({'sessionid': sessionid})

@csrf_exempt
def InitSession(request, sessionid):
    if sessionid == request.session['sessionid']:
        user_id = request.session['user_id']
        todaysdate = datetime.datetime.today().strftime('%d-%m-%Y')
        thishour = datetime.datetime.now().time().strftime("%H:00")
        # test with
        todaysdate = '13-11-2015'
        thishour = '11:00'
        cmisevents = CMISEvent.objects.filter(teacher_id=user_id, Dates__contains=todaysdate, Start=thishour)
        if not cmisevents:
            return JSONResponse({'no session at this time': sessionid})
        cmisevent = cmisevents[0]
        return JSONResponse({'time': thishour, 'day': todaysdate, 'event_id': cmisevent.__str__()})
    return JSONResponse({'no record found': sessionid})


@csrf_exempt
def SyncSession(request, sessionid):
    if sessionid == request.session['sessionid']:
        return JSONResponse({'syncd': sessionid})
    return HttpResponse(status=404)