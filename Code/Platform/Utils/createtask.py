#!/usr/bin/env python

"""
Don't use this yet, one-time script to add a task for Asher.
Will be updated later.
"""

import sys
from firebase import firebase

baseUrl = 'https://henry-test.firebaseio.com'
ref = firebase.FirebaseApplication(baseUrl)

projectName = sys.argv[1]
milestoneName = sys.argv[2]
taskName = sys.argv[3]
taskDesc = sys.argv[4]

def getProjectID(project,ref):
    path = '/projects'
    projects = ref.get(path,None)
    projects_with_names = {p:projects[p] for p in projects if 'name' in projects[p]}
    try:
        projectID = [p for p in projects_with_names if projects_with_names[p]['name'] == project][0]
    except:
        raise Exception('HENRY: Invalid or nonexistant project name')
    return projectID

def getMilestoneID(projectID,milestone,ref):
    path = '/projects/'+projectID+'/milestones'
    milestones = ref.get(path,None)
    milestones_with_names = {m:milestones[m] for m in milestones if 'name' in milestones[m]}
    try:
        milestoneID = [m for m in milestones_with_names if milestones_with_names[m]['name'] == milestone][0]
    except:
        raise Exception('HENRY: Invalid or nonexistant milestone name')
    return milestoneID

projectID = getProjectID(projectName,ref)
milestoneID = getMilestoneID(projectID,milestoneName,ref)

path = '/projects/'+projectID+'/milestones/'+milestoneID+'/tasks'
ref.post(path,{
    'name':taskName,
    'description':taskDesc
})
