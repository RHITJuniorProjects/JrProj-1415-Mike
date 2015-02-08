#!/usr/bin/python

from base64 import b64decode
from firebase import firebase
import json
import os
import requests
import subprocess
import sys
import traceback
import re

def getProjectID(ref,projectName):
    path = '/projects'
    projects = ref.get(path,None)
    projects_with_names = {p:projects[p] for p in projects if 'name' in projects[p]}
    try:
        projectID = [p for p in projects_with_names if projects_with_names[p]['name'] == projectName][0]
    except:
        raise Exception('HENRY: Invalid or nonexistant project name')
    return projectID


def getUserID(ref,email):
    path = '/users'
    users = ref.get(path,None)
    filteredusers = {u:users[u] for u in users if 'email' in users[u]}
    try:
        userID = [u for u in filteredusers if filteredusers[u]['email'] == email][0]
    except:
        print 'HENRY: Invalid or nonexistant email, commit failed'
        exit(1)
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
        mID = [m for m in filtered if filtered[m]['name']==milestone][0]
    except:
        print 'HENRY: Invalid or nonexistent milestone, commit failed'
        exit(1)
    return mID


def getTaskID(projectID,milestoneID,task):
    path = '/projects/'+projectID+'/milestones/'+milestoneID+'/tasks'
    tasks = ref.get(path,None)
    filtered = {t:tasks[t] for t in tasks if 'name' in tasks[t]}
    try:
        tID = [t for t in filtered if filtered[t]['name']==task][0]
    except:
        print 'HENRY: Invalid or nonexistant task, commit failed'
        exit(1)
    return tID


def getTask(ref,projectID,milestoneID,taskID): 
    return ref.get('/projects/'+projectID+'/milestones/'+milestoneID+'/tasks/'+taskID+'/name',None) 


def teamEmails(ref,pID): 
    path = '/projects/'+pID+'/members' 
    memberUIDs = ref.get(path,None) 
    all_users = ref.get('/users',None) 
    filtered_users = [ all_users[u]['email'] for u in all_users if u in memberUIDs] 
    return filtered_users 


def writeCommit(ref,msg,project,uid,hours,status,pos_loc,neg_loc,ts,projectID,milestoneID,taskID):
    path = '/commits/'+projectID+'/'
    try:
        result = ref.post(path,{
            'hours':hours,
            'user':uid,
            'added_lines_of_code':pos_loc,
            'removed_lines_of_code':neg_loc,
            'message':msg,
            'timestamp':ts,
            'milestone':milestoneID,
            'task':taskID,
            'status':status,
            'project':projectID
        })
    except:
        raise Exception('Connection to Firebase commits table denied')
    return result.values()[0]


def getMilestone(ref,projectID,milestoneID):
    return ref.get('/projects/'+projectID+'/milestones/'+milestoneID+'/name',None)


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
