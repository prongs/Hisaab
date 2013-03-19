from django.db import models
# Create your models here.

Model = models.Model

class Tester(Model):
    deviceId = models.CharField(max_length=100)


class FbUser(Model):

