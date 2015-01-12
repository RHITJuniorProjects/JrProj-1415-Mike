#!/usr/bin/env python


import sys
from firebase import firebase


baseUrl = 'https://henry-test.firebaseio.com'
ref = firebase.FirebaseApplication(baseUrl)

projectName = sys.argv[1]
milestoneName = sys.argv[2]
milestoneDesc = sys.argv[3]
dueDate = sys.argv[4]
hours = sys.argv[5]

def getProjectID(project,ref):
    path = '/projects'
    projects = ref.get(path,None)
    projects_with_names = {p:projects[p] for p in projects if 'name' in projects[p]}
    try:
        projectID = [p for p in projects_with_names if projects_with_names[p]['name'] == project][0]
    except:
        raise Exception('HENRY: Invalid or nonexistant project name')
    return projectID

projectID = getProjectID(projectName,ref)

path = '/projects/'+projectID+'/milestones'
print path
ref.post(path,{
    'name':milestoneName,
    'description':milestoneDesc,
    'added_lines_of_code':0,
    'description':milestoneDesc,
    'due_date':dueDate,
    'hours_percent':0,
    'removed_lines_of_code':0,
    'task_percent':0,
    'tasks_completed':0,
    'total_estimated_hours':20,
    'total_hours':0,
    'total_lines_of_code':0,
    'total_tasks':0
})
