#!/usr/bin/env python

"""
Run this command as
python createproject.py projectname description

"""

import sys
from firebase import firebase

if len(sys.argv) <> 3:
    print 'python createproject.py projectname description'
    exit()

baseUrl = 'https://henry-test.firebaseio.com'
ref = firebase.FirebaseApplication(baseUrl)

path = '/projects'
ref.post(path,{
    'name':sys.argv[1],
    'description':sys.argv[2]
})

