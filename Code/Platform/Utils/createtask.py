#!/usr/bin/env python

"""
Don't use this yet, one-time script to add a task for Asher.
Will be updated later.
"""

import sys
from firebase import firebase

baseUrl = 'https://henry-test.firebaseio.com'
ref = firebase.FirebaseApplication(baseUrl)

path = '/projects/-JYcg488tAYS5rJJT4Kh/milestones/-JYc_9ZGEPFM8cjChyKl/tasks'
ref.post(path,{
    'name':'Code Review',
    'assignedTo':'simplelogin:45',
    'category':'Testing',
    'description':'Review code',
    'added_lines_of_code':0,
    'removed_lines_of_code':0,
    'due_date':'2014-11-15',
    'percent_complet':0,
    'total_lines_of_code':0,
    'original_time_estimate':10,
    'total_hours':0
})

