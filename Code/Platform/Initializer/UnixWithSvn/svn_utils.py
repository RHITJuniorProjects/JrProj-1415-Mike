import subprocess
import sys

import firebase_utils


def usage():
    print 'SVN Usage Menu is still under construction'


def commit(ref):
    uID,pID,mID,tID,hours,status = prompt(ref)
    command = 'svn commit'.split(' ')
    pipe = subprocess.Popen(command,stdout=subprocess.PIPE)
    return pipe.communicate()[0].strip()


def getDefaults():
    return None,None,None,None,None


def prompt(ref):
    sys.stdin = open('/dev/tty')

    def_uID, def_pID, def_mID, def_tID, def_status = getDefaults()
   
    if def_uID != None:
        uID = def_uID
    else:
        sys.stdout.write('Email: ')
        sys.stdout.flush()
        email = raw_input()
        uID = firebase_utils.getUserID(ref,email)

    if def_pID != None:
        print 'Active projects (defaults to '+def_pID+'):'
    else:
        print 'Active projects:'
    sys.stdout.flush()
    idsByIndex = [] ; index = 1
    for pID, pName in firebase_utils.getActiveProjects(ref,uID).iteritems():
        print ' - '+str(index)+'. '+pName
        idsByIndex = idsByIndex + [pID]
        index = index + 1
    sys.stdout.write('Project: ')
    sys.stdout.flush()
    project = raw_input()
    if project.isdigit() and int(project) <= len(idsByIndex):
        pID = idsByIndex[int(project)-1]
    elif project == '' and def_pID != 0:
        pID = def_pID
    else:
        pID = firebase_utils.getProjectID(ref,project)

    if def_mID != None:
        print 'Active milestones (defaults to '+def_mID+'):'
    else:
        print 'Active milestones:'
    sys.stdout.flush()
    idsByIndex = [] ; index = 1
    for mID, mName in firebase_utils.getActiveMilestones(ref,uID,pID).iteritems():
        print ' - '+str(index)+'. '+mName
        idsByIndex = idsByIndex + [mID]
        index = index + 1
    sys.stdout.write('Milestone: ')
    sys.stdout.flush()
    milestone = raw_input()
    if milestone.isdigit() and int(milestone) <= len(idsByIndex):
        mID = idsByIndex[int(milestone)-1]
    elif milestone == '' and def_mID != 0:
        mID = def_mID
    else:
        mID = firebase_utils.getMilestoneID(ref,pID,milestone)

    if def_tID != None:
        def_task = firebase_utils.getTask(ref,pID,mID,def_tID)
        print 'Tasks assigned to you (defaults to '+def_task+'):'
    else:
        print 'Tasks assigned to you:'
    idsByIndex = [] ; index = 1
    for tID, tName in firebase_utils.getAssignedTasks(ref,uID,pID,mID).iteritems():
        print ' - '+str(index)+'. '+tName
        idsByIndex = idsByIndex + [tID]
        index = index + 1
    sys.stdout.write('Task: ')
    sys.stdout.flush()
    task = raw_input()
    if task.isdigit() and int(task) <= len(idsByIndex):
        tID = idsByIndex[int(task)-1]
    elif task == '' and def_tID != 0:
        tID = def_tID
    else:
        tID = getTaskID(pID,mID,task)


    sys.stdout.write('Hours: ')
    sys.stdout.flush()
    hours = raw_input()


    if def_status != None:
        print 'Select status (defaults to '+def_status+'):'
    else:
        print 'Select status:'
    possibleStatuses = ['New', 'Implementation', 'Testing', 'Verify', 'Regression', 'Closed']
    for i in range(len(possibleStatuses)):
        print ' - ' + str(i+1) + ': ', possibleStatuses[i]
    sys.stdout.write('Status: ')
    sys.stdout.flush()
    status = raw_input()
    if status.isdigit() and (int(status) <= len(possibleStatuses)):
        status = possibleStatuses[int(status)-1]
    elif status == '' and def_status != 0:
        status = def_status
    else:
        print 'HENRY: Invalid status, commit failed'
        exit(1)
    print 'I should be returning' 
    return uID,pID,mID,tID,hours,status


