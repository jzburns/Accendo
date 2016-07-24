# Create your views here.from django.http import HttpResponse
from django.http import HttpResponse
from rest_framework.renderers import JSONRenderer
from rest_framework.parsers import JSONParser
from accendo.models import CMISEvent
from accendo.serializers import CMISEventSerializer
from django.views.decorators.csrf import csrf_exempt

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
