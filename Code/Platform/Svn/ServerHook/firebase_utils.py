#!/usr/bin/python

from firebase import firebase
from base64 import b64decode
import subprocess
import traceback
import requests
import json
import time
import sys
import os
import re

def getProjectID(ref,projectName):
    path = '/projects'
    projects = ref.get(path,None)
    projects_with_names = {p:projects[p] for p in projects if 'name' in projects[p]}
    try:
        projectID = [p for p in projects_with_names if projects_with_names[p]['name'].lower() == projectName.lower()][0]
    except:
        raise Exception('HENRY: Invalid or nonexistant project name')
    return projectID


def getUserID(ref,email):
    path = '/users'
    users = ref.get(path,None)
    filteredusers = {u:users[u] for u in users if 'email' in users[u]}
    try:
        userID = [u for u in filteredusers if filteredusers[u]['email'].lower() == email.lower()][0]
    except:
        raise Exception('HENRY: Invalid username')
    return userID


def getActiveProjects(ref,userID):
    path = '/users/'+userID+'/projects'
    my_projects = ref.get('/users/'+userID+'/projects',None)
    all_projects = ref.get('/projects/',None)
    return {p:all_projects[p]['name'] for p in all_projects for q in my_projects if p == q}


def getActiveMilestones(ref,userID,projectID):
    path = '/projects/'+projectID+'/milestones'
    milestones = ref.get(path,None)
    return {m:milestones[m]['name'] for m in milestones if 'name' in milestones[m]}


def getMilestoneID(ref,projectID,milestone):
    path = '/projects/'+projectID+'/milestones'
    milestones = ref.get(path,None)
    filtered = {m:milestones[m] for m in milestones if 'name' in milestones[m]}
    try:
        mID = [m for m in filtered if filtered[m]['name'].lower() == milestone.lower()][0]
    except:
        raise Exception('HENRY: Invalid or nonexistent milestone, commit failed')
    return mID


def getTaskID(ref,pID,mID,task):
    path = '/projects/'+pID+'/milestones/'+mID+'/tasks'
    tasks = ref.get(path,None)
    filtered = {t:tasks[t] for t in tasks if 'name' in tasks[t]}
    try:
        tID = [t for t in filtered if filtered[t]['name'].lower() == task.lower()][0]
    except:
        raise Exception('Henry: Invalid or nonexistant task')
    return tID


def getTask(ref,projectID,milestoneID,taskID): 
    return ref.get('/projects/'+projectID+'/milestones/'+milestoneID+'/tasks/'+taskID+'/name',None) 


def getAssignedTasks(ref,userID,projectID,milestoneID):
    path = '/users/'+userID+'/projects/'+projectID+'/milestones/'+milestoneID+'/tasks'
    try:
        taskIDs = ref.get(path,None).keys()
    except:
        print 'HENRY: You have no assigned tasks for this milestone, commit failed'
        exit(1)
    path = '/projects/'+projectID+'/milestones/'+milestoneID+'/tasks'
    allTasks = ref.get(path,None)
    assignedTasks = {tID:allTasks[tID]['name'] for tID in taskIDs}
    return assignedTasks

def commit(ref,message,uID,pID,mID,tID,hours,status,pos_loc,neg_loc):
    path = '/commits/'+pID+'/'
    ref.post(path,{
        'message': message,
        'user': uID,
        'project': pID,
        'milestone': mID,
        'task': tID,
        'hours': hours,
        'status': status,
        'added_lines_of_code': pos_loc,
        'removed_lines_of_code': neg_loc,
        'timestamp': int(time.time()*1000)
    })
