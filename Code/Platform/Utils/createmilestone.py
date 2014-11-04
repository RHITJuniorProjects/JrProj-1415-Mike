#!/usr/bin/env python


import sys
from firebase import firebase


baseUrl = 'https://henry-test.firebaseio.com'
ref = firebase.FirebaseApplication(baseUrl)

projectName = sys.argv[1]
milestoneName = sys.argv[2]
milestoneDesc = sys.argv[3]

def getProjectID(project,ref):
    path = '/projects'
    projects = ref.get(path,None)
    print project
    projects_with_names = {p:projects[p] for p in projects if 'name' in projects[p]}
    print projects_with_names
    try:
        projectID = [p for p in projects_with_names if projects_with_names[p]['name'] == project][0]
    except:
        raise Exception('HENRY: Invalid or nonexistant project name')
    return projectID

projectID = getProjectID(projectName,ref)

path = '/projects/'+projectID+'/milestones'
ref.post(path,{
    'name':milestoneName,
    'description':milestoneDesc
})
