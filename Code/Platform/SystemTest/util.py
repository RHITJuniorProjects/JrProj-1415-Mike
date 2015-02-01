#!/usr/bin/python

#####
# Add more as necessary
#
# def getProjectID(ref,projectName):
# def getMilestoneID(ref,projectID,milestone):
# def getUserID(ref,email):
# def getTask(ref,projectID,milestoneID,taskID):
# def getTaskInfo(ref,projectID,milestoneID,taskID): 
# def getActiveProjects(ref,userID):
# def getActiveMilestones(ref,userID,projectID):
# def getAssignedTasks(ref,userID,projectID,milestoneID):
# def createUser(ref,name,email,num):
# def getProjects(ref,uid):
# def addMember(ref,pid,uid,role):
# def createProject(ref,name,description,due_date,uid):
# def createMilestone(ref,projectID,name,due_date,description):
# def createTask(ref,projectID,milestoneID,uid,name,due_date_description,original_hour_estimate,category):
# def commit(ref,projectID,milestoneID,tid,uid,hours,status,added,removed):
#####


from base64 import b64decode
from firebase import firebase
import json
import os
import requests
import subprocess
import sys
import traceback
import re
import time

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
        raise Exception('HENRY: Invalid username')
    return userID


def getActiveProjects(ref,userID):
    path = '/users/'+userID+'/projects'
    my_projects = ref.get('/users/'+userID+'/projects',None)
    all_projects = ref.get('/projects/',None)
    return {p:all_projects[p]['name'] for p in all_projects for q in my_projects if p == q}

def getProjectInfo(ref,pid):
    return ref.get('/projects/'+pid,None)


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

def getMilestoneInfo(ref,pid,mid):
    return ref.get('/projects/'+pid+'/milestones/'+mid,None)


def getTask(ref,projectID,milestoneID,taskID): 
    return ref.get('/projects/'+projectID+'/milestones/'+milestoneID+'/tasks/'+taskID+'/name',None) 

def getTaskInfo(ref,projectID,milestoneID,taskID): 
    return ref.get('/projects/'+projectID+'/milestones/'+milestoneID+'/tasks/'+taskID,None) 

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

def createUser(ref,name,email,num):
    path = '/users'
    uid = 'simplelogin:'+str(num)
    ref.put(path,uid,{
        'name':name,
        'email':email
    })

def getProjects(ref,uid):
    path = '/users/'+uid+'/projects'
    return ref.get(path,None)

def addMember(ref,pid,uid,role):
    path = '/projects/'+pid+'/members'
    ref.put(path,uid,role)

def createProject(ref,name,description,due_date,uid):
    path = '/projects'
    pid = ref.post(path,{
        'name':name,
        'description':description,
        'due_date':due_date,
        'members': { uid: 'Lead' }
    })['name']
    return pid

def createMilestone(ref,projectID,name,due_date,description):
    path = '/projects/'+projectID+'/milestones'
    mid = ref.post(path, {
        'name':name,
        'due_date':due_date,
        'description':description }) ['name']
    return mid

def createTask(ref,projectID,milestoneID,uid,name,due_date,description,original_hour_estimate,category):
    path = '/projects/'+projectID+'/milestones/'+milestoneID+'/tasks'
    tid = ref.post(path, {
        'name':name,
        'assignedTo':uid,
        'due_date':due_date,
        'description':description,
        'original_hour_estimate':original_hour_estimate,
        'category':category })['name']
    return tid

def commit(ref,projectID,milestoneID,tid,uid,hours=0,status='New',added=0,removed=0):
    path='/commits/'+projectID
    ref.post(path,{
        'added_lines_of_code':added,
        'hours':hours,
        'message':"testing",
        'milestone':milestoneID,
        'project':projectID,
        'removed_lines_of_code':removed,
        'status':status,
        'task':tid,
        'timestamp': time.time(),
        'user':uid })
    
