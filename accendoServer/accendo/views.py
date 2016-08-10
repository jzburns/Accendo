# Create your views here.from django.http import HttpResponse
import datetime
from django.http import HttpResponse
from rest_framework.renderers import JSONRenderer
from rest_framework.parsers import JSONParser
from accendo.models import CMISEvent, AttendEvent, Attendance
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

    #
    # TODO:
    # remove these precise error messages and replace with 303
    #

    try:
        nfcuser = NFCUser.objects.get(card_id=cardid)
    except NFCUser.DoesNotExist:
        return JSONResponse({'error': 'no such user'})

    if nfcuser.status == 'LOCKED':
        return JSONResponse({'error': 'account locked'})

    if nfcuser.pin != pin:
        nfcuser.wrong_pin_count += 1
        nfcuser.save();
        if nfcuser.wrong_pin_count == 3:
            nfcuser.status = 'LOCKED'
            nfcuser.save();
        return JSONResponse({'error': 'invalid pin'})
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
    if request.method == 'GET':
        if sessionid == request.session['sessionid']:
            user_id = request.session['user_id']
            # todaysdate = datetime.datetime.today().strftime('%d-%m-%Y')
            # thishour = datetime.datetime.now().time().strftime("%H:00")
            # test with
            todaysdate = '13-11-2015'
            thishour = '11:00'
            cmisevents = CMISEvent.objects.filter(teacher_id=user_id, Dates__contains=todaysdate, Start=thishour)
            if not cmisevents:
                return JSONResponse({'ERROR': "No event scheduled for you at this time"})
            cmisevent = cmisevents[0]
            request.session['eventid'] = cmisevent.EventID
            request.session['session_stamp'] = todaysdate + ' ' + thishour
            return JSONResponse({'event': cmisevent.__str__()})
        return JSONResponse({'ERROR': "Invalid Session"})
    return JSONResponse({'NON GET': "999"})


@csrf_exempt
def SyncSession(request, sessionid):
    if sessionid == request.session['sessionid']:
        return JSONResponse({'syncd': sessionid})
    return HttpResponse(status=404)

@csrf_exempt
def Attend(request, sessionid, cardid):
    if sessionid == request.session['sessionid']:
        # create a AttendEvent object and insert
        # compute %age attendance and return
        ev = CMISEvent.objects.get(EventID=request.session['eventid'])
        card = NFCUser.objects.get(card_id=cardid)

        event_session = request.session['session_stamp']

        try:
            # already attended this session event?

            # for test purposes
            # attendevent = AttendEvent.objects.get(event=ev, student=card)
            attendevent = AttendEvent.objects.get(Session=event_session)
            return JSONResponse({'DUP': "0000"})

        except AttendEvent.DoesNotExist:
            attendevent = AttendEvent()

        if attendevent:
            attendevent.student = card
            attendevent.event = ev
            attendevent.Session = event_session
            attendevent.save()

            # compute attendance data

            try:
                # already attended this session event?
                att = Attendance.objects.get()

            except Attendance.DoesNotExist:
                att = Attendance()

            if att:
                # compute the stats and save
                att.RunningTotal += 1
                att.AttendancePct = float(att.RunningTotal) / float(ev.TotalOccurrences) * 100.0
                att.event = ev
                att.student = card
                att.save()
                return JSONResponse({'pcntage': att.AttendancePct})

            return JSONResponse({'NON GET': "999"})

